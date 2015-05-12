package com.chappelle.jcraft;

import Properties.GameProperties;
import Properties.GeneratorProperties;
import chunkControl.ChunkControlManager;
import com.chappelle.jcraft.blocks.Blocks;
import com.chappelle.jcraft.blocks.JBlockHelper;
import com.cubes.BTControl;
import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockTerrainControlNew1;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import terrain.TerrainGenerator;
import terrain.TerrainGeneratorFactory;

public class RunningAppState extends AbstractAppState
{
    private HUDControl hudControl;
    private BlockGame app;
    private Nifty nifty;
    private Screen screen;
    private Node rootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private BulletAppState physics;
    private JBlockHelper blockHelper;
    private BTControl blockTerrain;

    private BTControl itemBlockTerrain;
    private PlayerControl player;
    private Node blocks;
    private Node itemBlocks;
    private CubesSettings cubeSettings;
    private GameSaveManager gameSaveManager;
    private EnvironmentAppState environment;
    private EnemyControl enemyControl;
    private BlockTerrainManager terrainManager;
    private Vector3Int playerStartLocation;

    private RunningActionListener actionListener = new RunningActionListener();
    private InventoryAppState inventoryAppState;
    private TerrainGenerator terrainGenerator;
    private boolean autoRun = false;


    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);

        this.app = (BlockGame) app;


        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.physics = this.stateManager.getState(BulletAppState.class);
        this.environment = new EnvironmentAppState();

        stateManager.attach(environment);

        setUpKeys();

        try
        {
            playerStartLocation = (Vector3Int.fromVector3f((Vector3f)ObjectPersister.load("playerStart.obj")));
        }
        catch (UnloadableObjectException e)
        {
            playerStartLocation = null;
        }

        setUpBlocks();

        terrainGenerator = TerrainGeneratorFactory.makeTerrainGenerator(GeneratorProperties.GENERATOR_TYPE, blockHelper, blockTerrain);

        this.blockHelper = new JBlockHelper(this.app, this);
        this.player = new PlayerControl(this.app, cubeSettings, blockHelper);
        this.player.setPlayerStartLocation(playerStartLocation);

        this.hudControl = new HUDControl(this.app, player, blockTerrain);


        this.enemyControl = new EnemyControl(this.app);

        rootNode.addControl(player);
        rootNode.addControl(hudControl);
        rootNode.addControl(enemyControl);
        rootNode.addControl(new ChunkControlManager(rootNode, blockHelper, blockTerrain, player, terrainGenerator));
        this.inventoryAppState = new InventoryAppState();
        stateManager.attach(inventoryAppState);

        this.nifty = app.getStateManager().getState(StartScreenState.class).getNifty();

        nifty.fromXml("Interface/hud.xml", "hud", hudControl);
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
    }


    public PlayerControl getPlayerControl()
    {
        return player;
    }

    public HUDControl getHUDControl()
    {
        return hudControl;
    }

    @Override
    public void cleanup()
    {
        super.cleanup();
        System.out.println("Saving World State...");
        if(GameProperties.SAVE)
        {
            for(Vector3Int loc : blockTerrain.getChunkLocations())
            {
                blockTerrain.markAsDoomed(loc);
            }
            blockTerrain.disposeOldChunks();
        }
        ObjectPersister.save(player.getWorldLocation(), "playerStart.obj");
        System.out.println("So long!");
    }

    @Override
    public void update(float tpf)
    {
        super.update(tpf);
        if(autoRun)
        {
            player.setForward(autoRun);
            player.setJumping(autoRun);
        }
    }

    public CubesSettings getCubeSettings()
    {
        return cubeSettings;
    }

    private void setUpBlocks()
    {
        cubeSettings = new CubesSettings(app);
        Blocks.registerBlocks(this.app);
        blockTerrain = new BlockTerrainControlNew1(cubeSettings, physics);
        initBlockTerrain();
        blockTerrain.addChunkListener(new BlockChunkListener()
        {
            @Override
            public void onSpatialUpdated(BlockChunkControl blockChunk)
            {
                 refreshBlocksNode();
            }
        });

        refreshBlocksNode();

        itemBlockTerrain = new BlockTerrainControlNew1(cubeSettings, null);
        itemBlocks = new Node("items");
        itemBlocks.setShadowMode(GameProperties.SHADOW_MODE);
        itemBlocks.addControl(itemBlockTerrain);
        rootNode.attachChild(itemBlocks);

        terrainManager = new BlockTerrainManager(cubeSettings, blockTerrain);
        stateManager.attach(terrainManager);
    }

    private void refreshBlocksNode()
    {
        if(blocks != null)
        {
            rootNode.detachChild(blocks);
            blocks.removeControl(blockTerrain);
            blocks.detachAllChildren();
        }
        blocks = new Node("blocks");
        blocks.setShadowMode(GameProperties.SHADOW_MODE);
        blocks.addControl(blockTerrain);
        rootNode.attachChild(blocks);
    }

    private void initBlockTerrain()
    {
        this.blockHelper = new JBlockHelper(this.app, this);
        TerrainGenerator terrainGenerator = TerrainGeneratorFactory.makeTerrainGenerator(GeneratorProperties.GENERATOR_TYPE, blockHelper, blockTerrain);
        //Vector3Int terrainOffset = new Vector3Int(Integer.MAX_VALUE / 2, 0, Integer.MAX_VALUE / 2);
        Vector3Int terrainOffset = new Vector3Int(1000, 0, 1000);
        if(playerStartLocation == null)
        {
            terrainGenerator.generateTerrain(terrainOffset);
            playerStartLocation = terrainGenerator.getPlayerStart();
        }
        else
        {
            int x = (playerStartLocation.x / GameProperties.CHUNK_SIZE.x) - (GameProperties.CHUNK_VIEW_DISTANCE.x / 2);
            int z = (playerStartLocation.z / GameProperties.CHUNK_SIZE.z) - (GameProperties.CHUNK_VIEW_DISTANCE.z / 2);
            if(x<0) x=0;
            if(z<0) z=0;
            terrainOffset.setX(x);
            terrainOffset.setZ(z);
            terrainGenerator.generateTerrain(terrainOffset);
        }
        System.out.println("Invalid Locations: " + BlockChunkControl.invalidLocationCount);
    }

    private void setUpKeys()
    {
        inputManager.addMapping("Add", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("Destroy", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("bulletBigger", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("inventory", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Shoot", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("locateBlock", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("door", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("bulletFaster", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("bulletSlower", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("tree", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("bulletSmaller", new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));


        inputManager.addMapping("F1", new KeyTrigger(KeyInput.KEY_F1));
        inputManager.addMapping("F2", new KeyTrigger(KeyInput.KEY_F2));
        inputManager.addMapping("F3", new KeyTrigger(KeyInput.KEY_F3));
        inputManager.addMapping("F4", new KeyTrigger(KeyInput.KEY_F4));
        inputManager.addMapping("F5", new KeyTrigger(KeyInput.KEY_F5));
        inputManager.addMapping("F6", new KeyTrigger(KeyInput.KEY_F6));
        inputManager.addMapping("F7", new KeyTrigger(KeyInput.KEY_F7));
        inputManager.addMapping("F8", new KeyTrigger(KeyInput.KEY_F8));
        inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
        inputManager.addMapping("F10", new KeyTrigger(KeyInput.KEY_F10));
        inputManager.addMapping("F11", new KeyTrigger(KeyInput.KEY_F11));
        inputManager.addMapping("F12", new KeyTrigger(KeyInput.KEY_F12));

        inputManager.addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("4", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("5", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("6", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("7", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("8", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("9", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("9", new KeyTrigger(KeyInput.KEY_0));

        inputManager.addListener(new RunningActionListener(), "Jump", "Left", "Right", "Backward", "Forward", "Add", "Destroy", "Shoot", "bulletBigger", "bulletSmaller", "bulletFaster", "bulletSlower", "tree", "door", "locateBlock", "inventory",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12");

    }

    private class RunningActionListener implements ActionListener
    {
        public void onAction(String name, boolean isPressed, float tpf)
        {
            if ("Left".equals(name))
            {
                player.setLeft(isPressed);
            }
            else if ("Right".equals(name))
            {
                player.setRight(isPressed);
            }
            else if ("Backward".equals(name))
            {
                player.setBackward(isPressed);
            }
            else if ("Forward".equals(name))
            {
                player.setForward(isPressed);
            }
            else if ("Jump".equals(name))
            {
                player.setJumping(isPressed);
            }
            else if ("Add".equals(name) && !isPressed)
            {
                player.placeBlock();
            }
            else if ("Destroy".equals(name) && !isPressed)
            {
                player.dig();
            }

            else if ("Shoot".equals(name) && !isPressed)
            {
                player.getGun().shoot();
            }
            else if ("bulletBigger".equals(name) && !isPressed)
            {
                player.getGun().bulletBigger();
            }
            else if ("bulletSmaller".equals(name) && !isPressed)
            {
                player.getGun().bulletSmaller();
            }
            else if ("bulletFaster".equals(name) && !isPressed)
            {
                player.getGun().bulletFaster();
            }
            else if ("bulletSlower".equals(name) && !isPressed)
            {
                player.getGun().bulletSlower();
            }
            else if("tree".equals(name) && !isPressed)
            {
               player.placeTree();
            }
            else if("door".equals(name) && !isPressed)
            {
               player.placeDoor();
            }
            else if("locateBlock".equals(name) && !isPressed)
            {
               player.locateBlock();
            }
            else if("inventory".equals(name) && !isPressed)
            {
                System.out.println("inventory screen");
                nifty.fromXml("Interface/inventory.xml", "inventoryScreen", inventoryAppState);
                inputManager.setCursorVisible(true);
                player.setEnabled(false);
                app.getFlyByCamera().setEnabled(false);
            }


            else if("1".equals(name) && !isPressed)
            {
                player.selectEquippedItem(1);
            }
            else if("2".equals(name) && !isPressed)
            {
                player.selectEquippedItem(2);
            }
            else if("3".equals(name) && !isPressed)
            {
                player.selectEquippedItem(3);
            }
            else if("4".equals(name) && !isPressed)
            {
                player.selectEquippedItem(4);
            }
            else if("5".equals(name) && !isPressed)
            {
                player.selectEquippedItem(5);
            }
            else if("6".equals(name) && !isPressed)
            {
                player.selectEquippedItem(6);
            }
            else if("7".equals(name) && !isPressed)
            {
                player.selectEquippedItem(7);
            }
            else if("8".equals(name) && !isPressed)
            {
                player.selectEquippedItem(8);
            }
            else if("9".equals(name) && !isPressed)
            {
                player.selectEquippedItem(9);
            }
            else if("0".equals(name) && !isPressed)
            {
            }

            else if ("F1".equals(name) && !isPressed)
            {
                gameSaveManager.saveGame();
            }
            else if ("F2".equals(name) && !isPressed)
            {
                gameSaveManager.loadWorld();
            }
            else if ("F3".equals(name) && !isPressed)
            {
                hudControl.toggleDebug();
            }
            else if("F4".equals(name) && !isPressed)
            {
            }
            else if ("F5".equals(name) && !isPressed)
            {
                app.toggleDebug();
            }
            else if("F6".equals(name) && !isPressed)
            {
            }
            else if ("F7".equals(name) && !isPressed)
            {
                blockTerrain.experiment();
            }
            else if("F8".equals(name) && !isPressed)
            {
                blockTerrain.disposeOldChunks();
            }
            else if("F9".equals(name) && !isPressed)
            {
            }
            else if("F10".equals(name) && !isPressed)
            {
                // throws NPE for some inexplicaple reason
                //player.safeLocation(terrainGenerator.getPlayerStart());
                player.safeLocation(new Vector3Int(5, GameProperties.CHUNK_SIZE.y, 5));
            }
            else if("F11".equals(name) && !isPressed)
            {
                autoRun = !autoRun;
                if(!autoRun)
                {
                    player.setJumping(false);
                    player.setForward(false);
                }
            }
            else if("F12".equals(name) && !isPressed)
            {
            }
        }
    }

    public BTControl getBlockTerrain()
    {
        return blockTerrain;
    }

    public Node getBlocks()
    {
        return blocks;
    }

    public BlockTerrainManager getTerrainManager()
    {
        return terrainManager;
    }
}
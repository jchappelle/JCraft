package com.chappelle.jcraft;

import Properties.GameProperties;
import Properties.MaterialProperties;
import com.chappelle.jcraft.blocks.Blocks;
import com.chappelle.jcraft.blocks.JBlock;
import com.chappelle.jcraft.blocks.JBlockHelper;
import com.cubes.BTControl;
import com.cubes.BlockChunkControl;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.debug.WireBox;
import java.io.Serializable;

public class PlayerControl extends AbstractControl implements PhysicsCollisionListener
{
    private BetterCharacterControl physicsCharacter;
    private Node characterNode;
    private float currentSpeed = GameProperties.PLAYER_WALK_SPEED;
    private int playerHealth = GameProperties.PLAYER_STARTING_HEALTH;
    private Vector3f forwardDir = new Vector3f();
    private Vector3f leftDir = new Vector3f();
    private Camera cam;
    private BulletAppState physics;
    private GunControl gunControl;
    private boolean left;
    private boolean right;
    private boolean backward;
    private boolean forward;
    private BlockGame app;
    private Vector3f walkDirection = new Vector3f();
    private JBlockHelper blockHelper;
    private BTControl blockTerrain;
    private float playerHeight;
    private BlockTerrainManager terrainManager;
    private boolean jumping;
    private Vector3Int playerStartLocation;
    private Inventory inventory;
    private int selectedItemIndex = 0;
    private AssetManager assetManager;
    private Node rootNode;
    private Geometry wireBox;
    private float halfBlockSize = GameProperties.CUBE_SIZE / 2;

    public PlayerControl(BlockGame app, CubesSettings cubeSettings, final JBlockHelper blockHelper)
    {
        this.app = app;
        this.characterNode = new Node("player");
        this.blockHelper = blockHelper;
        this.cam = app.getCamera();
        this.physics = app.getStateManager().getState(BulletAppState.class);
        this.blockTerrain = app.getStateManager().getState(RunningAppState.class).getBlockTerrain();
        this.physics.getPhysicsSpace().addCollisionListener(this);
        this.terrainManager = app.getStateManager().getState(RunningAppState.class).getTerrainManager();
        this.inventory = new Inventory();
        this.assetManager = app.getAssetManager();
        this.rootNode = app.getRootNode();
    }

    /**
     * Selects the item from the equipped items section on the HUD
     *
     * @param index 1-based index of the equipped items on the HUD
     */
    public void selectEquippedItem(int index)
    {
        selectedItemIndex = index;
        inventory.selectItem(index);
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    @Override
    public void setSpatial(Spatial spatial)
    {
        super.setSpatial(spatial);

        if (spatial instanceof Node)
        {
            Node parentNode = (Node) spatial;
            parentNode.attachChild(characterNode);

            this.blockTerrain = app.getStateManager().getState(RunningAppState.class).getBlockTerrain();

            this.gunControl = new GunControl(app, this);
            characterNode.addControl(gunControl);
            characterNode.setLocalTranslation(getPlayerStartLocation().toVector3f());
            characterNode.setUserData("player", this);

            if (GameProperties.SCALE_PLAYER_WITH_BLOCKS)
            {
                this.playerHeight = GameProperties.CUBE_SIZE * 2;
            }
            else
            {
                this.playerHeight = 6;
            }

            physicsCharacter = new BetterCharacterControl((GameProperties.CUBE_SIZE / 2), playerHeight, 5);
            characterNode.addControl(physicsCharacter);
            physics.getPhysicsSpace().add(physicsCharacter);
            physicsCharacter.setDuckedFactor(0.5f);

            physicsCharacter.setJumpForce(GameProperties.PLAYER_JUMP_FORCE);
            physicsCharacter.setGravity(GameProperties.WORLD_GRAVITY);

            this.wireBox = createWireBox(halfBlockSize, ColorRGBA.Blue);
//            this.rootNode.attachChild(wireBox);
            this.wireBox.setCullHint(Spatial.CullHint.Always);

            this.inventory.add(Blocks.GRASS, 64);
            this.inventory.add(Blocks.SAND, 64);
            this.inventory.add(Blocks.GRAVEL, 64);
            this.inventory.add(Blocks.TORCH, 64);
            this.inventory.add(Blocks.MOSSY_STONE_BRICK, 64);
        }
    }

    public void safeLocation(Vector3Int location)
    {
        setPlayerStartLocation(location);
        System.out.println("Resetting player location to " + getPlayerStartLocation());
        physicsCharacter.warp(getPlayerStartLocation().toVector3f());
    }

    private Vector3Int getPlayerStartLocation()
    {
        return playerStartLocation;
    }

    public void setPlayerStartLocation(Vector3Int playerStartLocation)
    {
        this.playerStartLocation = Vector3Int.fromVector3f(Utils.translateBlockToWorldLocation(playerStartLocation.toVector3f()));
    }

    public float getPlayerHeight()
    {
        return playerHeight;
    }

    public Node getPlayerNode()
    {
        return characterNode;
    }

    @Override
    protected void controlUpdate(float tpf)
    {
        if (isEnabled())
        {
            forwardDir.set(cam.getDirection()).multLocal(currentSpeed, 0.0f, currentSpeed);
            leftDir.set(cam.getLeft()).multLocal(currentSpeed);

            walkDirection.set(0, 0, 0);
            if (left)
            {
                walkDirection.addLocal(leftDir);
            }
            if (right)
            {
                walkDirection.addLocal(leftDir.negate());
            }
            if (forward)
            {
                walkDirection.addLocal(forwardDir);
            }
            if (backward)
            {
                walkDirection.addLocal(forwardDir.negate());
            }
            physicsCharacter.setWalkDirection(walkDirection);
            playerLocationSanityCheck();
            cam.setLocation(characterNode.getLocalTranslation().add(0, GameProperties.CUBE_SIZE + 1, 0));
            if (jumping)
            {
                physicsCharacter.jump();
            }
            highlightPointedBlock();
        }
    }

    private void highlightPointedBlock()
    {
        Vector3Int location = blockHelper.getPointedBlockLocationInChunkSpace(false);

        if(location == null)
        {
            wireBox.setCullHint(Spatial.CullHint.Always);
        }
        else
        {
            wireBox.setCullHint(Spatial.CullHint.Never);
            location.multLocal((int)GameProperties.CUBE_SIZE);
            Vector3f pos = location.toVector3f().addLocal(halfBlockSize, halfBlockSize, halfBlockSize);
            wireBox.setLocalTranslation(pos);
        }
    }

    public Geometry createWireBox(float size, ColorRGBA color)
    {
        Geometry g = new Geometry("wireframe cube", new WireBox(size, size, size));
        Material mat = new Material(assetManager, MaterialProperties.DEFAULT_UNSHADED);
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        return g;
    }

    private void playerLocationSanityCheck()
    {
        float i = getPhysicsLocation().getY();
        if (i < -10)
        {
            System.out.println("Resetting player location to " + getPlayerStartLocation());
            physicsCharacter.warp(getPlayerStartLocation().toVector3f());
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
    }

    public void setLeft(boolean left)
    {
        this.left = left;
    }

    public void setRight(boolean right)
    {
        this.right = right;
    }

    public void setForward(boolean forward)
    {
        this.forward = forward;
    }

    public void setBackward(boolean backward)
    {
        this.backward = backward;
    }

    public boolean isMoved()
    {
        return left || right || forward || backward;
    }

    public void setJumping(boolean jumping)
    {
        this.jumping = jumping;
    }

    public boolean isJumping()
    {
        return jumping;
    }

    public Vector3f getPhysicsLocation()
    {
        return characterNode.getLocalTranslation();
    }

    public Vector3f getWorldLocation()
    {
        return characterNode.getLocalTranslation().divide(GameProperties.CUBE_SIZE);
    }

    public Vector3Int getChunkLocation()
    {
        return Vector3Int.fromVector3f(getWorldLocation().divide(GameProperties.CHUNK_SIZE.toVector3f()));
    }

    /**
     * Causes the player to place a block at the pointed location
     */
    public void placeBlock()
    {
        ItemStack selectedItemStack = inventory.getEquippedSlot(selectedItemIndex);
        if (selectedItemStack != null)
        {
            PickedBlock pickedBlock = blockHelper.pickNeighborBlock();
            Vector3Int blockLocation = pickedBlock.getBlockLocation();
            if (blockLocation != null)
            {
                if (canPlaceBlock())
                {
                    terrainManager.setBlock(pickedBlock, selectedItemStack.getBlock());
                    inventory.subtract(selectedItemStack);
                }
            }
        }
    }

    public void placeTree()
    {
        Vector3Int blockLocation = blockHelper.getPointedBlockLocationInChunkSpace(true);
        if (blockLocation != null)
        {
            if (canPlaceBlock())
            {
                blockHelper.setTree(blockLocation);
            }
        }
    }

    public void locateBlock()
    {
        Vector3Int blockLocation = blockHelper.getPointedBlockLocationInChunkSpace(true);
        BlockChunkControl chunk = blockTerrain.fetchExistingChunkForBlockLocation(blockLocation);
        System.out.println("Player: " + getPhysicsLocation());
        System.out.println("Chunk space: " + blockLocation);
        System.out.println("World space: " + blockHelper.getPointedBlockLocationInWorldSpace());
        System.out.println("Chunk Location: " + chunk.getLocation());
        System.out.println("Block Location: " + chunk.getBlockLocation());

        System.out.println(blockHelper.pickBlock());
    }

    public void placeDoor()
    {
        Vector3Int blockLocation = blockHelper.getPointedBlockLocationInChunkSpace(true);
        if (blockLocation != null)
        {
            if (canPlaceBlock())
            {
                blockTerrain.setBlock(blockLocation, Blocks.WOODEN_DOOR_BOTTOM);
                blockTerrain.setBlock(blockLocation.add(new Vector3Int(0, 1, 0)), Blocks.WOODEN_DOOR_TOP);
            }
        }
    }

    public boolean canPlaceBlock()
    {
        float distanceToDesiredPlacement = getPhysicsLocation().distance(JBlockHelper.toVector(blockHelper.getPointedBlockLocationInWorldSpace()));
        return distanceToDesiredPlacement > GameProperties.CUBE_SIZE * 1.25 && distanceToDesiredPlacement < GameProperties.CUBE_SIZE * GameProperties.BLOCK_INTERACTION_RANGE + 1;
    }

    public boolean canDigBlock()
    {
        float distanceToDesiredPlacement = getPhysicsLocation().distance(JBlockHelper.toVector(blockHelper.getPointedBlockLocationInWorldSpace()));
        return distanceToDesiredPlacement < GameProperties.CUBE_SIZE * GameProperties.BLOCK_INTERACTION_RANGE;
    }

    public void hit(Enemy enemy)
    {
        playerHealth -= enemy.getDamageToPlayer();
        if (playerHealth <= 0)
        {
            System.out.println("Dude you died!");
            playerHealth = 100;
        }
    }

    /**
     * Causes the player to dig at the pointed location
     */
    public void dig()
    {
        PickedBlock target = blockHelper.pickBlock();
        if (target != null)
        {
            if (canDigBlock())
            {
                terrainManager.removeBlock(target.getBlock(), target.getBlockLocation());
            }
        }
    }

    public Vector3Int getPointedBlockLocationInWorldSpace()
    {
        return blockHelper.getPointedBlockLocationInWorldSpace();
    }

    public int getHealth()
    {
        return playerHealth;
    }

    public void collectItem(JBlock block)
    {
        inventory.add(block);
    }

    public GunControl getGun()
    {
        return gunControl;
    }

    @Override
    public void collision(PhysicsCollisionEvent event)
    {
        if (event.getNodeA() != null && event.getNodeB() != null)
        {
            Vector3f location = null;
            if ("player".equals(event.getNodeA().getName()))
            {
                Object enemyObj = event.getNodeB().getUserData("enemy");
                if (enemyObj != null)
                {
                    Enemy enemy = (Enemy) enemyObj;
                    hit(enemy);
                }
            }
            else if ("player".equals(event.getNodeB().getName()))
            {
                location = event.getLocalPointB();
                Object enemyObj = event.getNodeA().getUserData("enemy");
                if (enemyObj != null)
                {
                    Enemy enemy = (Enemy) enemyObj;
                    hit(enemy);
                }
            }
            if (location != null)
            {
                if (isMoved())
                {
                    JBlock block = blockHelper.getSteppedOnBlock(characterNode.getLocalTranslation());
                    if (block != null)
                    {
                        AudioNode audio = block.getStepAudio();
                        if (audio != null)
                        {
                            if (audio.getStatus() != AudioSource.Status.Playing)
                            {
                                audio.play();
                            }
                        }
                    }
                }
            }
        }
    }
}

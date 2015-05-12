package com.cubes.test;

import Properties.GameProperties;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.cubes.*;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import terrain.TerrainGeneratorFactory;
import terrain.TerrainGeneratorFactory;

public class MyPhysicsDemo extends SimpleApplication implements ActionListener{

    public static void main(String[] args){
        Logger.getLogger("").setLevel(Level.SEVERE);
        MyPhysicsDemo app = new MyPhysicsDemo();
        app.start();
    }

    public MyPhysicsDemo(){
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cubes Demo - Physics");
        settings.setFrameRate(60);
    }
    private final Vector3Int terrainSize = new Vector3Int(512, 30, 512);
    private BulletAppState bulletAppState;
    private CharacterControl playerControl;
    private Vector3f walkDirection = new Vector3f();
    private boolean[] arrowKeys = new boolean[4];
    private CubesSettings cubesSettings;
    private BlockTerrainControl blockTerrain;
    private Node terrainNode = new Node();
    private BitmapText pointedAtBlockLocationHUD;
    private BitmapText currentPlayerLocationHUD;
    @Override
    public void simpleInitApp(){
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        initControls();
        initBlockTerrain();
        initPlayer();
        initHUD();
        cam.lookAtDirection(new Vector3f(1, 0, 1), Vector3f.UNIT_Y);
    }

    private void initControls(){
        inputManager.addMapping("move_left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("move_right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("move_up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("move_down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "move_left");
        inputManager.addListener(this, "move_right");
        inputManager.addListener(this, "move_up");
        inputManager.addListener(this, "move_down");
        inputManager.addListener(this, "jump");
    }

    private void initBlockTerrain(){
        CubesTestAssets.registerBlocks();
        CubesTestAssets.initializeEnvironment(this);

        cubesSettings = CubesTestAssets.getSettings(this);
        blockTerrain = new BlockTerrainControl(cubesSettings, new Vector3Int(16, 1, 16));

        blockTerrain.setBlocksFromNoise(new Vector3Int(), terrainSize, 0.8f, CubesTestAssets.BLOCK_GRASS);
       // TerrainGeneratorFactory.makeTerrainGenerator(TerrainGeneratorFactory.GeneratorType.FLATLAND, null, blockTerrain).generateTerrain();
        blockTerrain.addChunkListener(new BlockChunkListener(){

            @Override
            public void onSpatialUpdated(BlockChunkControl blockChunk){
                Geometry optimizedGeometry = blockChunk.getOptimizedGeometry_Opaque();
                RigidBodyControl rigidBodyControl = optimizedGeometry.getControl(RigidBodyControl.class);
                if(rigidBodyControl == null){
                    rigidBodyControl = new RigidBodyControl(0);
                    optimizedGeometry.addControl(rigidBodyControl);
                    bulletAppState.getPhysicsSpace().add(rigidBodyControl);
                    rigidBodyControl.setFriction(1.0f);
                }
                rigidBodyControl.setCollisionShape(new MeshCollisionShape(optimizedGeometry.getMesh()));
            }
        });
        terrainNode.addControl(blockTerrain);
        terrainNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(terrainNode);
    }

    private void initPlayer(){
        playerControl = new CharacterControl(new CapsuleCollisionShape((GameProperties.CUBE_SIZE / 2), GameProperties.CUBE_SIZE * 2), 0.05f);
        playerControl.setJumpSpeed(25);
        playerControl.setFallSpeed(20);
        playerControl.setGravity(70);
        playerControl.setPhysicsLocation(new Vector3f(5, terrainSize.getY() + 5, 5).mult(GameProperties.CUBE_SIZE));
        bulletAppState.getPhysicsSpace().add(playerControl);
    }

    @Override
    public void simpleUpdate(float lastTimePerFrame){
        float playerMoveSpeed = ((GameProperties.CUBE_SIZE * 6.5f) * lastTimePerFrame);
        Vector3f camDir = cam.getDirection().mult(playerMoveSpeed);
        Vector3f camLeft = cam.getLeft().mult(playerMoveSpeed);
        walkDirection.set(0, 0, 0);
        if(arrowKeys[0]){ walkDirection.addLocal(camDir); }
        if(arrowKeys[1]){ walkDirection.addLocal(camLeft.negate()); }
        if(arrowKeys[2]){ walkDirection.addLocal(camDir.negate()); }
        if(arrowKeys[3]){ walkDirection.addLocal(camLeft); }
        walkDirection.setY(0);
        playerControl.setWalkDirection(walkDirection);
        cam.setLocation(playerControl.getPhysicsLocation());




        Vector3Int pointedLocation = getCurrentPointedBlockLocation(true);
        if(pointedLocation != null)
        {
                this.pointedAtBlockLocationHUD.setText("Pointed: [" + pointedLocation.getX() + "," + pointedLocation.getY() + "," + pointedLocation.getZ() + "]");
        }
        else
        {
            this.pointedAtBlockLocationHUD.setText("Pointed: NULL");
        }
        this.currentPlayerLocationHUD.setText("Location: " + toString(playerControl.getPhysicsLocation()));
    }

    private String toString(Vector3f vector)
    {
        return "[" + (int)vector.x + "," + (int)vector.y + "," + (int)vector.z + "]";
    }

    private CollisionResults getRayCastingResults(Node node)
    {
        Vector3f origin = cam.getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();
        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        node.collideWith(ray, results);
        return results;
    }

    public Vector3Int getCurrentPointedBlockLocation(boolean getNeighborLocation)
    {
        CollisionResults results = getRayCastingResults(terrainNode);
        if (results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
        Vector3f collisionLocation = Util.compensateFloatRoundingErrors(collisionContactPoint);
//        Vector3Int blockLocation = new Vector3Int(
//                (int) (collisionLocation.getX() / blockTerrain.getSettings().getBlockSize()),
//                (int) (collisionLocation.getY() / blockTerrain.getSettings().getBlockSize()),
//                (int) (collisionLocation.getZ() / blockTerrain.getSettings().getBlockSize()));

        Vector3Int blockLocation = new Vector3Int(
                (int) (collisionLocation.getX()),
                (int) (collisionLocation.getY()),
                (int) (collisionLocation.getZ()));

            return blockLocation;
//            return BlockNavigator.getPointedBlockLocation(blockTerrain, collisionContactPoint, getNeighborLocation);
        }
        return null;
    }

    protected void initHUD()
    {
        guiNode.detachAllChildren();

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");


        BitmapText crosshairs = new BitmapText(guiFont, false);
        crosshairs.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        crosshairs.setText("+");
        crosshairs.setLocalTranslation(settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2, settings.getHeight() / 2 + crosshairs.getLineHeight() / 2, 0);
        guiNode.attachChild(crosshairs);

        currentPlayerLocationHUD = new BitmapText(guiFont, false);
        currentPlayerLocationHUD.setSize(guiFont.getCharSet().getRenderedSize());
        currentPlayerLocationHUD.setLocalTranslation(100, 220, 0);
        guiNode.attachChild(currentPlayerLocationHUD);

        pointedAtBlockLocationHUD = new BitmapText(guiFont, false);
        pointedAtBlockLocationHUD.setSize(guiFont.getCharSet().getRenderedSize());
        pointedAtBlockLocationHUD.setLocalTranslation(100, 260, 0);
        guiNode.attachChild(pointedAtBlockLocationHUD);

    }

    @Override
    public void onAction(String actionName, boolean value, float lastTimePerFrame){
        if(actionName.equals("move_up")){
            arrowKeys[0] = value;
        }
        else if(actionName.equals("move_right")){
            arrowKeys[1] = value;
        }
        else if(actionName.equals("move_left")){
            arrowKeys[3] = value;
        }
        else if(actionName.equals("move_down")){
            arrowKeys[2] = value;
        }
        else if(actionName.equals("jump")){
            playerControl.jump();
        }
    }
}

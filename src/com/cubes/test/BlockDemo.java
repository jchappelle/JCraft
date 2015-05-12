package com.cubes.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.cubes.*;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class BlockDemo extends SimpleApplication
{

    public static void main(String[] args)
    {
        Logger.getLogger("").setLevel(Level.SEVERE);
        BlockDemo app = new BlockDemo();
        app.start();
    }

    public BlockDemo()
    {
        showSettings = false;
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cubes Demo - Tutorial");
    }

    @Override
    public void simpleInitApp()
    {
        CubesTestAssets.registerBlocks();


        BlockTerrainControl blockTerrain = new BlockTerrainControl(CubesTestAssets.getSettings(this), new Vector3Int(1, 1, 1));


        blockTerrain.setBlock(new Vector3Int(0, 0, 0), CubesTestAssets.BLOCK_GLASS);
        Node terrainNode = new Node();
        terrainNode.addControl(blockTerrain);
        rootNode.attachChild(terrainNode);

        cam.setLocation(new Vector3f(-10, 10, 16));
        cam.lookAtDirection(new Vector3f(1, -0.56f, -1), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(50);

        BulletAppState physics = new BulletAppState();

        stateManager.attach(physics);
        physics.getPhysicsSpace().setWorldMin(new Vector3f(0, 0, 0));
        physics.getPhysicsSpace().setWorldMax(new Vector3f(400, 400, 400));
        physics.getPhysicsSpace().setMaxSubSteps(4);
        physics.setDebugEnabled(true);

        Geometry geom = makeBox(new Vector3f(0, 5, 0));
        GhostControl ghost1 = new GhostControl(new BoxCollisionShape(new Vector3f(2, 5, 2)));
        geom.addControl(ghost1);
        physics.getPhysicsSpace().add(ghost1);

        geom = makeBox(new Vector3f(0, 10, 0));
        ghost1 = new GhostControl(new BoxCollisionShape(new Vector3f(2, 5, 2)));
        geom.addControl(ghost1);
        physics.getPhysicsSpace().add(ghost1);

        physics.getPhysicsSpace().addCollisionListener(new PhysicsCollisionListener() {

            public void collision(PhysicsCollisionEvent event)
            {
                System.out.println("A: " + event.getNodeA());
                System.out.println("B: " + event.getNodeB());
            }
        });

    }

    private Geometry makeBox(Vector3f location)
    {
        Box b = new Box(3, 3, 3);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/demos/dirt.jpg");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        mat.setTexture("ColorMap", tex2);
        geom.setMaterial(mat);
        geom.setLocalTranslation(location);
        geom.scale(0.15f);
        Node geomNode = new Node("item");
        geomNode.attachChild(geom);
        rootNode.attachChild(geomNode);
        return geom;
    }
}

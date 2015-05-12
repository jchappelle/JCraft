package com.chappelle.jcraft;

import Properties.GameProperties;
import Properties.SoundProperties;
import com.chappelle.jcraft.blocks.JBlock;
import com.cubes.BlockChunkControl;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;

public class ItemControl extends AbstractControl
{

    private long timeToLive = GameProperties.ITEM_TTL_SECONDS * 1000L;
    private Node node;
    private AssetManager assetManager;
    private CubesSettings cubeSettings;
    private List<ActiveGeometry> geometries = new ArrayList<ActiveGeometry>();
    private List<ActiveGeometry> doomed = new ArrayList<ActiveGeometry>();
    private BulletAppState physics;

    public ItemControl(BlockGame app, CubesSettings cubeSettings)
    {
        this.cubeSettings = cubeSettings;
        this.assetManager = app.getAssetManager();
        this.node = new Node();
        this.physics = app.getStateManager().getState(BulletAppState.class);
    }

    @Override
    protected void controlUpdate(float tpf)
    {
        for (ActiveGeometry activeGeometry : geometries)
        {
            Geometry geometry = activeGeometry.getGeometry();
            if (activeGeometry.isExpired())
            {
                physics.getPhysicsSpace().remove(geometry.getControl(GhostControl.class));
                geometry.removeControl(GhostControl.class);
                node.detachChild(geometry.getParent());
                doomed.add(activeGeometry);
            }
            else
            {
                checkCollisions(activeGeometry);
            }
            activeGeometry.animate(tpf);
        }
        geometries.removeAll(doomed);
        doomed.clear();
    }

    private void checkCollisions(ActiveGeometry g)
    {
        Geometry geometry = g.getGeometry();
        GhostControl ghost = geometry.getParent().getControl(GhostControl.class);
        for (PhysicsCollisionObject obj : ghost.getOverlappingObjects())
        {

            if (obj instanceof PhysicsRigidBody)
            {
                PhysicsRigidBody rigidBody = (PhysicsRigidBody) obj;
                if (rigidBody.getUserObject() instanceof Node)
                {
                    Node collisionNode = (Node) rigidBody.getUserObject();
                    if (collisionNode.getUserData("player") instanceof PlayerControl)
                    {
                        PlayerControl player = (PlayerControl) collisionNode.getUserData("player");
                        player.collectItem(g.getBlock());

                        physics.getPhysicsSpace().remove(ghost);
                        geometry.getParent().removeControl(GhostControl.class);
                        node.detachChild(geometry.getParent());
                        doomed.add(g);
                        Utils.PlaySound(SoundProperties.PICKUP_ITEM);
                    }
                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
    }

    @Override
    public void setSpatial(Spatial spatial)
    {
        super.setSpatial(spatial);

        if (spatial instanceof Node)
        {
            Node parentNode = (Node) spatial;
            parentNode.attachChild(node);
        }
    }

    public void addItemAt(JBlock block, BlockChunkControl chunk, Vector3Int blockLocation, Vector3f itemLocation)
    {
        Mesh dropItemMesh = block.makeDropItemMesh(chunk, chunk.getTerrain().getLocalBlockLocation(blockLocation, chunk));
        if (dropItemMesh != null)
        {
            Geometry geom = new Geometry("");
            geom.setMaterial(cubeSettings.getBlockMaterial());
            geom.setMesh(dropItemMesh);

            Node geomNode = new Node("item");
            geomNode.setLocalTranslation(itemLocation);
            geomNode.attachChild(geom);
            geom.center();

            node.attachChild(geomNode);

            //Set up collision detection
            GhostControl ghost = new GhostControl(new SphereCollisionShape(5.0f));
            geomNode.addControl(ghost);
            physics.getPhysicsSpace().add(ghost);
            ghost.setPhysicsLocation(itemLocation);

            //Remember this geometry so we can animate it and eventually expire it
            geometries.add(new ActiveGeometry(block, geom));
        }
    }

    private class ActiveGeometry
    {

        private long time;
        private JBlock block;
        private Geometry geometry;
        private Node parent;
        private int animationDegrees = 0;
        private int animationPrecision = 5;
        private int rotationMultiplier = 1;
        private int floatingMultiplier = 1;

        public ActiveGeometry(JBlock block, Geometry geometry)
        {
            this.block = block;
            this.time = System.currentTimeMillis();
            this.geometry = geometry;
            this.parent = geometry.getParent();
        }

        public JBlock getBlock()
        {
            return block;
        }

        public void animate(float tpf)
        {
            parent.rotate(0.0f, rotationMultiplier * tpf, 0.0f);

            double sin = Math.sin(toRadians(animationDegrees));
            float delta = (float) sin * tpf * floatingMultiplier;
            Vector3f currentLocation = geometry.getLocalTranslation().addLocal(0.0f, delta, 0.0f);
            geometry.setLocalTranslation(currentLocation);
            animationDegrees += animationPrecision;
        }

        private float toRadians(float degrees)
        {
            return FastMath.PI * (degrees) / 180;
        }

        public Geometry getGeometry()
        {
            return geometry;
        }

        public boolean isExpired()
        {
            return (System.currentTimeMillis() - time) > timeToLive;
        }
    }
}

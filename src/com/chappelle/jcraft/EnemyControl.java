package com.chappelle.jcraft;

import Properties.MaterialProperties;
import Properties.TextureProperties;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.texture.Texture;

public class EnemyControl extends AbstractControl
{
    private Node node;
    private AssetManager assetManager;
    private BulletAppState physics;
    private BlockGame app;

    public EnemyControl(BlockGame app)
    {
        this.node = new Node();
        this.assetManager = app.getAssetManager();
        this.app = app;

    }

    @Override
    public void setSpatial(Spatial spatial)
    {
        super.setSpatial(spatial);
        if(spatial instanceof Node)
        {
            Node parentNode = (Node)spatial;
            parentNode.attachChild(node);
        }
        this.physics = app.getStateManager().getState(BulletAppState.class);
    }

    public void spawnEnemy(Vector3f location)
    {
        Material mat = new Material(assetManager, MaterialProperties.DEFAULT_UNSHADED);
        TextureKey key2 = new TextureKey(TextureProperties.ROCK);
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        mat.setTexture("ColorMap", tex2);

        Spatial enemy = assetManager.loadModel("Models/dragon.j3o");
        enemy.setUserData("enemy", new Enemy());
        enemy.setMaterial(mat);
        node.attachChild(enemy);
        CollisionShape collisionShape = new BoxCollisionShape(new Vector3f(1.6f, 2.1f, 2.0f));
        RigidBodyControl rigid = new RigidBodyControl(collisionShape, 5);
        enemy.addControl(rigid);
        rigid.setPhysicsLocation(location);
        physics.getPhysicsSpace().add(enemy);

    }

    @Override
    protected void controlUpdate(float tpf)
    {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
    }

}

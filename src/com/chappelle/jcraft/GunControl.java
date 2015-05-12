package com.chappelle.jcraft;

import Properties.ProjectileProperties;
import Properties.TextureProperties;
import Properties.SoundProperties;
import Properties.MaterialProperties;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;

public class GunControl extends AbstractControl
{
    private float bulletRadius = ProjectileProperties.BULLET_INITIAL_RADIUS;
    private int bulletPower = ProjectileProperties.BULLET_INITIAL_POWER;
    private Material stone_mat;
    private Node node;
    private AssetManager assetManager;
    private Camera cam;
    private BulletAppState physics;
    private long timeToLive = ProjectileProperties.BULLET_TTL_SECONDS * 1000l;
    private List<ActiveProjectile> projectiles = new ArrayList<ActiveProjectile>();
    private List<ActiveProjectile> doomedProjectiles = new ArrayList<ActiveProjectile>();
    private PlayerControl player;

    public GunControl(BlockGame app, PlayerControl player)
    {
        this.node = new Node();
        this.assetManager = app.getAssetManager();
        this.physics = app.getStateManager().getState(BulletAppState.class);
        this.cam = app.getCamera();
        this.player = player;
    }

    @Override
    protected void controlUpdate(float tpf)
    {
        for(ActiveProjectile projectile : projectiles)
        {
            if(projectile.isExpired())
            {
                doomedProjectiles.add(projectile);
                node.detachChild(projectile.getGeometry());
            }
        }
        projectiles.removeAll(doomedProjectiles);
        doomedProjectiles.clear();
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
            setUpMaterials();
        }
    }

    private void setUpMaterials()
    {
        stone_mat = new Material(assetManager, MaterialProperties.DEFAULT_UNSHADED);
        TextureKey key2 = new TextureKey(TextureProperties.ROCK);
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);
    }

    public void shoot()
    {
        Sphere sphere = new Sphere(32, 32, bulletRadius, true, false);
        Geometry ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        ball_geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        int id = node.attachChild(ball_geo);
        System.out.println("Shooting: " + id);
        projectiles.add(new ActiveProjectile(ball_geo));
        /**
         * Position the cannon ball
         */


        Vector3f location = ball_geo.getLocalTranslation().add(cam.getDirection().mult(4 + bulletRadius));
        location.addLocal(new Vector3f(0.0f, 3.0f, 0.0f));
         ball_geo.setLocalTranslation(location);
        /**
         * Make the ball physcial with a mass > 0.0f
         */
//        ball_geo.setLocalTranslation(player.getPlayerNode().getLocalTranslation().add(new Vector3f));
        RigidBodyControl ball_phy = new RigidBodyControl(1f);
        /**
         * Add physical ball to physics space.
         */
        ball_geo.addControl(ball_phy);
        physics.getPhysicsSpace().add(ball_phy);
        /**
         * Accelerate the physcial ball to shoot it.
         */
        System.out.println("Direction: " + cam.getDirection());

        ball_phy.setLinearVelocity(cam.getDirection().mult(bulletPower));
        Utils.PlaySound(SoundProperties.MISC_BOW_USE);
    }

    public void bulletBigger()
    {
        bulletRadius += ProjectileProperties.BULLET_SIZE_INCREMENT;
        if(bulletRadius > ProjectileProperties.BULLET_MAX_RADIUS)
        {
            bulletRadius = ProjectileProperties.BULLET_MAX_RADIUS;
        }
    }

    public void bulletSmaller()
    {
        bulletRadius -= ProjectileProperties.BULLET_SIZE_INCREMENT;
        if (bulletRadius < ProjectileProperties.BULLET_MIN_RADIUS)
        {
            bulletRadius = ProjectileProperties.BULLET_MIN_RADIUS;
        }
    }

    public void bulletFaster()
    {
        bulletPower += ProjectileProperties.BULLET_POWER_INCREMENT;
        if(bulletPower > ProjectileProperties.BULLET_MAX_POWER)
        {
            bulletPower = ProjectileProperties.BULLET_MAX_POWER;
        }
    }

    public void bulletSlower()
    {
        bulletPower -= ProjectileProperties.BULLET_POWER_INCREMENT;
        if (bulletPower < 0)
        {
            bulletPower = 0;
        }
    }

    public float getBulletRadius()
    {
        return bulletRadius;
    }

    public int getBulletPower()
    {
        return bulletPower;
    }

    private class ActiveProjectile
    {
        private long time;
        private Geometry geometry;

        public ActiveProjectile(Geometry geometry)
        {
            this.time = System.currentTimeMillis();
            this.geometry = geometry;
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

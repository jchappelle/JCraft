package com.chappelle.jcraft.blocks;

import com.cubes.Vector3Int;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;

/**
 * Control for creating block explosions. It adds a ParticleEmitter to the scene graph
 * and allows it to emit particles for 3 seconds before removing it.
 */
public class BlockExplosionControl extends AbstractControl
{
    private final AssetManager assetManager;
    private final Node node;
    private final List<ActiveExplosion> activeExplosions = new ArrayList<ActiveExplosion>();
    private final long explosionTTL = 3000L;//3 seconds
    private final List<ActiveExplosion> doomedExplosions = new ArrayList<ActiveExplosion>();
    public BlockExplosionControl(AssetManager assetManager)
    {
        this.assetManager = assetManager;
        this.node = new Node();
    }

    @Override
    protected void controlUpdate(float tpf)
    {
        for(ActiveExplosion explosion : activeExplosions)
        {
            if(explosion.isComplete())
            {
                explosion.stop();
                doomedExplosions.add(explosion);
            }
        }
        activeExplosions.removeAll(doomedExplosions);
        doomedExplosions.clear();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
    }

    @Override
    public void setSpatial(Spatial spatial)
    {
        super.setSpatial(spatial);
        if(spatial instanceof Node)
        {
            Node parentNode = (Node) spatial;
            parentNode.attachChild(node);
        }
    }
    
    public void explodeBlock(JBlock block, Vector3Int explosionLocation)
    {
        Vector3f locationVector = new Vector3f(explosionLocation.getX(), explosionLocation.getY(), explosionLocation.getZ());
        explode(block, locationVector);
    }
    
    private void explode(JBlock block, Vector3f explosionLocation)
    {
        ParticleEmitter emitter = block.getDigParticleEmitter();

        Node emitterNode = new Node();
        emitterNode.attachChild(emitter);
        emitterNode.setLocalTranslation(explosionLocation);
        node.attachChild(emitterNode);
        emitter.emitAllParticles();     
        
        activeExplosions.add(new ActiveExplosion(emitterNode));
    }
    
    private class ActiveExplosion
    {
        private final Node emitterNode;
        private final long startTime;
        
        public ActiveExplosion(Node emitterNode)
        {
            this.emitterNode = emitterNode;
            this.startTime = System.currentTimeMillis();
        }
        
        public void stop()
        {
            node.detachChild(emitterNode);
        }
        
        public boolean isComplete()
        {
            return System.currentTimeMillis() - startTime >= explosionTTL;
        }
    }    
}

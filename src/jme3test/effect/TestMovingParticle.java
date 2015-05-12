package jme3test.effect;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * Particle that moves in a circle.
 *
 * @author Kirill Vainer
 */
public class TestMovingParticle extends SimpleApplication {
    
    private ParticleEmitter emit;
    private float angle = 0;
    
    public static void main(String[] args) {
        TestMovingParticle app = new TestMovingParticle();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        emit = new ParticleEmitter("Emitter", Type.Triangle, 300);
        emit.setGravity(0, 0, 0);
        emit.setVelocityVariation(1);
        emit.setLowLife(1);
        emit.setHighLife(1);
        emit.setInitialVelocity(new Vector3f(0, .5f, 0));
        emit.setImagesX(15);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        emit.setMaterial(mat);
        
        rootNode.attachChild(emit);
        
        inputManager.addListener(new ActionListener() {
            
            public void onAction(String name, boolean isPressed, float tpf) {
                if ("setNum".equals(name) && isPressed) {
                    emit.setNumParticles(1000);
                }
            }
        }, "setNum");
        
        inputManager.addMapping("setNum", new KeyTrigger(KeyInput.KEY_SPACE));
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        angle += tpf;
        angle %= FastMath.TWO_PI;
        float x = FastMath.cos(angle) * 2;
        float y = FastMath.sin(angle) * 2;
        emit.setLocalTranslation(x, 0, y);
    }
}


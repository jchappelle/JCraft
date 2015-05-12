
package chunkControl;

import com.chappelle.jcraft.PlayerControl;
import com.chappelle.jcraft.blocks.JBlockHelper;
import com.cubes.BTControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import terrain.TerrainGenerator;

/**
 *
 * @author Perry
 */
public class ChunkControlManager extends AbstractControl
{

    private JBlockHelper blockHelper;
    private BTControl terrainControl;
    private Node rootNode;
    private PlayerControl playerControl;
    private TerrainGenerator terrainGenerator;


    public ChunkControlManager(Node rootNode, JBlockHelper blockHelper, BTControl terrainControl, PlayerControl playerControl, TerrainGenerator terrainGenerator)
    {
        this.blockHelper=blockHelper;
        this.terrainControl=terrainControl;
        this.playerControl = playerControl;
        this.rootNode = rootNode;
        this.terrainGenerator = terrainGenerator;
        rootNode.addControl(new ChunkChangeControl(playerControl, terrainControl, terrainGenerator));
    }

    @Override
    protected void controlUpdate(float tpf)
    {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chunkControl;

import Properties.GameProperties;
import com.chappelle.jcraft.PlayerControl;
import com.cubes.BTControl;
import com.cubes.Chunk;
import com.cubes.Vector3Int;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.HashSet;
import java.util.Set;
import terrain.TerrainGenerator;

/**
 *
 * @author Perry
 */
public class ChunkChangeControl extends AbstractControl
{

    private PlayerControl playerControl;
    private Vector3Int currentChunk;
    private Vector3Int previousChunk;
    private Vector3Int viewDistance = GameProperties.CHUNK_VIEW_DISTANCE;
    private Vector3Int modifier = viewDistance.divide(2);
    private BTControl terrain;
    private TerrainGenerator terrainGenerator;
    private Set<Vector3Int> chunksToUnload = new HashSet<>();
    private final int MAX_CHUNKS = GameProperties.CHUNK_VIEW_DISTANCE.x * GameProperties.CHUNK_VIEW_DISTANCE.z;

    public ChunkChangeControl(PlayerControl playerControl, BTControl terrain, TerrainGenerator terrainGenerator)
    {
        this.terrain = terrain;
        this.playerControl = playerControl;
        currentChunk = playerControl.getChunkLocation();
        previousChunk = playerControl.getChunkLocation();
        this.terrainGenerator = terrainGenerator;
    }

    @Override
    protected void controlUpdate(float tpf)
    {
        currentChunk = playerControl.getChunkLocation();
        if (currentChunk.x < previousChunk.x)
        {
            downX();
        }
        if (currentChunk.x > previousChunk.x)
        {
            upX();
        }
        if (currentChunk.z < previousChunk.z)
        {
            downZ();
        }
        if (currentChunk.z > previousChunk.z)
        {
            upZ();
        }
        for (Vector3Int c : chunksToUnload)
        {
            terrain.markAsDoomed(c);
        }
        chunksToUnload.clear();
        previousChunk = currentChunk;
        if(terrain.getDoomedChunks().size() == 0)
        {
            if(terrain.getChunkLocations().size() > MAX_CHUNKS)
            {
                lookForAndKillRogueChunks();
            }
        }
    }


    /*
     * this is a kludge
     */
    private void lookForAndKillRogueChunks()
    {
        for(Vector3Int c:terrain.getChunkLocations())
        {
            if(c.getX() > currentChunk.x + modifier.x ||
                c.getX() < currentChunk.x - modifier.x ||
                c.getZ() > currentChunk.z + modifier.z ||
                c.getZ() < currentChunk.z - modifier.z)
            {
                chunksToUnload.add(c);
            }
        }
    }

    private void downX()
    {
        int xUnload = previousChunk.x + modifier.x;
        int xLoad = currentChunk.x - modifier.x;
        int y = 0;
        for (int z = currentChunk.z - modifier.z; z <= currentChunk.z + modifier.z; z++)
        {
            Vector3Int newChunk = new Vector3Int(xLoad, y, z);
            if (!terrain.hasChunk(newChunk))
            {
                Chunk chunk = terrainGenerator.generateChunk(newChunk);
                terrain.scheduleChunkAdd(chunk);
            }
            chunksToUnload.add(new Vector3Int(xUnload, y, z));
        }
    }

    private void upX()
    {
        int xUnload = previousChunk.x - modifier.x;
        int xLoad = currentChunk.x + modifier.x;
        int y = 0;

        for (int z = currentChunk.z - modifier.z; z <= currentChunk.z + modifier.z; z++)
        {
            Vector3Int newChunk = new Vector3Int(xLoad, y, z);
            if (!terrain.hasChunk(newChunk))
            {
                Chunk chunk = terrainGenerator.generateChunk(newChunk);
                terrain.scheduleChunkAdd(chunk);
            }
            chunksToUnload.add(new Vector3Int(xUnload, y, z));

        }
    }

    private void downZ()
    {
        int zUnload = previousChunk.z + modifier.z;
        int zLoad = currentChunk.z - modifier.z;
        for (int x = currentChunk.x - modifier.x; x <= currentChunk.x + modifier.x; x++)
        {
            int y = 0;
            Vector3Int newChunk = new Vector3Int(x, y, zLoad);
            if (!terrain.hasChunk(newChunk))
            {
                Chunk chunk = terrainGenerator.generateChunk(newChunk);
                terrain.scheduleChunkAdd(chunk);
            }
            chunksToUnload.add(new Vector3Int(x, y, zUnload));
        }
    }

    private void upZ()
    {
        int zUnload = previousChunk.z - modifier.z;
        int zLoad = currentChunk.z + modifier.z;
        for (int x = currentChunk.x - modifier.x; x <= currentChunk.x + modifier.x; x++)
        {
            int y = 0;
            Vector3Int newChunk = new Vector3Int(x, y, zLoad);
            if (!terrain.hasChunk(newChunk))
            {
                Chunk chunk = terrainGenerator.generateChunk(newChunk);
                terrain.scheduleChunkAdd(chunk);
            }
            chunksToUnload.add(new Vector3Int(x, y, zUnload));
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

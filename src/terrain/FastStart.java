package terrain;

import Properties.GameProperties;
import com.chappelle.jcraft.blocks.Blocks;
import com.chappelle.jcraft.blocks.JBlock;
import com.cubes.BTControl;
import com.cubes.Chunk;
import com.cubes.Vector3Int;

/**
 *
 * @author Perry
 */
public class FastStart implements TerrainGenerator
{
    private BTControl terrainControl;
    private Vector3Int size = GameProperties.CHUNK_VIEW_DISTANCE;
    private Vector3Int offset;
    public FastStart(BTControl terrainControl)
    {
        this.terrainControl = terrainControl;
    }

    @Override
    public void generateTerrain(Vector3Int offset)
    {
        this.offset = offset;
        for (int x = 0 + offset.x; x < offset.x + size.x; x++)
        {
            for (int z = 0+offset.z; z < offset.z + size.z; z++)
            {
                Chunk c = generateChunk(new Vector3Int(x, 0, z));
                c.placeInTerrain(terrainControl);
            }
        }
        terrainControl.addAllScheduledChunks();
    }

    @Override
    public Chunk generateChunk(Vector3Int location)
    {
        JBlock block = Blocks.COAL_BLOCK;
        if(location.z %2 == 0)
        {
            if(location.x % 2 == 0) block = Blocks.BIRCHLOG;
        }
        else
        {
            if(location.x % 2 == 1) block = Blocks.BIRCHLOG;
        }

        Chunk chunk = new Chunk();
        chunk.setLocation(location);
        for (int x = 0; x < GameProperties.CHUNK_SIZE.x; x++)
        {
            for (int z = 0; z < GameProperties.CHUNK_SIZE.z; z++)
            {
                chunk.setBlock(new Vector3Int(x, 0, z), block);
            }
        }
        return chunk;
    }

    @Override
    public Vector3Int getPlayerStart()
    {
        Vector3Int result = GameProperties.CHUNK_SIZE.mult(GameProperties.CHUNK_VIEW_DISTANCE).divide(2);
        result.addLocal(offset.mult(GameProperties.CHUNK_SIZE));
        result.setY(3);
        return result;
    }

}

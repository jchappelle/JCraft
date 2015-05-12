/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import Properties.GameProperties;
import com.chappelle.jcraft.blocks.Blocks;
import com.chappelle.jcraft.blocks.JBlock;
import com.cubes.BTControl;
import com.cubes.Chunk;
import com.cubes.Vector3Int;
import java.util.Random;

/**
 *
 * @author Perry
 */
public class Flatland implements TerrainGenerator
{
    private BTControl terrainControl;
    private Random rndm;
    private Vector3Int offset;

    public Flatland(BTControl terrainControl)
    {
        this.terrainControl = terrainControl;
    }

    public void generateTerrain(Vector3Int offset)
    {
        this.offset = offset;
        long seed = GameProperties.SEED;
        if(seed == -1)
        {
            rndm = new Random(System.currentTimeMillis());
            rndm.nextLong();
            seed = rndm.nextLong();
        }
        System.out.print("Generating Terrain ");
        System.out.print(GameProperties.CHUNK_VIEW_DISTANCE);
        for (int x = 0 + offset.x; x < offset.x + GameProperties.CHUNK_VIEW_DISTANCE.x; x++)
        {
            for (int z = 0 + offset.z; z < offset.z + GameProperties.CHUNK_VIEW_DISTANCE.z; z++)
            {
                System.out.print(".");
                Chunk c = generateChunk(new Vector3Int(x, 0, z));
                c.placeInTerrain(terrainControl);
            }
        }
        System.out.println();
        terrainControl.addAllScheduledChunks();
    }

    @Override
    public Vector3Int getPlayerStart()
    {
        Vector3Int result = GameProperties.CHUNK_SIZE.mult(GameProperties.CHUNK_VIEW_DISTANCE).divide(2);
        result.addLocal(offset.mult(GameProperties.CHUNK_SIZE));
        int y = GameProperties.CHUNK_SIZE.y * GameProperties.CHUNK_VIEW_DISTANCE.y;
        while(terrainControl.getBlock(result.x, y, result.z) == null && y > 1)
        {
            y--;
        }
        result.setY(y+2);

        return result;
    }

    @Override
    public Chunk generateChunk(Vector3Int chunkLocation)
    {
        int yMax = GameProperties.CHUNK_SIZE.y - 5;
        Chunk chunk = new Chunk();
        chunk.setLocation(chunkLocation);
        for (int x = 0; x < GameProperties.CHUNK_SIZE.x; x++)
        {
            for (int z = 0; z < GameProperties.CHUNK_SIZE.z; z++)
            {
                for (int y = 0; y < yMax; y++)
                {

                    JBlock place = Blocks.STONE;
                    if( y == yMax - 1)
                    {
                        place = Blocks.GRASS;
                    }
                    else if ( y > yMax - 5)
                    {
                        place = Blocks.DIRT;
                    }
                    else
                    {
                        double rand = Math.random() * 5000 ;
                        if(rand < 1000)
                        {
                            place = Blocks.DIRT;
                        }
                        if(rand < 500)
                        {
                            place = Blocks.GRAVEL;
                        }
                        if(rand < 350)
                        {
                            place = Blocks.COAL_BLOCK;
                        }
                        if(rand < 250)
                        {
                            place = Blocks.IRON_ORE;
                        }
                        if(rand < 50 && y < 30)
                        {
                            place = Blocks.GOLD_ORE;
                        }
                        if(rand < 10 && y < 16)
                        {
                            place = Blocks.DIAMOND_ORE;
                        }
                    }
                    chunk.setBlock(x, y, z, place);
                }
                if(chunkLocation.y == 0)
                {
                    chunk.setBlock(x, 0, z, Blocks.BEDROCK);
                }

            }
        }
        return chunk;
    }
}

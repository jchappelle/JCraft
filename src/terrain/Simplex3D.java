/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import Properties.GameProperties;
import Properties.GeneratorProperties;
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
public class Simplex3D implements TerrainGenerator
{
    private BTControl terrainControl;
    private Vector3Int offset;

    public Simplex3D(BTControl terrainControl)
    {
        this.terrainControl = terrainControl;
    }

    @Override
    public void generateTerrain(Vector3Int offset)
    {
        this.offset = offset;
        long seed = GameProperties.SEED;
        if(seed == -1)
        {
            Random rndm = new Random(System.currentTimeMillis());
            rndm.nextLong();
            seed = rndm.nextLong();
        }
        SimplexNoise.setSeed(seed);

        System.out.print("Generating Terrain");
        for (int x = 0 + offset.z; x < offset.x + GameProperties.CHUNK_VIEW_DISTANCE.x; x++)
        {
            for (int z = 0+ offset.z; z < offset.z + GameProperties.CHUNK_VIEW_DISTANCE.z; z++)
            {
                Chunk c = generateChunk(new Vector3Int(x, 0, z));
                c.placeInTerrain(terrainControl);
            }
        }
        terrainControl.addAllScheduledChunks();
        System.out.println();
    }

    private double sumOctave(int num_iterations, double x, double y, double z, double persistence, double scale)
    {
        double maxAmp = 0;
        double amp = 1;
        double noise = 0;

        //#add successively smaller, higher-frequency terms
        for(int i = 0; i < num_iterations; ++i)
        {
            noise += SimplexNoise.noise3D(x, y, z, scale) * amp;
            maxAmp += amp;
            amp *= persistence;
            scale *= 2;
        }

        //take the average value of the iterations
        noise /= maxAmp;

        return noise;
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

    public Chunk generateChunk(Vector3Int chunkLocation)
    {
        Vector3Int offset = chunkLocation.mult(GameProperties.CHUNK_SIZE);
        Chunk chunk = new Chunk();
        chunk.setLocation(chunkLocation);
        for (int x = 0; x < GameProperties.CHUNK_SIZE.x; x++)
        {
            for (int z = 0; z < GameProperties.CHUNK_SIZE.z; z++)
            {
                for (int y = 0; y < GameProperties.CHUNK_SIZE.y; y++)
                {
                    Double c = sumOctave(GeneratorProperties.ITERATIONS, x+offset.x, y+offset.y, z+offset.z, .5, .007);
                    JBlock place = Blocks.SPRUCE_LOG;
                    if(c>.05)
                    {
                        int b = y % 15;
                            if(b == 0)
                            {
                                place = Blocks.LOG;
                            }
                            if(b == 1)
                            {
                                place = Blocks.SNOWBLOCK;
                            }
                            if(b == 2)
                            {
                                place = Blocks.LOG;
                            }
                            if(b == 3)
                            {
                                place = Blocks.SPRUCE_LOG;
                            }
                            if(b == 4)
                            {
                                place = Blocks.COAL_BLOCK;
                            }
                            if(b == 5)
                            {
                                place = Blocks.BLUE;
                            }
                            if(b == 6)
                            {
                                place = Blocks.CACTUS;
                            }
                            if(b == 7)
                            {
                                place = Blocks.CHEST;
                            }
                            if(b == 8)
                            {
                                place = Blocks.DIAMOND_ORE;
                            }
                            if(b == 9)
                            {
                                place = Blocks.GOLD_ORE;
                            }
                            if(b == 10)
                            {
                                place = Blocks.LAPIS_ORE;
                            }
                            if(b == 11)
                            {
                                place = Blocks.LAPIS_BLOCK;
                            }
                            if(b == 12)
                            {
                                place = Blocks.FURNACE;
                            }
                            if(b == 13)
                            {
                                place = Blocks.IRON_ORE;
                            }
                            chunk.setBlock(x, y, z, place);
                    }
                    if(c>.4)
                    {
                        chunk.setBlock(x, y, z, Blocks.GOLDBLOCK);
                    }
                    if(c>.7 )
                    {
                        chunk.setBlock(x, y, z, Blocks.LAVA);
                    }
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


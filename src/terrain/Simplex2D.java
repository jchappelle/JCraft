/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import Properties.GameProperties;
import Properties.GeneratorProperties;
import com.chappelle.jcraft.blocks.Blocks;
import com.chappelle.jcraft.blocks.JBlock;
import com.chappelle.jcraft.blocks.JBlockHelper;
import com.cubes.BTControl;
import com.cubes.Chunk;
import com.cubes.Vector3Int;
import java.util.Random;

/**
 *
 * @author Perry
 */
public class Simplex2D implements TerrainGenerator
{
    private JBlockHelper blockHelper;
    private BTControl terrainControl;
    private int water_level = (int) (GameProperties.CHUNK_SIZE.y * .2f);
    private int stone_level = (int) (GameProperties.CHUNK_SIZE.y * .4f);
    private int snow_level = (int) (GameProperties.CHUNK_SIZE.y * .6f);
    private int ice_level = (int) (GameProperties.CHUNK_SIZE.y * .75f);
    private Vector3Int offset;

    public Simplex2D(JBlockHelper blockHelper, BTControl terrainControl)
    {
        this.blockHelper = blockHelper;
        this.terrainControl = terrainControl;
    }

    @Override
    public void generateTerrain(Vector3Int offset)
    {
        this.offset = offset;
        System.out.println("**** OFFSET " + offset);
        long seed = GameProperties.SEED;
        if(seed == -1)
        {
            Random rndm = new Random(System.currentTimeMillis());
            rndm.nextLong();
            seed = rndm.nextLong();
        }
        SimplexNoise.setSeed(seed);
        System.out.print("Generating Terrain");
        for (int x = 0 + offset.x; x < offset.x + GameProperties.CHUNK_VIEW_DISTANCE.x; x++)
        {
            for (int y = 0 + offset.y; y < offset.y + GameProperties.CHUNK_VIEW_DISTANCE.y; y++)
            {
                for (int z = 0 + offset.z; z < offset.z + GameProperties.CHUNK_VIEW_DISTANCE.z; z++)
                {
                    Chunk c = generateChunk(new Vector3Int(x, y, z));
                    c.placeInTerrain(terrainControl);
                }
            }
        }
        System.out.println();
        System.out.println("Adding All Chunks");
        terrainControl.addAllScheduledChunks();
    }

    @Override
    public Chunk generateChunk(Vector3Int chunkLocation)
    {
        Vector3Int offset = chunkLocation.mult(GameProperties.CHUNK_SIZE);
        Chunk chunk = new Chunk();
        chunk.setLocation(chunkLocation);
        for (int x = 0; x < GameProperties.CHUNK_SIZE.x; x++)
        {
            for (int z = 0; z < GameProperties.CHUNK_SIZE.z; z++)
            {
                chunk.setBlock(x, 0, z, Blocks.BEDROCK);
                Double c = sumOctave(GeneratorProperties.ITERATIONS, x+offset.x, z+offset.z, GeneratorProperties.PERSISTENCE, GeneratorProperties.SIMPLEX_SCALE);
                c = normalize(c, 1, GameProperties.CHUNK_SIZE.y);
                for (int y = 1; y < c; y++)
                {
                     JBlock place = Blocks.DIRT;

                    double rand = Math.random() * 5000 ;
                    if(rand < 1000)
                    {
                        place = Blocks.STONE;
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

                    chunk.setBlock(x, y, z, place);
                }

                if(c<water_level) // water
                {
                    for(int y=c.intValue()+1; y<=water_level; y++)
                    {
                        chunk.setBlock(x, y, z, Blocks.LAPIS_BLOCK);
                    }
                }

                for(int y = c.intValue()-2; y <= c.intValue(); y++)
                {
                    JBlock place = Blocks.GRASS;
                    if(c>stone_level)
                    {
                        place = Blocks.STONE;
                    }
                    if(c>snow_level)
                    {
                        place = Blocks.SNOWBLOCK;
                    }
                    if(c>ice_level)
                    {
                        place = Blocks.ICE;
                    }
                    chunk.setBlock(x,y,z, place);
                }
            }
        }
        return chunk;
    }

    private double sumOctave(int num_iterations, double x, double z, double persistence, double scale)
    {
        double maxAmp = 0;
        double amp = 1;
        double noise = 0;

        //#add successively smaller, higher-frequency terms
        for(int i = 0; i < num_iterations; ++i)
        {
            noise += SimplexNoise.noise2D(x, z, scale) * amp;
            maxAmp += amp;
            amp *= persistence;
            scale *= 2;
        }

        //take the average value of the iterations
        noise /= maxAmp;

        return noise;
    }

    private Double normalize(double value, double low, double high)
    {
        return value * (high - low) / 2 + (high + low) / 2;
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
}

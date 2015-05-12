/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import com.chappelle.jcraft.blocks.JBlockHelper;
import com.cubes.BTControl;
import com.cubes.Noise;
import java.util.Random;


/**
 *
 * @author Perry
 */
public class TerrainGeneratorFactory
{
    public  enum GeneratorType { FAST_START, FLATLAND, CUBES_NOISE, SIMPLEX_2D, SIMPLEX_3D, JOSHES_WORLD;}


    public static TerrainGenerator makeTerrainGenerator(GeneratorType type, JBlockHelper blockHelper, BTControl terrainControl)
    {
        switch(type)
        {
            case FAST_START:
                return new FastStart(terrainControl);
            case FLATLAND:
                return new Flatland(terrainControl);
            case CUBES_NOISE:
                Noise noise = new Noise(new Random(), .24f, 256, 256);
                noise.setTerrainControl(terrainControl);
                return noise;
            case SIMPLEX_2D:
                return new Simplex2D(blockHelper, terrainControl);
            case SIMPLEX_3D:
                return new Simplex3D(terrainControl);
            case JOSHES_WORLD:
                return new JoshesWorld(terrainControl);
            default:
                return new Flatland(terrainControl);
        }
    }
}

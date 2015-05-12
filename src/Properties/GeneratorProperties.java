package Properties;

import terrain.TerrainGeneratorFactory;

/**
 *
 * @author Perry
 */
public interface GeneratorProperties
{
    final TerrainGeneratorFactory.GeneratorType GENERATOR_TYPE = TerrainGeneratorFactory.GeneratorType.SIMPLEX_2D;
    final double SIMPLEX_SCALE = .006; // range from around 0.015 to around 0.001  The higher the number the more rugged and extreme the terain.
    final int ITERATIONS = 4; // Use a value of 1 to get very smooth rolling hills.  No need to go higher than 4.
    float INTERNAL_NOISE= .05f;
    float PERSISTENCE = .33f;
}

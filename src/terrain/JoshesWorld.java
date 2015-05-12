package terrain;

import com.cubes.BTControl;
import com.cubes.Chunk;
import com.cubes.Noise;
import com.cubes.Vector3Int;
import java.util.Random;

public class JoshesWorld implements TerrainGenerator
{
    private BTControl terrainControl;
    private Noise noise;

    JoshesWorld(BTControl terrainControl)
    {
        this.terrainControl = terrainControl;
        noise = new Noise(new Random(), 0.3f, 1024, 1024);
        noise.setTerrainControl(terrainControl);
    }

    @Override
    public void generateTerrain(Vector3Int offset)
    {
        noise.generateTerrain(offset);
    }

    @Override
    public Vector3Int getPlayerStart()
    {
        return noise.getPlayerStart();
    }

    @Override
    public Chunk generateChunk(Vector3Int location)
    {
        return noise.generateChunk(location);
    }
}

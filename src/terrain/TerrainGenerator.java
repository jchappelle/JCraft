/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import com.cubes.Chunk;
import com.cubes.Vector3Int;

/**
 *
 * @author Perry
 */
public interface TerrainGenerator
{
    public void generateTerrain(Vector3Int offset);
    public Chunk generateChunk(Vector3Int location);
    public Vector3Int getPlayerStart();
}

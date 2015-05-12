/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import Properties.GameProperties;
import com.chappelle.jcraft.blocks.JBlock;

/**
 *
 * @author Perry
 */
public class Chunk
{
    private Block[][][] blocks;
    private Vector3Int location;

    public Chunk()
    {
        blocks = new JBlock[GameProperties.CHUNK_SIZE.x][GameProperties.CHUNK_SIZE.y][GameProperties.CHUNK_SIZE.z];
    }

    public void setBlock(Vector3Int location, JBlock block)
    {
        blocks[location.x][location.y][location.z] = block;
    }

    public void setBlock(int x, int y, int z, JBlock block)
    {
        if(x<0) x=0;
        if(x>GameProperties.CHUNK_SIZE.x) x=GameProperties.CHUNK_SIZE.x;
        if(y<0) y=0;
        if(y>GameProperties.CHUNK_SIZE.y) y=GameProperties.CHUNK_SIZE.y;
        if(z<0) z=0;
        if(z>GameProperties.CHUNK_SIZE.z) z=GameProperties.CHUNK_SIZE.z;

        blocks[x][y][z] = block;
    }

    public Block getBlock(Vector3Int location)
    {
        return blocks[location.x][location.y][location.z];
    }

    public Block[][][] getBlocks()
    {
        return blocks;
    }

    public void fill(JBlock block)
    {
        for (int x = 0; x < GameProperties.CHUNK_SIZE.x; x++)
        {
            for (int y = 0; y < GameProperties.CHUNK_SIZE.y; y++)
            {
                for (int z = 0; z < GameProperties.CHUNK_SIZE.z; z++)
                {
                    blocks[x][y][z] = block;
                }
            }
        }
    }

    public Chunk deepCopy()
    {
        Chunk result = new Chunk();
        System.arraycopy(this.blocks, 0, result.blocks, 0, this.blocks.length);
        result.setLocation(location);
        return result;
    }

    public Vector3Int getLocation()
    {
        return location;
    }

    public void setLocation(Vector3Int location)
    {
        this.location = location;
    }

    public void placeInTerrain(BTControl terrain)
    {
        terrain.scheduleChunkAdd(this);
    }

}

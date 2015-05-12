/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chappelle.jcraft.blocks;

import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.BlockSkin;
import com.cubes.Vector3Int;
import com.cubes.shapes.BlockShape_Cuboid;

/**
 *
 * @author Perry
 */
public class ConnectorRod extends JBlock
{
    ConnectorRod(BlockSkin blockSkin)
    {
        super(new BlockSkin[]{blockSkin});
        
        setShapes(
           new BlockShape_Cuboid(new float[]{0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f}),
           new BlockShape_Cuboid(new float[]{0.5f, 0.5f, 0.2f, 0.2f, 0.2f, 0.2f}),
           new BlockShape_Cuboid(new float[]{0.2f, 0.2f, 0.5f, 0.5f, 0.2f, 0.2f}),
           new BlockShape_Cuboid(new float[]{0.2f, 0.2f, 0.2f, 0.2f, 0.5f, 0.5f})
       );
    }

    @Override
    protected int getShapeIndex(BlockChunkControl chunk, Vector3Int location)
    {
        if((chunk.getNeighborBlock_Global(location, Block.Face.Top) !=  null) && (chunk.getNeighborBlock_Global(location, Block.Face.Bottom) !=  null))
        {
            return 1;
        }
        else if((chunk.getNeighborBlock_Global(location, Block.Face.Left) !=  null) && (chunk.getNeighborBlock_Global(location, Block.Face.Right) !=  null))
        {
            return 2;
        }
        else if((chunk.getNeighborBlock_Global(location, Block.Face.Front) !=  null) && (chunk.getNeighborBlock_Global(location, Block.Face.Back) !=  null))
        {
            return 3;
        }
        return 0;
    }
}

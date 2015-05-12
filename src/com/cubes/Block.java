/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import com.cubes.shapes.BlockShape_Cube;
import com.jme3.math.Vector3f;

public class Block
{
    public static enum Face
    {
        Top, Bottom, Left, Right, Front, Back;

        public static Block.Face fromNormal(Vector3f normal)
        {
            return fromNormal(Vector3Int.fromVector3f(normal));
        }

        public static Block.Face fromNormal(Vector3Int normal)
        {
            int x = normal.getX();
            int y = normal.getY();
            int z = normal.getZ();
            if (x != 0)
            {
                if (x > 0)
                {
                    return Block.Face.Right;
                }
                else
                {
                    return Block.Face.Left;
                }
            }
            else if (y != 0)
            {
                if (y > 0)
                {
                    return Block.Face.Top;
                }
                else
                {
                    return Block.Face.Bottom;
                }
            }
            else if (z != 0)
            {
                if (z > 0)
                {
                    return Block.Face.Front;
                }
                else
                {
                    return Block.Face.Back;
                }
            }
            return null;
        }
    };
    private BlockShape[] shapes = new BlockShape[]
    {
        new BlockShape_Cube()
    };
    private BlockSkin[] skins;

    public Block(BlockSkin... skins)
    {
        this.skins = skins;
    }

    protected void setShapes(BlockShape... shapes)
    {
        this.shapes = shapes;
    }

    public BlockShape getShape(BlockChunkControl chunk, Vector3Int location)
    {
        return shapes[getShapeIndex(chunk, location)];
    }

    protected int getShapeIndex(BlockChunkControl chunk, Vector3Int location)
    {
        return 0;
    }

    public BlockSkin getSkin(BlockChunkControl chunk, Vector3Int location, Face face)
    {
        return skins[getSkinIndex(chunk, location, face)];
    }

    protected int getSkinIndex(BlockChunkControl chunk, Vector3Int location, Face face)
    {
        if (skins.length == 6)
        {
            return face.ordinal();
        }
        return 0;
    }
    
    /**
     * Returns whether or not this block completely covers the bottom block. Used for determining
     * if a block is on the surface or not.
     * 
     * @return whether or not this block completely covers the bottom block
     */
    public boolean smothersBottomBlock()
    {
        return true;
    }
}

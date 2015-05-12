/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import com.chappelle.jcraft.blocks.Blocks;
import java.util.HashMap;

/**
 *
 * @author Carl
 */
public class BlockManager{

    private static HashMap<Block, Short> BLOCK_TYPES = new HashMap<Block, Short>();
    private static Block[] TYPES_BLOCKS = new Block[2000];
    private static Short nextBlockType = 1;

    public static void register(Block block)
    {
        BLOCK_TYPES.put(block, nextBlockType);
        TYPES_BLOCKS[nextBlockType] = block;
        nextBlockType++;
    }

    public static Short getType(Block block)
    {
        return BLOCK_TYPES.get(block);
    }

    public static Block getBlock(Short type)
    {
        if(type >= 0)
        {
            return TYPES_BLOCKS[type];
        }
        else
        {
            return Blocks.DIRT;
        }
    }
}

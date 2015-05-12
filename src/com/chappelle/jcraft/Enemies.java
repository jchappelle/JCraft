package com.chappelle.jcraft;

import java.util.HashMap;

/**
 * Registry for all enemies for lookups
 */
public class Enemies 
{
    private static HashMap<Enemy, Byte> BLOCK_TYPES = new HashMap<Enemy, Byte>();
    private static Enemy[] TYPES_BLOCKS = new Enemy[256];
    private static byte nextType = 1;

    
    public static void register(Enemy block)
    {
        BLOCK_TYPES.put(block, nextType);
        TYPES_BLOCKS[nextType] = block;
        nextType++;
    }
    
    public static byte getType(Enemy enemy)
    {
        return BLOCK_TYPES.get(enemy);
    }
    
    public static Enemy getEnemy(byte type)
    {
        return TYPES_BLOCKS[type];
    }
    
}

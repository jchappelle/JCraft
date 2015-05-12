package com.chappelle.jcraft;

/**
 * Static accessor for the SimpleApplication
 */
public class Game 
{
    private static BlockGame INSTANCE = null;
    
    public static void setInstance(BlockGame game)
    {
        INSTANCE = game;
    }
    
    public static BlockGame getInstance()
    {
        return INSTANCE;
    }
}

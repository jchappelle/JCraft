package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.Vector3Int;

public class Grass extends JBlock
{
    public Grass()
    {
        super(new BlockSkin[]{
            BlockSkinProperties.GRASS_TOP,
            BlockSkinProperties.DIRT,
            BlockSkinProperties.GRASS_SIDE,
            BlockSkinProperties.GRASS_SIDE,
            BlockSkinProperties.GRASS_SIDE,
            BlockSkinProperties.GRASS_SIDE
        });
        
        setDigAudio(makeAudio(SoundProperties.DIG_GRASS_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_GRASS_2));
        setStepAudio(makeAudio(SoundProperties.STEP_SAND_2));
      

    }
    
    @Override
    protected int getSkinIndex(BlockChunkControl chunk, Vector3Int location, Block.Face face) 
    {
        if (chunk.isBlockOnSurface(location)) 
        {
            switch (face) 
            {
                case Top:
                    return 0;
                case Bottom:
                    return 1;
                default:
                    return 2;
            }
        }
        else
        {
            return 1;
        }
    }
}

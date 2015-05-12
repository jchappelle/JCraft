package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class Cactus extends JBlock
{
    public Cactus()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.CACTUS_TOP,
            BlockSkinProperties.CACTUS_TOP,
            BlockSkinProperties.CACTUS_SIDE,
            BlockSkinProperties.CACTUS_SIDE,
            BlockSkinProperties.CACTUS_SIDE,
            BlockSkinProperties.CACTUS_SIDE
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

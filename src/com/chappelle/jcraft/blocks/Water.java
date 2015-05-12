package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class Water extends JBlock
{
    public Water()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.WATER_1,
            BlockSkinProperties.WATER_1,
            BlockSkinProperties.WATER_2,
            BlockSkinProperties.WATER_3,
            BlockSkinProperties.WATER_4,
            BlockSkinProperties.WATER_5
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

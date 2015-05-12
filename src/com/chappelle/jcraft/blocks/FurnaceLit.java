package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class FurnaceLit extends JBlock
{
    public FurnaceLit()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.SMOOTSTONE,
            BlockSkinProperties.SMOOTSTONE,
            BlockSkinProperties.FURNACE_SIDE,
            BlockSkinProperties.FURNACE_SIDE,
            BlockSkinProperties.FURNACE_SIDE,
            BlockSkinProperties.FURNACE_FRONT_LIT
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class JackOLantern extends JBlock
{
    public JackOLantern()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.PUMPKIN_TOP,
            BlockSkinProperties.PUMPKIN_TOP,
            BlockSkinProperties.PUMPKIN_SIDE,
            BlockSkinProperties.PUMPKIN_SIDE,
            BlockSkinProperties.PUMPKIN_SIDE,
            BlockSkinProperties.PUMPKIN_FRONT_LIT
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

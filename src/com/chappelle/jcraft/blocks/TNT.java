package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class TNT extends JBlock
{
    public TNT()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.TNT_TOP,
            BlockSkinProperties.TNT_BOTTOM,
            BlockSkinProperties.TNT_SIDE,
            BlockSkinProperties.TNT_SIDE,
            BlockSkinProperties.TNT_SIDE,
            BlockSkinProperties.TNT_SIDE
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

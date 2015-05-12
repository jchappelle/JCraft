package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class Chest extends JBlock
{
    public Chest()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.CHEST_TOP,
            BlockSkinProperties.CHEST_TOP,
            BlockSkinProperties.CHEST_SIDE,
            BlockSkinProperties.CHEST_SIDE,
            BlockSkinProperties.CHEST_SIDE,
            BlockSkinProperties.CHEST_FRONT,
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

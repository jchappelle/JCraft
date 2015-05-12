package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class Lava extends JBlock
{
    public Lava()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.LAVA_1,
            BlockSkinProperties.LAVA_1,
            BlockSkinProperties.LAVA_2,
            BlockSkinProperties.LAVA_3,
            BlockSkinProperties.LAVA_4,
            BlockSkinProperties.LAVA_5
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

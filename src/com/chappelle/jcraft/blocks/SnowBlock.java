package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class SnowBlock extends JBlock
{
    public SnowBlock()
    {
        super(new BlockSkin[]{BlockSkinProperties.SNOW});
        
        setDigAudio(makeAudio(SoundProperties.DIG_CLOTH_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_CLOTH_2));
        setStepAudio(makeAudio(SoundProperties.STEP_CLOTH_4));
    }
}

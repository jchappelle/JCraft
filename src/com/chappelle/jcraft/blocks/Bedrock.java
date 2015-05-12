package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class Bedrock extends JBlock
{
    public Bedrock()
    {
        super(new BlockSkin[]{BlockSkinProperties.BEDROCK});
        
        setDigAudio(makeAudio(SoundProperties.STEP_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_STONE_3));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_4));
    }
}

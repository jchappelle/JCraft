package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class Gravel extends JBlock
{
    public Gravel()
    {
        super(new BlockSkin[]{BlockSkinProperties.GRAVEL});
        
        setDigAudio(makeAudio(SoundProperties.STEP_GRAVEL_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_GRAVEL_2));
        setStepAudio(makeAudio(SoundProperties.DIG_GRAVEL_4));
        setAffectedByGravity(true);
    }
}

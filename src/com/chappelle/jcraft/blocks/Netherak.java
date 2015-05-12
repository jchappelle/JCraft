package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class Netherak extends JBlock
{
    public Netherak()
    {
        super(new BlockSkin[]{BlockSkinProperties.NETHERAK});
        
        setDigAudio(makeAudio(SoundProperties.DIG_CLOTH_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_CLOTH_2));
        setStepAudio(makeAudio(SoundProperties.STEP_CLOTH_4));
    }
}

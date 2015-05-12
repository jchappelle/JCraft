package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class Leaves extends JBlock
{
    public Leaves()
    {
        super(new BlockSkin[]{BlockSkinProperties.LEAVES});
        
        setDigAudio(makeAudio(SoundProperties.STEP_SAND_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_SAND_3));
        setStepAudio(makeAudio(SoundProperties.STEP_SAND_4));
    }
}

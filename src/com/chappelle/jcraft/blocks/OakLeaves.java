package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class OakLeaves extends JBlock
{
    public OakLeaves()
    {
        super(new BlockSkin[]{BlockSkinProperties.OAK_LEAVES});

        setDigAudio(makeAudio(SoundProperties.STEP_SAND_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_SAND_3));
        setStepAudio(makeAudio(SoundProperties.STEP_SAND_4));
    }
}

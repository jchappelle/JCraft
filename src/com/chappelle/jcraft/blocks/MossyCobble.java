package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class MossyCobble extends JBlock
{
    public MossyCobble()
    {
        super(new BlockSkin[]{BlockSkinProperties.MOSSY_COBBLE});
        
        setDigAudio(makeAudio(SoundProperties.STEP_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_STONE_2));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_4));
    }
}

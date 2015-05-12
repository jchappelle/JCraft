package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class SoulSand extends JBlock
{
    public SoulSand()
    {
        super(new BlockSkin[]{BlockSkinProperties.SOULSAND});
        
        setDigAudio(makeAudio(SoundProperties.STEP_CLOTH_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_STONE_3));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_4));
    }
}

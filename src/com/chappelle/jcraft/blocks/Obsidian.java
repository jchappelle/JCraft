package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class Obsidian extends JBlock
{
    public Obsidian()
    {
        super(new BlockSkin[]{BlockSkinProperties.OBSIDIAN});
        
        setDigAudio(makeAudio(SoundProperties.STEP_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_STONE_2));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_4));
    }
}

package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class Spawner extends JBlock
{
    public Spawner()
    {
        super(new BlockSkin[]{BlockSkinProperties.SPAWNER});
        
        setDigAudio(makeAudio(SoundProperties.STEP_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_STONE_3));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_4));
    }
}

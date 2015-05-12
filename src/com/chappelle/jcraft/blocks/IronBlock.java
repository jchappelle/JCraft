package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class IronBlock extends JBlock
{
    public IronBlock()
    {
        super(new BlockSkin[]{BlockSkinProperties.IRON_BLOCK});
        
        setDigAudio(makeAudio(SoundProperties.DIG_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_STONE_2));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_3));
    }
}

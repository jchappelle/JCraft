package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class SmoothStone extends JBlock
{
    public SmoothStone()
    {
        super(new BlockSkin[]{BlockSkinProperties.SMOOTSTONE});
        
        setDigAudio(makeAudio(SoundProperties.DIG_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_STONE_2));
        setStepAudio(makeAudio(SoundProperties.DIG_STONE_4));
    }
}

package com.chappelle.jcraft.blocks;

import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class GenericBlock extends JBlock
{
    public GenericBlock(BlockSkin blockSkin)
    {
        super(new BlockSkin[]{blockSkin});
        
        setDigAudio(makeAudio(SoundProperties.STEP_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_STONE_2));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_4));
    }
}

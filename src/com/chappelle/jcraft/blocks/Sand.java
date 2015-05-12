package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;

public class Sand extends JBlock
{
    public Sand()
    {
        super(new BlockSkin[]{BlockSkinProperties.SAND});
        
        setDigAudio(makeAudio(SoundProperties.DIG_SAND_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_SAND_2));
        setStepAudio(makeAudio(SoundProperties.STEP_SAND_3));
        setAffectedByGravity(true);
    }

    @Override
    public int getMass()
    {
        return 3;
    }
}

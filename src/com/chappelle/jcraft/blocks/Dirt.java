package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class Dirt extends JBlock
{
    public Dirt()
    {
        super(new BlockSkin[]{BlockSkinProperties.DIRT });
        
        setDigAudio(makeAudio(SoundProperties.STEP_CLOTH_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_STONE_3));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_4));
    }
}

package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class DiamondOre extends JBlock
{
    public DiamondOre()
    {
        super(new BlockSkin[]{BlockSkinProperties.DIAMOND_ORE});
        
        setDigAudio(makeAudio(SoundProperties.DIG_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_STONE_2));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_3));
    }
}

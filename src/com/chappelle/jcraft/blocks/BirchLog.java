package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class BirchLog extends JBlock
{
    public BirchLog()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.BIRCH_LOG_END,
            BlockSkinProperties.BIRCH_LOG_END,
            BlockSkinProperties.BIRCH_LOG,
            BlockSkinProperties.BIRCH_LOG,
            BlockSkinProperties.BIRCH_LOG,
            BlockSkinProperties.BIRCH_LOG
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

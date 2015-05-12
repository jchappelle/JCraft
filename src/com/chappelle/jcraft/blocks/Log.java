package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class Log extends JBlock
{
    public Log()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.LOG_END,
            BlockSkinProperties.LOG_END,
            BlockSkinProperties.LOG,
            BlockSkinProperties.LOG,
            BlockSkinProperties.LOG,
            BlockSkinProperties.LOG
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

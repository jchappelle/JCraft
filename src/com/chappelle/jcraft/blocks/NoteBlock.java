package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class NoteBlock extends JBlock
{
    public NoteBlock()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.NOTEBLOCK_TOP,
            BlockSkinProperties.NOTEBLOCK_SIDE,
            BlockSkinProperties.NOTEBLOCK_SIDE,
            BlockSkinProperties.NOTEBLOCK_SIDE,
            BlockSkinProperties.NOTEBLOCK_SIDE,
            BlockSkinProperties.NOTEBLOCK_SIDE
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}
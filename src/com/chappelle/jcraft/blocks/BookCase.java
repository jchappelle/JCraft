package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class BookCase extends JBlock
{
    public BookCase()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.CHEST_TOP,
            BlockSkinProperties.CHEST_TOP,
            BlockSkinProperties.BOOKSHELF,
            BlockSkinProperties.BOOKSHELF,
            BlockSkinProperties.BOOKSHELF,
            BlockSkinProperties.BOOKSHELF
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

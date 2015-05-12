package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class OakSapling extends JBlock
{
    public OakSapling()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.INVISIBLE,
            BlockSkinProperties.INVISIBLE,
            BlockSkinProperties.OAK_SAPLING,
            BlockSkinProperties.OAK_SAPLING,
            BlockSkinProperties.OAK_SAPLING,
            BlockSkinProperties.OAK_SAPLING
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

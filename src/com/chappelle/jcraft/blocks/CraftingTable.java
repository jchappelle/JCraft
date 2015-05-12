package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

public class CraftingTable extends JBlock
{
    public CraftingTable()
    {
        super(new BlockSkin[]
        {
            BlockSkinProperties.CRAFTING_TOP_BOTTOM,
            BlockSkinProperties.CRAFTING_TOP_BOTTOM,
            BlockSkinProperties.CRAFTING_FRONTBACK,
            BlockSkinProperties.CRAFTING_FRONTBACK,
            BlockSkinProperties.CRAFTING_SIDES,
            BlockSkinProperties.CRAFTING_SIDES
        });                
                
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }
}

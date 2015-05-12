package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.Vector3Int;

public class WoodenDoorTop extends JBlock
{
    public WoodenDoorTop()
    {
        super(new BlockSkin[]{BlockSkinProperties.TRANSPARENT, BlockSkinProperties.TRANSPARENT, BlockSkinProperties.TRANSPARENT, BlockSkinProperties.TRANSPARENT, BlockSkinProperties.WOODEN_DOOR_TOP, BlockSkinProperties.TRANSPARENT });
        
        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_WOOD_1));
    }

    @Override
    public Vector3Int getSiblingVector()
    {
        return new Vector3Int(0, -1, 0);
    }
    
}

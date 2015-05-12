package com.chappelle.jcraft.blocks;

import Properties.SoundProperties;
import com.cubes.BlockSkin;
import com.cubes.shapes.BlockShape_Cuboid;

public class Wood extends JBlock
{
    public Wood(BlockSkin blockSkin, int extentsIndex)
    {
        super(new BlockSkin[]{blockSkin});
        
        if(extentsIndex > -1 && extentsIndex < 6)
        {
            float[] extents = new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f};
            extents[extentsIndex] = 0;
            setShapes(new BlockShape_Cuboid(extents));
        }

        setDigAudio(makeAudio(SoundProperties.DIG_WOOD_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_WOOD_2));
        setStepAudio(makeAudio(SoundProperties.DIG_WOOD_4));
    }

    public Wood(BlockSkin blockSkin)
    {
        this(blockSkin, -1);
    }

}

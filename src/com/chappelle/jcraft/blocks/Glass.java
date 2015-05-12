package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockChunkControl;
import com.cubes.BlockSkin;
import com.cubes.Vector3Int;
import com.jme3.scene.Mesh;

public class Glass extends JBlock
{
    public Glass()
    {
        super(new BlockSkin[]{BlockSkinProperties.GLASS});
        
        setDigAudio(makeAudio(SoundProperties.MISC_GLASS_1));
        setPlaceAudio(makeAudio(SoundProperties.STEP_STONE_3));
        setStepAudio(makeAudio(SoundProperties.STEP_STONE_4));        
    }
    
    @Override
    public Mesh makeDropItemMesh(BlockChunkControl chunk, Vector3Int location)
    {
        return null;
    }    
}

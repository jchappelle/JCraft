package com.chappelle.jcraft.blocks;

import Properties.BlockSkinProperties;
import Properties.SoundProperties;
import com.cubes.BlockChunkControl;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.Vector3Int;
import com.jme3.scene.Mesh;

public class Test extends JBlock
{
    public Test()
    {
        super(new BlockSkin[]{BlockSkinProperties.FIRE_TEXT});
        
        setDigAudio(makeAudio(SoundProperties.DIG_STONE_1));
        setPlaceAudio(makeAudio(SoundProperties.DIG_STONE_2));
        setStepAudio(makeAudio(SoundProperties.DIG_STONE_4));
    }
    
    @Override
    public Mesh makeDropItemMesh(BlockChunkControl chunk, Vector3Int location)
    {
        return null;
    }    
    
}

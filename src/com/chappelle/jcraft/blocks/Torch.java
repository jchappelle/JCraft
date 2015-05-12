package com.chappelle.jcraft.blocks;

import com.chappelle.jcraft.BlockTerrainManager;
import com.chappelle.jcraft.PickedBlock;
import com.cubes.BlockChunkControl;
import com.cubes.BlockSkin;
import com.cubes.Block;
import com.cubes.BlockState;
import com.cubes.MeshFactory;
import com.cubes.Vector3Int;
import com.cubes.shapes.BlockShape_Torch;
import com.jme3.scene.Mesh;

public class Torch extends JBlock
{
    /**
     * BlockState key that represents the contact normal of the block
     * clicked when placing the torch
     */
    public static final Short VAR_ORIENTATION = 1;
    public static final Short VAR_ATTACHED_BLOCK = 2;//TODO: could probably use just this one and remove the VAR_ORIENTATION in the future
    
    Torch(BlockSkin blockSkin)
    {
        super(new BlockSkin[]{blockSkin});
        
        setShapes(
                new BlockShape_Torch()
       );
    }
    
    @Override
    public Mesh makeDropItemMesh(BlockChunkControl chunk, Vector3Int location)
    {
        MeshFactory factory = new MeshFactory(new BlockShape_Torch(this));
        return factory.makeMesh(chunk, location, false);
    }

    @Override
    public boolean smothersBottomBlock()
    {
        return false;
    }

    @Override
    public void initBlockState(PickedBlock pickedBlock, BlockState blockState)
    {
        blockState.put(VAR_ORIENTATION, pickedBlock.getContactNormal());
        blockState.put(VAR_ATTACHED_BLOCK, pickedBlock.getBlockLocation().subtract(Vector3Int.fromVector3f(pickedBlock.getContactNormal())));
    }
    
    @Override
    public void onNeighborRemoved(Vector3Int removedBlockLocation, Vector3Int myLocation, BlockTerrainManager terrain)
    {
        BlockState state = terrain.getBlockState(myLocation);
        Vector3Int attachedLocation = (Vector3Int)state.get(VAR_ATTACHED_BLOCK);
        if(removedBlockLocation.equals(attachedLocation))
        {
            terrain.removeBlock(this, myLocation);
        }
    }
    
    @Override
    public boolean isValidPlacementFace(Block.Face face)
    {
        return face != Block.Face.Bottom;
    }
}

package com.chappelle.jcraft.blocks;

import com.cubes.Block;
import com.cubes.BlockSkin_TextureLocation;
import java.util.EnumMap;
import java.util.Map;

public class CubeDescriptor 
{
    private Map<Block.Face, BlockSkin_TextureLocation> faceMap;
    private float[] extents;

    public CubeDescriptor(float[] extents, BlockSkin_TextureLocation textureLocation)
    {
        this.faceMap = new EnumMap<Block.Face, BlockSkin_TextureLocation>(Block.Face.class);
        faceMap.put(Block.Face.Top, textureLocation);
        faceMap.put(Block.Face.Bottom, textureLocation);
        faceMap.put(Block.Face.Left, textureLocation);
        faceMap.put(Block.Face.Right, textureLocation);
        faceMap.put(Block.Face.Front, textureLocation);
        faceMap.put(Block.Face.Back, textureLocation);
        this.extents = extents;
    }

    public CubeDescriptor(float[] extents, Map<Block.Face, BlockSkin_TextureLocation> faceMap)
    {
        this.faceMap = faceMap;
        this.extents = extents;
    }

    public float[] getExtents()
    {
        return extents;
    }
    
    public Map<Block.Face, BlockSkin_TextureLocation> getFaceMap()
    {
        return faceMap;
    }

    public void put(Block.Face face, BlockSkin_TextureLocation location)
    {
        faceMap.put(face, location);
    }
}

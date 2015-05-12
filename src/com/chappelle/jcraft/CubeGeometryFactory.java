package com.chappelle.jcraft;

import Properties.GameProperties;
import Properties.TextureProperties;
import com.chappelle.jcraft.blocks.CubeDescriptor;
import com.chappelle.jcraft.blocks.JBlock;
import com.cubes.Block;
import com.cubes.BlockShape;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.cubes.shapes.BlockShape_Cuboid;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.util.BufferUtils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CubeGeometryFactory
{
    private CubesSettings cubeSettings;
    private Map<Block.Face, BlockSkin_TextureLocation> faceMap;
    private float[] extents;

    public CubeGeometryFactory(CubesSettings cubeSettings, CubeDescriptor cubeDescriptor)
    {
        this.cubeSettings = cubeSettings;
        this.extents = cubeDescriptor.getExtents();
        this.faceMap = cubeDescriptor.getFaceMap();
    }

    public Geometry makeGeometry(JBlock block)
    {
        Geometry result = new Geometry("", new Box(GameProperties.CUBE_SIZE, GameProperties.CUBE_SIZE, GameProperties.CUBE_SIZE));
        //result.setQueueBucket(RenderQueue.Bucket.Opaque);
        result.setMaterial(cubeSettings.getBlockMaterial());
        result.setMesh(generateOptimizedMesh(new BlockShape_Cuboid(extents), block.isTransparent()));
        return result;
    }

    public Mesh generateOptimizedMesh(BlockShape blockShape, boolean isTransparent)
    {
        LinkedList<Vector3f> positionsList = new LinkedList<Vector3f>();
        LinkedList<Short> indicesList = new LinkedList<Short>();
        LinkedList<Float> normalsList = new LinkedList<Float>();
        LinkedList<Vector2f> textureCoordinatesList = new LinkedList<Vector2f>();

        Vector3Int tmpLocation = new Vector3Int();
        tmpLocation.set(0, 0, 0);

        blockShape.prepare(isTransparent, positionsList, indicesList, normalsList, textureCoordinatesList);


        Vector3f blockLocation = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f blockLocation3f = new Vector3f(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
        Vector3f faceLoc_Bottom_TopLeft = blockLocation3f.add(new Vector3f((0.5f - extents[2]), (0.5f - extents[1]), (0.5f - extents[5])));
        Vector3f faceLoc_Bottom_TopRight = blockLocation3f.add(new Vector3f((0.5f + extents[3]), (0.5f - extents[1]), (0.5f - extents[5])));
        Vector3f faceLoc_Bottom_BottomLeft = blockLocation3f.add(new Vector3f((0.5f - extents[2]), (0.5f - extents[1]), (0.5f + extents[4])));
        Vector3f faceLoc_Bottom_BottomRight = blockLocation3f.add(new Vector3f((0.5f + extents[3]), (0.5f - extents[1]), (0.5f + extents[4])));
        Vector3f faceLoc_Top_TopLeft = blockLocation3f.add(new Vector3f((0.5f - extents[2]), (0.5f + extents[0]), (0.5f - extents[5])));
        Vector3f faceLoc_Top_TopRight = blockLocation3f.add(new Vector3f((0.5f + extents[3]), (0.5f + extents[0]), (0.5f - extents[5])));
        Vector3f faceLoc_Top_BottomLeft = blockLocation3f.add(new Vector3f((0.5f - extents[2]), (0.5f + extents[0]), (0.5f + extents[4])));
        Vector3f faceLoc_Top_BottomRight = blockLocation3f.add(new Vector3f((0.5f + extents[3]), (0.5f + extents[0]), (0.5f + extents[4])));


        //TOP
        addFaceIndices(indicesList, positionsList.size());
        positionsList.add(faceLoc_Top_BottomLeft);
        positionsList.add(faceLoc_Top_BottomRight);
        positionsList.add(faceLoc_Top_TopLeft);
        positionsList.add(faceLoc_Top_TopRight);
        addSquareNormals(normalsList, 0, 1, 0);
        addTextureCoordinates(textureCoordinatesList, faceMap.get(Block.Face.Top));

        //BOTTOM
        addFaceIndices(indicesList, positionsList.size());
        positionsList.add(faceLoc_Bottom_BottomRight);
        positionsList.add(faceLoc_Bottom_BottomLeft);
        positionsList.add(faceLoc_Bottom_TopRight);
        positionsList.add(faceLoc_Bottom_TopLeft);
        addSquareNormals(normalsList, 0, -1, 0);
        addTextureCoordinates(textureCoordinatesList, faceMap.get(Block.Face.Bottom));

        //LEFT
        addFaceIndices(indicesList, positionsList.size());
        positionsList.add(faceLoc_Bottom_TopLeft);
        positionsList.add(faceLoc_Bottom_BottomLeft);
        positionsList.add(faceLoc_Top_TopLeft);
        positionsList.add(faceLoc_Top_BottomLeft);
        addSquareNormals(normalsList, -1, 0, 0);
        addTextureCoordinates(textureCoordinatesList, faceMap.get(Block.Face.Left));

        //RIGHT
        addFaceIndices(indicesList, positionsList.size());
        positionsList.add(faceLoc_Bottom_BottomRight);
        positionsList.add(faceLoc_Bottom_TopRight);
        positionsList.add(faceLoc_Top_BottomRight);
        positionsList.add(faceLoc_Top_TopRight);
        addSquareNormals(normalsList, 1, 0, 0);
        addTextureCoordinates(textureCoordinatesList, faceMap.get(Block.Face.Right));

        //FRONT
        addFaceIndices(indicesList, positionsList.size());
        positionsList.add(faceLoc_Bottom_BottomLeft);
        positionsList.add(faceLoc_Bottom_BottomRight);
        positionsList.add(faceLoc_Top_BottomLeft);
        positionsList.add(faceLoc_Top_BottomRight);
        addSquareNormals(normalsList, 0, 0, 1);
        addTextureCoordinates(textureCoordinatesList,faceMap.get(Block.Face.Front));

        //BACK
        addFaceIndices(indicesList, positionsList.size());
        positionsList.add(faceLoc_Bottom_TopRight);
        positionsList.add(faceLoc_Bottom_TopLeft);
        positionsList.add(faceLoc_Top_TopRight);
        positionsList.add(faceLoc_Top_TopLeft);
        addSquareNormals(normalsList, 0, 0, -1);
        addTextureCoordinates(textureCoordinatesList, faceMap.get(Block.Face.Back));

        Vector3f[] positions = new Vector3f[positionsList.size()];
        Iterator<Vector3f> positionsIterator = positionsList.iterator();
        for (int i = 0; positionsIterator.hasNext(); i++)
        {
            positions[i] = positionsIterator.next().mult(GameProperties.CUBE_SIZE);
        }
        short[] indices = new short[indicesList.size()];
        Iterator<Short> indicesIterator = indicesList.iterator();
        for (int i = 0; indicesIterator.hasNext(); i++)
        {
            indices[i] = indicesIterator.next();
        }
        Vector2f[] textureCoordinates = textureCoordinatesList.toArray(new Vector2f[0]);
        float[] normals = new float[normalsList.size()];
        Iterator<Float> normalsIterator = normalsList.iterator();
        for (int i = 0; normalsIterator.hasNext(); i++)
        {
            normals[i] = normalsIterator.next();
        }
        return generateMesh(positions, indices, normals, textureCoordinates);
    }

    private Mesh generateMesh(Vector3f[] positions, short[] indices, float[] normals, Vector2f[] textureCoordinates)
    {
        Mesh mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(positions));
        mesh.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(indices));
        mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(textureCoordinates));
        mesh.updateBound();
        return mesh;
    }

    protected Vector2f getTextureCoordinates(BlockSkin_TextureLocation textureLocation, float xUnitsToAdd, float yUnitsToAdd)
    {
        float textureUnitX = (1f / TextureProperties.TEXTURE_TILES_X);
        float textureUnitY = (1f / TextureProperties.TEXTURE_TILES_Y);
        float x = (((textureLocation.getColumn() + xUnitsToAdd) * textureUnitX));
        float y = ((((-1 * textureLocation.getRow()) + (yUnitsToAdd - 1)) * textureUnitY) + 1);
        return new Vector2f(x, y);
    }

    private void addFaceIndices(List<Short> indices, int offset)
    {
        indices.add((short) (offset + 2));
        indices.add((short) (offset + 0));
        indices.add((short) (offset + 1));
        indices.add((short) (offset + 1));
        indices.add((short) (offset + 3));
        indices.add((short) (offset + 2));
    }

    private void addSquareNormals(List<Float> normals, float normalX, float normalY, float normalZ)
    {
        for (int i = 0; i < 4; i++)
        {
            normals.add(normalX);
            normals.add(normalY);
            normals.add(normalZ);
        }
    }

    private void addTextureCoordinates(List<Vector2f> textureCoordinates, BlockSkin_TextureLocation textureLocation)
    {
        textureCoordinates.add(getTextureCoordinates(textureLocation, 0, 0));
        textureCoordinates.add(getTextureCoordinates(textureLocation, 1, 0));
        textureCoordinates.add(getTextureCoordinates(textureLocation, 0, 1));
        textureCoordinates.add(getTextureCoordinates(textureLocation, 1, 1));
    }

}

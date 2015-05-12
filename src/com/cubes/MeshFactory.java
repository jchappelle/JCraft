package com.cubes;

import Properties.GameProperties;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.Iterator;
import java.util.LinkedList;

public class MeshFactory
{
    private static Vector3f[] positions;
    private static short[] indices;
    private static Vector2f[] textureCoordinates;
    private static float[] normals;
    private static Vector3Int size = GameProperties.CHUNK_SIZE;
    private BlockShape blockShape;

    public MeshFactory(BlockShape blockShape)
    {
        this.blockShape = blockShape;
    }

    public Mesh makeMesh(BlockChunkControl chunk, Vector3Int location, boolean isTransparent)
    {
        LinkedList<Vector3f> positionsList = new LinkedList<>();
        LinkedList<Short> indicesList = new LinkedList<>();
        LinkedList<Float> normalsList = new LinkedList<>();
        LinkedList<Vector2f> textureCoordinatesList = new LinkedList<>();

        blockShape.prepare(isTransparent, positionsList, indicesList, normalsList, textureCoordinatesList);
        blockShape.addTo(chunk, location);

        positions = new Vector3f[positionsList.size()];
        Iterator<Vector3f> positionsIterator = positionsList.iterator();
        for (int i = 0; positionsIterator.hasNext(); i++)
        {
            positions[i] = positionsIterator.next().mult(GameProperties.CUBE_SIZE);
        }
        indices = new short[indicesList.size()];
        Iterator<Short> indicesIterator = indicesList.iterator();
        for (int i = 0; indicesIterator.hasNext(); i++)
        {
            indices[i] = indicesIterator.next();
        }
        textureCoordinates = textureCoordinatesList.toArray(new Vector2f[0]);
        normals = new float[normalsList.size()];
        Iterator<Float> normalsIterator = normalsList.iterator();
        for (int i = 0; normalsIterator.hasNext(); i++)
        {
            normals[i] = normalsIterator.next();
        }
        return generateMesh();
    }

    private static Mesh generateMesh()
    {
        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(positions));
        mesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createShortBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(textureCoordinates));
        mesh.updateBound();
        return mesh;
    }
}

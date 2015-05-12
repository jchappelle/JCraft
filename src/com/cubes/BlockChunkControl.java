/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import Properties.GameProperties;
import com.chappelle.jcraft.ObjectPersister;
import com.chappelle.jcraft.UnloadableObjectException;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author Carl
 */
public class BlockChunkControl extends AbstractControl
{

    public static int invalidLocationCount = 0;
    private BTControl terrain;
    private static Vector3Int chunkSize = GameProperties.CHUNK_SIZE;
    private Vector3Int chunkLocation = new Vector3Int();
    private Vector3Int blockLocation = new Vector3Int();
    private Short[][][] blockTypes;
    private boolean[][][] blocks_IsOnSurface;
    private BlockState[][][] blockState;

    private Node node = new Node();
    private Geometry optimizedGeometry_Opaque;
    private Geometry optimizedGeometry_Transparent;
    private boolean needsMeshUpdate;
    private int blockCount = 0;

    public BlockChunkControl(BTControl terrain, Vector3Int chunkLocation)
    {
        this.terrain = terrain;
        this.chunkLocation = chunkLocation;
        blockLocation = chunkLocation.mult(chunkSize);
        node.setLocalTranslation(blockLocation.toVector3f().mult(GameProperties.CUBE_SIZE));
        blockTypes = new Short[chunkSize.x][chunkSize.y][chunkSize.z];
        blocks_IsOnSurface = new boolean[chunkSize.x][chunkSize.y][chunkSize.z];
        blockState = new BlockState[chunkSize.x][chunkSize.y][chunkSize.z];
    }

    public void setBlockState(Vector3Int location, Short key, Object value)
    {
        Block block = getBlock(location);
        if(block != null)
        {
            BlockState state = getBlockState(location);
            state.put(key, value);
        }
    }

    public Object getBlockStateValue(Vector3Int location, Short key)
    {
        BlockState state = getBlockState(location);
        if(state == null)
        {
            return null;
        }
        else
        {
            return state.get(key);
        }
    }

    public BlockState getBlockState(Vector3Int location)
    {
        BlockState state = blockState[location.getX()][location.getY()][location.getZ()];
        if(state == null)
        {
            state = new BlockState();
            blockState[location.getX()][location.getY()][location.getZ()] = state;
        }
        return state;
    }

    public void dispose()
    {
        if(GameProperties.SAVE)
        {
            save();
        }
        node.detachAllChildren();
        node.removeFromParent();
    }

    protected void finalize()
    {
    }

    @Override
    public void setSpatial(Spatial spatial)
    {
        Spatial oldSpatial = this.spatial;
        super.setSpatial(spatial);
        if (spatial instanceof Node)
        {
            Node parentNode = (Node) spatial;
            parentNode.attachChild(node);
        }
        else if (oldSpatial instanceof Node)
        {
            Node oldNode = (Node) oldSpatial;
            oldNode.detachChild(node);
        }
    }

    @Override
    protected void controlUpdate(float lastTimePerFrame)
    {
    }

    @Override
    protected void controlRender(RenderManager renderManager, ViewPort viewPort)
    {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Block getNeighborBlock_Local(Vector3Int location, Block.Face face)
    {
        Vector3Int neighborLocation = BlockNavigator.getNeighborBlockLocalLocation(location, face);
        return getBlock(neighborLocation);
    }

    public Block getNeighborBlock_Global(Vector3Int location, Block.Face face)
    {
        return terrain.getBlock(getNeighborBlockGlobalLocation(location, face));
    }

    private Vector3Int getNeighborBlockGlobalLocation(Vector3Int location, Block.Face face)
    {
        Vector3Int neighborLocation = BlockNavigator.getNeighborBlockLocalLocation(location, face);
        neighborLocation.addLocal(blockLocation);
        return neighborLocation;
    }

    public Block getBlock(Vector3Int location)
    {
        if (isValidBlockLocation(location))
        {
            Short blockType = blockTypes[location.getX()][location.getY()][location.getZ()];
            if (blockType != null)
            {
                return BlockManager.getBlock(blockType);
            }
        }
        return null;
    }

    public void setBlock(Vector3Int location, Block block)
    {
        if (isValidBlockLocation(location))
        {
            Short blockType = BlockManager.getType(block);
            blockTypes[location.x][location.y][location.z] = blockType;
            updateBlockState(location);
            needsMeshUpdate = true;
            blockCount++;
            if(terrain.getDoomedChunks().contains(chunkLocation))
            {
                terrain.getDoomedChunks().remove(chunkLocation);
            }
        }
    }

    public void removeBlock(Vector3Int location)
    {
        if (isValidBlockLocation(location))
        {
            blockTypes[location.getX()][location.getY()][location.getZ()] = 0;
            blockState[location.getX()][location.getY()][location.getZ()] = null;
            updateBlockState(location);
            needsMeshUpdate = true;
            blockCount--;
            if(blockCount <= 0)
            {
                terrain.markAsDoomed(chunkLocation);
            }
        }
    }

    private boolean isValidBlockLocation(Vector3Int location)
    {
        return Util.isValidIndex(blockTypes, location);
    }

    public boolean updateSpatial()
    {
        if (needsMeshUpdate)
        {
            if (optimizedGeometry_Opaque == null)
            {
                optimizedGeometry_Opaque = new Geometry("");
                optimizedGeometry_Opaque.setQueueBucket(Bucket.Opaque);
                node.attachChild(optimizedGeometry_Opaque);
                updateBlockMaterial();
            }
            if (optimizedGeometry_Transparent == null)
            {
                optimizedGeometry_Transparent = new Geometry("");
                optimizedGeometry_Transparent.setQueueBucket(Bucket.Transparent);
                node.attachChild(optimizedGeometry_Transparent);
                updateBlockMaterial();
            }
            optimizedGeometry_Opaque.setMesh(BlockChunk_MeshOptimizer.generateOptimizedMesh(this, false));
            optimizedGeometry_Transparent.setMesh(BlockChunk_MeshOptimizer.generateOptimizedMesh(this, true));
            needsMeshUpdate = false;
            return true;
        }
        return false;
    }

    public void updateBlockMaterial()
    {
        if(blockCount == 0)
        {
            optimizedGeometry_Opaque = null;
            optimizedGeometry_Transparent = null;
        }
        if (optimizedGeometry_Opaque != null)
        {
            optimizedGeometry_Opaque.setMaterial(terrain.getSettings().getBlockMaterial());
        }
        if (optimizedGeometry_Transparent != null)
        {
            optimizedGeometry_Transparent.setMaterial(terrain.getSettings().getBlockMaterial());
        }
    }

    private void updateBlockState(Vector3Int location)
    {
        updateBlockInformation(location);
        for (int i = 0; i < Block.Face.values().length; i++)
        {
            Vector3Int neighborLocation = getNeighborBlockGlobalLocation(location, Block.Face.values()[i]);
            BlockChunkControl chunk = terrain.fetchExistingChunkForBlockLocation(neighborLocation);
            if (chunk != null)
            {
                chunk.updateBlockInformation(neighborLocation.subtract(chunk.getBlockLocation()));
            }
        }
    }

    private void updateBlockInformation(Vector3Int location)
    {
        Block neighborBlock_Top = terrain.getPointedAtBlock(getNeighborBlockGlobalLocation(location, Block.Face.Top));
        if (isValidBlockIndex(location))
        {
            blocks_IsOnSurface[location.x][location.y][location.z] = (neighborBlock_Top == null || !neighborBlock_Top.smothersBottomBlock());
        }
        else
        {
            Exception e = new Exception("Invalid block location");
            e.printStackTrace();
            System.out.println("Invalid loc: " + location);
            invalidLocationCount++;
        }
    }

    private boolean isValidBlockIndex(Vector3Int index)
    {
        return ((index.x >= 0) && (index.x < chunkSize.x)
                && (index.y >= 0) && (index.y < chunkSize.y)
                && (index.z >= 0) && (index.z < chunkSize.z));
    }

    public boolean isBlockOnSurface(Vector3Int location)
    {
        return blocks_IsOnSurface[location.getX()][location.getY()][location.getZ()];
    }

    public BTControl getTerrain()
    {
        return terrain;
    }

    public Vector3Int getLocation()
    {
        return chunkLocation;
    }

    public Vector3Int getBlockLocation()
    {
        return blockLocation;
    }

    public Node getNode()
    {
        return node;
    }

    public Geometry getOptimizedGeometry_Opaque()
    {
        return optimizedGeometry_Opaque;
    }

    public Geometry getOptimizedGeometry_Transparent()
    {
        return optimizedGeometry_Transparent;
    }

    public void setOptimizedGeometry_Opaque(Geometry optimizedGeometry_Opaque)
    {
        this.optimizedGeometry_Opaque = optimizedGeometry_Opaque;
    }

    public void setOptimizedGeometry_Transparent(Geometry optimizedGeometry_Transparent)
    {
        this.optimizedGeometry_Transparent = optimizedGeometry_Transparent;
    }


    public void setLocation(Vector3Int location)
    {
        this.chunkLocation = location;
    }

    public int getBlockCount()
    {
        return blockCount;
    }

    public void save()
    {
        String fileName = "chunks/" + chunkLocation + ".chk";
        ObjectPersister.save(blockTypes, fileName);
        fileName = "chunks/" + chunkLocation + ".chs";
        ObjectPersister.save(blockState, fileName);
    }

    public static BlockChunkControl load(Vector3Int location, BTControl terrain)
    {
        String fileName = "chunks/" + location + ".chk";
        Short[][][] blkTypes;
        try
        {
            blkTypes = (Short[][][])(ObjectPersister.load(fileName));
        }
        catch (UnloadableObjectException e)
        {
            return null;
        }

        fileName = "chunks/" + location + ".chs";
        BlockState[][][] blkStatuses;

        try
        {
        blkStatuses = (BlockState[][][])(ObjectPersister.load(fileName));
        }
        catch (UnloadableObjectException e)
        {
            return null;
        }

        BlockChunkControl result = new BlockChunkControl(terrain, location);
        result.blockCount = 0;
        for (int x = 0; x < chunkSize.x; x++)
        {
            for (int y = 0; y < chunkSize.y; y++)
            {
                for (int z = 0; z < chunkSize.z; z++)
                {
                    if(blkTypes[x][y][z] != null)
                    {
                        result.blockTypes[x][y][z] = blkTypes[x][y][z];
                        result.blockState[x][y][z] = blkStatuses[x][y][z];
                        result.updateBlockState(new Vector3Int(x, y, z));
                        result.blockCount++;
                    }
                }
            }
        }
        result.needsMeshUpdate = true;

        if (terrain.getDoomedChunks().contains(location))
        {
            terrain.getDoomedChunks().remove(location);
        }
        return result;
    }
}
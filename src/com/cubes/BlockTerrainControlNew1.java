/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import Properties.GameProperties;
import com.chappelle.jcraft.blocks.Blocks;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import java.util.ArrayList;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Carl
 */
public class BlockTerrainControlNew1 extends AbstractControl implements BTControl
{

    private CubesSettings settings;
    private Map<Vector3Int, BlockChunkControl> chunks;
    private Set<Vector3Int> doomedChunks = new HashSet<>();
    private Set<Chunk> scheduledChunks = new HashSet<>();
    private Vector3Int chunkSize = GameProperties.CHUNK_SIZE;
    private float cubeSize = GameProperties.CUBE_SIZE;
    private int maxChunks = GameProperties.CHUNK_VIEW_DISTANCE.x * GameProperties.CHUNK_VIEW_DISTANCE.x;
    private ArrayList<BlockChunkListener> chunkListeners = new ArrayList<>();
    private BulletAppState physics;

    public BlockTerrainControlNew1(CubesSettings settings, BulletAppState physics)
    {
        this.settings = settings;
        this.physics = physics;
        chunks = new HashMap<>();
        System.out.println("MaxChunks: " + maxChunks);
    }

    @Override
    public void setSpatial(Spatial spatial)
    {
        Spatial oldSpatial = this.spatial;
        super.setSpatial(spatial);

        for (Vector3Int key : chunks.keySet())
        {
            if (spatial == null)
            {
                oldSpatial.removeControl(chunks.get(key));
            }
            else
            {
                if (chunks.get(key) != null)
                {
                    spatial.addControl(chunks.get(key));
                }
            }
        }
    }

    public Set<Chunk> getScheduledChunks()
    {
        return scheduledChunks;
    }

    private boolean updateSpatial()
    {
        boolean wasUpdateNeeded = false;
        for (Vector3Int key : chunks.keySet())
        {
            if (updateSpatialForChunk(chunks.get(key)))
            {
                wasUpdateNeeded = true;
            }
        }
        return wasUpdateNeeded;
    }

    private boolean updateSpatialForChunk(BlockChunkControl chunk)
    {
        boolean result = false;
        if (chunk != null)
        {
            if (chunk.updateSpatial())
            {
                result = true;
                updateCollisionShape(chunk);
            }
        }
        return result;
    }

    @Override
    protected void controlUpdate(float lastTimePerFrame)
    {
        updateSpatial();
        addNewChunks();
        removeDoomedChunks();

    }

    public void scheduleChunkAdd(Chunk chunk)
    {
        scheduledChunks.add(chunk);
    }

    private void removeDoomedChunks()
    {
        if (doomedChunks.iterator().hasNext())
        {
            Vector3Int doomed = doomedChunks.iterator().next();
            if (chunks.keySet().contains(doomed))
            {
                BlockChunkControl doomedChunk = chunks.get(doomed);
                updateGeometry(doomedChunk.getOptimizedGeometry_Opaque(), doomedChunk, true);
                updateGeometry(doomedChunk.getOptimizedGeometry_Transparent(), doomedChunk, true);

                chunks.get(doomed).dispose();
                chunks.remove(doomed);
            }
            doomedChunks.remove(doomed);
        }
    }

    public void addAllScheduledChunks()
    {
        while (scheduledChunks.iterator().hasNext())
        {
            addNewChunks();
        }

    }

    private void addNewChunks()
    {
        if (scheduledChunks.iterator().hasNext())
        {
            Chunk newChunk = scheduledChunks.iterator().next();  // we've paid the price to generate a chunk from therrain generator at this point... need a better way
            Vector3Int location = newChunk.getLocation();

            BlockChunkControl c = null;
            if(GameProperties.SAVE)
            {
                c = BlockChunkControl.load(location, this);
            }
            if (c != null)
            {
                chunks.put(location, c);
            }
            else
            {
                int xOffset = location.x * GameProperties.CHUNK_SIZE.x;
                int yOffset = location.y * GameProperties.CHUNK_SIZE.y;
                int zOffset = location.z * GameProperties.CHUNK_SIZE.z;

                for (int x = 0; x < GameProperties.CHUNK_SIZE.x; x++)
                {
                    for (int y = 0; y < GameProperties.CHUNK_SIZE.y; y++)
                    {
                        for (int z = 0; z < GameProperties.CHUNK_SIZE.z; z++)
                        {
                            if (newChunk.getBlocks()[x][y][z] != null)
                            {
                                setBlock(x + xOffset, y + yOffset, z + zOffset, newChunk.getBlocks()[x][y][z]);
                            }
                        }
                    }
                }

            }

            scheduledChunks.remove(newChunk);
        }
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

    @Override
    public Block getBlock(int x, int y, int z)
    {
        return getBlock(new Vector3Int(x, y, z));
    }

    @Override
    public Block getBlock(Vector3Int location)
    {
        BlockTerrain_LocalBlockState localBlockState = getLocalBlockState(location);
        if (localBlockState != null)
        {
            return localBlockState.getBlock();
        }
        return null;
    }

    @Override
    public Block getPointedAtBlock(Vector3Int location)
    {
        BlockTerrain_LocalBlockState localBlockState = getPointedAtLocalBlockState(location);
        if (localBlockState != null)
        {
            return localBlockState.getBlock();
        }
        return null;
    }

    @Override
    public void setBlock(int x, int y, int z, Block block)
    {
        setBlock(new Vector3Int(x, y, z), block);
    }

    @Override
    public void setBlock(Vector3Int location, Block block)
    {
        BlockTerrain_LocalBlockState localBlockState = getLocalBlockState(location);
        if (localBlockState != null)
        {
            localBlockState.setBlock(block);
        }
    }

    public void removeBlock(int x, int y, int z)
    {
        removeBlock(new Vector3Int(x, y, z));
    }

    @Override
    public void removeBlock(Vector3Int location)
    {
        BlockTerrain_LocalBlockState localBlockState = getLocalBlockState(location);
        if (localBlockState != null)
        {
            localBlockState.removeBlock();
        }
    }

    public BlockTerrain_LocalBlockState getLocalBlockState(Vector3Int blockLocation)
    {
        if (blockLocation.hasNegativeCoordinate())
        {
            return null;
        }
        BlockChunkControl chunk = getChunk(blockLocation);
        if (chunk != null)
        {
            Vector3Int localBlockLocation = getLocalBlockLocation(blockLocation, chunk);
            return new BlockTerrain_LocalBlockState(chunk, localBlockLocation);
        }
        return null;
    }

    private BlockTerrain_LocalBlockState getPointedAtLocalBlockState(Vector3Int blockLocation)
    {
        if (blockLocation.hasNegativeCoordinate())
        {
            return null;
        }
        BlockChunkControl chunk = getPointedAtChunk(blockLocation);


        if (chunk != null)
        {
            Vector3Int localBlockLocation = getLocalBlockLocation(blockLocation, chunk);
            return new BlockTerrain_LocalBlockState(chunk, localBlockLocation);
        }
        return null;
    }

    public BlockChunkControl getPointedAtChunk(Vector3Int blockLocation)
    {
        if (blockLocation.hasNegativeCoordinate())
        {
            return null;
        }
        Vector3Int chunkLocation = blockLocation.divide(chunkSize);

        return chunks.get(chunkLocation);
    }

    public BlockChunkControl getChunk(Vector3Int blockLocation)
    {
        if (blockLocation.hasNegativeCoordinate())
        {
            return null;
        }
        Vector3Int chunkLocation = blockLocation.divide(chunkSize);

        BlockChunkControl result = chunks.get(chunkLocation);
        if (result == null)
        {
            BlockChunkControl chunk = new BlockChunkControl(this, chunkLocation);
            chunks.put(chunkLocation, chunk);
            return chunk;
        }
        return result;
    }

    @Override
    public boolean hasChunk(Vector3Int chunkLocation)
    {
        return chunks.containsKey(chunkLocation) || scheduledChunks.contains(chunkLocation);
    }

    @Override
    public BlockChunkControl fetchExistingChunkForBlockLocation(Vector3Int blockLocation)
    {
        if (blockLocation.hasNegativeCoordinate())
        {
            return null;
        }
        return chunks.get(blockLocation.divide(chunkSize));
    }

    public Vector3f getBlockTranslation(Vector3Int loc)
    {
        return new Vector3f(loc.x * cubeSize, loc.y * cubeSize, loc.z * cubeSize);
    }

    public Vector3Int getLocalBlockLocation(Vector3Int blockLocation, BlockChunkControl chunk)
    {
        return new Vector3Int((blockLocation.getX() - chunk.getBlockLocation().getX()), (blockLocation.getY() - chunk.getBlockLocation().getY()), (blockLocation.getZ() - chunk.getBlockLocation().getZ()));
    }

    @Override
    public void addChunkListener(BlockChunkListener blockChunkListener)
    {
        chunkListeners.add(blockChunkListener);
        System.out.println("CLs " + chunkListeners.size());
    }

    public void removeChunkListener(BlockChunkListener blockChunkListener)
    {
        chunkListeners.remove(blockChunkListener);
    }

    public void updateBlockMaterial()
    {
        for (Vector3Int key : chunks.keySet())
        {
            chunks.get(key).updateBlockMaterial();
        }
    }

    @Override
    public CubesSettings getSettings()
    {
        return settings;
    }

    public void setChunk(BlockChunkControl chunk)
    {
        Vector3Int from = chunk.getLocation().mult(GameProperties.CHUNK_SIZE);
        Vector3Int to = chunk.getLocation().mult(GameProperties.CHUNK_SIZE).add(GameProperties.CHUNK_SIZE);
        int xx = 0;
        for (int x = from.x; x < to.x; x++)
        {
            int zz = 0;
            for (int z = from.z; z < to.z; z++)
            {
                int yy = 0;
                for (int y = from.y; y <= to.y; y++)
                {
                    Block block = chunk.getBlock(new Vector3Int(xx, yy, zz));
                    if (block != null)
                    {
                        setBlock(new Vector3Int(x, y, z), Blocks.BIRCHLOG);
                    }

                    yy++;
                }
                zz++;
            }
            xx++;
        }
    }

    public void setBlocksForMaximumFaces(Vector3Int location, Vector3Int size, Block block)
    {
        Vector3Int tmpLocation = new Vector3Int();
        for (int x = 0; x < size.getX(); x++)
        {
            for (int y = 0; y < size.getY(); y++)
            {
                for (int z = 0; z < size.getZ(); z++)
                {
                    if (((x ^ y ^ z) & 1) == 1)
                    {
                        tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
                        setBlock(tmpLocation, block);
                    }
                }
            }
        }
    }

    @Override
    public void markAsDoomed(Vector3Int chunkLocation)
    {
        doomedChunks.add(chunkLocation);
    }

    @Override
    public Set<Vector3Int> getDoomedChunks()
    {
        return doomedChunks;
    }

    @Override
    public void disposeOldChunks()
    {
        for (Vector3Int location : doomedChunks)
        {
            chunks.get(location).dispose();
            chunks.remove(location);
        }
        doomedChunks.clear();
    }

    @Override
    public void setBlocksFromNoise(Vector3Int location, Vector3Int size, float roughness, Block block)
    {
        Noise noise = new Noise(null, roughness, size.getX(), size.getZ());
        noise.initialise();
        float gridMinimum = noise.getMinimum();
        float gridLargestDifference = (noise.getMaximum() - gridMinimum);
        float[][] grid = noise.getGrid();
        for (int x = 0; x < grid.length; x++)
        {
            float[] row = grid[x];
            for (int z = 0; z < row.length; z++)
            {
                /*---Calculation of block height has been summarized to minimize the java heap---
                 float gridGroundHeight = (row[z] - gridMinimum);
                 float blockHeightInPercents = ((gridGroundHeight * 100) / gridLargestDifference);
                 int blockHeight = ((int) ((blockHeightInPercents / 100) * size.getY())) + 1;
                 ---*/
                int blockHeight = (((int) (((((row[z] - gridMinimum) * 100) / gridLargestDifference) / 100) * size.getY())) + 1);
                Vector3Int tmpLocation = new Vector3Int();
                for (int y = 0; y < blockHeight; y++)
                {
                    tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
                    setBlock(tmpLocation, block);
                }
            }
        }
    }

    public Set<Vector3Int> getChunkLocations()
    {
        return chunks.keySet();
    }

    // This method is a *lot* faster than using (int)Math.floor(x)
    private static int fastFloor(float x)
    {
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }
    private static boolean on = true;



    private void updateCollisionShape(BlockChunkControl chunk)
    {
        updateGeometry(chunk.getOptimizedGeometry_Opaque(), chunk, false);
        updateGeometry(chunk.getOptimizedGeometry_Transparent(), chunk, false);

        if (chunk.getBlockCount() <= 0)
        {
            chunk.getTerrain().markAsDoomed(chunk.getLocation());
        }
        for (int i = 0; i < chunkListeners.size(); i++)
        {
            BlockChunkListener blockTerrainListener = chunkListeners.get(i);
            blockTerrainListener.onSpatialUpdated(chunk);
        }
    }

    private void updateGeometry(Geometry chunkGeometry, BlockChunkControl chunk, boolean doomed)
    {
        if (chunkGeometry != null)
        {
            RigidBodyControl rigidBodyControl = chunkGeometry.getControl(RigidBodyControl.class);
            if (chunkGeometry.getTriangleCount() > 0 && chunk.getBlockCount() > 0 && !doomed)
            {
                if (rigidBodyControl != null)
                {
                    chunkGeometry.removeControl(rigidBodyControl);
                    physics.getPhysicsSpace().remove(rigidBodyControl);
                }
                rigidBodyControl = new RigidBodyControl(0);
                chunkGeometry.addControl(rigidBodyControl);
                chunkGeometry.getControl(RigidBodyControl.class).setFriction(GameProperties.FRICTION_DEFAULT);
                physics.getPhysicsSpace().add(rigidBodyControl);
            }
            else
            {
                if (rigidBodyControl != null)
                {
                    chunkGeometry.removeControl(rigidBodyControl);
                    physics.getPhysicsSpace().remove(rigidBodyControl);
                }
                chunkGeometry.removeFromParent();
            }
        }
    }

    @Override
    public void experiment()
    {
        on = !on;
    }

}
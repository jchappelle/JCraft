/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import com.jme3.scene.control.Control;
import java.util.Set;

/**
 *
 * @author Perry
 */
public interface BTControl extends Control
{
    public Block getBlock(Vector3Int location);
    public Block getPointedAtBlock(Vector3Int location);

    public Block getBlock(int x, int y, int z);
    public void removeBlock(Vector3Int location);
    public void setBlock(Vector3Int location, Block block);

    public void experiment();
    public void addChunkListener(BlockChunkListener blockChunkListener);
    public void setBlocksFromNoise(Vector3Int location, Vector3Int size, float roughness, Block block);
    public CubesSettings getSettings();
    public boolean hasChunk(Vector3Int chunkLocation);
    public BlockChunkControl fetchExistingChunkForBlockLocation(Vector3Int blockLocation);

    public void setBlock(int x, int y, int z, Block block);
    public Set<Vector3Int> getChunkLocations();
    public void markAsDoomed(Vector3Int chunkLocation);
    public Set<Vector3Int> getDoomedChunks();
    public void addAllScheduledChunks();
    public void scheduleChunkAdd(Chunk chunk);
    public void disposeOldChunks();
    BlockChunkControl getChunk(Vector3Int blockLocation);
    BlockTerrain_LocalBlockState getLocalBlockState(Vector3Int blockLocation);
    Vector3Int getLocalBlockLocation(Vector3Int blockLocation, BlockChunkControl chunk);
}

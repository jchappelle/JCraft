package com.chappelle.jcraft;

import Properties.GameProperties;
import com.chappelle.jcraft.blocks.BlockExplosionControl;
import com.chappelle.jcraft.blocks.Blocks;
import com.chappelle.jcraft.blocks.JBlock;
import com.chappelle.jcraft.blocks.Torch;
import com.cubes.BTControl;
import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.BlockNavigator;
import com.cubes.BlockState;
import com.cubes.BlockTerrain_LocalBlockState;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;

public class BlockTerrainManager extends AbstractAppState
{

    private CubesSettings cubeSettings;
    private BTControl terrain;
    private Node node = new Node("active blocks");
    private List<FallingBlocks> fallingBlocksList = new ArrayList<FallingBlocks>();
    private ItemControl itemControl;
    private BlockExplosionControl blockExplosionControl;
    private BlockGame app;

    public BlockTerrainManager(CubesSettings cubeSettings, BTControl terrain)
    {
        this.cubeSettings = cubeSettings;
        this.terrain = terrain;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);

        this.app = (BlockGame) app;
        this.app.getRootNode().attachChild(node);
        this.itemControl = new ItemControl(this.app, cubeSettings);
        this.app.getRootNode().addControl(itemControl);
        this.blockExplosionControl = new BlockExplosionControl(this.app.getAssetManager());
        Node explosionNode = new Node();
        explosionNode.addControl(blockExplosionControl);
        this.app.getRootNode().attachChild(explosionNode);
        this.app.getRootNode().attachChild(node);
    }

    private Vector3Int toBlockCenter(Vector3Int blockLocation)
    {
        return blockLocation.mult((int) GameProperties.CUBE_SIZE);
    }

    public void removeBlock(JBlock block, Vector3Int location)
    {
        if (block != Blocks.BEDROCK)
        {
            terrain.removeBlock(location);
            checkForFallingBlocks(location);

            Vector3Int siblingVector = block.getSiblingVector();
            if (siblingVector != null)
            {
                terrain.removeBlock(location.add(siblingVector));
            }

            Vector3Int blockCenter = toBlockCenter(location);
            blockExplosionControl.explodeBlock(block, blockCenter);
            block.playDigAudio();

            BlockTerrain_LocalBlockState localState = terrain.getLocalBlockState(location);
            itemControl.addItemAt(block, localState.getChunk(), location, blockCenter.toVector3f().add(1.5f, 1.5f, 1.5f));

            //Notify neighbors of block removal
            for (Block.Face face : Block.Face.values())
            {
                Vector3Int neighborLocation = BlockNavigator.getNeighborBlockLocalLocation(location, face);
                JBlock neighbor = (JBlock) terrain.getBlock(neighborLocation);
                if (neighbor != null)
                {
                    neighbor.onNeighborRemoved(location, neighborLocation, this);
                }
            }

        }
    }

    public void setBlock(PickedBlock pickedBlock, JBlock blockToPlace)
    {
        pickedBlock.setBlock(blockToPlace);//Kind of a hack, the pickedBlock has a null block at this point

        Vector3Int location = pickedBlock.getBlockLocation();
        Block.Face face = Block.Face.fromNormal(pickedBlock.getContactNormal());
        if (blockToPlace.isValidPlacementFace(face))
        {
            JBlock bottomBlock = (JBlock) terrain.getBlock(location.subtract(0, 1, 0));
            if (blockToPlace.isAffectedByGravity() && bottomBlock == null)
            {
                Geometry geometry = blockToPlace.makeBlockGeometry();
                geometry.setName("active block");
                Vector3f placementLocation = location.mult((int) GameProperties.CUBE_SIZE).toVector3f();
                geometry.setLocalTranslation(placementLocation);
                node.attachChild(geometry);

                FallingBlocks fallingBlocks = new FallingBlocks(getFloorGeometry(location), location);
                fallingBlocks.add(blockToPlace, geometry);
                fallingBlocksList.add(fallingBlocks);
            }
            else
            {
                terrain.setBlock(location, blockToPlace);
                pickedBlock.getBlock().initBlockState(pickedBlock, getBlockState(location));
                blockToPlace.playPlaceAudio();
            }
        }
    }

    public BlockState getBlockState(Vector3Int location)
    {
        BlockTerrain_LocalBlockState localState = terrain.getLocalBlockState(location);
        BlockChunkControl chunk = localState.getChunk();
        return chunk.getBlockState(localState.getLocalBlockLocation());
    }

    private void checkForFallingBlocks(Vector3Int location)
    {
        BlockTerrain_LocalBlockState localState = terrain.getLocalBlockState(location);
        BlockChunkControl chunk = localState.getChunk();

        Vector3Int topBlockLocation = location.add(0, 1, 0);
        JBlock topBlock = (JBlock) terrain.getPointedAtBlock(topBlockLocation);
        if (topBlock != null && topBlock.isAffectedByGravity())
        {
            //Loop over all upper blocks and add geometries if necessary
            int y = 1;

            FallingBlocks fallingBlocks = new FallingBlocks(getFloorGeometry(location), topBlockLocation);
            int height = GameProperties.CHUNK_SIZE.y;
            for (int i = location.getY() + 1; i < height; i++, y++)
            {
                Vector3Int currentLocation = location.add(0, y, 0);
                JBlock block = (JBlock) terrain.getBlock(currentLocation);
                if (block != null && block.isAffectedByGravity())
                {
                    terrain.removeBlock(currentLocation);

                    Geometry geometry = block.makeBlockGeometry();
                    geometry.setName("active block");
                    Vector3f placementLocation = toBlockCenter(location).toVector3f().add(0, y * GameProperties.CUBE_SIZE, 0);
                    geometry.setLocalTranslation(placementLocation);
                    node.attachChild(geometry);

                    fallingBlocks.add(block, geometry);
                }
                else
                {
                    break;
                }
            }
            fallingBlocksList.add(fallingBlocks);
        }
    }

    private Geometry getFloorGeometry(Vector3Int location)
    {
        return terrain.fetchExistingChunkForBlockLocation(location).getOptimizedGeometry_Opaque();
    }

    public void cleanup()
    {
        super.cleanup();
    }

    @Override
    public void update(float tpf)
    {
        super.update(tpf);

        List<FallingBlocks> doomed = new ArrayList<FallingBlocks>();

        for (FallingBlocks fallingBlocks : fallingBlocksList)
        {
            if (fallingBlocks.isDead())
            {
                doomed.add(fallingBlocks);
                for (Geometry geometry : fallingBlocks.getGeometries())
                {
                    node.detachChild(geometry);
                }
            }
            else
            {
                if (fallingBlocks.hitFloor())
                {
                    fallingBlocks.setDead(true);//Don't remove the geometries yet, it causes the block to blink
                    Vector3Int startingLocation = fallingBlocks.getStartingLocation();
                    int distanceFell = (int) (Math.round(fallingBlocks.getFallDistance()) / GameProperties.CUBE_SIZE);
                    Vector3Int placementLocation = new Vector3Int(startingLocation.getX(), startingLocation.getY() - distanceFell, startingLocation.getZ());
                    for (JBlock block : fallingBlocks.getBlocks())
                    {
                        terrain.setBlock(placementLocation, block);
                        placementLocation.addLocal(0, 1, 0);
                    }
                }
                else
                {
                    fallingBlocks.fall(tpf);
                }
            }
        }
        fallingBlocksList.removeAll(doomed);
        doomed.clear();
    }

    private class FallingBlocks
    {

        private List<JBlock> blocks = new ArrayList<JBlock>();
        private List<Geometry> geometries = new ArrayList<Geometry>();
        private Geometry floor;
        private boolean dead;
        private float fallDistance;
        private Vector3Int startingLocation;//This is the location of the bottom most block
        private boolean isPlacingBlock;

        public FallingBlocks(Geometry floor, Vector3Int startingLocation)
        {
            this.floor = floor;
            this.startingLocation = startingLocation;
        }

        public boolean isIsPlacingBlock()
        {
            return isPlacingBlock;
        }

        public Vector3Int getStartingLocation()
        {
            return startingLocation;
        }

        public List<Geometry> getGeometries()
        {
            return geometries;
        }

        public boolean isDead()
        {
            return dead;
        }

        public void setDead(boolean dead)
        {
            this.dead = dead;
        }

        public List<JBlock> getBlocks()
        {
            return blocks;
        }

        public void add(JBlock block, Geometry geometry)
        {
            geometries.add(geometry);
            blocks.add(block);
        }

        public float getFallDistance()
        {
            return fallDistance;
        }

        public void fall(float tpf)
        {
            float fallAmount = tpf * 12;
            for (Geometry geometry : geometries)
            {
                geometry.setLocalTranslation(geometry.getLocalTranslation().subtract(0, fallAmount, 0));
            }
            fallDistance += fallAmount;
        }

        private Geometry getBottomBlock()
        {
            if (geometries.isEmpty())
            {
                return null;
            }
            return geometries.get(0);

        }

        public boolean hitFloor()
        {
            if (fallDistance > GameProperties.CUBE_SIZE * 0.8)
            {
                Vector3f origin = getBottomBlock().getWorldTranslation().add(GameProperties.CUBE_SIZE / 2, 0, GameProperties.CUBE_SIZE / 2);
                Vector3f direction = new Vector3f(0.0f, -1.0f, 0.0f);
                Ray ray = new Ray(origin, direction);
                CollisionResults results = new CollisionResults();

                floor.collideWith(ray, results);

                if (results.size() > 0)
                {
                    Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
                    float distanceToFloor = collisionContactPoint.distance(origin);
                    if (distanceToFloor < 0.2)
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
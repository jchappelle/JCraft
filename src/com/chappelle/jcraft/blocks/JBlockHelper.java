package com.chappelle.jcraft.blocks;

import com.chappelle.jcraft.BlockGame;
import Properties.GameProperties;
import com.chappelle.jcraft.RunningAppState;
import com.chappelle.jcraft.PickedBlock;
import com.cubes.BTControl;
import com.cubes.BlockNavigator;
import com.cubes.CubesSettings;
import com.cubes.Util;
import com.cubes.Vector3Int;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class JBlockHelper
{
    private BTControl blockTerrain;
    private AppSettings settings;
    private Camera cam;
    private CubesSettings cubeSettings;
    private RunningAppState runningState;

    public JBlockHelper(BlockGame game, RunningAppState runningState)
    {
        this.runningState = runningState;
        this.blockTerrain = runningState.getBlockTerrain();
        this.settings = game.getAppSettings();
        this.cam = game.getCamera();
        this.cubeSettings = runningState.getCubeSettings();
    }

    public void setTree(Vector3Int location)
    {
        int logHeight = 3;
        int leavesHeight = 3;;
        Vector3Int cursor = new Vector3Int(location.getX(), location.getY()+logHeight, location.getZ());

        int y = cursor.getY();
        for(int i = 0; i < leavesHeight; i++,y++)
        {
            blockTerrain.setBlock(new Vector3Int(cursor.getX(), y, cursor.getZ()), Blocks.OAK_LEAVES);
            blockTerrain.setBlock(new Vector3Int(cursor.getX()+1, y, cursor.getZ()), Blocks.OAK_LEAVES);
            blockTerrain.setBlock(new Vector3Int(cursor.getX()-1, y, cursor.getZ()), Blocks.OAK_LEAVES);
            blockTerrain.setBlock(new Vector3Int(cursor.getX(), y, cursor.getZ()+1), Blocks.OAK_LEAVES);
            blockTerrain.setBlock(new Vector3Int(cursor.getX(), y, cursor.getZ()-1), Blocks.OAK_LEAVES);
            blockTerrain.setBlock(new Vector3Int(cursor.getX()-1, y, cursor.getZ()-1), Blocks.OAK_LEAVES);
            blockTerrain.setBlock(new Vector3Int(cursor.getX()+1, y, cursor.getZ()+1), Blocks.OAK_LEAVES);
            blockTerrain.setBlock(new Vector3Int(cursor.getX()+1, y, cursor.getZ()-1), Blocks.OAK_LEAVES);
            blockTerrain.setBlock(new Vector3Int(cursor.getX()-1, y, cursor.getZ()+1), Blocks.OAK_LEAVES);
        }

        cursor.set(location.getX(), location.getY(), location.getZ());
        double rand = Math.random() * 3;
        JBlock log = Blocks.LOG;
        if(rand>1)
        {
            log = Blocks.SPRUCE_LOG;
        }
        if(rand>2)
        {
            log = Blocks.BIRCHLOG;
        }


        for(int i = 0; i < logHeight+leavesHeight - 1; i++)
        {
            blockTerrain.setBlock(cursor, log);
            cursor.addLocal(0, 1, 0);
        }
    }
    public JBlock getBlock(Vector3Int blockLocation)
    {
        return (JBlock) blockTerrain.getBlock(blockLocation);
    }

    private CollisionResults getRayCastingResults(Node node, Integer rayLength)
    {
        Vector3f origin = cam.getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();
        Ray ray = new Ray(origin, direction);
        if(rayLength != null)
        {
            ray.setLimit(rayLength);
        }
        CollisionResults results = new CollisionResults();
        node.collideWith(ray, results);
        return results;
    }

    /**
     * Returns the pointed at block location in chunk space. The coordinates are relative to the chunk
     * and the BlockNavigator is used to get the pointed block location. This method should be used
     * when manipulating blocks in the Cubes api.
     * @param getNeighborLocation Whether to return the neighbor block location. Useful for adding blocks.
     * @return A Vector3Int representing the pointed at block location in chunk space
     */
    public Vector3Int getPointedBlockLocationInChunkSpace(boolean getNeighborLocation)
    {
        CollisionResults results = getRayCastingResults(runningState.getBlocks(), GameProperties.BLOCK_PICK_LENGTH);
        if (results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
            return BlockNavigator.getPointedBlockLocation(blockTerrain, collisionContactPoint, getNeighborLocation);
        }
        return null;
    }

    /**
     * Returns the world space coordinates for the block that is being pointed at with the crosshairs.
     * It does this by doing an infinite length ray cast
     * @return A Vector3Int representing the world space coordinates for the pointed at block.
     */
    public Vector3Int getPointedBlockLocationInWorldSpace()
    {
        return toVector3Int(getPointedBlockLocationInWorldSpaceVector());
    }

    public PickedBlock pickBlock()
    {
        return pickBlock(false);
    }
    
    public PickedBlock pickNeighborBlock()
    {
        return pickBlock(true);
    }
    
    private PickedBlock pickBlock(boolean getNeighborLocation)
    {
        CollisionResults results = getRayCastingResults(runningState.getBlocks(), null);
        if (results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
            Vector3Int blockLocation = BlockNavigator.getPointedBlockLocation(blockTerrain, collisionContactPoint, getNeighborLocation);
            JBlock block = getBlock(blockLocation);
            if(!getNeighborLocation && block == null)//If we are getting a neighbor then the block could be null, otherwise we don't allow null
            {
                return null;
            }
            return new PickedBlock(block, blockLocation, results);
        }
        return null;
    }

    /**
     * Returns the center of the block. This can be used to place real geometries in the scene
     * at the given chunk location. Just set the Geometry's localTranslation to the result of
     * this method.
     */
    public Vector3Int getBlockCenterInWorldSpace(Vector3Int chunkLocation)
    {
        return chunkLocation.mult((int)GameProperties.CUBE_SIZE);
    }

    private Vector3f getPointedBlockLocationInWorldSpaceVector()
    {
        CollisionResults results = getRayCastingResults(runningState.getBlocks(), null);
        if (results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
            Vector3f collisionLocation = Util.compensateFloatRoundingErrors(collisionContactPoint);
            return collisionLocation;
        }
        return null;
    }

    public static Vector3Int toVector3Int(Vector3f vector)
    {
        if(vector == null)
        {
            return null;
        }
        vector = Util.compensateFloatRoundingErrors(vector);
        return new Vector3Int((int)vector.getX(), (int)vector.getY(), (int)vector.getZ());
    }

    public static Vector3f toVector(Vector3Int point)
    {
        return new Vector3f(point.getX(), point.getY(), point.getZ());
    }

    public float getDistanceToPointedLocation(Vector3f playerLocation)
    {
        Vector3f pointedBlock = getPointedBlockLocationInWorldSpaceVector();
        if(pointedBlock == null)
        {
            return 0;
        }
        else
        {
            return playerLocation.distance(pointedBlock);
        }
    }

    public JBlock getSteppedOnBlock(Vector3f playerLocation)
    {
        Vector3f origin = playerLocation;
        Vector3f direction = new Vector3f(0.0f, -1.0f, 0.0f);
        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        runningState.getBlocks().collideWith(ray, results);

        if(results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
            Vector3Int blockLocation = BlockNavigator.getPointedBlockLocation(blockTerrain, collisionContactPoint, false);
            if(blockLocation != null)
            {
                return getBlock(blockLocation);
            }
        }
        return null;
    }

}

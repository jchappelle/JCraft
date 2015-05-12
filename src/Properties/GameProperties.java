/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import com.cubes.Vector3Int;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;

/**
 *
 * @author Perry
 */
public interface GameProperties
{
    final long SEED = 54321; // pick a number.. any number... as long it's long -1 generates a random seed
    final boolean SAVE = true;
    final boolean SKY_ENABLED = true;
    final Vector3Int CHUNK_SIZE = new Vector3Int(11, 60, 11);
    final Vector3Int CHUNK_VIEW_DISTANCE = new Vector3Int(13, 1, 13); // in Chunks
    final boolean USE_FPP = false;
    final int ANTIALIAS_AMOUNT = 1;
    final int RESOLUTION_WIDTH = 1200;
    final int RESOLUTION_HEIGHT = 800;

    final float FRICTION_DEFAULT = 0.0f;
    final boolean FLY_CAMERA=false;
    final float FLY_CAMERA_SPEED=100;
    final RenderQueue.ShadowMode SHADOW_MODE = RenderQueue.ShadowMode.Receive;
    final float BLOCK_INTERACTION_RANGE=5;

    final int HUD_X_OFFSET = 10;
    final int HUD_Y_INCREMENT = 20;
    final int HUD_Y_OFFSET = 680;

    final boolean SHOW_SETTINGS_ON_STARTUP=false;
    final boolean FULL_SCREEN = false;
    final boolean PLAY_MUSIC = false;
    final boolean LOOP_MUSIC = true;
    final float PLAYER_WALK_SPEED = 40.0f;
    final float PLAYER_RUN_SPEED = 0.6f;
    final float PLAYER_FLY_SPEED = 20f;
    final float PLAYER_STRAFF_SPEED_ADJUSTMENT = 0.6f;

    final Vector3f PLAYER_JUMP_FORCE = new Vector3f(0.0f, 100.0f, 0.0f);
    final Vector3f WORLD_GRAVITY = new Vector3f(0.0f, -70.0f, 0.0f);
    final String WORLD_SAVE_LOCATION = "c:/jcraft/saves/";
    final int BLOCK_PICK_LENGTH = 15;
    final int ITEM_TTL_SECONDS = 300;
    final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(0.7f, 0.8f, 1f, 1.0f);
    final Vector3f WORLD_LIGHT_DIRECTION = new Vector3f(1.8f, -1, .8f).normalizeLocal();
    final ColorRGBA WORLD_LIGHT_COLOR = new ColorRGBA(1f, 1f, 1f, 1.0f);
    final int WORLD_SHADOW_MAP_SIZE = 512;
    final int WORLD_SHADOW_NB_SPLITS = 3;
    final int WORLD_PHYSICS_SUBSTEPS = 4;
    final float WORLD_TIME_RATE = 10f;
    final float WORLD_INITIAL_TIME_OF_DAY = 15f;
    final float WORLD_CLOUD_FLATTENING = 0.9f;
    final float WORLD_CLOUD_CLOUDINESS = 0.8f;
    final boolean WORLD_STAR_MOTION = true;
    final int WORLD_EQUATOR_SAMPLES = 12;
    final int WORLD_MERIDIAN_SAMPLES = 24;
    final int WORLD_SKY_RESOLUTION = 512;
    final float WORLD_OBSERVER_LATITUDE = 0.2f;
    final float WORLD_BLOOM_INTENSITY = 1.7f;
    final float WORLD_BLOOM_BLUR_SCALE =  2.5f;
    final float WORLD_BLOOM_EXPOSURE_POWER = 1.0f;

    final int PLAYER_STARTING_HEALTH = 100;
    final float WORLD_DROP_ITEM_SHRINK_FACTOR = .15f;

    final float CUBE_SIZE = 3f;
    final boolean SCALE_PLAYER_WITH_BLOCKS=true;

}

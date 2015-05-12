package com.chappelle.jcraft.blocks;

import Properties.GameProperties;
import Properties.MaterialProperties;
import Properties.TextureProperties;
import com.chappelle.jcraft.BlockTerrainManager;
import com.chappelle.jcraft.CubeGeometryFactory;
import com.chappelle.jcraft.Game;
import com.chappelle.jcraft.PickedBlock;
import com.chappelle.jcraft.RunningAppState;
import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockState;
import com.cubes.CubesSettings;
import com.cubes.MeshFactory;
import com.cubes.Vector3Int;
import com.cubes.shapes.BlockShape_Cuboid;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public class JBlock extends Block
{
    private static int nextId = 1;
    private int id;
    private AudioNode digAudio;
    private AudioNode placeAudio;
    private AudioNode stepAudio;
    private AssetManager assetManager;
    private BlockSkin[] skins;
    private boolean transparent;
    private CubesSettings cubeSettings;
    private boolean affectedByGravity = false;
    private int spriteIndex;
    private int stackSize;
    private CubeGeometryFactory geometryFactory;
    
    private static final float[] DROP_ITEM_EXTENTS = new float[]{GameProperties.WORLD_DROP_ITEM_SHRINK_FACTOR,GameProperties.WORLD_DROP_ITEM_SHRINK_FACTOR,GameProperties.WORLD_DROP_ITEM_SHRINK_FACTOR,GameProperties.WORLD_DROP_ITEM_SHRINK_FACTOR,GameProperties.WORLD_DROP_ITEM_SHRINK_FACTOR,GameProperties.WORLD_DROP_ITEM_SHRINK_FACTOR};
    private static final float[] BLOCK_EXTENTS = new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f};
    public JBlock(BlockSkin... skins)
    {
        super(skins);
        
        id = nextId++;
        this.skins = skins;
        transparent = skins[0].isTransparent();
        BlockManager.register(this);
        this.assetManager = Game.getInstance().getAssetManager();
        this.cubeSettings = Game.getInstance().getStateManager().getState(RunningAppState.class).getCubeSettings();
        
        int skinIndex = 0;
        if(skins.length < 5)
        {
            skinIndex = 0;
        }
        else
        {
            skinIndex = 4;
        }
        spriteIndex = skins[skinIndex].getTextureLocation().getRow()*16+skins[skinIndex].getTextureLocation().getColumn();            
        this.stackSize = 64;
        
        this.geometryFactory = new CubeGeometryFactory(cubeSettings, new CubeDescriptor(BLOCK_EXTENTS, skins[0].getTextureLocation()));
    }
    
    public int getSpriteIndex()
    {
        return spriteIndex;
    }

    public AudioNode getDigAudio()
    {
        return digAudio;
    }

    public void setDigAudio(AudioNode digAudio)
    {
        this.digAudio = digAudio;
    }

    public void playPlaceAudio()
    {
        playIfExists(placeAudio);
    }

    public void playDigAudio()
    {
        playIfExists(digAudio);
    }

    public AudioNode getPlaceAudio()
    {
        return placeAudio;
    }

    public void setPlaceAudio(AudioNode placeAudio)
    {
        this.placeAudio = placeAudio;
    }

    public AudioNode getStepAudio()
    {
        return stepAudio;
    }

    public void setStepAudio(AudioNode stepAudio)
    {
        this.stepAudio = stepAudio;
    }

    protected AudioNode makeAudio(String location)
    {
        AudioNode result = new AudioNode(assetManager, location);
        result.setReverbEnabled(false);
        result.setVolume(.3f);
        return result;
    }

    private void playIfExists(AudioNode audio)
    {
        if (audio != null)
        {
            audio.stop();
            audio.play();
        }
    }

    /**
     * Returns the ParticleEmitter for emitting particles when the user digs
     * this block
     */
    public ParticleEmitter getDigParticleEmitter()
    {
        ParticleEmitter emitter = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
        Material material = new Material(assetManager, MaterialProperties.PARTICLE);
        material.setTexture("Texture", assetManager.loadTexture(TextureProperties.DEBRIS));
        emitter.setMaterial(material);
        emitter.setImagesX(3);
        emitter.setImagesY(3); // 3x3 texture animation
        emitter.setRotateSpeed(4);
        emitter.setSelectRandomImage(true);
        emitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 8, 0));
        emitter.setStartColor(ColorRGBA.White);
        emitter.setEndColor(ColorRGBA.White);
        emitter.setGravity(0, 15, 0);
        emitter.getParticleInfluencer().setVelocityVariation(.90f);
        return emitter;
    }

    public int getMass()
    {
        return 1;
    }

    public boolean isTransparent()
    {
        return transparent;
    }

    public Vector3Int getSiblingVector()
    {
        return null;
    }

    public boolean isAffectedByGravity()
    {
        return affectedByGravity;
    }

    public void setAffectedByGravity(boolean affectedByGravity)
    {
        this.affectedByGravity = affectedByGravity;
    }

    public int getId()
    {
        return id;
    }

    public void setStackSize(int stackSize)
    {
        this.stackSize = stackSize;
    }
    
    public int getStackSize()
    {
        return stackSize;
    }
    
    public Mesh makeDropItemMesh(BlockChunkControl chunk, Vector3Int location)
    {
        MeshFactory mesh = new MeshFactory(new BlockShape_Cuboid(this, DROP_ITEM_EXTENTS));
        return mesh.makeMesh(chunk, location, false);
    }

    public Geometry makeBlockGeometry()
    {
        return geometryFactory.makeGeometry(this);
    }

    public void initBlockState(PickedBlock pickedBlock, BlockState blockState)
    {    
    }
    
    public void onNeighborRemoved(Vector3Int removedBlockLocation, Vector3Int myLocation, BlockTerrainManager aThis)
    {
    }
    
    public boolean isValidPlacementFace(Block.Face face)
    {
        return true;
    }
}

package com.chappelle.jcraft;

import Properties.GameProperties;
import Properties.TextureProperties;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import jme3utilities.Misc;
import jme3utilities.MyAsset;
import jme3utilities.TimeOfDay;
import jme3utilities.ViewPortListener;
import jme3utilities.WaterProcessor;
import jme3utilities.sky.GlobeRenderer;
import jme3utilities.sky.SkyControl;
import jme3utilities.sky.Updater;

public class EnvironmentAppState extends AbstractAppState implements ViewPortListener
{
    private static final float initialTimeOfDay = GameProperties.WORLD_INITIAL_TIME_OF_DAY;
    private static final float timeRate = GameProperties.WORLD_TIME_RATE;
    /**
     * width and height of rendered shadow maps (pixels per side, &gt;0)
     */
    final private static int shadowMapSize = GameProperties.WORLD_SHADOW_MAP_SIZE;
    /**
     * number of shadow map splits (&gt;0)
     */
    final private static int shadowMapSplits = GameProperties.WORLD_SHADOW_NB_SPLITS;
    private BlockGame app;
    private SkyControl sky;
    private Spatial cubeMap = null;
    private Node sceneNode;
    private AssetManager assetManager;
    private Camera cam;
    private Node rootNode;
    private ViewPort viewPort;
    private TimeOfDay timeOfDay;

    private AmbientLight ambientLight = null;
    private DirectionalLight mainLight = null;

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);

        this.app = (BlockGame) app;
        this.viewPort = this.app.getViewPort();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.cam = this.app.getCamera();
        this.sceneNode = new Node();

        initializeLights();
        cubeMap = MyAsset.createStarMap(assetManager, "purple-nebula-complex");
        sceneNode.attachChild(cubeMap);
        if(GameProperties.SKY_ENABLED)
        {
        this.sky = new SkyControl(assetManager, cam, GameProperties.WORLD_CLOUD_FLATTENING, GameProperties.WORLD_STAR_MOTION, true);
        sceneNode.addControl(sky);
        rootNode.attachChild(sceneNode);
        sky.clearStarMaps();
        sky.setCloudiness(GameProperties.WORLD_CLOUD_CLOUDINESS);
        sky.setCloudModulation(true);

        Texture moonTexture = MyAsset.loadTexture(assetManager, TextureProperties.MOON_TEXTURE);
        Material moonMaterial = MyAsset.createShadedMaterial(assetManager, moonTexture);
        int equatorSamples = GameProperties.WORLD_EQUATOR_SAMPLES;
        int meridianSamples = GameProperties.WORLD_MERIDIAN_SAMPLES;
        int resolution = GameProperties.WORLD_SKY_RESOLUTION;
        GlobeRenderer moonRenderer = new GlobeRenderer(moonMaterial,Image.Format.Luminance8Alpha8, equatorSamples, meridianSamples, resolution);
        stateManager.attach(moonRenderer);
        sky.setMoonRenderer(moonRenderer);

        timeOfDay = new TimeOfDay(initialTimeOfDay);
        stateManager.attach(timeOfDay);
        timeOfDay.setRate(timeRate);

        for (Light light : rootNode.getLocalLightList())
        {
            if (light.getName().equals("ambient"))
            {
                sky.getUpdater().setAmbientLight((AmbientLight) light);
            }
            else if (light.getName().equals("main"))
            {
                sky.getUpdater().setMainLight((DirectionalLight) light);
            }
        }
        sky.getSunAndStars().setObserverLatitude(GameProperties.WORLD_OBSERVER_LATITUDE);


        Updater updater = sky.getUpdater();

        updater.addViewPort(viewPort);
        updater.setAmbientLight(ambientLight);
        updater.setMainLight(mainLight);
        this.sky.setEnabled(true);

        //addShadows(viewPort);
        //addBloom(viewPort);
          
        }
//        addWater();
    }

   /**
     * Create a horizontal square of water and add it to the scene.
     * <p>
     * During initialization of the water processor (on the 1st update), the
     * processor will discover the SkyControl and put the SkyControl in charge
     * of the processor's background colors.
     */
    private void addWater() {
        WaterProcessor wp = new WaterProcessor(assetManager);
        viewPort.addProcessor(wp);
        wp.addListener(sky.getUpdater());
        wp.addListener(this);
        //wp.setDebug(true);
        wp.setDistortionMix(1f);
        wp.setDistortionScale(0.1f);
        wp.setReflectionClippingOffset(0f);
        wp.setReflectionScene(sceneNode);
        wp.setRefractionClippingOffset(0f);
        wp.setWaterTransparency(0f);
        wp.setWaveSpeed(0.02f);

        float diameter = 400f; // world units
        Geometry water = wp.createWaterGeometry(diameter, diameter);
        rootNode.attachChild(water);

        float depth = 0.3f; // world units
        Plane waterPlane = new Plane(Vector3f.UNIT_Y, depth);
        wp.setPlane(waterPlane);
        wp.setWaterDepth(depth);

        float xzOffset = diameter / 2f;
        water.setLocalTranslation(-xzOffset, depth, xzOffset);
        Vector2f textureScale = new Vector2f(10f, 10f);
        water.getMesh().scaleTextureCoordinates(textureScale);
    }

    private void initializeLights()
    {
        mainLight = new DirectionalLight();
        mainLight.setName("main");

        ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White.mult(1.3f));
        ambientLight.setName("ambient");
    }

    private void addBloom(ViewPort viewPort)
    {
        assert viewPort != null;

        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setBloomIntensity(GameProperties.WORLD_BLOOM_INTENSITY);
        bloom.setBlurScale(GameProperties.WORLD_BLOOM_BLUR_SCALE);
        bloom.setExposurePower(GameProperties.WORLD_BLOOM_EXPOSURE_POWER);
        Misc.getFpp(viewPort, assetManager).addFilter(bloom);
        sky.getUpdater().addBloomFilter(bloom);
    }

    @Override
    public void update(float tpf)
    {
        super.update(tpf);
        if(GameProperties.SKY_ENABLED)
        {
            float hour = timeOfDay.getHour();
            sky.getSunAndStars().setHour(hour);
            sky.getSunAndStars().orientExternalSky(cubeMap);
        }
    }

   private void addShadows(ViewPort viewPort)
   {
        boolean shadowFilter = false;


        Updater updater = sky.getUpdater();
        if (shadowFilter)
        {
            DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, shadowMapSize, shadowMapSplits);
            dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
            dlsf.setLight(mainLight);
            Misc.getFpp(viewPort, assetManager).addFilter(dlsf);
            updater.addShadowFilter(dlsf);

        }
        else
        {
            DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, shadowMapSize, shadowMapSplits);
            dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
            dlsr.setLight(mainLight);
            updater.addShadowRenderer(dlsr);
            viewPort.addProcessor(dlsr);
        }
    }
   /**
     * Callback when a view port is added, to apply shadows to the viewport.
     *
     * @param viewPort (not null)
     */
    @Override
    public void addViewPort(ViewPort viewPort) {
        assert viewPort != null;
        addShadows(viewPort);
    }

    /**
     * Callback when a view port is removed. Does nothing.
     */
    @Override
    public void removeViewPort(ViewPort unused) {
        /* no action required */
    }}

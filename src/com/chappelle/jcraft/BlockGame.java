package com.chappelle.jcraft;

import Properties.DebugProperties;
import Properties.MusicProperties;
import Properties.GameProperties;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.system.AppSettings;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * This is the main for the game
 */
public class BlockGame extends SimpleApplication implements Serializable
{
    private BulletAppState bulletAppState;
    private AudioNode music;
    private boolean debugState = false;

    public static void main(String[] args)
    {
        Logger.getLogger("").setLevel(DebugProperties.DEFAULT_LOG_LEVEL);
        BlockGame app = new BlockGame();
        app.start();
    }

    public BlockGame()
    {
        settings = new AppSettings(true);
        settings.setWidth(GameProperties.RESOLUTION_WIDTH);
        settings.setHeight(GameProperties.RESOLUTION_HEIGHT);
        settings.setTitle("JCraft - Minecraft Clone");
        settings.setSamples(GameProperties.ANTIALIAS_AMOUNT);
        settings.setFullscreen(GameProperties.FULL_SCREEN);
        showSettings = GameProperties.SHOW_SETTINGS_ON_STARTUP;
        Game.setInstance(this);
    }

    @Override
    public void simpleInitApp()
    {
        stateManager.getState(StatsAppState.class).toggleStats();
        flyCam.setDragToRotate(true);
        StartScreenState startScreenState = new StartScreenState(this);
        stateManager.attach(startScreenState);

        if(GameProperties.USE_FPP)
        {
            FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
            SSAOFilter ssao = new SSAOFilter();
            fpp.addFilter(ssao);
            viewPort.addProcessor(fpp);
        }

    }

    public BitmapFont getGuiFont()
    {
        return guiFont;
    }

    public void setGuiFont(BitmapFont guiFont)
    {
        this.guiFont = guiFont;
    }

    public AppSettings getAppSettings()
    {
        return this.settings;
    }

    public void startGame()
    {
        flyCam.setDragToRotate(false);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        bulletAppState.getPhysicsSpace().setMaxSubSteps(GameProperties.WORLD_PHYSICS_SUBSTEPS);
        bulletAppState.setDebugEnabled(debugState);

        RunningAppState runningState = new RunningAppState();
        stateManager.attach(runningState);

        music = new AudioNode(assetManager, MusicProperties.CALM2);
        music.setReverbEnabled(false);
        music.setPositional(false);
        music.setLooping(GameProperties.LOOP_MUSIC);

        if(GameProperties.PLAY_MUSIC)
        {
            music.play();
        }
    }

    public void toggleDebug()
    {
        debugState = !debugState;
        bulletAppState.setDebugEnabled(debugState);
    }
}
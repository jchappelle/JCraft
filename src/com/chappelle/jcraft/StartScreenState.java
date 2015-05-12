package com.chappelle.jcraft;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartScreenState extends AbstractAppState implements ScreenController
{
    private Logger log = Logger.getLogger(StartScreenState.class.getName());
    private Nifty nifty;
    private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private Node localRootNode = new Node("Start Screen RootNode");
    private Node localGuiNode = new Node("Start Screen GuiNode");
    private final ColorRGBA backgroundColor = ColorRGBA.Gray;
    private BlockGame app;
    private NiftyJmeDisplay niftyDisplay;

    public StartScreenState(SimpleApplication app)
    {
        this.rootNode = app.getRootNode();
        this.viewPort = app.getViewPort();
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();
        this.app = (BlockGame) app;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);

        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);
        viewPort.setBackgroundColor(backgroundColor);

        niftyDisplay = new NiftyJmeDisplay(assetManager, app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/start.xml", "start", this);
        nifty.addXml("Interface/hud.xml");
        nifty.addXml("Interface/inventory.xml");
        app.getGuiViewPort().addProcessor(niftyDisplay);                    
//        try
//        {
//            log.log(Level.INFO, "Validating start.xml");
//            nifty.validateXml("Interface/start.xml");
//            log.log(Level.INFO, "Validating hud.xml");            
//            nifty.validateXml("Interface/hud.xml");
//            log.log(Level.INFO, "Validating inventory.xml");            
//            nifty.validateXml("Interface/inventory.xml");
//            
//        }
//        catch (Exception ex)
//        {
//            log.log(Level.SEVERE, null, ex);
//        }

    }

    public Nifty getNifty()
    {
        return nifty;
    }
    @Override
    public void update(float tpf)
    {
    }

    @Override
    public void cleanup()
    {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);

        super.cleanup();
    }

    public void bind(Nifty nifty, Screen screen)
    {
    }

    public void onStartScreen()
    {
    }

    public void onEndScreen()
    {
    }

    public void startGame()
    {
        app.startGame();
    }

    public void stopGame()
    {
        System.out.println("Stopping...");
        app.stop();
    }
}
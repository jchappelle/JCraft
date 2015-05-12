package com.chappelle.jcraft;

import Properties.TextureProperties;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.HashMap;
import java.util.Map;

public class InventoryAppState extends AbstractAppState implements ScreenController, DroppableDropFilter
{
    private Inventory inventory;
    private PanelBuilder panelBuilder;
    private ControlBuilder slotBuilder;
    private ControlBuilder itemBuilder;
    private Nifty nifty;
    private Screen screen;
    private InputManager inputManager;
    private FlyByCamera flyCam;
    private PlayerControl playerControl;
    private AppStateManager stateManager;
    private Map<String, ItemStackLocation> locations = new HashMap<String, ItemStackLocation>();
    
    public boolean accept(Droppable drpbl, Draggable drgbl, Droppable drpbl1)
    {
        
        String slotId = drpbl1.getId();
        String itemId = drgbl.getId();
        
        System.out.println("To ID: " + slotId);
        System.out.println("Item: " + itemId);
        
        ItemStackLocation location = locations.get(itemId);
        if(location == null)
        {
            return false;
        }
        else
        {            
            int newItemSlot = Integer.parseInt(slotId.replace("itemSlot", ""));
            inventory.move(location.inventoryLocation, newItemSlot, location.itemStack); 
            location.inventoryLocation = newItemSlot;
            location.slotId = slotId;
            return true;
        }

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        
        inventory = app.getStateManager().getState(RunningAppState.class).getPlayerControl().getInventory();
        
        inputManager = app.getInputManager();
        flyCam = ((SimpleApplication)app).getFlyByCamera();
        playerControl = stateManager.getState(RunningAppState.class).getPlayerControl();
        this.stateManager = app.getStateManager();
    }

    
    public void bind(Nifty nifty, Screen screen)
    {
        this.screen = screen;
        this.nifty = nifty;        
    }

    public void onStartScreen()
    {
        if("inventoryScreen".equals(screen.getScreenId()))
        {
            locations.clear();
            
            inputManager.addMapping("close", new KeyTrigger(KeyInput.KEY_F12));        
            inputManager.addListener(new InventoryActionListener(), "close");
            
            int index = 0;
            int numColumns = 9;
            int rowCount = 5;
            for(int col = 0; col < numColumns; col++)
            {           
                panelBuilder = new PanelBuilder("");
                
                panelBuilder.id("inventoryColumn" + col);
                panelBuilder.childLayoutVertical();
                Element column = panelBuilder.build(nifty, screen, screen.findElementByName("inventorySlots"));
                for(int j = 0; j < rowCount; j++,index++)
                {
                    slotBuilder = new ControlBuilder("itemSlot");
                    
                    slotBuilder.id("itemSlot" + index);
                    Element e = slotBuilder.build(nifty, screen, column);
                    
                    e.findNiftyControl("itemSlot" + index, Droppable.class).addFilter(InventoryAppState.this);   
                }
                
                Element equippedColumn = panelBuilder.build(nifty, screen, screen.findElementByName("equippedSlots"));
                slotBuilder = new ControlBuilder("itemSlot");
                String equipmentSlotId = "itemSlot" + (col + Inventory.INVENTORY_SIZE);
                slotBuilder.id(equipmentSlotId);
                Element e = slotBuilder.build(nifty, screen, equippedColumn);
                e.findNiftyControl(equipmentSlotId, Droppable.class).addFilter(InventoryAppState.this);   
            }

            ItemStack[] items = inventory.getItemStacks();
            for(int i = 0; i < Inventory.INVENTORY_SIZE; i++)
            {
                String itemId = "item" + i;
                String slotId = "itemSlot" + i;                
                                
                Element itemSlot = screen.findElementByName(slotId);
                if(itemSlot != null)
                {
                    Element e = itemSlot.findElementByName("item" + i);
                    ItemStack item = items[i];
                    locations.put(itemId, new ItemStackLocation(slotId, itemId, i, item));
                    if(item != null)
                    {
                        if(e == null)
                        {
                            itemBuilder = new ControlBuilder("item");
                            itemBuilder.id(itemId);
                            itemBuilder.visibleToMouse(true);                            
                            e = itemBuilder.build(nifty, screen, itemSlot);
                        }                        
                        final int spriteIndex = item.getBlock().getSpriteIndex();                
                        ImageBuilder imageBuilder = new ImageBuilder()
                        {{ 
                            filename(TextureProperties.ACTIVE_BLOCK_TEXTURE);
                            imageMode("sprite:32,32," + spriteIndex);
                            width("50");
                            height("50");

                        }};
                        imageBuilder.build(nifty, screen, e);      
                        
                        TextBuilder textBuilder = new TextBuilder();
                        textBuilder.style("nifty-label");
                        textBuilder.font("Interface/Fonts/ArialBlack.fnt");
                        textBuilder.textVAlignBottom();                        
                        textBuilder.textHAlignRight();
                        textBuilder.text(Integer.toString(item.getCount()));
                        textBuilder.alignCenter();
                        textBuilder.build(nifty, screen, e);
                        
                    }    
                    else
                    {
                        Element blockItem = itemSlot.findElementByName("item" + i);
                        if(blockItem != null)
                        {
                            blockItem.setVisible(false);
                            blockItem.markForRemoval();
                        }
                        
                    }
                    
                }
            }            
            
            for(int i = Inventory.INVENTORY_SIZE; i < items.length; i++)
            {
                String slotId = "itemSlot" + i;
                String itemId = "item" + i;
                
                Element itemSlot = screen.findElementByName(slotId);
                if(itemSlot != null)
                {
                    Element e = itemSlot.findElementByName("item" + i);
                    ItemStack item = items[i];
                    
                    if(item != null)
                    {
                        if(e == null)
                        {
                            itemBuilder = new ControlBuilder("item");
                            itemBuilder.id(itemId);
                            itemBuilder.visibleToMouse(true);
                            itemBuilder.parameter("count", Integer.toString(item.getCount()));                    
                            e = itemBuilder.build(nifty, screen, itemSlot);                            
                        }
                        locations.put(itemId, new ItemStackLocation(slotId, itemId, i, item));
                        
                        final int spriteIndex = item.getBlock().getSpriteIndex();                
                        ImageBuilder imageBuilder = new ImageBuilder()
                        {{ 
                            filename(TextureProperties.ACTIVE_BLOCK_TEXTURE);
                            imageMode("sprite:32,32," + spriteIndex);
                            width("50");
                            height("50");

                        }};
                        imageBuilder.build(nifty, screen, e);      
                        
                        TextBuilder textBuilder = new TextBuilder();
                        textBuilder.style("nifty-label");
                        textBuilder.textVAlignBottom();                        
                        textBuilder.textHAlignRight();
                        textBuilder.font("Interface/Fonts/ArialBlack.fnt");
                        textBuilder.text(Integer.toString(item.getCount()));
                        textBuilder.alignCenter();
                        textBuilder.build(nifty, screen, e);
                        
                    } 
                    else
                    {
                        Element blockItem = itemSlot.findElementByName("item" + i);
                        if(blockItem != null)
                        {
                            blockItem.setVisible(false);
                            blockItem.markForRemoval();
                        }
                        
                    }
                }
            }            
            
        }
    }

    public void onEndScreen()
    {
    }

    private class InventoryActionListener implements ActionListener
    {
        public void onAction(String name, boolean isPressed, float tpf)
        {
            if ("close".equals(name))
            {
                nifty.fromXml("Interface/hud.xml", "hud", stateManager.getState(RunningAppState.class).getHUDControl());
                inputManager.setCursorVisible(false);
                playerControl.setEnabled(true);
                flyCam.setEnabled(true);
                
            }
        }
    }
    
    private class ItemStackLocation
    {
        public int inventoryLocation;
        public String slotId;
        public String itemId;
        private ItemStack itemStack;
        
        public ItemStackLocation(String slotId, String itemId, int inventoryLocation, ItemStack itemStack)
        {
            this.inventoryLocation = inventoryLocation;
            this.slotId = slotId;
            this.itemId = itemId;
            this.itemStack = itemStack;
        }
    }
}

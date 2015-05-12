/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chappelle.jcraft;

import Properties.GameProperties;
import com.jme3.font.BitmapFont;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Perry
 */
public class HUD 
{
    private List<HUDText> textList;
    private int y_offset = GameProperties.HUD_Y_OFFSET;
    private BlockGame app;
    private BitmapFont font;
    
    public HUD(BlockGame app, BitmapFont font)
    {
        this.app = app;
        this.font = font;
        textList = new LinkedList<HUDText>();
    }
    
    public void addText(String text)
    {
        HUDText txt = new HUDText(app, font, GameProperties.HUD_X_OFFSET, y_offset);
        txt.setText(text);
        y_offset-=GameProperties.HUD_Y_INCREMENT;
        textList.add(txt);
    }
    
    public List<HUDText> getText()
    {
        return textList;
    }
    
    public void clear()
    {
        app.getGuiNode().detachAllChildren();
        y_offset = GameProperties.HUD_Y_OFFSET;
        textList = new LinkedList<HUDText>();
    }
}

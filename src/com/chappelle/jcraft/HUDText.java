/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chappelle.jcraft;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;

/**
 *
 * @author Perry
 */
public class HUDText 
{
    private BitmapText text;

    public HUDText(BlockGame app, BitmapFont guiFont, int x, int y)
    {
        text = new BitmapText(guiFont, false);
        text.setSize(guiFont.getCharSet().getRenderedSize());
        text.setLocalTranslation(x, y, 0);
        app.getGuiNode().attachChild(text);
    }

    public void setText(String text)
    {
        this.text.setText(text);
    }
}   

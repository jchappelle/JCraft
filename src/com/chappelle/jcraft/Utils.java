/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chappelle.jcraft;

import Properties.GameProperties;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;

/**
 *
 * @author Perry
 */
public class Utils
{
    // TODO as a performance boost it might be worth it to cache the sounds at some point.
    public static void PlaySound(String soundToPlay)
    {
        AudioNode sound = new AudioNode(Game.getInstance().getAssetManager(), soundToPlay);
        sound.setReverbEnabled(false);
        sound.play();
    }

    private static Vector3f worldCenter;

    public static Vector3f translateBlockToWorldLocation(Vector3f location)
    {
        return location.mult(GameProperties.CUBE_SIZE);
    }

}

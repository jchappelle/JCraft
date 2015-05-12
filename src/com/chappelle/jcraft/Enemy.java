package com.chappelle.jcraft;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;

public class Enemy implements Savable
{
    /*Damage applied to player when he is hit*/
    private int damageToPlayer = 10;
    
    /*Current health*/
    private int health = 0;
    
    /*Max damage until I die*/
    private int maxDamage = 10;
    
    /*Damage incurred when player hit me*/
    private int damageFromPlayer = 1;
    
    //Public Enemy #1...ha
    public Enemy()
    {
    }
    
    public void hit()
    {
        health -= damageFromPlayer;
    }
    
    public int getHealth()
    {
        return health;
    }

    public int getDamageToPlayer()
    {
        return damageToPlayer;
    }

    public void setDamageToPlayer(int damageToPlayer)
    {
        this.damageToPlayer = damageToPlayer;
    }

    public int getMaxDamage()
    {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage)
    {
        this.maxDamage = maxDamage;
    }

    public int getDamageFromPlayer()
    {
        return damageFromPlayer;
    }

    public void setDamageFromPlayer(int damageFromPlayer)
    {
        this.damageFromPlayer = damageFromPlayer;
    }

    public void write(JmeExporter ex) throws IOException
    {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(damageToPlayer, "damageToPlayer", 10);
        capsule.write(health, "health", 0);                
        capsule.write(maxDamage, "maxDamage", 10);
        capsule.write(damageFromPlayer, "damageFromPlayer", 1);        
        
    }

    public void read(JmeImporter im) throws IOException
    {
        InputCapsule capsule = im.getCapsule(this);
        damageToPlayer = capsule.readInt("damageToPlayer", 10);
        health = capsule.readInt("health", 0);
        maxDamage = capsule.readInt("maxDamage", 10);
        damageFromPlayer = capsule.readInt("damageFromPlayer", 1);
    }
   
}

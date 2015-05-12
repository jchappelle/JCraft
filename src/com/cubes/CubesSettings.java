/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import Properties.TextureProperties;
import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;

/**
 *
 * @author Carl
 */
public class CubesSettings{

    public CubesSettings(Application application){
        assetManager = application.getAssetManager();

        this.blockMaterial = (new BlockChunk_Material(assetManager, TextureProperties.ACTIVE_BLOCK_TEXTURE));
    }

    private AssetManager assetManager;
    private Material blockMaterial;

    public AssetManager getAssetManager(){
        return assetManager;
    }

    public Material getBlockMaterial(){
        return blockMaterial;
    }

}

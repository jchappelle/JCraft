/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes.shapes;

import com.cubes.Block;
/**
 *
 * @author Carl
 */
public class BlockShape_Cube extends BlockShape_Cuboid{

    public BlockShape_Cube(){
        super(new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f});
    }
    
    public BlockShape_Cube(Block defaultBlock){
        super(defaultBlock, new float[]{0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f});
    }
    
}

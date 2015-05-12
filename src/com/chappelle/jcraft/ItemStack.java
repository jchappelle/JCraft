package com.chappelle.jcraft;

import com.chappelle.jcraft.blocks.JBlock;

public class ItemStack 
{
    private int id;
    private JBlock block;
    private int count = 1;
    
    public ItemStack(int id, JBlock block)
    {
        if(block == null)
        {
            throw new IllegalArgumentException("Block cannot be null!");
        }
        this.id = id;
        this.block = block;
    }

    public int getRemaining()
    {
        return block.getStackSize() - count;
    }
    
    public boolean isFull()
    {
        return getRemaining() <= 0;
    }
    
    public boolean isNotFull()
    {
        return !isFull();
    }
    
    public boolean canAccept(JBlock block)
    {
        return isNotFull() && this.block == block;
    }
    
    public void add(int amount)
    {
        if(count + amount > block.getStackSize())
        {
            this.count = block.getStackSize();
        }
        else
        {
            this.count += amount;   
        }
    }
    
    public void subtract(int amount)
    {
        if(count - amount <= 0)
        {
            count = 0;
        }
        else
        {
            this.count -= amount;   
        }
    }
    
    public int getId()
    {
        return id;
    }

    public JBlock getBlock()
    {
        return block;
    }

    public int getCount()
    {
        return count;
    }
    
    
}

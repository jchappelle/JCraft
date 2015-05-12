package com.cubes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BlockState implements Serializable
{
    private Map<Short, Object> blockState;

    public void put(Short key, Object value)
    {
        if(blockState == null)
        {
            blockState = new HashMap<>();
        }
        blockState.put(key, value);
    }

    public Object get(Short key)
    {
        if(blockState == null)
        {
            return null;
        }
        else
        {
            return blockState.get(key);
        }
    }
}

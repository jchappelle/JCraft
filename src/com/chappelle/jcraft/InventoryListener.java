package com.chappelle.jcraft;

public interface InventoryListener 
{
    void onInventoryChanged();
    
    void onSelectionChanged(int oldIndex, int newIndex);
}

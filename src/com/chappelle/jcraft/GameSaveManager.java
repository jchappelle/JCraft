package com.chappelle.jcraft;

import Properties.GameProperties;
import com.cubes.BlockTerrainControl;
import com.cubes.network.CubesSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GameSaveManager
{
    private BlockTerrainControl blockTerrain;

    public GameSaveManager(BlockTerrainControl blockTerrain)
    {
        this.blockTerrain = blockTerrain;
    }

    public boolean saveFileExists()
    {
        return new File(GameProperties.WORLD_SAVE_LOCATION).exists();
    }

    public void loadWorld()
    {
        FileInputStream fileInputStream;
        File file = new File(GameProperties.WORLD_SAVE_LOCATION);

        byte[] terrainData = new byte[(int) file.length()];
        try
        {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(terrainData);
            fileInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        CubesSerializer.readFromBytes(blockTerrain, terrainData);
        //showArrayData(terrainData);
    }

    public void saveGame()
    {
        byte[] terrainData = CubesSerializer.writeToBytes(blockTerrain);
        try
        {
            FileOutputStream fos = new FileOutputStream(GameProperties.WORLD_SAVE_LOCATION);
            fos.write(terrainData);
            fos.flush();
            fos.close();
            System.out.println("Game saved to " + GameProperties.WORLD_SAVE_LOCATION);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //showArrayData(terrainData);

    }

//    private void showArrayData(byte[] array)
//    {
//        for(int i=0; i<array.length; i++)
//        {
//            System.out.print(array[i] + ".");
//        }
//        System.out.println();
//    }
}

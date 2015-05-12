/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chappelle.jcraft;

import Properties.GameProperties;
import com.cubes.BlockChunkControl;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Perry
 */
public class ObjectPersister
{
    public static void save(Object objectToSave, String name)
    {
        String fileName = GameProperties.WORLD_SAVE_LOCATION + name;
        ObjectOutputStream out = null;
        try
        {
            out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(objectToSave);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if(out != null)
                {
                    out.close();
                }

            } catch (IOException ex)
            {
                Logger.getLogger(BlockChunkControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Object load(String name) throws UnloadableObjectException
    {
        String fileName = GameProperties.WORLD_SAVE_LOCATION + name;
        ObjectInputStream in = null;
        Object result;
        try
        {
            in = new ObjectInputStream(new FileInputStream(fileName));
            result = in.readObject();
        }
        catch (Exception e)
        {
            UnloadableObjectException e1 = new UnloadableObjectException(fileName);
            e1.initCause(e);
            throw e1;
        }
        finally
        {
            try
            {
                if(in != null)
                {
                     in.close();
                }
            }
            catch (IOException ex)
            {
                // eat it
            }
        }
        return result;
    }
}

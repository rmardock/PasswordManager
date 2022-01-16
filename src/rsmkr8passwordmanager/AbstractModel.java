/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsmkr8passwordmanager;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Ryan Mardock
 */
/*
    References:
        1) https://docs.oracle.com/javase/8/docs/api/java/io/File.html
*/
public abstract class AbstractModel {
    //Method to check if file exists
    public static boolean fileExists(String path){
        boolean exists;
        File file = new File(path);
        if(file.length() == 0){
            exists = false;
        }
        else{
            exists = true;
        }
        return exists;
    }
    //Method to create file 
    public static void createFile(String path){
        try{
            File file = new File(path);
            file.createNewFile();
        }
        catch(IOException a){
            System.out.println("An error has occured while creating this file.\nIOException: " + a.getMessage());
        }
    }
    //Method to delete file
    public static void deleteFile(String path){    
        File file = new File(path);
        file.delete();
        System.out.println("No master password file detected!\nDeleting password file...");
    }
}
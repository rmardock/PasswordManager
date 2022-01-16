/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsmkr8passwordmanager;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Ryan Mardock
 */
/*
    References:
        1) https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html
        2) https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html
        3) https://docs.oracle.com/javase/8/docs/api/java/io/FileOutputStream.html
        4) https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
        5) https://docs.oracle.com/javase/8/docs/api/java/io/FileReader.html
*/
public class MasterPasswordAuthentication extends AbstractModel implements ModelInterface {
    private final String path = "masterPass.txt";
    private static String encryptedPassword = "";
    private String newEncryptedPassword;
    private BufferedReader readFile;
    
    
    //Constructor
    public MasterPasswordAuthentication(){
        //Read from file upon instantiation
        readFromFile();
    }
    //Method to write to a file
    //Overrides abstract method from ModelInterface
    //FileOutputStream methods from https://docs.oracle.com/javase/8/docs/api/java/io/FileOutputStream.html
    @Override
    public void writeToFile(){
        try{
            FileOutputStream stream = new FileOutputStream(path, false);
            stream.write(newEncryptedPassword.getBytes());
            stream.close();
        }
    //End of content from https://docs.oracle.com/javase/8/docs/api/java/io/FileOutputStream.html
        catch(IOException a){
            System.out.println("IOException!\nError writing to " + path + "!");
        }
    }
    //Method to read encrypted password from file
    //Overrides abstract method from ModelInterface
    //BufferedReader methods from https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
    @Override
    public void readFromFile(){
        try{
            File masterPasswordFile = new File(path);
            readFile = new BufferedReader(new FileReader(masterPasswordFile));
            //Assign encrypted password from file into global variable
            encryptedPassword = readFile.readLine();
            readFile.close();
        }
    //End of content from https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
        catch(FileNotFoundException a){
            System.out.println("Error! File not found.");
        }
        catch(IOException b){
            System.out.println("Exception: " + b.getMessage());
        }
    }
    //Method to encrypt master password using SHA-1 
    //Encryption using MessageDigest from https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html
    public String encrypt(String passwordText){
        String sha1 = "";
        try{
            MessageDigest d = MessageDigest.getInstance("SHA-1");
            d.update(passwordText.getBytes("utf8"));
            //Complete hash computation and assign to variable as hexadecimal value
            sha1 = new BigInteger(1, d.digest()).toString(16);
        }
    //End of content from https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html
        catch (NoSuchAlgorithmException a){
            System.out.println("NoSuchAlgorithmException: " + a.getMessage());
        }
        catch(UnsupportedEncodingException b){
            System.out.println("UnsupportedEncodingException: " + b.getMessage());
        }
        return sha1;
    }
    //Method to check if two password strings match
    public boolean passwordMatchCheck(String password, String confPassword){
        if(password.equals(confPassword)){
            return true;
        }
        else{
            return false;
        }
    }
    //Getter for encrypted password
    public String getMasterPass(){
        return encryptedPassword;
    }
    //Setter for new encrypted password
    public void setNewEncryptedPassword(String newEncryptedPassword){
        this.newEncryptedPassword = encrypt(newEncryptedPassword);
    }
}
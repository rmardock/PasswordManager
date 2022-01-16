/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsmkr8passwordmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Ryan Mardock
 */
/*
    References:
        1) https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html
        2) https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
        3) https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html
        4) https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
*/
public class PasswordVault extends AbstractModel implements ModelInterface {
    private final String path = "passwordFile.txt";
    private ArrayList<String> passwordList;
    private Scanner scanner;
    
    //Method to encrypt or decrypt passwords
    //StringBuilder methods from https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html
    public static String encryptDecryptPassword(String password, boolean encryption){
        StringBuilder sb = new StringBuilder();
        String result;
        for(char c: password.toCharArray()){
            if(encryption == true){
                c += KEY;
                sb.append(c);
            }
            else{
                c -= KEY;
                sb.append(c);
            }
        }
        result = sb.toString();
        return result;
    //End of content from https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html
    }
    //Method to write passwords to file
    //Overrides abstract method from ModelInterface
    //FileWriter methods from https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html
    @Override
    public void writeToFile(){
        try{
            FileWriter w = new FileWriter(path);
            for(String password : passwordList){
                w.write(password + System.lineSeparator());
            }
            w.close();
        }
    //End of content from https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html
        catch(IOException a){
            System.out.println("Error writing to " + path + "!");
        }
    }
    //Method to read passwords from file into ArrayList
    //Overrides abstract method from ModelInterface
    //ArrayList methods from https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
    //Scanner methods from https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
    @Override
    public void readFromFile(){
        try{
            scanner = new Scanner(new File(path));
            passwordList = new ArrayList();
            while(scanner.hasNextLine()){
                //Add encrypted passwords to ArrayList
                passwordList.add(scanner.nextLine());
            }
            scanner.close();
        }
    //End of content from https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
    //End of content from https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
        catch(FileNotFoundException a){
            System.out.println("Exception: FileNotFoundException.\n" + a.getMessage());
        }
    }
    //Getter for password list 
    public ArrayList getPasswordList(){
        return passwordList;
    }
    //Method to add password to ArrayList
    public void addPassword(String password){
        passwordList.add(password);
        writeToFile();
    }
    //Method to remove password from ArrayList
    public void removePassword(int index){
        passwordList.remove(index);
        writeToFile();
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsmkr8passwordmanager;

/**
 *
 * @author Ryan Mardock
 */

public interface ModelInterface {
    //Key constant for simple encryption/decryption algorithm
    public static final int KEY = 5;
    //Method to write to a file
    abstract void writeToFile();
    //Method to read from a file
    abstract void readFromFile();
}
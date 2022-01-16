/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsmkr8passwordmanager;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author Ryan Mardock
 */
/*
    References:
        1) https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
        2) https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Parent.html
*/
//SceneSwitch abstract class was modeled after the Switcher example shown in class
public abstract class SceneSwitch {
    public static Scene scene;
    public static final HashMap<String, SceneSwitch> controllers = new HashMap<>();
    private Parent root;
    
    //Method to get FXML, controller, and add to HashMap
    public static SceneSwitch add(String sceneName){
        SceneSwitch controller = controllers.get(sceneName);
        if(controller == null){
            try{
                FXMLLoader loader = new FXMLLoader(SceneSwitch.class.getResource(sceneName + ".fxml"));
                Parent root = loader.load();
                controller = loader.getController();
                controller.setRoot(root);
                controllers.put(sceneName, controller);
            }
            catch(IOException a){
                System.out.println("Error loading " + controller + "!\n");
                System.out.println("IOException: " + a.getMessage());
                controller = null;
            }
        }
        return controller;
    }
    //Method to change to another view
    public static void changeView(String sceneName){
        SceneSwitch controller = controllers.get(sceneName);
        if(controller == null){
            controller = add(sceneName);
            System.out.println("Controller " + controller + " added.");
        }
        if(controller != null){
            if(scene != null){
                scene.setRoot(controller.getRoot());
            }
        }
    }
    //Getter for root
    public Parent getRoot(){
        return root;
    }
    //Setter for root
    public void setRoot(Parent root){
        this.root = root;
    }
}
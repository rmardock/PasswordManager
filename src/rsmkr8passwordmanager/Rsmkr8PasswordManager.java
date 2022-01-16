/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsmkr8passwordmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ryan Mardock
 */
/*
    References:
        1) https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html
*/
public class Rsmkr8PasswordManager extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        final String path = "masterPass.txt";
        final String pwdPath = "passwordFile.txt";
        String startingView = "ExistingPasswordView";
        //Check if master password exists, assign correct starting view
        //If master password does not exist, create new file and assign correct starting view
        if(!AbstractModel.fileExists(path)){
            //If password file exists when no master password file does, delete password file
            //This is to prevent the deletion of the master password file to gain access to passwords
            if(AbstractModel.fileExists(pwdPath)){
                AbstractModel.deleteFile(pwdPath);
            }
            AbstractModel.createFile(path);
            startingView = "NewMasterPasswordView";
        } 
        Parent root = FXMLLoader.load(getClass().getResource(startingView + ".fxml"));
        Scene scene = new Scene(root);
        SceneSwitch.scene = scene;
        SceneSwitch.changeView(startingView);
        //stage methods from https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setHeight(748);
        stage.setWidth(1280);
        stage.show();
        //End of content from https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
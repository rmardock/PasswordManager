/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsmkr8passwordmanager;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Ryan Mardock
 */
/*
    References:
        1) https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
*/
public class ChangePasswordViewController extends SceneSwitch implements Initializable {
    private MasterPasswordAuthentication auth;
    private Alert alert;
    private final String view = "PasswordVaultView";
    
    //FXML elements
    @FXML
    private Text errorText;
    @FXML
    private PasswordField newMasterPass;
    @FXML
    private PasswordField confirmNewMasterPass;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    //Handler for submit button
    @FXML
    private void submitMasterPassChange(ActionEvent event) {
        auth = new MasterPasswordAuthentication();
        //If passwords match
        if(auth.passwordMatchCheck(newMasterPass.getText(), confirmNewMasterPass.getText())){
            auth.setNewEncryptedPassword(newMasterPass.getText());
            auth.writeToFile();
            errorText.setText("");
            newMasterPass.setText("");
            confirmNewMasterPass.setText("");
            //Reload global variable in MasterPasswordAuthentication Model
            auth.readFromFile();
            //Show confirmation alert
            changeAlert();
            //Change to Password Vault view
            SceneSwitch.changeView(view);
        }
        //If passwords do not match
        else{
            errorText.setText("Passwords do not match! Please try again.");
            newMasterPass.setText("");
            confirmNewMasterPass.setText("");
        }
    }
    //Method to spawn and show success alert
    //Alert methods from https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
    private void changeAlert(){
        alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Success!");
        alert.setTitle("");
        alert.setContentText("Your master password has been successfully changed!");
        alert.showAndWait();
    }
    //End of content from https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
}
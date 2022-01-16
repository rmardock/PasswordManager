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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Ryan Mardock
 */
public class NewMasterPasswordViewController extends SceneSwitch implements Initializable {
    private MasterPasswordAuthentication auth;
    private final String view = "PasswordVaultView";
    
    //FXML elements
    @FXML
    private TextField createPassword;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Text errorMessage;
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    //Handler for submit button
    @FXML
    private void submitNewPassword(ActionEvent event) {
        auth = new MasterPasswordAuthentication();
        if(auth.passwordMatchCheck(createPassword.getText(), confirmPassword.getText())){
            //Create Master Password file
            auth.setNewEncryptedPassword(createPassword.getText());
            auth.writeToFile();
            //Clear error message and input fields
            errorMessage.setText("");
            createPassword.setText("");
            confirmPassword.setText("");
            //Switch to PasswordVaultView
            SceneSwitch.changeView(view);
        }
        //If passwords do not match
        else{
            errorMessage.setText("Passwords do not match! Please try again.");
            createPassword.setText("");
            confirmPassword.setText("");
        }
    }
}
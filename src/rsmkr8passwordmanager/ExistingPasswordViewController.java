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
 *
 * @author Ryan Mardock
 */
public class ExistingPasswordViewController extends SceneSwitch implements Initializable {
    private final String view = "PasswordVaultView";
    private MasterPasswordAuthentication auth = new MasterPasswordAuthentication();
    
    //FXML elements
    @FXML
    private TextField passwordInput;
    @FXML
    private Text errorText;  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    //Handler for password submit button
    @FXML
    private void passwordSubmit(ActionEvent event) {
        //Encrypt user password input
        String encryptedPasswordInput = auth.encrypt(passwordInput.getText());
        String encryptedPassword = auth.getMasterPass();
        //If passwords match
        if(encryptedPasswordInput.equals(encryptedPassword)){
            passwordInput.setText("");
            errorText.setText("");
            System.out.println("Password Authentication Successful!");
            SceneSwitch.changeView(view);
        }
        else{
            errorText.setText("Incorrect password! Try again.");
            passwordInput.setText("");
        }
    }
    
}
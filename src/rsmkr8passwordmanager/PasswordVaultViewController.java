/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsmkr8passwordmanager;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ryan Mardock
 */
/*
    References:
        1) https://docs.oracle.com/javase/8/javafx/api/javafx/animation/TranslateTransition.html
        2) https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
        3) https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TextInputDialog.html
        4) https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html
        5) https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.AlertType.html
        6) https://docs.oracle.com/javase/8/javafx/api/javafx/beans/value/ChangeListener.html
*/
public class PasswordVaultViewController extends SceneSwitch implements Initializable {
    private TranslateTransition showMenu;
    private TranslateTransition hideMenu;
    private TranslateTransition showMenuButton;
    private TranslateTransition hideMenuButton;
    private TranslateTransition listViewExpand;
    private TranslateTransition listViewContract;
    private TranslateTransition appTitleLeft;
    private TranslateTransition appTitleRight;
    private final int transitionDuration = 300;
    private boolean menuVisible = false;
    private TextInputDialog dialog;
    private Alert alert;
    private final String errorTitle = "Error!";
    private final String errorText = "The password you entered was incorrect!";
    private final String aboutTitle = "About";
    private final String aboutText = "Rsmkr8 Password Manager\n\nVersion 1.0\n\n" + 
            "This is an application to securely store multiple passwords. " + 
            "All passwords are encryped and protected by a master password. " + 
            "The master password is encrypted using SHA-1 encryption.\n\n" + 
            "Created by Ryan Mardock";
    private final String newEntryTitle = "New Password Entry";
    private final String newEntryContent = "Enter your new password: ";
    private final String changePassTitle = "Change Password";
    private final String changePassContent = "Enter your master password: ";
    private final String path = "passwordFile.txt";
    private final String changePassView = "ChangePasswordView";
    private final String view = "ExistingPasswordView";
    private Button okButton;
    private ArrayList passwordList;
    private MasterPasswordAuthentication auth;
    private PasswordVault vault;
    
    //FXML elements
    @FXML
    private ListView<String> passwordListView;
    @FXML
    private AnchorPane menuAnchorPane;
    @FXML
    private Button menuButton;
    @FXML
    private Label applicationTitle;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //If password file does not exist, create it
        if(!AbstractModel.fileExists(path)){
            AbstractModel.createFile(path);
        }
        //If password list is not empty, load list into ListView
        if(AbstractModel.fileExists(path)){
            loadPasswordList();
        }
        //Call method to setup menu transitions
        setupTransitions();
    }    
    //Handler for add password button press
    //ListView methods from https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html
    @FXML
    private void addPassword(ActionEvent event) {
        String newPassword = runTextInputDialog(newEntryTitle, newEntryContent);
        //Encrypt password
        String encryptedNewPass = PasswordVault.encryptDecryptPassword(newPassword, true);
        //Load password list. This will instantiate ArrayList in the model
        vault = new PasswordVault();
        vault.readFromFile();
        //Add password to Model ArrayList and write to file
        vault.addPassword(encryptedNewPass);
        //Add password to ListView
        passwordListView.getItems().add(newPassword);
    }
    //Handler for remove password button
    @FXML
    private void removePassword(ActionEvent event) {   
        if(passwordListView.getSelectionModel() == null){
            System.out.println("No items are selected.");
        }
        else{
            int index = passwordListView.getSelectionModel().getSelectedIndex();
            String password = passwordListView.getSelectionModel().getSelectedItem();
            //Remove from listview and file/arraylist
            vault.removePassword(index);
            vault.readFromFile();
            passwordListView.getItems().remove(index);
            //Clear ListView cell selection
            if(index == 0){
                passwordListView.getSelectionModel().clearSelection(0);
            }
            else{
                passwordListView.getSelectionModel().clearSelection(index - 1);
            }
        }
    }
    //End of content from https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html
    //Handler for vault lock button
    @FXML
    private void lockVault(ActionEvent event) {
        //Switch to ExistingPasswordView
        SceneSwitch.changeView(view);
    }
    //Handler for changeMasterPassword button press
    @FXML
    private void changeMasterPassword(ActionEvent event) {
        auth = new MasterPasswordAuthentication();
        String masterPassword = runTextInputDialog(changePassTitle, changePassContent);
        String encryptedPasswordInput = auth.encrypt(masterPassword);
        //If passwords match, switch to change password view
        if(auth.getMasterPass().equals(encryptedPasswordInput)){
            SceneSwitch.changeView(changePassView);
        }
        //If passwords do not match, show error alert 
        else{
            alert(errorTitle, errorText, AlertType.ERROR);
        }
    }
    //Handler for about page button
    @FXML
    private void aboutPage(ActionEvent event) {
        alert(aboutTitle, aboutText, AlertType.INFORMATION);
    }
    //Handler for menu button
    @FXML
    private void menuButtonAction(ActionEvent event) {
        //If menu is visible
        if(menuVisible){
            showMenu.stop();
            showMenuButton.stop();
            appTitleRight.stop();
            listViewContract.stop();
            hideMenu.play();
            hideMenuButton.play();
            appTitleLeft.play();
            passwordListView.setPrefWidth(1280);
            listViewExpand.play();
        }
        //If menu is not visible
        else{
            hideMenu.stop();
            hideMenuButton.stop();
            appTitleLeft.stop();
            listViewExpand.stop();
            showMenu.play();
            showMenuButton.play();
            appTitleRight.play();
            listViewContract.play();
        }
    }
    //Method to run TextInputDialog
    //TextInputDialog methods from https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TextInputDialog.html
    private String runTextInputDialog(String title, String content){
        String passString;
        //Setup TextInputDialog
        dialog = new TextInputDialog();
        dialog.setHeaderText("");
        dialog.setTitle(title);
        dialog.setContentText(content);
        //Assign TextInputDialog ok button to variable and diable button
        okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        //Call method to check input
        restrictInput();
        Optional<String> passwordInput = dialog.showAndWait();
        passString = passwordInput.get();
        return passString;
    }
    //End of content from https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TextInputDialog.html
    //Method to run error Alert
    //Alert methods from https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
    private void alert(String title, String content, AlertType alertType){
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(content);
        alert.showAndWait();
    }
    //End of content from https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
    //Method to get password ArrayList from model and populate ListView
    private void loadPasswordList(){
        vault = new PasswordVault();
        vault.readFromFile();
        passwordList = vault.getPasswordList();
        for(int i = 0; i < passwordList.size(); i++){
            passwordListView.getItems().add(PasswordVault.encryptDecryptPassword(passwordList.get(i).toString(), false));
        }
    }
    //Method to set up transitions
    //TranslateTransitions methods from https://docs.oracle.com/javase/8/javafx/api/javafx/animation/TranslateTransition.html
    private void setupTransitions(){
        //Menu transition for showing menu
        showMenu = new TranslateTransition(Duration.millis(transitionDuration), menuAnchorPane);
        showMenu.setByX(436.0);
        showMenu.setOnFinished(event -> menuVisible = true);
        //Menu transition for hiding menu
        hideMenu = new TranslateTransition(Duration.millis(transitionDuration), menuAnchorPane);
        hideMenu.setByX(-436.0);
        hideMenu.setOnFinished(event -> menuVisible = false);
        //Button transition for showing menu
        showMenuButton = new TranslateTransition(Duration.millis(transitionDuration), menuButton);
        showMenuButton.setByX(357);
        //Button transition for hiding menu
        hideMenuButton = new TranslateTransition(Duration.millis(transitionDuration), menuButton);
        hideMenuButton.setByX(-357);
        //ListView transition for hiding menu
        listViewExpand = new TranslateTransition(Duration.millis(transitionDuration), passwordListView);
        listViewExpand.setByX(-436);
        //ListView transition for showing menu
        listViewContract = new TranslateTransition(Duration.millis(transitionDuration), passwordListView);
        listViewContract.setByX(436);
        //Set width of ListView after transition is finished
        listViewContract.setOnFinished(event -> passwordListView.setPrefWidth(844));
        //Label transition for hiding menu
        appTitleLeft = new TranslateTransition(Duration.millis(transitionDuration), applicationTitle);
        appTitleLeft.setByX(-218);
        //Label transition for showing menu
        appTitleRight = new TranslateTransition(Duration.millis(transitionDuration), applicationTitle);
        appTitleRight.setByX(218);
    //End of content from https://docs.oracle.com/javase/8/javafx/api/javafx/animation/TranslateTransition.html
    }
    //Method to check if TextInputDialog input field is empty and disable/enable ok button accordingly
    private void restrictInput(){
        //ChangeListener and changed() method from https://docs.oracle.com/javase/8/javafx/api/javafx/beans/value/ChangeListener.html
        //Invokes changed() when the observable value (the user input) is changed
        dialog.getEditor().textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String > observable, String oldValue, String newValue){
                //If TextInputDialog input field is empty, disable ok button
                if(newValue.matches("")){
                    okButton.setDisable(true);
                }
                //If TextInputDialog input field is not empty, enable ok button
                else{
                    okButton.setDisable(false);
                }
            }
        });
    }
}
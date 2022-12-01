package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class PasswordForAdmin {
    private static boolean IS_ADMIN = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button okBtn;

    @FXML
    private PasswordField passField;

    @FXML
    void clickOkBtn(ActionEvent event) {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        IS_ADMIN = passField.getText().equals("admin");
        stage.close();
    }

    @FXML
    void initialize() {
    }

    public static boolean IsAdmin() {
        return IS_ADMIN;
    }
}

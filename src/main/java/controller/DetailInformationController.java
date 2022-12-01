package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.MainWindowController.GetCurrentDepartment;

public class DetailInformationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField firstNameTF;

    @FXML
    private TextField lastNameTF;

    @FXML
    private TextArea nameDepartmentTF;

    @FXML
    private Button okBtn;

    @FXML
    private TextField patronymicTF;

    @FXML
    private TextField shortNameDepartmentTF;

    @FXML
    void clickOkBtn(ActionEvent event) {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
       nameDepartmentTF.setText(GetCurrentDepartment().getNameProperty());
       shortNameDepartmentTF.setText(GetCurrentDepartment().getShortName());
       lastNameTF.setText(GetCurrentDepartment().getDirector().getLastName());
       firstNameTF.setText(GetCurrentDepartment().getDirector().getFirstName());
       patronymicTF.setText(GetCurrentDepartment().getDirector().getPatronymic());

    }

}

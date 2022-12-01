package controller;

import animation.Shake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.IdDepartment;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.MainWindowController.GetIdDepartments;

public class SettingsIdDepartmentController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    // ------------------------- кнопки ------------------------------------
    @FXML
    private Button addBT;
    @FXML
    private Button saveBT;
    @FXML
    private Button removeBT;
    // ---------------------------------------------------------------------

    // ---------------------- текстовые поля--------------------------------
    @FXML
    private TextField nameDepartmentTF;
    @FXML
    private TextField idDepartmentTF;
    // ---------------------------------------------------------------------

    // ------------------------- таблица -----------------------------------
    @FXML
    private TableView<IdDepartment> idDepTBL;
    @FXML
    private TableColumn<IdDepartment, String> nameDepTBLCol;
    @FXML
    private TableColumn<IdDepartment, String> idDepTBLCol;
    // ---------------------------------------------------------------------

    @FXML
    void initialize() {
        idDepTBL.setItems(GetIdDepartments());
        nameDepTBLCol.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        idDepTBLCol.setCellValueFactory(cellData -> cellData.getValue().getIdDepartmentProperty());

        getIdDepartmentValue();
    }


    @FXML
    void addDep(ActionEvent event) {
        if (isFullData()) {
            GetIdDepartments().add(new IdDepartment(nameDepartmentTF.getText(), idDepartmentTF.getText()));
        } else {
            shakeField();
        }
    }

    @FXML
    void removeDep(ActionEvent event) {
        TableView.TableViewSelectionModel<IdDepartment> selectionModel = idDepTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1)) {
            idDepTBL.getItems().remove(selectionModel.getSelectedItem());
        }
    }

    @FXML
    void saveDep(ActionEvent event) {
        TableView.TableViewSelectionModel<IdDepartment> selectionModel = idDepTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1) && !isContainsDepartment()) {
            int index = selectionModel.getSelectedIndex();
            idDepTBL.getItems().get(index).setNameProperty(nameDepartmentTF.getText());
            idDepTBL.getItems().get(index).setIdDepartmentProperty(idDepartmentTF.getText());
        }
    }

    private void getIdDepartmentValue() {
        TableView.TableViewSelectionModel<IdDepartment> selectionModel = idDepTBL.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if (newVal != null) {
                nameDepartmentTF.setText(newVal.getNameDepartment());
                idDepartmentTF.setText(newVal.getIdDepartment());
            }
        });
    }

    private boolean isContainsDepartment() {
        for (IdDepartment i : GetIdDepartments()) {
            if (nameDepartmentTF.getText().equals(i.getNameDepartment())) return true;
        }
        return false;
    }

    private void shakeField() {
        if (nameDepartmentTF.getText().isEmpty()) {
            Shake shake = new Shake(nameDepartmentTF);
            shake.playAnimation();
        }
        if (idDepartmentTF.getText().isEmpty()) {
            Shake shake = new Shake(idDepartmentTF);
            shake.playAnimation();
        }
    }

    private boolean isFullData() {
        return (!nameDepartmentTF.getText().isEmpty() && !idDepartmentTF.getText().isEmpty());
    }
}

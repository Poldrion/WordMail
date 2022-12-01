package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Department;
import model.Director;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.MainWindowController.GetGroupDepartmentObsList;

public class SettingsGroupDepartmentController implements Initializable {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    // ------------------------------------ Текстовые поля ------------------------------------
    @FXML
    private TextField depPathTF;
    @FXML
    private Label groupID;
    // ----------------------------------------------------------------------------------------

    // --------------------------------------- Таблица ----------------------------------------
    @FXML
    private TableView<Department> groupDepTBL;
    @FXML
    private TableColumn<Department, String> groupDepTblCol;
    // ----------------------------------------------------------------------------------------

    // ---------------------------------------- Кнопки ----------------------------------------
    @FXML
    private Button addBT;
    @FXML
    private Button delBT;
    @FXML
    private Button saveBT;
    // ----------------------------------------------------------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupDepTBL.setItems(GetGroupDepartmentObsList());
        groupDepTblCol.setCellValueFactory(cellData -> cellData.getValue().shortNameProperty());

        getGroupDepValue();
    }

    private void getGroupDepValue() {
        TableView.TableViewSelectionModel<Department> selectionModel = groupDepTBL.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if (newVal != null) {
                depPathTF.setText(newVal.getShortName());
                groupID.setText(String.valueOf(newVal.getId()));
            }
        });
    }

    @FXML
    void addRow(ActionEvent event) {
        if (depPathTF.getText() != null) {
            Department tempDep = new Department(getMaxId() + 1, depPathTF.getText(), depPathTF.getText(),
                    new Director("", "", "", true, ""));
            if (!isContainsDepartment())
                GetGroupDepartmentObsList().add(tempDep);
        }
    }

    @FXML
    void delRow(ActionEvent event) {
        TableView.TableViewSelectionModel<Department> selectionModel = groupDepTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1)) {
            groupDepTBL.getItems().remove(selectionModel.getSelectedItem());
        }
    }

    @FXML
    void saveRow(ActionEvent event) {
        TableView.TableViewSelectionModel<Department> selectionModel = groupDepTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1) && !isContainsDepartment()) {
            int index = selectionModel.getSelectedIndex();
            groupDepTBL.getItems().get(index).setNameProperty(depPathTF.getText());
            groupDepTBL.getItems().get(index).setShortName(depPathTF.getText());
        }
    }

    private boolean isContainsDepartment() {
        for (Department d : GetGroupDepartmentObsList()) {
            if (depPathTF.getText().equals(d.getShortName())) return true;
        }
        return false;
    }

    private int getMaxId() {
        int tempMaxId = 0;
        for (Department d : GetGroupDepartmentObsList()) {
            if (d.getId() > tempMaxId) tempMaxId = d.getId();
        }
        return tempMaxId;
    }


}

package controller;

import animation.Shake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Executor;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.MainWindowController.GetExecutors;

public class SettingsExecutorController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    // --------------------------------------- Таблица ----------------------------------------
    @FXML
    private TableView<Executor> executorTBL;
    @FXML
    private TableColumn<Executor, String> executorTBLCol;
    // ----------------------------------------------------------------------------------------

    // ------------------------------------ Текстовые поля ------------------------------------
    @FXML
    private TextField emailExecutorTF;
    @FXML
    private TextField fioExecutorTF;
    @FXML
    private TextField phoneExecutorTF;
    // ----------------------------------------------------------------------------------------

    // ---------------------------------------- Кнопки ----------------------------------------
    @FXML
    private Button addBT;
    @FXML
    private Button delBT;
    @FXML
    private Button saveBT;
    // ----------------------------------------------------------------------------------------


    @FXML
    void initialize() {
        executorTBL.setItems(GetExecutors());
        executorTBLCol.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        getExecutorValue();
    }


    @FXML
    void addExecutor(ActionEvent event) {
        if (isFullData()) {
            GetExecutors().add(new Executor(fioExecutorTF.getText(), phoneExecutorTF.getText(), emailExecutorTF.getText()));
        } else {
            shakeField();
        }
    }

    @FXML
    void removeExecutor(ActionEvent event) {
        TableView.TableViewSelectionModel<Executor> selectionModel = executorTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1)) {
            executorTBL.getItems().remove(selectionModel.getSelectedItem());
        }
    }

    @FXML
    void saveExecutor(ActionEvent event) {
        TableView.TableViewSelectionModel<Executor> selectionModel = executorTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1) && !isContainsDepartment()) {
            int index = selectionModel.getSelectedIndex();
            executorTBL.getItems().get(index).setNameProperty(fioExecutorTF.getText());
            executorTBL.getItems().get(index).setPhoneProperty(phoneExecutorTF.getText());
            executorTBL.getItems().get(index).setEmailProperty(emailExecutorTF.getText());
        }
    }

    private void getExecutorValue() {
        TableView.TableViewSelectionModel<Executor> selectionModel = executorTBL.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if (newVal != null) {
                fioExecutorTF.setText(newVal.getName());
                phoneExecutorTF.setText(newVal.getPhone());
                emailExecutorTF.setText(newVal.getEmail());
            }
        });
    }

    private boolean isContainsDepartment() {
        for (Executor e : GetExecutors()) {
            if (executorTBLCol.getText().equals(e.getName())) return true;
        }
        return false;
    }

    private void shakeField() {
        if (fioExecutorTF.getText().isEmpty()) {
            Shake shake = new Shake(fioExecutorTF);
            shake.playAnimation();
        }
        if (phoneExecutorTF.getText().isEmpty()) {
            Shake shake = new Shake(phoneExecutorTF);
            shake.playAnimation();
        }
        if (emailExecutorTF.getText().isEmpty()) {
            Shake shake = new Shake(emailExecutorTF);
            shake.playAnimation();
        }
    }

    private boolean isFullData() {
        return (!emailExecutorTF.getText().isEmpty() && !fioExecutorTF.getText().isEmpty() && !phoneExecutorTF.getText().isEmpty());
    }
}

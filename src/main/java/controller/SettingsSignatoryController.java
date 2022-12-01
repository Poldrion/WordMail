package controller;

import animation.Shake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Signatory;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.MainWindowController.GetSignatories;

public class SettingsSignatoryController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    // ------------------------- кнопки ------------------------------------
    @FXML
    private Button addBT;
    @FXML
    private Button delBT;
    @FXML
    private Button saveBT;
    // ---------------------------------------------------------------------
    // ---------------------- текстовые поля--------------------------------
    @FXML
    private TextField fioSignatoryTF;
    @FXML
    private TextArea postSignatoryTA;
    @FXML
    private TextField shortPostTF;
    // ---------------------------------------------------------------------

    // ------------------------- таблица -----------------------------------
    @FXML
    private TableView<Signatory> signatoryTBL;
    @FXML
    private TableColumn<Signatory, String> signatoryTBLCol;
    // ---------------------------------------------------------------------

    @FXML
    void initialize() {
        signatoryTBL.setItems(GetSignatories());
        signatoryTBLCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        getSignatoryValue();
    }

    @FXML
    void addSignatory(ActionEvent event) {
        if (isFullData()) {
            GetSignatories().add(new Signatory(fioSignatoryTF.getText(), postSignatoryTA.getText(), shortPostTF.getText()));
        } else {
            shakeField();
        }
    }

    @FXML
    void removeSignatory(ActionEvent event) {
        TableView.TableViewSelectionModel<Signatory> selectionModel = signatoryTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1)) {
            signatoryTBL.getItems().remove(selectionModel.getSelectedItem());
        }
    }

    @FXML
    void saveSignatory(ActionEvent event) {
        TableView.TableViewSelectionModel<Signatory> selectionModel = signatoryTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1) && !isContainsSignatory()) {
            int index = selectionModel.getSelectedIndex();
            signatoryTBL.getItems().get(index).setNameSignatoryProperty(fioSignatoryTF.getText());
            signatoryTBL.getItems().get(index).setPostProperty(postSignatoryTA.getText());
            signatoryTBL.getItems().get(index).setShortPostProperty(shortPostTF.getText());
        }
    }

    private void getSignatoryValue() {
        TableView.TableViewSelectionModel<Signatory> selectionModel = signatoryTBL.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if (newVal != null) {
                fioSignatoryTF.setText(newVal.getNameSignatory());
                postSignatoryTA.setText(newVal.getPost());
                shortPostTF.setText(newVal.getShortPost());
            }
        });
    }

    private boolean isContainsSignatory() {
        for (Signatory s : GetSignatories()) {
            if (signatoryTBLCol.getText().equals(s.getNameSignatory())) return true;
        }
        return false;
    }

    private void shakeField() {
        if (fioSignatoryTF.getText().isEmpty()) {
            Shake shake = new Shake(fioSignatoryTF);
            shake.playAnimation();
        }
        if (postSignatoryTA.getText().isEmpty()) {
            Shake shake = new Shake(postSignatoryTA);
            shake.playAnimation();
        }
        if (shortPostTF.getText().isEmpty()) {
            Shake shake = new Shake(shortPostTF);
            shake.playAnimation();
        }
    }

    private boolean isFullData() {
        return (!fioSignatoryTF.getText().isEmpty() && !postSignatoryTA.getText().isEmpty() && !shortPostTF.getText().isEmpty());
    }

}

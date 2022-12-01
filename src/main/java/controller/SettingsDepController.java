package controller;

import animation.Shake;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Department;
import model.Director;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.MainWindowController.GetGroupDepartmentObsList;
import static controller.MainWindowController.GetRootElement;
import static javafx.scene.control.SelectionMode.SINGLE;

public class SettingsDepController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    // ------------ кнопки ----------------------
    @FXML
    private Button addBT;
    @FXML
    private Button delBT;
    @FXML
    private Button saveBT;
    // ------------------------------------------

    // ----------- таблица ----------------------
    @FXML
    private TreeTableView<Department> departmentTBL;
    @FXML
    private TreeTableColumn<Department, String> depAddress;
    @FXML
    private TreeTableColumn<Department, String> directorDepAddress;
    // ------------------------------------------

    // ------------- пол ------------------------
    @FXML
    private ToggleGroup gender;
    @FXML
    private RadioButton maleRB;
    @FXML
    private RadioButton femaleRB;
    // ------------------------------------------

    @FXML
    private TextField departmentTF;
    @FXML
    private TextField shortDepartmentTF;
    @FXML
    private TextField firstNameTF;
    @FXML
    private TextField lastNameTF;
    @FXML
    private TextField patronymicTF;
    @FXML
    private TextArea appealTF;

    @FXML
    private ChoiceBox<Department> rootBlock;

    private final TreeItem<Department> root = GetRootElement();


    @FXML
    void initialize() {
        // настрока отображения таблицы
        depAddress.setCellValueFactory(param -> param.getValue().getValue().shortNameProperty());
        directorDepAddress.setCellValueFactory(param -> param.getValue().getValue().getDirector().fioDirectorDepartment());

        departmentTBL.setRoot(root);
        departmentTBL.setShowRoot(false);
        departmentTBL.getSelectionModel().setSelectionMode(SINGLE);

        rootBlock.setItems(GetGroupDepartmentObsList());

        // всплывающие подсказки
        Tooltip appealTooltip = new Tooltip("""
                Введите должность, инициалы имени и отчества
                руководителя подразделения в радительном падеже.

                Например:

                Первому заместителю генерального директора\s
                по производству - главному инженеру
                И.И. Иванову""");
        appealTF.setTooltip(appealTooltip);
    }

    @FXML
    void addDepartment(ActionEvent event) {
        if (isFullData()) {
            int groupId = rootBlock.getValue().getId();

            Department newDep = new Department(getNewID(groupId), departmentTF.getText(), shortDepartmentTF.getText(),
                    new Director(firstNameTF.getText(), patronymicTF.getText(), lastNameTF.getText(), maleRB.isSelected(), appealTF.getText()));
            TreeItem<Department> tempElement = departmentTBL.getRoot().getChildren().get(rootBlock.getValue().getId() - 1);
            tempElement.getChildren().add(new TreeItem<>(newDep));
        } else {
            shakeField();
        }
    }

    @FXML
    void removeDepartment(ActionEvent event) {
        TreeTableView.TreeTableViewSelectionModel<Department> selectionModel = departmentTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1) && !selectionModel.isEmpty())
            if (selectionModel.getSelectedItem().getParent() != GetRootElement())
                selectionModel.getSelectedItem().getParent().getChildren().remove(selectionModel.getSelectedItem());
    }

    @FXML
    void saveDepartment(ActionEvent event) {
        TreeTableView.TreeTableViewSelectionModel<Department> selectionModel = departmentTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1) && !selectionModel.isEmpty())
            if (selectionModel.getSelectedItem().getParent() != GetRootElement()) {
                Department currentDepartment = selectionModel.getSelectedItem().getValue();
                if (isFullData()) {
                    currentDepartment.setNameProperty(departmentTF.getText());
                    currentDepartment.setShortName(shortDepartmentTF.getText());
                    currentDepartment.getDirector().setFirstNameProperty(firstNameTF.getText());
                    currentDepartment.getDirector().setPatronymicProperty(patronymicTF.getText());
                    currentDepartment.getDirector().setLastNameProperty(lastNameTF.getText());
                    currentDepartment.getDirector().setAppealProperty(appealTF.getText());
                    currentDepartment.getDirector().setIsMaleProperty(maleRB.isSelected());
                } else {
                    shakeField();
                }
            }
        departmentTBL.refresh();
    }

    private void shakeField() {
        if (departmentTF.getText().isEmpty()) {
            Shake shake = new Shake(departmentTF);
            shake.playAnimation();
        }
        if (shortDepartmentTF.getText().isEmpty()) {
            Shake shake = new Shake(shortDepartmentTF);
            shake.playAnimation();
        }
        if (firstNameTF.getText().isEmpty()) {
            Shake shake = new Shake(firstNameTF);
            shake.playAnimation();
        }
        if (patronymicTF.getText().isEmpty()) {
            Shake shake = new Shake(patronymicTF);
            shake.playAnimation();
        }
        if (lastNameTF.getText().isEmpty()) {
            Shake shake = new Shake(lastNameTF);
            shake.playAnimation();
        }
        if (appealTF.getText().isEmpty() || appealTF.getText() == null) {
            Shake shake = new Shake(appealTF);
            shake.playAnimation();
        }
    }

    @FXML
    void loadData(MouseEvent event) {
        TreeTableView.TreeTableViewSelectionModel<Department> selectionModel = departmentTBL.getSelectionModel();
        if (!selectionModel.isSelected(-1) && !selectionModel.isEmpty()) {
            departmentTF.setText(selectionModel.getSelectedItem().getValue().getNameProperty());
            shortDepartmentTF.setText(selectionModel.getSelectedItem().getValue().getShortName());
            firstNameTF.setText(selectionModel.getSelectedItem().getValue().getDirector().getFirstName());
            lastNameTF.setText(selectionModel.getSelectedItem().getValue().getDirector().getLastName());
            patronymicTF.setText(selectionModel.getSelectedItem().getValue().getDirector().getPatronymic());
            appealTF.setText(selectionModel.getSelectedItem().getValue().getDirector().getAppeal());

            if (selectionModel.getSelectedItem().getValue().getDirector().isMale()) maleRB.fire();
            else femaleRB.fire();

            if (selectionModel.getSelectedItem().getParent() == GetRootElement())
                rootBlock.setValue(selectionModel.getSelectedItem().getValue());
            else rootBlock.setValue(selectionModel.getSelectedItem().getParent().getValue());
        }
    }

    private boolean isFullData() {
        return !departmentTF.getText().isEmpty() && !shortDepartmentTF.getText().isEmpty() && !firstNameTF.getText().isEmpty() &&
                !lastNameTF.getText().isEmpty() && !patronymicTF.getText().isEmpty() && !appealTF.getText().isEmpty() &&
                (maleRB.isSelected() || femaleRB.isSelected()) && rootBlock.getValue() != null;
    }

    private int getNewID(int tempId) {
        int tempMaxID = 0;
        ObservableList<TreeItem<Department>> currentGroup = GetRootElement().getChildren().get(tempId - 1).getChildren();
        if (currentGroup.isEmpty()) {
            tempMaxID = tempId * 1000;
        } else {
            for (TreeItem<Department> d : currentGroup) {
                if (d.getValue().getId() > tempMaxID) {
                    tempMaxID = d.getValue().getId();
                }
            }
        }

        int minId = rootBlock.getValue().getId() * 1000;
        int maxId = rootBlock.getValue().getId() * 1000 + 999;


        for (Department d : GetGroupDepartmentObsList()) {
            if (d.getId() >= minId && d.getId() <= maxId) {
                tempMaxID = d.getId();
            }
        }
        return tempMaxID + 1;
    }

}

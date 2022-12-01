package controller;

import animation.Shake;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import service.CreateFile;
import service.PrimaryStageAware;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.control.SelectionMode.SINGLE;
import static model.GroupDepartment.GetGroupDepartment;
import static service.Constants.*;
import static service.Utilities.*;

public class MainWindowController implements PrimaryStageAware {
    private static final TreeItem<Department> ROOT = new TreeItem<>(new Department(0, "root", "root", new Director("", "", "", true, "")));
    private static final ObservableList<Department> GROUP_DEPARTMENT_OBS_LIST = FXCollections.observableArrayList(GetGroupDepartment());
    private static final ObservableList<Executor> EXECUTORS = FXCollections.observableArrayList(LoadDataFromFile(EXECUTORS_FILE));
    private static final ObservableList<Signatory> SIGNATORIES = FXCollections.observableArrayList(LoadDataFromFile(SIGNATORIES_FILE));
    private static final ObservableList<IdDepartment> ID_DEPARTMENTS = FXCollections.observableArrayList(LoadDataFromFile(ID_DEPARTMENTS_FILE));
    private static final ObservableList<Department> LIST_TO_ADDRESS = FXCollections.observableArrayList();
    private static final byte MENU_ITEM_IMG_WIDTH = 32;
    private static final byte MENU_ITEM_IMG_HEIGHT = 32;
    private static Department currentDepartment;

    private Stage stage;

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private VBox mainWindowPane;

    @FXML
    private DatePicker dateMail;
    @FXML
    private TextArea subjectMail;

    // ------------------- таблица -------------------
    @FXML
    private TreeTableView<Department> departmentTable;
    @FXML
    private TreeTableColumn<Department, String> depAddress;
    @FXML
    private TreeTableColumn<Department, String> directorDepAddress;
    // -----------------------------------------------

    // ----------------- комбо-боксы -----------------
    @FXML
    private ComboBox<Signatory> signatory;
    @FXML
    private ComboBox<Executor> executorMail;
    @FXML
    private ComboBox<IdDepartment> depSend;
    // -----------------------------------------------

    // ------------------- кнопки --------------------
    @FXML
    private Button createFile;
    @FXML
    private Button upBtn;
    @FXML
    private Button downBtn;
    @FXML
    private Button toFirstPosBtn;
    @FXML
    private Button toLastPosBtn;

    // -----------------------------------------------

    // ------------------- меню настроек -------------
    @FXML
    private MenuItem settingGroupDepartment;
    @FXML
    private MenuItem settingDepartment;
    @FXML
    private MenuItem settingExecutor;
    @FXML
    private MenuItem settingSignatory;
    @FXML
    private MenuItem settingIdDepartment;
    @FXML
    private MenuItem settingPathFiles;
    @FXML
    public MenuItem closeApp;
    @FXML
    private MenuItem aboutApp;
    @FXML
    private MenuItem menuAdmin;
    // -----------------------------------------------
    @FXML
    private MenuItem contextMenuMainTbl;


    @FXML
    private ListView<Department> listAddress;


    @FXML
    void initialize() {
        // настрока отображения таблицы
        setValueTable();

        // настройка отображения списка исполнителя
        setValueExecutor();

        // настройка отображения списка подписантов
        setValueSignatory();

        // настройка отображения списка подразделений-отправителей
        setValueIdDepartment();

        // настройка отображения текущей даты
        setCurrentData();

        // настрока отображения листа адресатов
        setValueListToAddress();

        // установка иконок в меню
        setIconsToMenuItems();

        // настройка видимости элементов меню
        setVisibleAndActiveMenuItem();

    }


    @FXML
    void activateAdmin(ActionEvent event) throws IOException {
        Parent rootPass = FXMLLoader.load(getClass().getClassLoader().getResource("passwordForAdmin.fxml"));
        Scene scene = new Scene(rootPass);
        Stage stage = new Stage();
        stage.setTitle("Введите пароль Администратора");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_GEAR));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        if (PasswordForAdmin.IsAdmin()) {
            settingGroupDepartment.setDisable(false);
            settingGroupDepartment.setVisible(true);
            settingDepartment.setDisable(false);
            settingDepartment.setVisible(true);
            updateTitle("Формирование шаблона исходящего письма - Администратор");
            menuAdmin.setText("Вкл. режим \"Пользователь\"");
            ImageView setUser = new ImageView(new Image(IMG_USER));
            setUser.setFitHeight(MENU_ITEM_IMG_HEIGHT);
            setUser.setFitWidth(MENU_ITEM_IMG_WIDTH);
            menuAdmin.setGraphic(setUser);
        } else {
            setVisibleAndActiveMenuItem();
            updateTitle("Формирование шаблона исходящего письма - Пользователь");
            menuAdmin.setText("Вкл. режим \"Администратор\"");
            ImageView setAdmin = new ImageView(new Image(IMG_ADMIN_SETTINGS_MALE));
            setAdmin.setFitHeight(MENU_ITEM_IMG_HEIGHT);
            setAdmin.setFitWidth(MENU_ITEM_IMG_WIDTH);
            menuAdmin.setGraphic(setAdmin);
        }
    }

    @FXML
    void openSettGroupDepWindow(ActionEvent event) throws IOException {
        Parent rootGroupDep = FXMLLoader.load(getClass().getClassLoader().getResource("settingsGroupDepartment.fxml"));
        Scene scene = new Scene(rootGroupDep);
        Stage stage = new Stage();
        stage.setTitle("Настройка списка групп подразделений");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_GEAR));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        ROOT.getChildren().clear();
        setValueTable();
        SaveDataFromLists(LoadListFiles().get(0), OnlyGroupDepartment());
    }

    @FXML
    void openSettingsDepController(ActionEvent event) throws IOException {
        Parent rootGroupDep = FXMLLoader.load(getClass().getClassLoader().getResource("settingsDepartment.fxml"));
        Scene scene = new Scene(rootGroupDep);
        Stage stage = new Stage();
        stage.setTitle("Настройка подразделений");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_GEAR));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        departmentTable.refresh();
        SaveDataFromLists(LoadListFiles().get(1), OnlyDepartment());
    }

    @FXML
    void openSettingsExecutor(ActionEvent event) throws IOException {
        Parent rootGroupDep = FXMLLoader.load(getClass().getClassLoader().getResource("settingsExecutor.fxml"));
        Scene scene = new Scene(rootGroupDep);
        Stage stage = new Stage();
        stage.setTitle("Настройка исполнителей");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_GEAR));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        // Сохранение списка исполнителей
        SaveDataFromLists(EXECUTORS_FILE, GetExecutors());
    }

    @FXML
    void openSettingsSignatories(ActionEvent event) throws IOException {
        Parent rootGroupDep = FXMLLoader.load(getClass().getClassLoader().getResource("settingsSignatory.fxml"));
        Scene scene = new Scene(rootGroupDep);
        Stage stage = new Stage();
        stage.setTitle("Настройка подписанта");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_GEAR));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        signatory.getSelectionModel().selectNext();
        signatory.getSelectionModel().selectPrevious();
        // Сохранение списка подписантов
        SaveDataFromLists(SIGNATORIES_FILE, GetSignatories());


    }

    @FXML
    void openSettingsIdDepartment(ActionEvent event) throws IOException {
        Parent rootGroupDep = FXMLLoader.load(getClass().getClassLoader().getResource("settingsIdDepartment.fxml"));
        Scene scene = new Scene(rootGroupDep);
        Stage stage = new Stage();
        stage.setTitle("Настройка подразделения-отправителя");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_GEAR));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        // Сохранение списка подразделений-отправителей
        SaveDataFromLists(ID_DEPARTMENTS_FILE, GetIdDepartments());
    }

    @FXML
    void openSettingsPathFiles(ActionEvent event) throws IOException {
        Parent rootGroupDep = FXMLLoader.load(getClass().getClassLoader().getResource("settingsPathFiles.fxml"));
        Scene scene = new Scene(rootGroupDep);
        Stage stage = new Stage();
        stage.setTitle("Настройка путей к файлам со списками подразделений");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_GEAR));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    void closeApp(ActionEvent event) {
        // Сохранение списка исполнителей
        SaveDataFromLists(EXECUTORS_FILE, GetExecutors());

        // Сохранение списка подписантов
        SaveDataFromLists(SIGNATORIES_FILE, GetSignatories());

        // Сохранение списка подразделений-отправителей
        SaveDataFromLists(ID_DEPARTMENTS_FILE, GetIdDepartments());
        System.exit(0);
    }

    @FXML
    void pressDownBtn(ActionEvent event) {
        if (listAddress.getItems().size() > 1) {
            Department currentElement = listAddress.getSelectionModel().getSelectedItem();
            int indexCurrentElement = listAddress.getSelectionModel().getSelectedIndex();
            int indexNextElement = indexCurrentElement + 1;
            if (indexNextElement < LIST_TO_ADDRESS.size()) {
                Department nextElement = listAddress.getItems().get(indexNextElement);
                LIST_TO_ADDRESS.set(indexCurrentElement, nextElement);
                LIST_TO_ADDRESS.set(indexNextElement, currentElement);
            }
        }
    }

    @FXML
    void pressUpBtn(ActionEvent event) {
        if (listAddress.getItems().size() > 1) {
            Department currentElement = listAddress.getSelectionModel().getSelectedItem();
            int indexCurrentElement = listAddress.getSelectionModel().getSelectedIndex();
            int indexPrevElement = indexCurrentElement - 1;
            if (indexPrevElement > -1) {
                Department nextElement = listAddress.getItems().get(indexPrevElement);
                LIST_TO_ADDRESS.set(indexCurrentElement, nextElement);
                LIST_TO_ADDRESS.set(indexPrevElement, currentElement);
            }
        }
    }

    @FXML
    void pressToFirstPosBtn(ActionEvent event) {
        if (listAddress.getItems().size() > 1) {
            Department currentElement = listAddress.getSelectionModel().getSelectedItem();
            LIST_TO_ADDRESS.remove(currentElement);
            LIST_TO_ADDRESS.add(0, currentElement);
        }
    }

    @FXML
    void pressToLastPosBtn(ActionEvent event) {
        if (listAddress.getItems().size() > 1) {
            Department currentElement = listAddress.getSelectionModel().getSelectedItem();
            LIST_TO_ADDRESS.remove(currentElement);
            LIST_TO_ADDRESS.add(LIST_TO_ADDRESS.size(), currentElement);
        }
    }

    @FXML
    void pressCreateFile(ActionEvent event) {
        if (LIST_TO_ADDRESS.size() > 0 && subjectMail.getText() != null && !subjectMail.getText().trim().equals(""))
                new CreateFile(this).start();
            else
                shakeField();
    }


    @FXML
    void clickAboutApp(ActionEvent event) throws IOException {
        Parent about = FXMLLoader.load(getClass().getClassLoader().getResource("about.fxml"));
        Scene scene = new Scene(about);
        Stage stage = new Stage();
        stage.setTitle("О приложении");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_ABOUT));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    void clickContextMenuMainTbl(ActionEvent event) throws IOException {
        currentDepartment = departmentTable.getSelectionModel().getSelectedItem().getValue();
        Parent detail = FXMLLoader.load(getClass().getClassLoader().getResource("detailInformation.fxml"));
        Scene scene = new Scene(detail);
        Stage stage = new Stage();
        stage.setTitle("Детальная информация");
        stage.setScene(scene);
        stage.getIcons().add(new Image(IMG_GEAR));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public static Department GetCurrentDepartment() {
        return currentDepartment;
    }

    public static TreeItem<Department> GetRootElement() {
        return ROOT;
    }

    public static ObservableList<Department> GetGroupDepartmentObsList() {
        return GROUP_DEPARTMENT_OBS_LIST;
    }

    public static ObservableList<Executor> GetExecutors() {
        return EXECUTORS;
    }

    public static ObservableList<Signatory> GetSignatories() {
        return SIGNATORIES;
    }

    public static ObservableList<IdDepartment> GetIdDepartments() {
        return ID_DEPARTMENTS;
    }

    public static ObservableList<Department> getAddressList() {
        return LIST_TO_ADDRESS;
    }

    public IdDepartment getIdDepartment() {
        return depSend.getValue();
    }

    public LocalDate getDateMail() {
        return dateMail.getValue();
    }

    public Signatory getSignatory() {
        return signatory.getValue();
    }

    public Executor getExecutor() {
        return executorMail.getValue();
    }

    public String getSubjectMail() {
        return subjectMail.getText();
    }

    public String getParentName(Department department) {
        String parentName = "";
        ObservableList<TreeItem<Department>> groupDep = departmentTable.getRoot().getChildren();
        for (TreeItem<Department> gD : groupDep) {
            ObservableList<TreeItem<Department>> allChildrenInGroup = gD.getChildren();
            for (TreeItem<Department> element : allChildrenInGroup) {
                if (element.getValue().equals(department))
                    parentName = gD.getValue().getShortName();
            }
        }
        return parentName;
    }


    // Получение списка списков для заполнения подразделений
    private ArrayList<ArrayList<Department>> getDepartments() {
        ArrayList<ArrayList<Department>> result = new ArrayList<>();
        // кол-во записей в списке групп подразделений
        ArrayList<Department> tempGroup = LoadDataFromFile(LoadListFiles().get(0));
        int countGroup = 0;
        for (Department d : tempGroup) {
            if (d.getId() > countGroup) countGroup = d.getId();
        }
        // создание пустых списков в кол-ве равном (countGroup + 1)
        for (int i = 0; i <= countGroup; i++) {
            result.add(new ArrayList<>());
        }
        // получение списка подразделений
        ArrayList<Department> loadDepartmentFromFile = LoadDataFromFile(LoadListFiles().get(1));

        for (Department d : loadDepartmentFromFile) {
            int idGroup = d.getId() / 1000;
            result.get(idGroup).add(d);
        }
        return result;
    }

    private void setValueTable() {
        // формирование перечня групп подразделений
        for (Department d : GROUP_DEPARTMENT_OBS_LIST) {
            ROOT.getChildren().add(new TreeItem<>(d));
        }
        // Заполнение подразделениями в соответствии с группами
        ArrayList<ArrayList<Department>> departments = getDepartments();
        for (TreeItem<Department> element : ROOT.getChildren()) {
            int idGroup = element.getValue().getId();
            ArrayList<Department> currentDepartments = departments.get(idGroup);
            for (Department d : currentDepartments) {
                element.getChildren().add(new TreeItem<>(d));
            }
        }
        depAddress.setCellValueFactory(param -> param.getValue().getValue().shortNameProperty());
        directorDepAddress.setCellValueFactory(param -> param.getValue().getValue().getDirector().fioDirectorDepartment());

        departmentTable.setRoot(ROOT);
        departmentTable.setShowRoot(false);
        departmentTable.getSelectionModel().setSelectionMode(SINGLE);

        // настройка двойного клика мышкой по строке таблицы
        departmentTable.setRowFactory(ttv -> {
            TreeTableRow<Department> row = new TreeTableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && (!row.isEmpty()) && (row.getTreeItem().getParent() != ROOT)) {
                    Department rowData = row.getItem();
                    if (!LIST_TO_ADDRESS.contains(rowData))
                        LIST_TO_ADDRESS.add(rowData);
                    mouseEvent.consume();
                }
            });
            return row;
        });
    }

    private void setValueExecutor() {
        executorMail.setItems(EXECUTORS);
        if (executorMail.getItems().size() > 0)
            executorMail.setValue(EXECUTORS.get(0));
    }

    private void setValueSignatory() {
        signatory.setItems(SIGNATORIES);
        if (signatory.getItems().size() > 0)
            signatory.setValue(SIGNATORIES.get(0));
    }

    private void setValueIdDepartment() {
        depSend.setItems(ID_DEPARTMENTS);
        if (depSend.getItems().size() > 0)
            depSend.setValue(ID_DEPARTMENTS.get(0));
    }

    private void setCurrentData() {
        dateMail.setValue(LocalDate.now());
    }

    private void setValueListToAddress() {
        Tooltip tooltip = new Tooltip("Для удаления подразделения дважды кликнуть\nпо нужной строке левой клавишей мышки");
        listAddress.setTooltip(tooltip);
        listAddress.setItems(LIST_TO_ADDRESS);

        // настройка двойного клика мышкой по элементу списка
        listAddress.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Department element = listAddress.getSelectionModel().getSelectedItem();
            if (mouseEvent.getClickCount() == 2 && (element != null)) {
                LIST_TO_ADDRESS.remove(element);
                mouseEvent.consume();
            }
        });
    }

    private void setIconsToMenuItems() {
        ImageView closeIcon = new ImageView(new Image(IMG_SHUTDOWN));
        closeIcon.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        closeIcon.setFitWidth(MENU_ITEM_IMG_HEIGHT);
        closeApp.setGraphic(closeIcon);

        ImageView setGroupDepIcon = new ImageView(new Image(IMG_SETTINGS));
        setGroupDepIcon.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        setGroupDepIcon.setFitWidth(MENU_ITEM_IMG_WIDTH);
        settingGroupDepartment.setGraphic(setGroupDepIcon);

        ImageView setDepIcon = new ImageView(new Image(IMG_SETTINGS));
        setDepIcon.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        setDepIcon.setFitWidth(MENU_ITEM_IMG_WIDTH);
        settingDepartment.setGraphic(setDepIcon);

        ImageView setExecutorIcon = new ImageView(new Image(IMG_SETTINGS));
        setExecutorIcon.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        setExecutorIcon.setFitWidth(MENU_ITEM_IMG_WIDTH);
        settingExecutor.setGraphic(setExecutorIcon);

        ImageView setSignatoryIcon = new ImageView(new Image(IMG_SETTINGS));
        setSignatoryIcon.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        setSignatoryIcon.setFitWidth(MENU_ITEM_IMG_WIDTH);
        settingSignatory.setGraphic(setSignatoryIcon);

        ImageView setIdDepIcon = new ImageView(new Image(IMG_SETTINGS));
        setIdDepIcon.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        setIdDepIcon.setFitWidth(MENU_ITEM_IMG_WIDTH);
        settingIdDepartment.setGraphic(setIdDepIcon);

        ImageView setPathFileIcon = new ImageView(new Image(IMG_SETTINGS));
        setPathFileIcon.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        setPathFileIcon.setFitWidth(MENU_ITEM_IMG_WIDTH);
        settingPathFiles.setGraphic(setPathFileIcon);

        ImageView setAdmin = new ImageView(new Image(IMG_ADMIN_SETTINGS_MALE));
        setAdmin.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        setAdmin.setFitWidth(MENU_ITEM_IMG_WIDTH);
        menuAdmin.setGraphic(setAdmin);

        ImageView about = new ImageView(new Image(IMG_ABOUT));
        about.setFitHeight(MENU_ITEM_IMG_HEIGHT);
        about.setFitWidth(MENU_ITEM_IMG_HEIGHT);
        aboutApp.setGraphic(about);
    }

    private void shakeField() {
        if (subjectMail.getText().isEmpty() || subjectMail.getText().trim().equals("")) {
            Shake shakeSubjectTF = new Shake(subjectMail);
            shakeSubjectTF.playAnimation();
        }
        if (listAddress.getItems().isEmpty()) {
            Shake shakeListAddress = new Shake(listAddress);
            shakeListAddress.playAnimation();
        }
    }

    private void setVisibleAndActiveMenuItem() {
        settingGroupDepartment.setDisable(true);
        settingGroupDepartment.setVisible(false);
        settingDepartment.setDisable(true);
        settingDepartment.setVisible(false);

    }

    @Override
    public void setPrimaryStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    private void updateTitle(String title) {
        if (stage != null) {
            stage.setTitle(title);
        } else {
            System.out.println("Warning: null stage");
        }
    }
}


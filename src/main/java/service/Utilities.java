package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import model.Department;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static controller.MainWindowController.GetGroupDepartmentObsList;
import static controller.MainWindowController.GetRootElement;
import static service.Constants.FILE_PATH;

public class Utilities {

    /**
     * Загрузка списка адресов расположения файлов с данными
     *
     * @return список с адресами расположения файлов с данными
     */
    public static ArrayList<String> LoadListFiles() {
        // формирование списка адресов файлов
        ArrayList<String> listPaths = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            while (br.ready()) {
                listPaths.add(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Файл с путями к другим файлам отсутствует!");
            alert.showAndWait();
            try {
                Files.createFile(Path.of(FILE_PATH));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return listPaths;
    }

    /**
     * Загрузка списка строк из указанного файла
     *
     * @param path - путь к файлу, из которого необходимо загрузить список строк
     * @return - список со строками из файла
     */

    public static ArrayList<String> LoadFromFile(String path) {
        ArrayList<String> tempList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while (br.ready()) {
                tempList.add(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            try {
                Files.createFile(Path.of(path));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return tempList;
    }

    /**
     * Сохранение (сериализация) данных из ObservableList в файл
     *
     * @param filePath       - файл в который происходит сохранение (сериализация)
     * @param observableList - списов, который необходимо сохранить (сериализовать)
     */
    public static void SaveDataFromLists(String filePath, ObservableList<?> observableList) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(observableList));
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загрузка (десериализация) данных из файла в список, для дальнейшей инициализации ObservableList
     *
     * @param filePath - путь к файлу, из которого происходит загрузка (десериализация) данных
     * @param <T>      - класс, к которому необходимо привести объекты в списке
     * @return - возвращает список с объектами, приведенными к необходимому классу
     */

    public static <T> ArrayList<T> LoadDataFromFile(String filePath) {
        ArrayList<T> temp = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (ArrayList<T>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (temp == null) return new ArrayList<>();
        return temp;
    }

    // ----------------------- метод для выбора только подразделений ------------------------------------

    public static ObservableList<Department> OnlyDepartment() {
        ArrayList<Department> result = new ArrayList<>();
        for (TreeItem<Department> element : GetRootElement().getChildren()) {
            ObservableList<TreeItem<Department>> elementChildren = element.getChildren();
            for (TreeItem<Department> e : elementChildren) {
                result.add(e.getValue());
            }
        }
        return FXCollections.observableArrayList(result);
    }

    // ----------------------- метод для выбора только групп --------------------------------------------

    public static ObservableList<Department> OnlyGroupDepartment() {
        ArrayList<Department> result = new ArrayList<>();
        ArrayList<Department> tempList = new ArrayList<>(GetGroupDepartmentObsList());
        for (Department d : tempList) {
            if (d.getId() < 100) {
                result.add(d);
            }
        }
        return FXCollections.observableArrayList(result);
    }

    public static void SetCustomContextMenuTextInputControl(TextInputControl tic) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem customUndo = new MenuItem("Отменить последнее действие");
        customUndo.setOnAction(e -> tic.undo());

        MenuItem customRedo = new MenuItem("Вернуть последнее действие");
        customRedo.setOnAction(e -> tic.redo());

        MenuItem customCut = new MenuItem("Вырезать");
        customCut.setOnAction(e -> tic.cut());

        MenuItem customCopy = new MenuItem("Копировать");
        customCopy.setOnAction(e -> tic.copy());

        MenuItem customPaste = new MenuItem("Вставить");
        customPaste.setOnAction(e -> tic.paste());

        MenuItem customClear = new MenuItem("Очистить");
        customClear.setOnAction(e -> tic.clear());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        MenuItem customSelectAll = new MenuItem("Выделить все");
        customSelectAll.setOnAction(e -> tic.selectAll());

        contextMenu.getItems().addAll(customUndo, customRedo, customCut, customCopy, customPaste, customClear, separatorMenuItem, customSelectAll);
        tic.setContextMenu(contextMenu);
    }

}

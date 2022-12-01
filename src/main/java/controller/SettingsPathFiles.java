package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import service.Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static service.Constants.FILE_PATH;

public class SettingsPathFiles {

    private final ArrayList<String> filesPath = Utilities.LoadFromFile(FILE_PATH);

    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();


    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    @FXML
    private TextField groupDepPathTF;
    @FXML
    private TextField depPathTF;
    @FXML
    public TextField resultsDirTF;

    @FXML
    private Label infoLabel;

    @FXML
    private Button saveBT;
    @FXML
    private Button btnChooseDirResults;
    @FXML
    private Button btnChooseFileDep;
    @FXML
    private Button btnChooseFileGroupDep;


    @FXML
    void initialize() {
        groupDepPathTF.setText(filesPath.get(0));
        depPathTF.setText(filesPath.get(1));
        resultsDirTF.setText(filesPath.get(2));

    }

    @FXML
    void save(ActionEvent event) {
        if (!groupDepPathTF.getText().isEmpty() && !depPathTF.getText().isEmpty() && !resultsDirTF.getText().isEmpty()) {
            String groupFile = groupDepPathTF.getText();
            String depFile = depPathTF.getText();
            String resultFile = resultsDirTF.getText();
            try {
                if (Files.exists(Path.of(groupFile)) && Files.exists(Path.of(depFile)) && Files.exists(Path.of(resultFile))) {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
                    bw.write(groupFile);
                    bw.newLine();
                    bw.write(depFile);
                    bw.newLine();
                    bw.write(resultFile);
                    bw.close();
                    infoLabel.setText("Изменения сохранены");
                    infoLabel.setTextFill(Color.GREEN);
                    infoLabel.setVisible(true);
                } else {
                    throw new IOException();
                }
            } catch (IOException e) {
                StringBuilder resultText = new StringBuilder("Изменения не сохранены!!!");
                if (!Files.exists(Path.of(groupFile))) {
                    resultText.append(System.lineSeparator()).append("Файл с группами подразделений не найден!");
                }
                if (!Files.exists(Path.of(depFile))) {
                    resultText.append(" ").append("Файл с подразделениями не найден!");
                }
                if (!Files.exists(Path.of(resultFile))) {
                    resultText.append(" ").append("Папка для хранения результатов работы программы не найдена!");
                }
                infoLabel.setText(resultText.toString());
                infoLabel.setTextFill(Color.RED);
                infoLabel.setVisible(true);
            }
        }
    }

    @FXML
    void clickBtnChooseDirResults(ActionEvent event) {
        configuringDirectoryChooser(directoryChooser);
        File dir = directoryChooser.showDialog(resultsDirTF.getScene().getWindow());
        if (dir != null) {
            resultsDirTF.setText(dir.getAbsolutePath());
        } else {
            resultsDirTF.setText("");
        }
    }

    @FXML
    void clickBtnChooseFileDep(ActionEvent event) {
        fileChooser.setTitle("Выбор файла с подразделениями");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл подразделений", "department.bin"));
        File file = fileChooser.showOpenDialog(depPathTF.getScene().getWindow());
        if (file != null) {
            List<File> files = Collections.singletonList(file);
            printLog(depPathTF, files);
        }
    }

    @FXML
    void clickBtnChooseFileGroupDep(ActionEvent event) {
        fileChooser.setTitle("Выбор файла с группами подразделений");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл группы подразделений", "groupDepartment.bin"));
        File file = fileChooser.showOpenDialog(groupDepPathTF.getScene().getWindow());
        if (file != null) {
            List<File> files = Collections.singletonList(file);
            printLog(groupDepPathTF, files);
        }
    }

    private void printLog(TextField textField, List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
            textField.setText(file.getAbsolutePath());
        }
    }

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        directoryChooser.setTitle("Выберите папку, в которой будут храниться результаты работы программы");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
}

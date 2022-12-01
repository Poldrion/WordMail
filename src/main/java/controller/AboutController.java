package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    @FXML
    private Hyperlink emailForFeedback;
    @FXML
    private Hyperlink siteHyperlink;
    @FXML
    private Button okBtn;

    @FXML
    void clickEmailHyperlink(ActionEvent event) throws IOException {
        String str = "mailto:Romashka2007@inbox.ru?subject=Обратная%20связь%20о%20работе%20приложения%20«Формирование%20шаблона%20служебной%20записки»";
        Desktop.getDesktop().mail(URI.create(str));
    }

    @FXML
    void clickSiteHyperlink(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://icons8.ru"));
    }

    @FXML
    void clickOkBtn(ActionEvent event) {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {

    }

}

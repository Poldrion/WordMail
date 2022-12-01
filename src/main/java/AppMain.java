import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import service.PrimaryStageAware;

import java.io.IOException;

import static service.Constants.IMG_MICROSOFT_WORD;


public class AppMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
            loader.setControllerFactory((Class<?> type) -> {
                try {
                    Object controller = type.newInstance();
                    if (controller instanceof PrimaryStageAware) {
                        ((PrimaryStageAware) controller).setPrimaryStage(primaryStage);
                    }
                    return controller;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Parent root = loader.load();
            primaryStage.setTitle("Формирование шаблона исходящего письма");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(IMG_MICROSOFT_WORD));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}

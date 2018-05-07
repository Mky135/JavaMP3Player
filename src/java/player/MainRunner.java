package player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainRunner extends Application
{
    static Scene scene;
    public static Stage stage;

    public void start(Stage stage) throws Exception
    {
        stage.setTitle("Java Mp3 Player");

        Parent mainRoot = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        scene = new Scene(mainRoot);
        MainRunner.stage = stage;
        // Display the window
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.toFront();
        stage.sizeToScene();
        stage.getIcons().add(new Image(MainRunner.class.getResourceAsStream("/images/icon.png")));
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

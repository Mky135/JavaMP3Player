import controller.mp3PlayerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainRunner extends Application
{

    static Scene scene;

    public void start(Stage stage) throws Exception
    {
        stage.setTitle("Java Mp3 Player");
        Parent mainRoot = FXMLLoader.load(getClass().getResource("main.fxml"));
        scene = new Scene(mainRoot);

        // Display the window
        stage.setScene(scene);
//        stage.sizeToScene();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        stage.sizeToScene();
        stage.show();

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application
{

    public void start(Stage stage) throws Exception
    {
        stage.setTitle("Java Mp3 Player");
        Parent mainRoot = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(mainRoot);

        // Display the window
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

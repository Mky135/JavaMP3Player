package player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import player.controller.EqualizerController;


public class MainRunner extends Application
{
    private Scene playerScene;
    private Scene eqScene;
    private static Stage eqStage;

    public void start(Stage stage) throws Exception
    {
        eqStage = new Stage();

        eqStage.setTitle("Equalizer");
        stage.setTitle("Java Mp3 Player");

        Parent eqRoot = FXMLLoader.load(getClass().getResource("/fxml/equalizer.fxml"));
        Parent mainRoot = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

        eqScene = new Scene(eqRoot);
        playerScene = new Scene(mainRoot);

        // Display MP3 Player
        stage.setScene(playerScene);
        stage.toFront();
        stage.getIcons().add(new Image(MainRunner.class.getResourceAsStream("/images/icon.png")));
        stage.show();

        //Displays Equalizer
        eqStage.setResizable(false);
        eqStage.setY(0);
        eqStage.setX(0);
        eqStage.setScene(eqScene);
        eqStage.toFront();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public static void setEQStageShow(boolean visable)
    {
        if(visable)
            eqStage.show();
        else
            eqStage.hide();
    }
}

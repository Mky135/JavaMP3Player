package player;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;


public class MainRunner extends Application
{
    private static Stage eqStage;
    private static Stage notifyStage;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public void start(Stage stage) throws Exception
    {
        eqStage = new Stage();
        notifyStage = new Stage();

        stage.setTitle("Java Mp3 Player");
        eqStage.setTitle("Equalizer");

        Parent mainRoot = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Parent eqRoot = FXMLLoader.load(getClass().getResource("/fxml/equalizer.fxml"));
        Parent notifyRoot = FXMLLoader.load(getClass().getResource("/fxml/notification.fxml"));

        Scene eqScene = new Scene(eqRoot);
        Scene playerScene = new Scene(mainRoot);
        Scene notifyScene = new Scene(notifyRoot);

        // Display MP3 Player
        stage.setScene(playerScene);
        stage.toFront();
        stage.setResizable(false);
        stage.getIcons().add(new Image(MainRunner.class.getResourceAsStream("/images/icon.png")));
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());

        //Displays Equalizer
        eqStage.setResizable(false);
        eqStage.setY(0);
        eqStage.setX(0);
        eqStage.setScene(eqScene);
        eqStage.toFront();

        //Displays Notification
        notifyStage.setWidth(325);
        notifyStage.initStyle(StageStyle.UNDECORATED);
        notifyStage.setResizable(false);
        notifyStage.setX(screenSize.getWidth());
        notifyStage.setY(40);
        notifyStage.setScene(notifyScene);
        notifyStage.setAlwaysOnTop(true);
        notifyStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public static void setEQStageShow(boolean visible)
    {
        if(visible) { eqStage.show(); }
        else { eqStage.hide();}
    }

    public static void setNotifyStageShow()
    {
        int waitTime = 15;
        int add = 10;

        new Thread(() -> {
            while(notifyStage.getX() >= screenSize.getWidth() - notifyStage.getWidth() - 10)
            {
                notifyStage.setX(notifyStage.getX() - add);
                try
                {
                    Thread.sleep(waitTime);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            try
            {
                Thread.sleep(4000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }

            while(notifyStage.getX() <= screenSize.getWidth())
            {
                notifyStage.setX(notifyStage.getX() + add);
                try
                {
                    Thread.sleep(waitTime);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

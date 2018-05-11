package player.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import player.MainRunner;
import player.utils.SongHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class mp3PlayerController implements Initializable
{
    static SongHandler songHandler = new SongHandler();

    @FXML
    Label songName;

    @FXML
    ImageView image;

    @FXML
    Button playButton;

    @FXML
    ComboBox<String> songBox;

    @FXML
    Slider slider;

    @FXML
    Slider volume;

    @FXML
    Label vLabel;

    @FXML
    public Label runTime;

    @FXML
    Label totalTime;

    private boolean eq = false;

    public void toggle()
    {
        songHandler.toggle();
        updateUI();
        startSlider();
    }

    public void toggleEq()
    {
        MainRunner.setEQStageShow(eq = !eq);
    }

    public void skip()
    {
        songHandler.skipSong();
        updateUI();
        NotificationController.updateUI();
    }

    public void back()
    {
        if(Integer.valueOf(runTime.getText().substring(3)) < 5)
        {
            songHandler.back();
            NotificationController.updateUI();
        }
        else
        {
            songHandler.playSong(songHandler.getThisSongName());
        }
        updateUI();
    }

    public void playSong()
    {
        songHandler.playSong(songBox.getValue());
        NotificationController.updateUI();
        updateUI();
        startSlider();
    }

    public void shuffle()
    {
        songHandler.stop();
        songHandler.shuffle();
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        songBox.setItems(FXCollections.observableArrayList(songHandler.getSongs()));
        songBox.setValue(songBox.getItems().get(0));

        NotificationController.updateUI();
        updateUI();
        startSlider();
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        songBox.setItems(FXCollections.observableArrayList(songHandler.getSongs()));
        songBox.setVisibleRowCount(12);
        volume.setMin(0);
        volume.setMax(100);
        volume.setValue(100);
        volume.setMajorTickUnit(10);
        volume.setMinorTickCount(1);
        volume.setBlockIncrement(10);
        volume.setShowTickLabels(true);
        songHandler.setVolume(volume.getValue());

        volume.valueProperty().addListener(e -> {
            songHandler.setVolume(volume.getValue());
            vLabel.setText("Volume: " + (int) volume.getValue() + "%");
        });

        slider.setMin(0);
        slider.setValue(40);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);

        slider.valueProperty().addListener(e ->
                                           {
//                                               System.out.println(slider.getValue());
//                                               songHandler.setTime(slider.getValue());
//                                               slider.setValue(songHandler.getTime().toSeconds());
//                                               System.out.println(songHandler.getTime().toSeconds());
                                           });

        totalTime.setText(returnTime(songHandler.getRunTime()));
        updateUI();
    }

    private boolean playing = true;

    private void startSlider()
    {
        slider.setMax(songHandler.getRunTime().toSeconds());
        totalTime.setText(returnTime(songHandler.getRunTime()));

        new Thread(() -> {

            while(playing)
            {
                slider.setMax(songHandler.getRunTime().toSeconds());

                Platform.runLater(() -> {
                    runTime.setText(returnTime(songHandler.getTime()));
                    totalTime.setText(returnTime(songHandler.getRunTime()));
                });

                try
                {
                    Thread.sleep(100);
                }
                catch(InterruptedException ex)
                {
                    break;
                }

                slider.setValue((int) songHandler.getTime().toSeconds());
            }
        }).start();
    }

    private String returnTime(Duration duration)
    {
        StringBuilder time = new StringBuilder();

        if(duration.toMinutes() % 60 < 10)
        {
            time.append("0");
            time.append((int) duration.toMinutes() % 60);
        }
        else
        { time.append((int) duration.toMinutes() % 60); }

        time.append(':');

        if(duration.toSeconds() % 60 < 10)
        {
            time.append("0");
            time.append((int) duration.toSeconds() % 60);
        }
        else
        { time.append((int) duration.toSeconds() % 60); }

        if(time.toString().equalsIgnoreCase("0:0"))
        {
            return "00:00";
        }
        else
        {
            return time.toString();
        }
    }

    private void updateUI()
    {
        playButton.setText(songHandler.getStatus());
        songName.setText(songHandler.getThisSongName());
        songHandler.getMediaPlayer().setOnEndOfMedia((this::skip));
        image.setImage(songHandler.getThisAlbumArtwork());

        double ratio = image.getImage().getWidth() / image.getImage().getHeight();

        if(ratio == 1.7777777777777777)
        {
            image.setPreserveRatio(true);
            image.setFitWidth(300);
        }
        else if(ratio == 1.3333333333333333)
        {
            image.setPreserveRatio(false);
            image.setFitWidth(300);
        }

        image.setSmooth(true);
        image.setCache(true);

        songHandler.setVolume(volume.getValue());
        songBox.setValue(songHandler.getThisSongName());
        totalTime.setText(returnTime(songHandler.getRunTime()));
    }


    public void updateOnKey(KeyEvent keyEvent)
    {
        switch(keyEvent.getCode())
        {
            case F7:
                back();
                break;
            case F8:
                toggle();
                break;
            case F9:
                skip();
                break;
            case F10:
                volume.setValue(0);
                songHandler.setVolume(volume.getValue());
                break;
            case F11:
                volume.setValue(volume.getValue() - 10);
                songHandler.setVolume(volume.getValue());
                break;
            case F12:
                volume.setValue(volume.getValue() + 10);
                songHandler.setVolume(volume.getValue());
                break;
        }
    }
}

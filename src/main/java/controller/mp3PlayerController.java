package controller;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.SongHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mp3PlayerController implements Initializable
{
    static SongHandler songHandler = new SongHandler();

    Thread timer;
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

    public void toggle()
    {
        songHandler.toggle();
        System.out.println(songHandler.getStatus());
        updateUI();

        startSlider();
    }

    public void skip()
    {
        songHandler.skipSong();
        updateUI();
    }

    public void back()
    {
        songHandler.back();
        updateUI();
    }

    public void playSong()
    {
        songBox.setValue(songHandler.getThisSongName());
        songHandler.playSong(songBox.getValue());
        updateUI();

        startSlider();
    }

    public void shuffle()
    {
        songHandler.stop();
        songHandler.shuffle();
        songBox.setItems(FXCollections.observableArrayList(songHandler.getSongs()));

        updateUI();
        startSlider();
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        updateUI();
        songBox.setItems(FXCollections.observableArrayList(songHandler.getSongs()));
        songBox.setVisibleRowCount(12);

        slider.setMin(0);
        slider.setValue(40);
//        slider.setShowTickLabels(true);
//        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);

        slider.valueProperty().addListener(e ->
                                           {
//                                               timer.stop();
//                                               System.out.println(slider.getValue());
//                                               songHandler.setTime(slider.getValue());
//                                               slider.setValue(songHandler.getTime().toSeconds());
//                                               System.out.println(songHandler.getTime().toSeconds());
//                                               timer.start();
                                           });
    }


    boolean playing = true;

    public void startSlider()
    {
        slider.setMax(songHandler.getRunTime().toSeconds());
        System.out.println(songHandler.getRunTime().toSeconds());
        new Thread(() -> {

            while(playing)
            {
                slider.setMax(songHandler.getRunTime().toSeconds());
                if(songHandler.getTime().toSeconds() % 1 == 0)
                {

                    slider.setValue((int) songHandler.getTime().toSeconds());

                }
            }
        }).start();
    }

    void updateUI()
    {
        playButton.setText(songHandler.getStatus());
        songName.setText(songHandler.getThisSongName());
        songHandler.getMediaPlayer().setOnEndOfMedia((this::skip));
        image.setImage(songHandler.getThisAlbumArtwork());

        double ratio = image.getImage().getWidth()/image.getImage().getHeight();

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

        songBox.setValue(songHandler.getThisSongName());
    }
}

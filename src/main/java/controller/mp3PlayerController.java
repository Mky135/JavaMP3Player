package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import utils.SongHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class mp3PlayerController implements Initializable
{
    SongHandler songHandler = new SongHandler();

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
        playButton.setText(songHandler.getStatus());
        songName.setText(songHandler.getThisSongName());
        startSlider();
    }

    public void skip()
    {
        songHandler.skipSong();
        playButton.setText(songHandler.getStatus());
        songName.setText(songHandler.getThisSongName());
    }

    public void back()
    {
        songHandler.back();
        playButton.setText(songHandler.getStatus());
        songName.setText(songHandler.getThisSongName());
    }

    public void playSong()
    {
        songHandler.playSong(songBox.getValue());
        playButton.setText(songHandler.getStatus());
        songName.setText(songHandler.getThisSongName());

        startSlider();
    }
    
    public void initialize(URL location, ResourceBundle resources)
    {
        image.setImage(songHandler.getThisAlbumArtwork());
        songName.setText(songHandler.getThisSongName());
        songBox.setItems(FXCollections.observableArrayList(songHandler.getSongs()));


        slider.setMin(0);
        slider.setValue(40);
//        slider.setShowTickLabels(true);
//        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);

        slider.valueProperty().addListener(e->
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

}

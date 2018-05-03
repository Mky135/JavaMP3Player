package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import utils.SongHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class mp3PlayerController implements Initializable
{
    SongHandler songHandler = new SongHandler();

    @FXML
    Label songName;

    @FXML
    ImageView image;

    @FXML
    Button playButton;

    public void play()
    {
        songHandler.play();
        playButton.setText(songHandler.getStatus());
        songName.setText(songHandler.getThisSongName());
    }

    public void skip()
    {
        songHandler.skipSong();
        songName.setText(songHandler.getThisSongName());
    }

    
    public void initialize(URL location, ResourceBundle resources)
    {
        image.setImage(songHandler.getThisAlbumArtwork());
        songName.setText(songHandler.getThisSongName());
    }
}

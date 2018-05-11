package player.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import player.MainRunner;

import java.net.URL;
import java.util.ResourceBundle;

import static player.controller.mp3PlayerController.songHandler;

public class NotificationController implements Initializable
{
    @FXML
    ImageView artwork;

    @FXML
    Label songName;

    @FXML
    Label extras;

    private static ImageView staticArtWork;
    private static Label staticSongName;
    private static Label staticExtras;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        artwork.setImage(songHandler.getThisAlbumArtwork());
        staticArtWork = artwork;
        staticSongName = songName;
        staticExtras = extras;
    }

    public static void updateUI()
    {
        MainRunner.setNotifyStageShow();

        if(songHandler.getThisAlbumArtwork() != null)
            staticArtWork.setImage(songHandler.getThisAlbumArtwork());

        staticSongName.setText(songHandler.getThisSongName());
        staticExtras.setText(songHandler.getArtistName() + " - " + songHandler.getAlbumName());
    }

}

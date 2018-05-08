package player.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

import static player.controller.mp3PlayerController.songHandler;

public class equalizerController implements Initializable
{
    @FXML
    Slider zeroBar;

    @FXML
    Slider oneBar;

    @FXML
    Slider twoBar;

    @FXML
    Slider threeBar;

    @FXML
    Slider fourBar;

    @FXML
    Slider fiveBar;

    @FXML
    Slider sixBar;

    @FXML
    Slider sevenBar;

    @FXML
    Slider eightBar;

    Slider[] sliders;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sliders = new Slider[] { zeroBar, oneBar, twoBar, threeBar, fourBar, fiveBar, sixBar, sevenBar, eightBar};
        addSlidersListener();

    }

    private void addSlidersListener()
    {
        for(int i=0; i<sliders.length; i++)
        {
            int finalI = i;
            sliders[i].valueProperty().addListener(e -> songHandler.setBand(finalI, sliders[finalI].getValue()));
        }
    }
}

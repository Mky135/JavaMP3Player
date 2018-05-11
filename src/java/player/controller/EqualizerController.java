package player.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import player.utils.EqualizerSettings;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static player.controller.mp3PlayerController.songHandler;

public class EqualizerController implements Initializable
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

    @FXML
    CheckBox on;

    @FXML
    ComboBox<String> modeBox;

    public static CheckBox staticOn;

    private Slider[] sliders;

    double[] gains = new double[EqualizerSettings.values().length];

//    private EqualizerSettings manuel = EqualizerSettings.manuel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sliders = new Slider[] { zeroBar, oneBar, twoBar, threeBar, fourBar, fiveBar, sixBar, sevenBar, eightBar };

//        refreshBandsOnTimer();
        for(Slider slider : sliders)
        {
            slider.setDisable(true);
        }
        modeBox.setItems(getNames());

        staticOn = on;
    }

    public void setEq()
    {

//        if(modeBox.getValue().equals("manuel"))
//        {
//            setManuel();
//        }
//        else
//        {
            for(EqualizerSettings settings : EqualizerSettings.values())
            {
                if(settings.getName().equalsIgnoreCase(modeBox.getValue()))
                {
                    gains = settings.getGains();
                    break;
                }
            }
//        }

//        for(Slider slider : sliders)
//        {
//            slider.setDisable(false);
//        }

        for(int i = 0; i < gains.length - 1; i++)
        {
            int finalI = i;
            double gain = gains[i];
            Platform.runLater(() -> sliders[finalI].setValue(gain));
            Platform.runLater(() -> songHandler.setBand(finalI, gain));
        }

        modeBox.setValue(modeBox.getValue());

//        for(Slider slider : sliders)
//        {
//            slider.setDisable(true);
//        }
    }

//    public void setManuel()
//    {
//        modeBox.setValue(manuel.getName());
//        double[] gains = manuel.getGains();
//
//        for(int i = 0; i < gains.length - 1; i++)
//        {
//            gains[i] = sliders[i].getValue();
//        }
//
//        manuel.setGains(gains);
//    }

    private ObservableList<String> getNames()
    {
        ArrayList<String> strings = new ArrayList<>();

        for(EqualizerSettings equalizerSettings : EqualizerSettings.values())
        {
            strings.add(equalizerSettings.getName());
        }

        return FXCollections.observableArrayList(strings);
    }

    private void refreshBandsOnTimer()
    {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            for(int i = 0; i < sliders.length; i++)
            {
                final int finalI = i;
                Platform.runLater(() -> songHandler.setBand(finalI, sliders[finalI].getValue()));
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void toggleEQ()
    {
        songHandler.toggleEQ();
    }
}

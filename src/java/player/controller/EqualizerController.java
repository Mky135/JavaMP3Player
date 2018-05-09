package player.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
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

    private Slider[] sliders;

    private EqualizerSettings manuel = EqualizerSettings.manuel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sliders = new Slider[] { zeroBar, oneBar, twoBar, threeBar, fourBar, fiveBar, sixBar, sevenBar, eightBar };

        addSlidersListener();
        modeBox.setItems(getNames());
    }

    public void setEq()
    {
        double[] gains = manuel.getGains();

        if(modeBox.getValue().equals("manuel"))
        {
            setManuel();
        }
        else
        {
            for(EqualizerSettings settings : EqualizerSettings.values())
            {
                if(settings.getName().equalsIgnoreCase(modeBox.getValue()))
                {
                    gains = settings.getGains();
                    break;
                }
            }
        }

        for(int i = 0; i < gains.length - 1; i++)
        {
            int finalI = i;
            double gain = gains[i];
            Platform.runLater(() -> sliders[finalI].setValue(gain));
            songHandler.setBand(finalI, gain);
        }

        modeBox.setValue(modeBox.getValue());
    }

    public void setManuel()
    {
        modeBox.setValue(manuel.getName());
        double[] gains = manuel.getGains();

        for(int i = 0; i < gains.length-1; i++)
        {
            gains[i] = sliders[i].getValue();
        }

        manuel.setGains(gains);
    }

    private ObservableList<String> getNames()
    {
        ArrayList<String> strings = new ArrayList<>();

        for(EqualizerSettings equalizerSettings : EqualizerSettings.values())
        {
            strings.add(equalizerSettings.getName());
        }

        return FXCollections.observableArrayList(strings);
    }

    private void addSlidersListener()
    {
        for(int i = 0; i < sliders.length; i++)
        {
            int finalI = i;
            sliders[i].valueProperty().addListener(e -> songHandler.setBand(finalI, sliders[finalI].getValue()));
        }
    }

    public void toggleEQ()
    {
        songHandler.toggleEQ();
    }
}

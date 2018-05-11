package player.utils;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class SongHandler
{
    private Mp3File currentSong;
    private ArrayList<File> files;
    private MediaPlayer mediaPlayer;
    private int currentIndex;
    private Status status;
    private double[] gains;
    private boolean on = false;

    public SongHandler()
    {
        files = new ArrayList<>();
        String user = System.getProperty("user.home");
        PlaylistHandler playlistHandler = new PlaylistHandler(user + "/Documents/workspace/JavaMP3Player/src/resources/playlist/Electro Swing.txt");

        files = playlistHandler.getSongs();

        setMediaPlayer(0);
        status = Status.stopped;

        mediaPlayer.getAudioEqualizer().setEnabled(on);
        gains = new double[mediaPlayer.getAudioEqualizer().getBands().size()];
    }

    public void toggleEQ()
    {
        mediaPlayer.getAudioEqualizer().setEnabled(on = !on);
    }

    public void setEQ(boolean on)
    {
        this.on = on;
        mediaPlayer.getAudioEqualizer().setEnabled(on);
    }

    public void setBand(int index, double gain)
    {
        EqualizerBand band = mediaPlayer.getAudioEqualizer().getBands().get(index);
        band.setGain(gain);
        switch(mediaPlayer.getStatus())
        {
            case READY:
                Platform.runLater(() -> mediaPlayer.getAudioEqualizer().getBands().set(index, band));
                break;
            case PLAYING:
                Platform.runLater(() -> mediaPlayer.getAudioEqualizer().getBands().set(index, band));
                break;
            case STOPPED:
                Platform.runLater(() -> mediaPlayer.getAudioEqualizer().getBands().set(index, band));
                break;
            case PAUSED:
                Platform.runLater(() -> mediaPlayer.getAudioEqualizer().getBands().set(index, band));
                break;
        }
    }

    private void listf(String directoryName)
    {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for(File file : Objects.requireNonNull(fList))
        {
            if(file.isFile() && file.getName().contains(".mp3"))
            {
                files.add(file);
            }
            else if(file.isDirectory())
            {
                listf(file.getAbsolutePath());
            }
        }
    }

    private void setCurrentSong(File file)
    {
        try
        {
            currentSong = new Mp3File(file.getPath());
        }
        catch(IOException | UnsupportedTagException | InvalidDataException e)
        {
            e.printStackTrace();
        }
    }

    public void playSong(String songName)
    {
        mediaPlayer.stop();

        for(int i = 0; i < files.size(); i++)
        {
            if(files.get(i).getName().equalsIgnoreCase(songName))
            {
                setCurrentSong(files.get(i));
                setMediaPlayer(i);
                break;
            }
        }

        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
    }

    public void toggle()
    {
        switch(status)
        {
            case paused:
                play();
                break;
            case playing:
                stop();
                break;
            case stopped:
                play();
                break;
        }
    }

    public void skipSong()
    {
        mediaPlayer.stop();
//        setGains(mediaPlayer.getAudioEqualizer().getBands());

        currentIndex++;
        if(currentIndex > files.size() - 1)
        {
            setMediaPlayer(0);
        }
        else
        {
            setMediaPlayer(currentIndex);
        }

//        setMediaPlayerBands();
        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
    }

    public void back()
    {
        mediaPlayer.stop();
        setGains(mediaPlayer.getAudioEqualizer().getBands());

        currentIndex--;
        if(currentIndex >= 0)
        {
            setMediaPlayer(currentIndex);
        }
        else
        {
            currentIndex = 0;
        }

//        setMediaPlayerBands();

        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
    }

    private void setGains(ObservableList<EqualizerBand> bands)
    {
        for(int i = 0; i < bands.size(); i++)
        {
            gains[i] = bands.get(i).getGain();
        }
    }

    private void setMediaPlayerBands()
    {
        System.out.println(mediaPlayer.getAudioEqualizer().getBands().toString());
        mediaPlayer.play();
        try
        {
            Thread.sleep(255);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        mediaPlayer.stop();

        for(int i = 0; i < gains.length; i++)
        {
            setBand(i, gains[i]);
        }

        System.out.println(mediaPlayer.getAudioEqualizer().getBands().toString());
    }

    public void stop()
    {
        status = Status.paused;
        mediaPlayer.pause();
    }

    private void play()
    {
        status = Status.playing;
        mediaPlayer.play();
    }

    public void shuffle()
    {
        ArrayList<File> temp = files;

        Thread thread = new Thread(() -> Collections.shuffle(temp));
        thread.start();

        files = temp;

        setMediaPlayer(0);
        play();
    }

    private void setMediaPlayer(int index)
    {
        currentIndex = index;
        setCurrentSong(files.get(index));
        Media current = new Media(files.get(currentIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(current);
//        mediaPlayer.getAudioEqualizer().setEnabled(on);
    }

    public void setVolume(double value)
    {
        mediaPlayer.setVolume(value / 100);
    }

    public ObservableList<EqualizerBand> getMediaBand()
    {
        return mediaPlayer.getAudioEqualizer().getBands();
    }

    public String getArtistName()
    {
        if(currentSong.getId3v2Tag().getArtist() != null)
        {
            return currentSong.getId3v2Tag().getArtist();
        }
        else
        {
            return "";
        }
    }

    public String getAlbumName()
    {
        if(currentSong.getId3v2Tag().getAlbum() != null)
        {
            return currentSong.getId3v2Tag().getAlbum();
        }
        else
        {
            return "";
        }
    }

    public Image getThisAlbumArtwork()
    {
        try
        {
            return getAlbumArtwork(currentSong);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getThisSongName()
    {
        return files.get(currentIndex).getName();
    }

    private static Image getAlbumArtwork(Mp3File song) throws IOException
    {
        byte[] imageData;
        BufferedImage img;
        Image image = null;

        if(song.hasId3v2Tag())
        {
            ID3v2 id3v2tag = song.getId3v2Tag();
            imageData = id3v2tag.getAlbumImage();
            //converting the bytes to an image
            img = ImageIO.read(new ByteArrayInputStream(imageData));
            image = SwingFXUtils.toFXImage(img, null);
            File outputfile = new File("image.jpg");
            ImageIO.write(img, "jpg", outputfile);
        }

        return image;
    }

    public Status getStatus()
    {
        return status;
    }

    public ArrayList<String> getSongs()
    {
        ArrayList<String> songNames = new ArrayList<>();

        for(File file : files)
        {
            songNames.add(file.getName());
        }

        return songNames;
    }

    public Duration getTime()
    {
        return mediaPlayer.getCurrentTime();
    }

    public Duration getRunTime()
    {
        return mediaPlayer.getTotalDuration();
    }

    public void setTime(double seconds)
    {
//        mediaPlayer.seek(new Duration(6000));
        mediaPlayer.seek(new Duration(seconds / 1000));
    }

    public MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }

    public ArrayList<File> getFiles()
    {
        return files;
    }
}

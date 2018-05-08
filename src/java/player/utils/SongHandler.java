package player.utils;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
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
import java.util.Arrays;
import java.util.Collections;

public class SongHandler
{
    private Mp3File currentSong;
    private ArrayList<File> files;
    private MediaPlayer mediaPlayer;
    private int currentIndex;
    private Status status;
    private ObservableList<EqualizerBand> bands;

    public SongHandler()
    {
        files = new ArrayList<>();
        String user = System.getProperty("user.home");
        String folder = user + "/Music/iTunes/iTunes Media/Music/Unknown Artist/Electro Swing January 2017";
        String folder1 = user + "/Music/iTunes/iTunes Media/Music/";

        listf(folder);
//        listf(folder1);

        files.add(new File("/Users/90308982/Music/iTunes/iTunes Media/Music/Willy Wonka/Unknown Album/Willy Wonka - Pure Imagination (Trap Remix).mp3"));

        setMediaPlayer(0);
        bands = mediaPlayer.getAudioEqualizer().getBands();
        mediaPlayer.getAudioEqualizer().setEnabled(true);
        status = Status.stopped;
    }

    public void setBand(int index, double gain)
    {
        EqualizerBand band = mediaPlayer.getAudioEqualizer().getBands().get(index);
        band.setGain(gain);
        mediaPlayer.getAudioEqualizer().getBands().set(index, band);

    }

    private void listf(String directoryName)
    {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for(File file : fList)
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

        currentIndex++;
        if(currentIndex > files.size() - 1)
        {
            setMediaPlayer(0);
        }
        else
        {
            setMediaPlayer(currentIndex);
        }

        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
    }

    public void back()
    {
        mediaPlayer.stop();

        currentIndex--;

        if(currentIndex >= 0)
        {
            setMediaPlayer(currentIndex);
        }
        else
        {
            currentIndex = 0;
        }

        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
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
    }

    public void setVolume(double value)
    {
        mediaPlayer.setVolume(value / 100);
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
        Image image;

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
        else
        {
            image = null;
        }

        return image;
    }

    public String getStatus()
    {
        return status.Status;
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

    enum Status
    {
        playing("Playing"),
        stopped("Stopped"),
        paused("Paused");

        String Status;

        Status(String state)
        {
            this.Status = state;
        }
    }

}

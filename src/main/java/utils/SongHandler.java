package utils;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import controller.mp3PlayerController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SongHandler
{
    Mp3File currentSong;
    static File[] files;
    static ArrayList<Mp3File> songs;
    Media current;
    MediaPlayer mediaPlayer;
    int currentIndex;
    Status status;

    public SongHandler()
    {
        songs = new ArrayList<Mp3File>();
        String user = System.getProperty("user.home");
        String folder = user + "/Music/iTunes/iTunes Media/Music/Unknown Artist/Electro Swing January 2017";
        File fFolder = new File(folder);
        files = fFolder.listFiles();

        for(File file : files)
        {
            addSongToList(file);
            System.out.println(file.getName());
        }

        currentIndex = 6;
        currentSong = songs.get(currentIndex);
        current = new Media(files[currentIndex].toURI().toString());
        mediaPlayer = new MediaPlayer(current);
        status = Status.stopped;
    }

    public void playSong(String songName)
    {
        mediaPlayer.stop();

        Mp3File song = null;
        for(int i = 0; i < files.length; i++)
        {
            if(files[i].getName().equalsIgnoreCase(songName))
            {
                song = songs.get(i);
                break;
            }
        }
        currentIndex = songs.indexOf(song);
        currentSong = songs.get(currentIndex);
        current = new Media(files[currentIndex].toURI().toString());
        mediaPlayer = new MediaPlayer(current);

        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
    }

    public void play()
    {
        status = Status.playing;
        mediaPlayer.play();
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


    public void stop()
    {
        status = Status.paused;
        mediaPlayer.pause();
    }

    public void skipSong()
    {
        mediaPlayer.stop();

        currentIndex++;
        currentSong = songs.get(currentIndex);
        current = new Media(files[currentIndex].toURI().toString());
        mediaPlayer = new MediaPlayer(current);

        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
    }

    public void back()
    {
        mediaPlayer.stop();

        currentIndex--;
        currentSong = songs.get(currentIndex);
        current = new Media(files[currentIndex].toURI().toString());
        mediaPlayer = new MediaPlayer(current);
        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
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
        return getSongName(currentSong);
    }

    static String getSongName(Mp3File song)
    {
        return files[songs.indexOf(song)].getName();
    }

    static Image getAlbumArtwork(Mp3File song) throws IOException
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

    private void addSongToList(File file)
    {
        try
        {
            songs.add(new Mp3File(file.getPath()));
        }
        catch(IOException | UnsupportedTagException | InvalidDataException e)
        {
            e.printStackTrace();
        }
    }

    public String getStatus()
    {
        return status.Status;
    }

    public ArrayList<String> getSongs()
    {
        ArrayList<String> songNames = new ArrayList<String>();

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
        mediaPlayer.seek(new Duration(seconds/1000));
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

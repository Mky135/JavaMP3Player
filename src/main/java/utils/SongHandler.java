package utils;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

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
import java.util.Collections;

public class SongHandler
{
    Mp3File currentSong;
    static ArrayList<File> files;
    static ArrayList<Mp3File> songs;
    Media current;
    MediaPlayer mediaPlayer;
    int currentIndex;
    Status status;

    public SongHandler()
    {
        songs = new ArrayList<>();
        files = new ArrayList<>();
        String user = System.getProperty("user.home");
        String folder = user + "/Music/iTunes/iTunes Media/Music/Electro Swing/Electro Swing September 2017";
        String folder1 = user + "/Music/iTunes/iTunes Media/Music/";

        listf(folder);
//        listf(folder1);

        files.add(new File("/Users/90308982/Music/iTunes/iTunes Media/Music/Willy Wonka/Unknown Album/Willy Wonka - Pure Imagination (Trap Remix).mp3"));

        for(File file : files)
        {
            addSongToList(file);
//            System.out.println(file.getName());
        }

        currentIndex = 0;
        currentSong = songs.get(currentIndex);
        current = new Media(files.get(currentIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(current);
        status = Status.stopped;
    }

    public void listf(String directoryName)
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

    public void playSong(String songName)
    {
        mediaPlayer.stop();

        Mp3File song = null;
        for(int i = 0; i < files.size(); i++)
        {
            if(files.get(i).getName().equalsIgnoreCase(songName))
            {
                song = songs.get(i);
                break;
            }
        }
        currentIndex = songs.indexOf(song);
        currentSong = songs.get(currentIndex);
        current = new Media(files.get(currentIndex).toURI().toString());
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
        if(currentIndex > songs.size()-1)
        {
            currentIndex = 0;
        }

        currentSong = songs.get(currentIndex);
        current = new Media(files.get(currentIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(current);

        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
    }

    public void back()
    {
        mediaPlayer.stop();

        if(currentIndex > 0)
        {
            currentIndex--;
        }
        currentSong = songs.get(currentIndex);
        current = new Media(files.get(currentIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(current);
        if(status == Status.playing)
        { play(); }
        else
        { status = Status.stopped; }
    }

    public void shuffle()
    {
        ArrayList<File> temp = files;
        Collections.shuffle(temp);
        ArrayList<Mp3File> tempSongs = new ArrayList<>();

        for(File file : temp)
        {
            try
            {
                tempSongs.add(new Mp3File(file.getPath()));
            }
            catch(IOException | UnsupportedTagException | InvalidDataException e)
            {
                e.printStackTrace();
            }
        }

        files = temp;
        songs = tempSongs;

        currentIndex = 0;
        currentSong = songs.get(currentIndex);
        current = new Media(files.get(currentIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(current);
        play();
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
        return getSongName(currentSong);
    }

    static String getSongName(Mp3File song)
    {
        return files.get(songs.indexOf(song)).getName();
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

    private static void addSongToList(File file)
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

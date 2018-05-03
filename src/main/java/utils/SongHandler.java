package utils;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
    String status;

    public static void main(String[] args) throws InvalidDataException, IOException, UnsupportedTagException
    {

    }

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
        status = "stopped";
    }

    public void playSong(Mp3File song)
    {
         currentIndex = songs.indexOf(song);
         currentSong = songs.get(currentIndex);
         current = new Media(files[currentIndex].toURI().toString());
         mediaPlayer = new MediaPlayer(current);
         mediaPlayer.play();
    }

    public void play()
    {
        status = "Playing";
        mediaPlayer.play();
    }

    public void skipSong()
    {
        mediaPlayer.stop();
        currentIndex++;
        currentSong = songs.get(currentIndex);
        current = new Media(files[currentIndex].toURI().toString());
        mediaPlayer = new MediaPlayer(current);
        play();
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
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedTagException e)
        {
            e.printStackTrace();
        }
        catch(InvalidDataException e)
        {
            e.printStackTrace();
        }
    }

    public String getStatus()
    {
        return status;
    }

}

package player.utils;

import java.io.File;
import java.util.ArrayList;

public class PlaylistHandler
{
    private ArrayList<File> songs;

    private FileHandler fileHandler;

    private ArrayList<File> allSongs;

    public PlaylistHandler(String playlist)
    {
        String user = System.getProperty("user.home");
        String folder1 = user + "/Music/iTunes/iTunes Media/Music/";

        songs = new ArrayList<>();
        allSongs = new ArrayList<>();

        listf(folder1);

        fileHandler = new FileHandler(playlist);
        getNames();
    }

    private void getNames()
    {
        String line;
        String user = System.getProperty("user.home");
        int index;

        for(int i = 1; i < fileHandler.getNumLines(); i++)
        {
            line = fileHandler.getLine(i);
            index = line.indexOf(replaceSlashed(user));

            File file = new File(replaceColon(line.substring(index)));

            if(file.getName().contains("..."))
            {
                for(File f : allSongs)
                {
                    if(f.getName().contains(file.getName().substring(0, file.getName().indexOf("..."))))
                    {
                        file = f;
                        break;
                    }
                }
            }

            songs.add(file);
        }
    }

    private String replaceSlashed(String string)
    {
        StringBuilder builder = new StringBuilder();
        for(char c : string.toCharArray())
        {
            if(c == '/')
            {
                builder.append(":");
            }
            else
            {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    private String replaceColon(String string)
    {
        StringBuilder builder = new StringBuilder();
        for(char c : string.toCharArray())
        {
            if(c == ':')
            {
                builder.append('/');
            }
            else
            {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    public String getPlaylistNames()
    {
        StringBuilder builder = new StringBuilder();
        for(File file : songs)
        {
            builder.append(file.getName());
            builder.append("\n");
        }

        return builder.toString();
    }

    public ArrayList<File> getSongs()
    {
        return songs;
    }

    private void listf(String directoryName)
    {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for(File file : fList)
        {
            if(file.isFile() && file.getName().contains(".mp3"))
            {
                allSongs.add(file);
            }
            else if(file.isDirectory())
            {
                listf(file.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args)
    {
        String user = System.getProperty("user.home");
        PlaylistHandler playlistHandler = new PlaylistHandler(user + "/Documents/workspace/JavaMP3Player/Electro Swing.txt");

        System.out.println(playlistHandler.getPlaylistNames());
    }
}

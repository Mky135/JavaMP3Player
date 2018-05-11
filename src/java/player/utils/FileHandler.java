package player.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class to ease the handling of files
 */
public class FileHandler extends File
{
    /**
     * The path to the file
     */
    private final String fileName;

    /**
     * The {@link File} object representing the file
     */
    private File file;

    FileHandler(String fileName)
    {
        super(fileName);
        this.fileName = fileName;
        file = new File(fileName);
    }

    /**
     * Read the whole file of the file at the specified path
     *
     * @return The contents of the file (in a very large string)
     */
    public String readWholeFile() throws IOException
    {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder result = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null)
        {
            result.append(line);
            result.append("\n");
        }
        fileReader.close();

        return result.toString();
    }

    /**
     * Gets a line of file
     *
     * @param line the line number of the line to be returned
     * @return String of the line specified
     */
    public String getLine(int line)
    {
        try
        {
            return Files.readAllLines(Paths.get(fileName)).get(line);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Writes to file
     *
     * @param object object that is been written to the file
     * @param append Whether or not to append to the file
     */
    public void writeToFile(Object object, boolean append) throws IOException
    {
        FileWriter write = new FileWriter(fileName, append);
        write.write(object.toString());
        write.flush();
        write.close();
    }

    /**
     * @return Number of lines in the file
     */
    public int getNumLines()
    {
        int count = 0;
        if(file.exists())
        {
            try
            {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while(bufferedReader.readLine() != null)
                {
                    count++;
                }

                fileReader.close();
            }
            catch(IOException e) {e.printStackTrace();}
        }

        return count;
    }

    /**
     * Builds up the file, looking for the oldLine and replacing it with the newLine
     * @param oldLine line that is being replaced
     * @param newLine line that is being substituted in
     */
    public void modifyLine(String oldLine, String newLine)
    {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        FileWriter writer = null;

        try
        {
            reader = new BufferedReader(new FileReader(file));

            //Builds up the file looking for oldLine and replacing it with newLine
            for(int i=0; i<getNumLines(); i++)
            {
                if(getLine(i).equalsIgnoreCase(oldLine))
                    stringBuilder.append(newLine + "\n");
                else
                    stringBuilder.append(getLine(i) + "\n");
            }

            //Rewriting the input text file with newContent
            writer = new FileWriter(file);
            writer.write(stringBuilder.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            //Closing the resources
            reader.close();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
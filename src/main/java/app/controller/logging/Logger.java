package app.controller.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger
{
    private String directoryPath;
    private String fileName;
    private String delimiter = ",";
    private boolean CSV = false;
    private boolean append = true;
    private boolean prependDateTime = false;

    /**
     * Default constructor path set to projectRoot/log/
     */
    public Logger()
    {
        this.directoryPath = FilePath.getInternal("/logs/");
        this.fileName = "log";
        createDirectory();
    }

    /**
     * Set different logging location using the internal filePath from the project root
     * @param fileName - "log"
     */
    public Logger(String fileName)
    {
        this.directoryPath = FilePath.getInternal("/logs/");
        this.fileName = stripFileType(fileName);
        createDirectory();
    }

    /**
     * Set different logging location using the internal filePath from the project root
     * @param internalFilePath - "/src/main/java/src/config/"
     * @param fileName - "log"
     */
    public Logger(String internalFilePath, String fileName)
    {
        internalFilePath = validatePath(internalFilePath);
        this.directoryPath = FilePath.getInternal(internalFilePath);
        this.fileName = stripFileType(fileName);
        createDirectory();
    }

    public void createDirectory()
    {
        File file = new File(directoryPath + fileName);
        if(!file.exists() || !file.isDirectory())
        {
            boolean success = file.mkdirs();
            if (!success)
                throw new RuntimeException("Logging directory was not created");
        }
    }

    /**
     * Append a string to a new line of the {@code log.txt} file
     * Creates a new file if one cannot be found
     * @param str		- The string to append
     */
    public void log(String str)
    {
        if(prependDateTime)
        {
            str = prependNow(str);
        }
        try
        {
            FileWriter writer = getFileWriter();
            assert writer != null;
            writer.write(str + "\n");
            writer.close();
        }
        catch (IOException e)
        {
            printExceptionMessage();
            e.printStackTrace();
        }
    }

    /**
     * Append a string array to a new line of a {@code .csv} file
     * Creates a new file if one cannot be found
     * @param str		- The array of strings to append with comma delimiters
     */
    public void log(String[] str)
    {
        String delim = getDelimiter();
        try
        {
            FileWriter writer = getFileWriter();
            assert writer != null;
            StringBuilder sb = new StringBuilder();

            if(prependDateTime)
            {
                writer.write(prependNow(""));
                sb.append(prependNow(""));
            }
            for (String s : str)
            {
                writer.write(s + delim);
                sb.append(s).append(delim);
            }
            writer.close();
        }
        catch (IOException e)
        {
            printExceptionMessage();
            e.printStackTrace();
        }
    }

    /**
     * Append an integer array to a new line of a {@code .csv} file
     * Creates a new file if one cannot be found
     * @param I		- The array of integers to append with comma delimiters
     */
    public void log(int[] I)
    {
        String delim = getDelimiter();
        try
        {
            FileWriter writer = getFileWriter();
            assert writer != null;
            StringBuilder sb = new StringBuilder();

            if(prependDateTime)
            {
                String time = now() + delim;
                writer.write(time);
                sb.append(time);
            }
            for (int i : I)
            {
                writer.write(i + delim);
                sb.append(i).append(delim);
            }
            writer.close();
        }
        catch (IOException e)
        {
            printExceptionMessage();
            e.printStackTrace();
        }
    }

    /**
     * Append an integer array to a new line of a {@code .csv} file
     * Creates a new file if one cannot be found
     * @param I		- The array of integers to append with comma delimiters
     */
    public void log(String s, int[] I)
    {
        String delim = getDelimiter();
        try
        {
            FileWriter writer = getFileWriter();
            assert writer != null;
            StringBuilder sb = new StringBuilder();

            if(prependDateTime)
            {
                String time = now() + delim;
                writer.write(time);
                sb.append(time);
            }

            writer.write(s + delim);
            sb.append(s).append(delim);

            for (int i : I)
            {
                writer.write(i + delim);
                sb.append(i).append(delim);
            }

            writer.write("\n");
            writer.close();
        }
        catch (IOException e)
        {
            printExceptionMessage();
            e.printStackTrace();
        }
    }

    /**
     * Append a double array to a new line of a {@code .csv} file
     * Creates a new file if one cannot be found
     * @param D		- The array of doubles to append with comma delimiters
     */
    public void log(double[] D)
    {
        String delim = getDelimiter();
        try
        {
            FileWriter writer = getFileWriter();
            assert writer != null;
            StringBuilder sb = new StringBuilder();

            if(prependDateTime)
            {
                writer.write(prependNow(delim));
                sb.append(prependNow(delim));
            }
            for (double d : D)
            {
                writer.write(d + delim);
                sb.append(d).append(delim);
            }
            writer.close();
            System.out.println(sb);
        }
        catch (IOException e)
        {
            printExceptionMessage();
            e.printStackTrace();
        }
    }


    public void setOutputCsv()
    {
        CSV = true;
    }

    public void setOutputTxt()
    {
        CSV = false;
    }

    public void setPrependDateTime(boolean bool)
    {
        prependDateTime = bool;
    }

    public void setDirectoryPath(String directoryPath)
    {
        directoryPath = validatePath(directoryPath);
        this.directoryPath = directoryPath;
    }

    public void setFileName(String fileName)
    {
        this.fileName = stripFileType(fileName);
    }

    private FileWriter getFileWriter()
    {
        String filePath = directoryPath + fileName;
        if(CSV)
            filePath = filePath + ".csv";
        else
            filePath = filePath + ".txt";
        File file = new File(filePath);

        try {
            if (!file.exists())
            {
                boolean success = file.createNewFile();
                if (!success)
                    throw new RuntimeException("File: " + fileName + " was not created");
            }
            return new FileWriter(file, append);
        }  catch(IOException e) {
            System.out.println("File not found");
            System.out.println("File path used: " + filePath);
            e.printStackTrace();
        }
        return null;
    }

    private void printExceptionMessage()
    {
        System.out.println("unable to log to csv" +
                "\nPath: " + directoryPath +
                "\nName: " + fileName);
    }

    private String validatePath(String path)
    {
        if(!path.startsWith("/"))
            path = "/" + path;
        if(!path.endsWith("/"))
            path = path + "/";
        return path;
    }

    private String stripFileType(String fileName)
    {
        if(fileName.endsWith(".txt") || fileName.endsWith(".csv"))
            fileName = fileName.substring(0, fileName.length()-4);
        return fileName;
    }

    private String prependNow(String str)
    {
        return "[" + now() + "]" + str;
    }

    private String now()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    private String getDelimiter()
    {
        if(CSV)
            return delimiter;
        return "";
    }

    public void setDelimiter(String delim)
    {
        delimiter = delim;
    }
}
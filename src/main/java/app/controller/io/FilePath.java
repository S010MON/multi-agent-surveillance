package app.controller.io;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Locale;

public abstract class FilePath
{
    private static final String internalPathUnix = "/src/main/resources/";
    private static final String internalPathWin = "/src/main/resources/";
    private static final String internalPathMac = "/src/main/resources/";

    /**
     * Creates a file path to saved games with the correct file name appended
     */
    public static String getFilePath(String fileName)
    {
        FileSystem fileSystem = FileSystems.getDefault();
        String path = fileSystem.getPath("").toAbsolutePath().toString();
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("nix") || os.contains("nux") || os.contains("aix"))
            return path.concat(internalPathUnix + fileName);
        if(os.contains("mac"))
            return path.concat(internalPathMac + fileName);
        if(os.contains("win"))
            return path.concat(internalPathWin + fileName);
        else throw new RuntimeException();
    }
}
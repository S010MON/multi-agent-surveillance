package app.controller.logging;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public abstract class FilePath
{

    public static String getRoot(String fileName)
    {
        FileSystem fileSystem = FileSystems.getDefault();
        String path = fileSystem.getPath("").toAbsolutePath().toString();
        return path.concat(fileName);
    }

    public static String getRoot()
    {
        return getRoot("");
    }

    public static String getInternalWithFilename(String internalFilePath, String fileName)
    {
        FileSystem fileSystem = FileSystems.getDefault();
        String path = fileSystem.getPath("").toAbsolutePath().toString();
        return path.concat(internalFilePath + fileName);
    }

    public static String getInternal(String internalFilePath)
    {
        return getInternalWithFilename(internalFilePath, "");
    }
}

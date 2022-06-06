package app.model.agents.nNet;

import app.controller.io.FilePath;
import deepnetts.util.Tensor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TensorSaver
{
    public static void save(String fileName, Tensor tensor) throws IOException
    {
        Integer col = tensor.getCols();
        Integer row = tensor.getRows();
        clear(fileName);
        write(fileName, col.toString());
        write(fileName, row.toString());

        for(float entry : tensor.getValues())
        {
            write(fileName, Float.toString(entry));
        }
    }

    public static void write(String fileName, String data)
    {
        try
        {
            String filePath = FilePath.get(fileName);
            File file = new File(filePath);

            if (!file.exists())
                file.createNewFile();

            FileWriter writer = new FileWriter(file,true);
            writer.write(data + "\n");
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Unable to save file");
        }
    }

    public static void clear(String fileName)
    {
        try
        {
            String filePath = FilePath.get(fileName);
            File file = new File(filePath);
            if (file.exists())
                file.delete();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Unable to clear file");
        }
    }
}

package app.model.agents.nNet;

import app.controller.io.FilePath;
import deepnetts.util.Tensor;

import java.io.*;
import jogging.Logger;

public class TensorLoader
{
    public static Tensor load(String fileName) throws IOException
    {
        String path = FilePath.get(fileName);
        File file = new File(path);
        int cols = getCols(file);
        int rows = getRows(file);
        float[] data = readTensor(file);
        Tensor ret = new Tensor(rows,cols);
        ret.setValues(data);
        return ret;
    }

    private static int getCols(File file) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int cols = Integer.parseInt(reader.readLine());
        int rows = Integer.parseInt(reader.readLine());
        reader.close();
        return cols;
    }

    private static int getRows(File file) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int cols = Integer.parseInt(reader.readLine());
        int rows = Integer.parseInt(reader.readLine());
        reader.close();
        return rows;
    }

    private static float[] readTensor(File file) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int cols = Integer.parseInt(reader.readLine());
        int rows = Integer.parseInt(reader.readLine());
        float[] tensor = new float[cols*rows];

        for(int i = 0; i < tensor.length; i++)
        {
            tensor[i] = Float.parseFloat(reader.readLine());
        }
        reader.close();
        return tensor;
    }
}
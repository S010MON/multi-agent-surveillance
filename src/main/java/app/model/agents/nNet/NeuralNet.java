package app.model.agents.nNet;

import app.controller.graphicsEngine.Ray;
import app.controller.io.FilePath;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.AgentImp;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.NeuralNetwork;
import deepnetts.net.layers.AbstractLayer;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.util.FileIO;
import deepnetts.util.Tensor;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NeuralNet extends AgentImp implements Comparable
{
    @Getter private FeedForwardNetwork neuralNet;
    @Getter private String saveName = FilePath.get("nNet/full/network.json");
    @Getter private NetworkType networkType = NetworkType.FULL;
    @Getter private String resourcePath = "nNet/full/";
    private static int inputsNum = 360;
    private static int outputsNum = 8;
    private static int hiddenSize = 2 * (int) Math.round(Math.sqrt(inputsNum*outputsNum));
    @Getter @Setter private double score = 0;

    public NeuralNet(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        try
        {
            this.neuralNet = (FeedForwardNetwork) FileIO.createFromJson(new File(saveName));
            NetworkManager.fillNN(this);
        }
        catch(Exception exception)
        {
            System.out.println(saveName + " not found, generating new network");
            this.neuralNet = buildNN();
        }
    }

    public NeuralNet(FeedForwardNetwork n)
    {
        super(new Vector(), new Vector(), 10, Type.INTRUDER);
        this.neuralNet = n;
    }

    public NeuralNet(FeedForwardNetwork n, NetworkType type)
    {
        super(new Vector(), new Vector(), 10, Type.INTRUDER);
        this.neuralNet = n;
        this.resourcePath = NetworkType.getPath(type);
    }

    @Override
    public Move move()
    {
        float[] input = gatherInput();
        float[] pred = neuralNet.predict(input);

        Vector dir = decode(pred[0], pred[1], pred[2], pred[3]);
        Vector pos = decode(pred[4], pred[5], pred[6], pred[7]).scale(maxSprint);
        return new Move(dir, pos);
    }

    private float[] gatherInput()
    {
        float[] data = new float[360];

        for(int i = 0; i < data.length; i++)
            data[i] = -1;

        for(Ray r: view)
        {
            int index = (int) Math.round(r.angle());
            if(index >= 360)
                index = 0;
            data[index] = (float) r.length();
        }

        return normalise(data);
    }

    private Vector decode(float x_pos, float x_neg, float y_pos, float y_neg)
    {
        double x = 0;
        if(x_pos > x_neg)
            x = 1;
        else if(x_pos < x_neg)
            x = -1;

        double y = 0;
        if(y_pos > y_neg)
            y = 1;
        else if(y_pos < y_neg)
            y = -1;

        return new Vector(x, y);
    }

    public void mutate(double alpha, double epsilon)
    {
        int layerID = 0;
        List<AbstractLayer> layers = neuralNet.getLayers();

        for(AbstractLayer layer : layers)
        {
            if(layerID != 0)
            {
                layer.setWeights(mutate(layer.getWeights(), alpha, epsilon));
                layer.setBiases(mutate(layer.getBiases(), alpha, epsilon));
            }
            layerID++;
        }
    }

    private Tensor mutate(Tensor tensor, double alpha,  double epsilon)
    {
        Tensor t = tensor.copy();
        for(int r = 0; r < t.getRows(); r++)
        {
            for(int c = 0; c < t.getCols(); c++)
            {
                t.set(r, c, mutate(t.get(r, c), alpha, epsilon));
            }
        }
        return t;
    }

    private float[] mutate(float[] f, double alpha,  double epsilon)
    {
        float[] m = new float[f.length];
        for(int i = 0; i< f.length; i++)
        {
            m[i] = mutate(f[i], alpha, epsilon);
        }
        return m;
    }

    private float mutate(float f, double alpha, double epsilon)
    {
        if(Math.random() < alpha)
        {
            if(Math.random() > 0.5)
            {
                return f + (float) (Math.random() * epsilon);
            }
            else
            {
                return f + (float) (Math.random() * epsilon);
            }
        }
        return f;
    }

    public NeuralNet breed(NeuralNet other)
    {
        FeedForwardNetwork copy = buildNN();
        List<AbstractLayer> layers_copy = copy.getLayers();
        List<AbstractLayer> layers_this = neuralNet.getLayers();
        List<AbstractLayer> layers_other = other.getNeuralNet().getLayers();

        for(int i = 1; i < layers_copy.size(); i++)
        {
            Tensor weights = crossPollinate(layers_this.get(i).getWeights(), layers_other.get(i).getWeights());
            layers_copy.get(i).setWeights(weights);

            float[] biases = crossPollinate(layers_this.get(i).getBiases(), layers_other.get(i).getBiases());
            layers_copy.get(i).setBiases(biases);
        }
        return new NeuralNet(copy);
    }

    public Tensor crossPollinate(Tensor t1, Tensor t2)
    {
        Tensor out = t1.copy();

        for(int c = 0; c < t1.getCols(); c++)
        {
            for(int r = 0; r < t1.getRows(); r++)
            {
                if(Math.random() > 0.5)
                    out.set(r, c, t2.get(r, c));
            }
        }
        return out;
    }

    public float[] crossPollinate(float[] b1, float[] b2)
    {
        float[] out = new float[b1.length];

        for(int i = 0; i < b1.length; i++)
        {
            if(Math.random() > 0.5)
                out[i] = b1[i];
            else
                out[i] = b2[i];
        }
        return out;
    }

    public void save()
    {
        try
        {
            FileIO.writeToFileAsJson(neuralNet, saveName);
            NetworkManager.save(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static FeedForwardNetwork buildNN()
    {
        return FeedForwardNetwork.builder()
                                 .addInputLayer(inputsNum)
                                 .addFullyConnectedLayer(hiddenSize,ActivationType.RELU)
                                 .addFullyConnectedLayer(hiddenSize,ActivationType.RELU)
                                 .addFullyConnectedLayer(hiddenSize,ActivationType.RELU)
                                 .addOutputLayer(outputsNum, ActivationType.TANH)
                                 .lossFunction(LossType.CROSS_ENTROPY)
                                 .randomSeed(123)
                                 .build();
    }

    public NeuralNet copy()
    {
        FeedForwardNetwork copy = buildNN();

        List<AbstractLayer> layers_original = neuralNet.getLayers();
        List<AbstractLayer> layers_copy = copy.getLayers();

        for(int i = 1; i < layers_original.size(); i++)
        {
            layers_copy.get(i).setWeights(layers_original.get(i).getWeights());
            layers_copy.get(i).setBiases(layers_original.get(i).getBiases());
        }

        return new NeuralNet(copy);
    }

    @Override public int compareTo(Object o)
    {
        if(o instanceof NeuralNet)
        {
            NeuralNet other = (NeuralNet) o;
            if(other.getScore() > this.getScore())
                return 1;
            if(this.getScore() > other.getScore())
                return -1;
        }
        return 0;
    }
}
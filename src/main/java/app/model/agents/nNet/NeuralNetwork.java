package app.model.agents.nNet;

import app.controller.io.FilePath;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.AgentImp;
import deepnetts.data.DataSets;
import deepnetts.data.MLDataItem;
import deepnetts.data.TabularDataSet;
import deepnetts.eval.Evaluators;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.net.train.opt.OptimizerType;
import deepnetts.util.FileIO;
import lombok.Getter;

import javax.visrec.ml.data.Column;
import javax.visrec.ml.data.DataSet;
import javax.visrec.ml.eval.EvaluationMetrics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NeuralNetwork extends AgentImp
{
    @Getter private FeedForwardNetwork neuralNet;
    private String nNetSaveName = "networkSave.json";
    private String nNetSavePath = "src/main/java/app/model/agents/nNet/";

    public NeuralNetwork(Vector position, Vector direction, double radius, Type type) throws IOException
    {
        super(position, direction, radius, type);
        try
        {
            this.neuralNet = (FeedForwardNetwork) FileIO.createFromJson(new File(nNetSavePath));
            NetworkManager.fillNN(this);
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
            this.train();
            NetworkManager.fillNN(this);
        }
    }

    @Override
    public Move move()
    {
        float[] input = {0.0f};
        float[] prediction = neuralNet.predict(input);

        Vector dir = new Vector(prediction[0], prediction[1]);
        Vector pos = new Vector(prediction[2], prediction[3]);
        return new Move(dir, pos);
    }

    public void train()
    {
        int inputsNum = 360 * 12;
        int outputsNum = 4;
        try
        {
            String filePath = FilePath.get("data.csv");
            TabularDataSet<MLDataItem> data = DataSets.readCsv(filePath, inputsNum, outputsNum,true);
            data.shuffle();
            TabularDataSet[] split = trainTestSplit(data,0.3f,inputsNum,outputsNum);
            TabularDataSet trainData = split[0];
            TabularDataSet testData = split[1];
            int hiddenSize = 2 * (int) Math.round(Math.sqrt(inputsNum*outputsNum));
            if(neuralNet == null)
                buildNN(inputsNum,hiddenSize,outputsNum);
            optimizeTrainer(neuralNet,testData);
            neuralNet.train(trainData);
            printMetrics(trainData,testData);
            FileIO.writeToFileAsJson(neuralNet,nNetSavePath);
            NetworkManager.save(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void buildColumns(TabularDataSet dataSet, int numInputs, int numOutputs)
    {
        ArrayList<Column> cols = new ArrayList<>();
        for(int in = 0; in < numInputs; in++)
        {
            Column temp = new Column("i"+in,Column.Type.DECIMAL,false);
            cols.add(temp);
        }
        for(int out = 0; out < numOutputs; out++)
        {
            Column temp = new Column("o"+out,Column.Type.BINARY,true);
            cols.add(temp);
        }
        dataSet.setColumns(cols);
    }

    private void buildNN(int inputs, int hiddensize, int outputs)
    {
        this.neuralNet = FeedForwardNetwork.builder()
                                           .addInputLayer(inputs)
                                           .addFullyConnectedLayer(hiddensize,ActivationType.RELU)
                                           .addFullyConnectedLayer(hiddensize,ActivationType.RELU)
                                           .addFullyConnectedLayer(hiddensize,ActivationType.RELU)
                                           .addOutputLayer(outputs, ActivationType.SOFTMAX)
                                           .lossFunction(LossType.CROSS_ENTROPY)
                                           .randomSeed(123)
                                           .build();
    }

    private TabularDataSet[] trainTestSplit(TabularDataSet dataSet,float testSize,int inputs, int outputs)
    {
        DataSet<MLDataItem>[] dataSplit = dataSet.split(1.0-testSize,testSize);
        TabularDataSet<MLDataItem> trainData = (TabularDataSet) dataSplit[0];
        TabularDataSet<MLDataItem> testData = (TabularDataSet) dataSplit[1];
        buildColumns(trainData,inputs,outputs);
        buildColumns(testData,inputs,outputs);
        return new TabularDataSet[]{trainData,testData};
    }

    private void optimizeTrainer(FeedForwardNetwork n, TabularDataSet testDataSet)
    {
        BackpropagationTrainer trainer = n.getTrainer();
        trainer.setOptimizer(OptimizerType.MOMENTUM);
        trainer.setShuffle(true);
        trainer.setTestSet(testDataSet);
        trainer.setMaxEpochs(200);
        trainer.setEarlyStopping(true);
        trainer.setEarlyStoppingPatience(2);
    }

    private void printMetrics(TabularDataSet trainData, TabularDataSet testData)
    {
        System.out.println("---------TRAIN DATA--------");
        EvaluationMetrics em = Evaluators.evaluateClassifier(neuralNet,trainData);
        System.out.println(em);
        System.out.println("---------TEST DATA--------");
        EvaluationMetrics em2 = Evaluators.evaluateClassifier(neuralNet,testData);
        System.out.println(em2);
    }
}
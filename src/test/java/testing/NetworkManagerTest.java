package testing;

import app.model.agents.nNet.NetworkManager;
import app.model.agents.nNet.NetworkType;
import app.model.agents.nNet.NeuralNet;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkManagerTest
{
    @Test void fillTest() throws IOException
    {
        FeedForwardNetwork n = new FeedForwardNetwork.Builder()
                .addInputLayer(3)
                .addFullyConnectedLayer(50, ActivationType.RELU)
                .addOutputLayer(1,ActivationType.SIGMOID)
                .randomSeed(111)
                .build();
        NeuralNet net1 = new NeuralNet(n, NetworkType.TEST);

        float[] inputs = {3,4,1};
        float prediction = n.predict(inputs)[0];
        NetworkManager.save(net1);
        NetworkManager.fillNN(net1);

        FeedForwardNetwork n2 = new FeedForwardNetwork.Builder()
                .addInputLayer(3)
                .addFullyConnectedLayer(50, ActivationType.RELU)
                .addOutputLayer(1,ActivationType.SIGMOID)
                .randomSeed(999)
                .build();
        NeuralNet net2 = new NeuralNet(n2, NetworkType.TEST);
        NetworkManager.fillNN(net2);
        n2 = net2.getNeuralNet();
        float prediction2 = n2.predict(inputs)[0];
        assertEquals(prediction,prediction2);
    }
}
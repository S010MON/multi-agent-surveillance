package app.model.agents.nNet;

import app.controller.GameEngine;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.Type;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class GeneticAlgorithm extends GameEngine
{
    public static final double alpha = 0.01;
    public static final double epsilon = 0.01;
    public static final double population = 100;
    public static final boolean VERBOSE = false;
    public static int maxTics = 5;

    public static void main(String[] args)
    {
        Settings settings = FileManager.loadSettings("src/main/resources/genetic_algorithm_1");
        ArrayList<NeuralNet> generation = init();

        for(int i = 0; i < 1000; i++)
        {
            System.out.print("Gen " + i + ":  computing");
            generation = select(generation, settings);
            System.out.println("\rGen " + i + ": " + generation.get(0).getScore() + "% in " + maxTics + " tics");
            generation.get(0).save();

            generation = breed(generation);

            if(i % 10 == 0)
                maxTics += 2;
        }
    }

    public static ArrayList<NeuralNet> init()
    {
        ArrayList<NeuralNet> generation = new ArrayList<>();
        generation.add(new NeuralNet(new Vector(), new Vector(), 10, Type.INTRUDER));
        generation.get(0).save();

        for(int i = 1; i < population; i++)
        {
            generation.add(new NeuralNet(NeuralNet.buildNN()));
        }

        return generation;
    }

    public static ArrayList<NeuralNet> select(ArrayList<NeuralNet> population, Settings settings)
    {
        PriorityQueue<NeuralNet> heap = new PriorityQueue<>();
        for(NeuralNet baby: population)
        {
            Map map = new Map(settings);
            map.addAgent(baby);
            GeneticAlgorithm ga = new GeneticAlgorithm(map);
            baby.setScore(ga.runMap());
            heap.add(baby);
        }

        ArrayList<NeuralNet> topTen = new ArrayList<>();
        int cutoff = (int) Math.round(population.size() * (0.1));
        for(int i = 0; i < cutoff; i++)
        {
            topTen.add(heap.poll());
        }
        return topTen;
    }

    public static ArrayList<NeuralNet> breed(ArrayList<NeuralNet> parents)
    {
        ArrayList<NeuralNet> children = new ArrayList<>();

        for(int i = 0; i < parents.size(); i++)
        {
            for(int j = 0; j < parents.size(); j++)
            {
                if( i == j)
                    children.add(parents.get(i));
                else
                    children.add(parents.get(i).breed(parents.get(j)));
            }
        }

        children.forEach(e -> e.mutate(alpha, epsilon));
        return children;
    }

    public static ArrayList<NeuralNet> divide(ArrayList<NeuralNet> parents)
    {
        ArrayList<NeuralNet> children = new ArrayList<>();

        for(NeuralNet parent: parents)
        {
            for(int i = 0; i < 10; i ++)
            {
                if(i == 0)
                    children.add(parent);
                else
                {
                    NeuralNet child = parent.copy();
                    child.mutate(alpha, epsilon);
                    children.add(child);
                }
            }
        }
        return children;
    }

    //##################################################################################################################

    public GeneticAlgorithm(Map map)
    {
        super(map);
    }

    public double runMap()
    {
        double prevPercentage = 0;
        double currentPercentage = 0;

        while(!complete() && tics < maxTics )
        {
            tick();
            currentPercentage = map.percentageComplete(Type.INTRUDER);

            if(VERBOSE)
            {
                if(currentPercentage != prevPercentage)
                {
                    updatePercentageBar(currentPercentage);
                    prevPercentage = currentPercentage;
                }
            }
        }

        if(VERBOSE) System.out.println(" " + tics);

        return currentPercentage;
    }

    protected boolean complete()
    {
        return  map.percentageComplete(Type.INTRUDER) > 0.85;
    }

    protected void updatePercentageBar(double percent)
    {
        StringBuilder bar = new StringBuilder();
        for(double d = 0; d < percent; d += 0.01)
        {
            bar.append("#");
        }
        bar.append(" ").append(percent).append("%");
        System.out.print("\r" + bar);
    }
}

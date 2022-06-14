package app.model.agents.nNet;

import app.controller.GameEngine;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.RandomSettingsGenerator;
import app.controller.settings.Settings;
import app.controller.settings.SettingsGenerator;
import app.model.Map;
import app.model.Type;
import jogging.Logger;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class GeneticAlgorithm extends GameEngine
{
    public static final double alpha = 0.01;
    public static final double epsilon = 0.001;
    public static final double population = 100;
    public static final boolean VERBOSE = false;
    public static int maxTics = 10;
    public static long tics_achieved = 0;

    public static void main(String[] args)
    {
        Logger logger = new Logger("GA_training");
        ArrayList<NeuralNet> generation = init();
        double bestScore = 0;
        long bestTics = 0;

        for(int i = 0; i < 1000; i++)
        {
            generation = select(generation, RandomSettingsGenerator.generateRandomSettings());
            logger.log("Gen " + i + "," + generation.get(0).getScore() + "," + tics_achieved);

            if(generation.get(0).getScore() > bestScore && tics_achieved > bestTics)
            {
                for(int j = 0; j < 10; j++)
                {
                    generation.get(j).save("net_" + j);
                }
                bestScore = generation.get(0).getScore();
                bestTics = tics_achieved;
            }
            generation = breed(generation);
            maxTics++;
        }

    }

    public static ArrayList<NeuralNet> init()
    {
        ArrayList<NeuralNet> generation = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            generation.add(new NeuralNet("net_" + i));
        }

        generation = breed(generation);

        return generation;
    }

    public static ArrayList<NeuralNet> select(ArrayList<NeuralNet> population, Settings settings)
    {
        PriorityQueue<NeuralNet> heap = new PriorityQueue<>();
        settings.setNoOfGuards(0);
        settings.setNoOfIntruders(0);
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
        boolean dead = false;
        double prevPercentage = 0;
        double currentPercentage = 0;
        double distanceTravelled = 0;
        Vector startPt = map.getAgents().get(0).getPosition();
        Vector prevPos = map.getAgents().get(0).getPosition();


        do {
            tick();
            currentPercentage = map.percentageComplete(Type.INTRUDER);
            distanceTravelled += prevPos.dist(map.getAgents().get(0).getPosition());

            dead = dead(prevPos, map);
            prevPos = map.getAgents().get(0).getPosition();

            if(VERBOSE)
            {
                if(currentPercentage != prevPercentage)
                {
                    updatePercentageBar(currentPercentage);
                    prevPercentage = currentPercentage;
                }
            }
        }
        while(!dead && tics < maxTics && !complete(map));

        double finDist = startPt.dist(map.getAgents().get(0).getPosition());
        double score = (3*tics/maxTics + currentPercentage)/4;

        if(VERBOSE) System.out.println(" travelled " + finDist + " in " + tics + " tics: " + score);

        tics_achieved = tics;
        return score;
    }

    protected boolean dead(Vector prevPos, Map map)
    {
        return prevPos.equals(map.getAgents().get(0).getPosition());
    }

    protected boolean complete(Map map)
    {
        return map.percentageComplete(Type.INTRUDER) >= 0.85;
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

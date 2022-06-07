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
    public static final double epsilon = 0.001;
    public static final double population = 100;
    public static final boolean VERBOSE = false;

    public static void main(String[] args)
    {
        Settings settings = FileManager.loadSettings("src/main/resources/genetic_algorithm_1");
        NeuralNet best = new NeuralNet(new Vector(), new Vector(), 10, Type.GUARD);

        for(int i = 0; i < 10000; i++)
        {
            best = run(best, settings);
            System.out.println("Generation " + i + " best score: " + best.getScore());
            best.save();
        }
    }

    public static NeuralNet run(NeuralNet parent, Settings settings)
    {
        // 1) Spawn Babies.
        ArrayList<NeuralNet> babies = new ArrayList<>();
        babies.add(parent.copy());

        for(int i = 0; i < population; i++)
        {
            NeuralNet mutatedBaby = parent.copy();
            mutatedBaby.mutate(alpha, epsilon);
            babies.add(mutatedBaby);
        }

        // 2) for each Baby:
        PriorityQueue<NeuralNet> heap = new PriorityQueue<>();
        for(NeuralNet baby: babies)
        {
            Map map = new Map(settings);
            map.addAgent(baby);
            GeneticAlgorithm ga = new GeneticAlgorithm(map);
            baby.setScore(ga.runMap());
            heap.add(baby);
        }

        // 3) Select top Baby
        return heap.poll();
    }

    public GeneticAlgorithm(Map map)
    {
        super(map);
    }

    public double runMap()
    {
        double prevPercentage = 0;
        double currentPercentage = 0;

        while(!complete() && tics < 100 )
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
        return  map.percentageComplete(Type.GUARD) > 0.85;
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

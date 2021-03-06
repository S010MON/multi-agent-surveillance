package app.controller.soundEngine;

import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import app.controller.soundEngine.SoundVector;
import app.model.agents.Agent;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SoundSource
{
    @Getter private Vector position;
    @Getter private double amplitude;                                                 /** How loud the sound is in dB */
    @Getter private double frequency;                                                 /** The sound's frequency in hz */
    @Setter @Getter private ArrayList<SoundRay> rays;

    public SoundSource(Vector position, double amplitude, double frequency)
    {
        this.position = position;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.rays = new ArrayList<>();
    }

    /**
     * Conducts a breadth first search of the rays (held in a tree form) to find any intersection between the sound
     * and the agent provided.
     * @param agent
     * @return a {@code SoundVector} which gives direction, amplitude and frequency of any sounds heard.  Returns
     * {@code null} if no intersection is found.
     */
    public ArrayList<SoundVector> heard(Agent agent)
    {
        ArrayList<SoundVector> output = new ArrayList<>();
        Queue<SoundRay> queue = new ConcurrentLinkedQueue<>(rays);

        while(!queue.isEmpty())
        {
            SoundRay ray = queue.poll();

            if(Intersection.hasLimitedIntersection(ray.getU(), ray.getV(), agent.getPosition(), agent.getRadius()))
            {
                double amplitude = this.amplitude / Math.log(collectDistances(ray, agent));
                Vector direction = ray.direction().normalise().scale(-1);
                output.add(new SoundVector(direction, amplitude, this.frequency));
            }
            queue.addAll(ray.getChildren());
        }
        return output;
    }

    public void draw(GraphicsContext gc)
    {
        rays.forEach(r -> r.draw(gc));
    }

    public void decay()
    {
        amplitude = amplitude * 0.5;
    }

    private double collectDistances(SoundRay ray, Agent agent)
    {
        double dist = ray.getU().dist(agent.intersection(ray));
        while(ray.hasParent())
        {
            ray = ray.getParent();
            dist += ray.length();
        }
        return dist;
    }
}

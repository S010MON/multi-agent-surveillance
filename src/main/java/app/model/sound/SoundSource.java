package app.model.sound;

import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import app.controller.soundEngine.SoundVector;
import app.model.agents.Agent;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.PriorityQueue;

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
    public SoundVector isHeard(Agent agent)
    {
        PriorityQueue<SoundRay> Q = new PriorityQueue<>();
        Q.addAll(rays);

        while(!Q.isEmpty())
        {
            SoundRay ray = Q.poll();

            if(agent.isCrossed(ray.getU(), ray.getV()))
            {
                double amplitude = this.amplitude / collectDistances(ray, agent);;
                return new SoundVector(ray.direction(), amplitude, this.frequency);
            }
            Q.addAll(ray.getChildren());
        }
        return null;
    }

    public void draw(GraphicsContext gc)
    {
        rays.forEach(r -> r.draw(gc));
    }

    public void decay()
    {
        amplitude = amplitude * 0.9;
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

package app.model;

import app.controller.AgentImp;
import app.controller.Beam;
import app.controller.Vector;

import java.util.ArrayList;

public class MapTemp
{
    private ArrayList<Beam> beams;
    private ArrayList<Placeable> objects;
    private ArrayList<Agent> agents;

    /**
     * Temporary map for testing ray drawing, will be swapped out for proper one once made
     */
    public MapTemp()
    {
        beams = new ArrayList<>();
        objects = createObjects();
        agents = new ArrayList<Agent>();
        agents.add(new AgentImp(new Vector(400, 250), new Vector(1,0), 10));
    }

    // Temp method to create a set of rays - GraphicsEngine should generate this
    private ArrayList<Beam> createBeams()
    {
        ArrayList<Beam> beams = new ArrayList<>();
        Vector origin = new Vector(400,250);
        Vector end = new Vector(400, 0);
        Beam beam = new Beam(origin, end);
        for(int i = 0; i < 100; i++)
        {
            beams.add(beam.rotate(i * 5.0));
        }
        return beams;
    }

    private ArrayList<Placeable> createObjects()
    {
        ArrayList<Placeable> objects = new ArrayList<>();
        Vector p1 = new Vector(200,100);
        Vector p2 = new Vector(600,100);
        Vector p3 = new Vector(600,400);
        Vector p4 = new Vector(200,400);
        objects.add(new Wall(p1, p2));
        objects.add(new Wall(p2, p3));
        objects.add(new Wall(p3, p4));
        objects.add(new Wall(p4, p1));
        return objects;
    }

    public ArrayList<Beam> getBeams()
    {
        return beams;
    }

    public ArrayList<Placeable> getObjects()
    {
        return objects;
    }

    public ArrayList<Agent> getAgents()
    {
        return agents;
    }
}

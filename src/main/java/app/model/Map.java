package app.model;

import app.controller.AgentImp;
import app.controller.Human;
import app.controller.Vector;

import java.util.ArrayList;

public class Map
{
    private ArrayList<Placeable> objects;
    private ArrayList<Agent> agents;
    private Human human;

    /**
     * Temporary map for testing ray drawing, will be swapped out for proper one once made
     */
    public Map()
    {
        objects = createObjects();
        agents = new ArrayList<>();
        human = new Human(new Vector(400, 250), new Vector(1,0), 10);
        agents.add(human);
        //agents.add(new AgentImp(new Vector(100, 100), new Vector(1,0), 10));
    }

    public void moveHuman(Vector v)
    {
        human.setNextMove(v);
    }

    private ArrayList<Placeable> createObjects()
    {
        ArrayList<Placeable> objects = new ArrayList<>();
        Vector p1 = new Vector(200,100);
        Vector p2 = new Vector(600,100);
        Vector p3 = new Vector(600,400);
        Vector p4 = new Vector(450,400);
        Vector p5 = new Vector(350,400);
        Vector p6 = new Vector(200,400);
        objects.add(new Wall(p1, p2));
        objects.add(new Wall(p2, p3));
//        objects.add(new Wall(p3, p4));
//        objects.add(new Wall(p5, p6));
//        objects.add(new Wall(p6, p1));
        return objects;
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

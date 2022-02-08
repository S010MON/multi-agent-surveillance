package app.model;

import app.controller.Settings;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.objects.Placeable;
import app.model.objects.Wall;

import java.awt.geom.Rectangle2D;
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

    public Map(Settings settings)
    {
        objects = createObjects();
        objects.addAll(rectangleDecomposer(settings.getWalls()));
        objects.addAll(rectangleDecomposer(settings.getShade()));
        objects.addAll(rectangleDecomposer(settings.getTowers()));
        objects.addAll(rectangleDecomposer(settings.getPortals()));
        objects.addAll(rectangleDecomposer(settings.getTextures()));
    }

    private ArrayList<Placeable> rectangleDecomposer(ArrayList<Rectangle2D> rectangles)
    {
        ArrayList<Placeable> recWalls = new ArrayList<>();

        for(Rectangle2D wall: rectangles)
        {
            Vector tl = new Vector(wall.getMinX(),wall.getMinY());    // top left corner
            Vector tr = new Vector(wall.getMaxX(), wall.getMinY());    // top right corner
            Vector lr = new Vector(wall.getMaxX(),wall.getMaxY());     // lower right corner
            Vector ll = new Vector(wall.getMinX(),wall.getMaxY());     // lower left corner

            recWalls.add(new Wall (tl,tr));
            recWalls.add(new Wall (tr,lr));
            recWalls.add(new Wall (lr,ll));
            recWalls.add(new Wall (ll,tl));
        }
        return recWalls;
    }

    public void walk(Vector v)
    {
        human.walk(v);
    }

    public void run(Vector v)
    {
        human.run(v);
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
        objects.add(new Wall(p3, p4));
        objects.add(new Wall(p5, p6));
        objects.add(new Wall(p6, p1));
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

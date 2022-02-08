package app.model;

import app.controller.Settings;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.boundary.BoundaryFactory;
import app.model.boundary.Placeable;
import app.model.texture.Texture;
import app.model.texture.TextureFactory;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;

public class Map
{
    private ArrayList<Placeable> objects;
    private ArrayList<Texture> textures;
    private ArrayList<Agent> agents;
    private Human human;

    /**
     * Temporary map for testing ray drawing, will be swapped out for proper one once made
     */
    public Map()
    {
        objects = createObjects();
        textures = createTextures();
        agents = new ArrayList<>();
        human = new Human(new Vector(400, 250), new Vector(1,0), 10);
        agents.add(human);
        //agents.add(new AgentImp(new Vector(100, 100), new Vector(1,0), 10));
    }

    /** To be replaced once settings uses javafx.Rectangle */
    public Map(Settings settings)
    {
        settings.getWalls()
                .stream()
                .forEach(e -> addWall(e));

    }

    public void walk(Vector v)
    {
        human.walk(v);
    }

    public void run(Vector v)
    {
        human.run(v);
    }

    public ArrayList<Placeable> getObjects()
    {
        return objects;
    }

    public ArrayList<Agent> getAgents()
    {
        return agents;
    }

    public ArrayList<Texture> getTextures()
    {
        return textures;
    }

    public void addWall(Rectangle2D rectangle)
    {
        objects.addAll(BoundaryFactory.make(Furniture.WALL, rectangle));
        textures.add(TextureFactory.make(Furniture.WALL, rectangle));
    }

    private ArrayList<Placeable> createObjects()
    {
        ArrayList<Placeable> objects = new ArrayList<>();
        Rectangle2D r1 = new Rectangle2D(100, 100, 200, 100);
        Rectangle2D r2 = new Rectangle2D(600, 100, 200, 100);
        Rectangle2D r3 = new Rectangle2D(500, 500, 100, 100);
        objects.addAll(BoundaryFactory.make(Furniture.WALL, r1));
        objects.addAll(BoundaryFactory.make(Furniture.SHADE, r2));
        objects.addAll(BoundaryFactory.make(Furniture.WALL, r3));
        return objects;
    }

    private ArrayList<Texture> createTextures()
    {
        ArrayList<Texture> textures = new ArrayList<>();
        Rectangle2D r1 = new Rectangle2D(100, 100, 200, 100);
        Rectangle2D r2 = new Rectangle2D(600, 100, 200, 100);
        Rectangle2D r3 = new Rectangle2D(500, 500, 100, 100);
        textures.add(TextureFactory.make(Furniture.WALL, r1));
        textures.add(TextureFactory.make(Furniture.SHADE, r2));

        return textures;
    }
}

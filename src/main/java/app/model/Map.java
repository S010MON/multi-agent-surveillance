package app.model;

import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.boundary.BoundaryFactory;
import app.model.boundary.Placeable;
import app.model.texture.Texture;
import app.model.texture.TextureFactory;
import javafx.scene.shape.Rectangle;
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
//    public Map(Settings settings)
//    {
//        objects = createObjects();
//        objects.addAll(rectangleDecomposer(settings.getWalls()));
//        objects.addAll(rectangleDecomposer(settings.getShade()));
//        objects.addAll(rectangleDecomposer(settings.getTowers()));
//        objects.addAll(rectangleDecomposer(settings.getPortals()));
//        objects.addAll(rectangleDecomposer(settings.getTextures()));
//    }

    public void walk(Vector v)
    {
        human.walk(v);
    }

    public void run(Vector v)
    {
        human.run(v);
    }

    public void addWall()
    {

    }

    private ArrayList<Placeable> createObjects()
    {
        ArrayList<Placeable> objects = new ArrayList<>();
        Rectangle r1 = new Rectangle(100, 100, 200, 100);
        Rectangle r2 = new Rectangle(600, 100, 200, 100);
        Rectangle r3 = new Rectangle(500, 500, 100, 100);
        objects.addAll(BoundaryFactory.make(Furniture.WALL, r1));
        objects.addAll(BoundaryFactory.make(Furniture.SHADE, r2));
        objects.addAll(BoundaryFactory.make(Furniture.WALL, r3));
        return objects;
    }

    private ArrayList<Texture> createTextures()
    {
        ArrayList<Texture> textures = new ArrayList<>();
        Rectangle r1 = new Rectangle(100, 100, 200, 100);
        Rectangle r2 = new Rectangle(600, 100, 200, 100);
        Rectangle r3 = new Rectangle(500, 500, 100, 100);
        textures.add(TextureFactory.make(Furniture.WALL, r1));
        textures.add(TextureFactory.make(Furniture.SHADE, r2));

        return textures;
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
}

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

    public Map(Settings settings)
    {
        System.out.print("Loading settings ... ");

        objects = new ArrayList<>();
        textures = new ArrayList<>();
        agents = new ArrayList<>();
        human = new Human(new Vector(400, 250), new Vector(1,0), 10);
        agents.add(human);

        settings.getWalls()
                .stream()
                .forEach(e -> addFurniture(Furniture.WALL, e));

        settings.getShade()
                .stream()
                .forEach(e -> addFurniture(Furniture.SHADE, e));

        System.out.println("done.");
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

    public void addFurniture(Furniture furniture, Rectangle2D rectangle)
    {
        objects.addAll(BoundaryFactory.make(furniture, rectangle));
        textures.add(TextureFactory.make(furniture, rectangle));
    }
}

package app.model;

import app.controller.Settings;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.boundary.BoundaryFactory;
import app.model.boundary.Boundary;
import app.model.texture.Texture;
import app.model.texture.TextureFactory;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;

public class Map
{
    private ArrayList<Furniture> furniture;
    private ArrayList<Boundary> objects;
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
                .forEach(e -> addFurniture(FurnitureType.WALL, e));

        settings.getShade()
                .stream()
                .forEach(e -> addFurniture(FurnitureType.SHADE, e));

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

    public ArrayList<Boundary> getObjects()
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

    public void addFurniture(FurnitureType furniture, Rectangle2D rectangle)
    {
        objects.addAll(BoundaryFactory.make(furniture, rectangle));
        textures.add(TextureFactory.make(furniture, rectangle));
    }

    public ArrayList<Furniture> getFurniture()
    {
        return furniture;
    }
}

package app.model;

import app.controller.Settings;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;

public class Map
{
    private ArrayList<Furniture> furniture;
    private ArrayList<Agent> agents;
    private Human human;

    public Map(Settings settings)
    {
        System.out.print("Loading settings ... ");

        agents = new ArrayList<>();
        human = new Human(new Vector(400, 250), new Vector(1,0), 10);
        agents.add(human);

        furniture = new ArrayList<>();
        settings.getWalls().forEach(e -> addFurniture(FurnitureType.WALL, e));
        settings.getShade().forEach(e -> addFurniture(FurnitureType.SHADE, e));

        // Temp test of glass
        Rectangle2D window = new Rectangle2D(300,150, 50,50);
        furniture.add(FurnitureFactory.make(FurnitureType.GLASS, window));

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

    public ArrayList<Agent> getAgents()
    {
        return agents;
    }

    public void addFurniture(FurnitureType type, Rectangle2D rectangle)
    {
        this.furniture.add(FurnitureFactory.make(type, rectangle));
    }

    public ArrayList<Furniture> getFurniture()
    {
        return furniture;
    }
}

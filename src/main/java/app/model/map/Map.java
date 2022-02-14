package app.model.map;

import app.controller.Settings;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.boundary.Boundary;
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
    private Settings settings;

    public Map(Settings settings)
    {
        System.out.print("Loading settings ... ");
        this.settings = settings;

        /* Make furniture */
        furniture = new ArrayList<>();
        settings.getWalls().forEach(e -> addFurniture(FurnitureType.WALL, e));
        settings.getShade().forEach(e -> addFurniture(FurnitureType.SHADE, e));
        settings.getGlass().forEach(e -> addFurniture(FurnitureType.GLASS, e));

        agents = new ArrayList<>();

        // On creation add the right number of guards
        for(int i = 0; i < settings.getNoOfGuards(); i++)
        {

        }

        // On creation add the right number of infiltrators
        for(int i = 0; i < settings.getNoOfIntruders(); i++)
        {

        }

        human = new Human(new Vector(380, 250), new Vector(1,0), 10);
        agents.add(human);



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

    public Settings getSetting(){return settings;}

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

    public ArrayList<Boundary> getBoundaries()
    {
        ArrayList<Boundary> boundaries = new ArrayList<>();
        furniture.forEach(e -> boundaries.addAll(e.getBoundaries()));
        return boundaries;
    }
}

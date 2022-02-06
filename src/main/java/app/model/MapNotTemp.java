package app.model;

import app.controller.AgentImp;
import app.controller.Settings;
import app.controller.Vector;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MapNotTemp
{
    private ArrayList<Placeable> objects;
    private ArrayList<Agent> agents;
    private Settings setting;

    private ArrayList<Rectangle2D> walls;
//    private ArrayList<Rectangle2D> shade;
//    private ArrayList<Rectangle2D> towers;
//    private ArrayList<Rectangle2D> portals;
//    private ArrayList<Point> teleportTo;
//    private ArrayList<Rectangle2D> textures;
//    private ArrayList<Integer> textureType;

    /**
     * Temporary map for testing ray drawing, will be swapped out for proper one once made
     */
    public MapNotTemp(Settings setting)
    {
        setting = setting;
        objects = createObjects();
        agents = new ArrayList<>();
        agents.add(new AgentImp(new Vector(400, 250), new Vector(1,0), 10));
        agents.add(new AgentImp(new Vector(100, 100), new Vector(1,0), 10));
    }




    // make it ArrayList<Placeable> instead of void
    private void mainDecomposer(){

        if (setting.getWalls().size()!= 0){
            rectangleDecomposer(setting.getWalls()); // decompose rectangle
        }
    }

    private void rectangleDecomposer(ArrayList<Rectangle2D> walls){

        for(Rectangle2D wall: walls){
            Vector tl = new Vector(wall.getMinX(),wall.getMinY());    // top left corner
            Vector tr = new Vector(wall.getMaxX(), wall.getMinY());    // top right corner
            Vector lr = new Vector(wall.getMaxX(),wall.getMaxY());     // lower right corner
            Vector ll = new Vector(wall.getMinX(),wall.getMaxY());     // lower left corner

            objects.add(new Wall (tl,tr));
            objects.add(new Wall (tr,lr));
            objects.add(new Wall (lr,ll));
            objects.add(new Wall (ll,tl));
        }
    }

    private ArrayList<Placeable> createObjects()
    {
        ArrayList<Placeable> objects = new ArrayList<>();
        Vector b1 = new Vector(0,0);
        Vector b2 = new Vector(800,0);
        Vector b3 = new Vector(800,500);
        Vector b4 = new Vector(0,500);
//        objects.add(new Border(b1, b2));
//        objects.add(new Border(b2, b3));
//        objects.add(new Border(b3, b4));
//        objects.add(new Border(b4, b1));

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

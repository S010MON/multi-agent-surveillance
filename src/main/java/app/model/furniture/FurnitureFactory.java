package app.model.furniture;

import app.controller.linAlg.Vector;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class FurnitureFactory
{
    public static ArrayList<Placeable> make(Furniture f, Rectangle rectangle)
    {
        ArrayList<Placeable> objects = new ArrayList<>();
        Vector[] corner = cornersOf(rectangle);
                objects.add(create(f, corner[0], corner[1]));
        objects.add(create(f, corner[1], corner[2]));
        objects.add(create(f, corner[2], corner[3]));
        objects.add(create(f, corner[3], corner[0]));
        return objects;
    }

    private static Placeable create(Furniture f, Vector a, Vector b)
    {
        switch (f)
        {
            case WALL -> new VisibleBoundary(a, b);
            case SHADE -> new Boundary(a, b);
        }
        return null; // Redundant
    }

    private static Vector[] cornersOf(Rectangle r)
    {
        Vector[] corners = new Vector[4];
        corners[0] = new Vector(r.getX(),r.getY());                                            // top left corner
        corners[1] = new Vector(r.getX() + r.getWidth(),r.getY());                          // top right corner
        corners[2] = new Vector(r.getX(),r.getY() + r.getHeight());                         // lower right corner
        corners[3] = new Vector(r.getX() + r.getWidth(), r.getY() + r.getHeight());      // lower left corner
        return corners;
    }
}

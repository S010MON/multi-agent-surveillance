package app.model.boundary;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

public abstract class BoundaryFactory
{
    public static ArrayList<Boundary> make(FurnitureType f, Rectangle2D rectangle)
    {
        ArrayList<Boundary> objects = new ArrayList<>();
        Vector[] corner = cornersOf(rectangle);
                objects.add(create(f, corner[0], corner[1]));
        objects.add(create(f, corner[1], corner[2]));
        objects.add(create(f, corner[2], corner[3]));
        objects.add(create(f, corner[3], corner[0]));
        return objects;
    }

    private static Boundary create(FurnitureType f, Vector a, Vector b)
    {
        switch (f)
        {
            case WALL -> { return new VisibleBoundary(a, b);}
            case SHADE -> { return new InvisibleBoundary(a, b);}
            case GLASS -> { return  new TransparentBoundary();}
        }
        return null; // Redundant by design
    }

    private static Vector[] cornersOf(Rectangle2D r)
    {
        Vector[] corners = new Vector[4];
        corners[0] = new Vector(r.getMinX(), r.getMinY());      // top left corner
        corners[1] = new Vector(r.getMaxX(), r.getMinY());      // top right corner
        corners[2] = new Vector(r.getMaxX(), r.getMaxY());      // lower right corner
        corners[3] = new Vector(r.getMinX(), r.getMaxY());      // lower left corner
        return corners;
    }
}

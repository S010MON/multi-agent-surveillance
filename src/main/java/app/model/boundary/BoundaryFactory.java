package app.model.boundary;

import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

public abstract class BoundaryFactory
{
    public static ArrayList<Boundary> make(SettingsObject obj)
    {
        ArrayList<Boundary> objects = new ArrayList<>();
        Vector[] corner = cornersOf(obj.getRect());
        objects.add(create(obj.getType(), corner[0], corner[1], obj.getTeleportTo()));
        objects.add(create(obj.getType(), corner[1], corner[2], obj.getTeleportTo()));
        objects.add(create(obj.getType(), corner[2], corner[3], obj.getTeleportTo()));
        objects.add(create(obj.getType(), corner[3], corner[0], obj.getTeleportTo()));
        objects.removeAll(Collections.singleton(null));
        if(obj.getType() == FurnitureType.TARGET)
        {
            double x = obj.getRect().getMinX() + obj.getRect().getWidth()/2;
            double y = obj.getRect().getMinY() + obj.getRect().getHeight()/2;
            objects.add(new Flag(new Vector(x, y), 10));
        }
        return objects;
    }

    private static Boundary create(FurnitureType f, Vector a, Vector b, Vector teleport)
    {
        switch (f)
        {
            case SHADE, GUARD_SPAWN, INTRUDER_SPAWN -> { return new BoundaryImp(a, b);}
            case WALL, TOWER, BORDER -> { return new VisibleBoundary(a, b);}
            case GLASS -> { return  new TransparentBoundary(a, b);}
            case TARGET -> {return new TargetBoundary(a, b);}
            case PORTAL -> { return new PortalBoundary(a, b, teleport);}
        }
        return null;
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

package app.model.soundBoundary;

import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.boundary.*;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

public abstract class SoundBoundaryFactory {
    public static ArrayList<SoundBoundary> make(SettingsObject obj)
    {
        ArrayList<SoundBoundary> objects = new ArrayList<>();
        Vector[] corner = cornersOf(obj.getRect());
        objects.add(create(obj.getType(), corner[0], corner[1]));
        objects.add(create(obj.getType(), corner[1], corner[2]));
        objects.add(create(obj.getType(), corner[2], corner[3]));
        objects.add(create(obj.getType(), corner[3], corner[0]));
        return objects;
    }

    private static SoundBoundary create(FurnitureType f, Vector a, Vector b)
    {
        // right now they all refer to the same boundary type, but this way it allows for more flexibility later on if we decide to implement weirder sound boundaries
        switch (f)
        {
            case WALL, TOWER -> { return new SoundBoundaryImp(a, b);}
            case GLASS -> { return  new SoundBoundaryImp(a, b);}
            case SHADE, GUARD_SPAWN, INTRUDER_SPAWN, TARGET -> { return new SoundBoundaryImp(a, b);}
            case PORTAL -> { return new SoundBoundaryImp(a, b); }
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

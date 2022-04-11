package app.model.soundBoundary;

import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.boundary.*;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.Collections;

public abstract class SoundBoundaryFactory
{
    public static ArrayList<SoundBoundary> make(SettingsObject obj)
    {
        ArrayList<SoundBoundary> objects = new ArrayList<>();
        Vector[] corner = cornersOf(obj.getRect());
        objects.add(create(obj.getType(), corner[0], corner[1]));
        objects.add(create(obj.getType(), corner[1], corner[2]));
        objects.add(create(obj.getType(), corner[2], corner[3]));
        objects.add(create(obj.getType(), corner[3], corner[0]));
        objects.removeAll(Collections.singleton(null));
        return objects;
    }

    private static SoundBoundary create(FurnitureType f, Vector a, Vector b)
    {
        switch (f)
        {
            case WALL, TOWER, SHADE, GUARD_SPAWN, INTRUDER_SPAWN, TARGET, GLASS, PORTAL, BORDER
                    -> { return new SoundBoundaryImp(a, b);}
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

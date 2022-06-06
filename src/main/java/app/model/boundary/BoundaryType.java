package app.model.boundary;

import app.model.Type;
import app.model.furniture.FurnitureType;

public enum BoundaryType
{
    VISIBLE_SOLID,
    VISIBLE_TRANSPARENT,
    VISIBLE_TRANSPARENT_SOLID,
    TRANSPARENT,
    TRANSPARENT_SOLID;

    public static BoundaryType of(Type type)
    {
        if(type == null)
            return null;
        switch(type)
        {
            case WALL, BORDER, TOWER -> {return BoundaryType.VISIBLE_SOLID;}
            case SHADE, GUARD_SPAWN, INTRUDER_SPAWN, SIREN -> {return BoundaryType.TRANSPARENT;}
            case GLASS -> {return BoundaryType.TRANSPARENT_SOLID;}
            case PORTAL, TARGET -> {return BoundaryType.VISIBLE_TRANSPARENT;}
            default -> {return null;}
        }
    }

    public static boolean isSolid(BoundaryType type)
    {
        if(type == null)
            return false;
        switch(type)
        {
            case TRANSPARENT_SOLID, VISIBLE_SOLID, VISIBLE_TRANSPARENT_SOLID -> {return true;}
            default -> {return false;}
        }
    }

    public static boolean isVisible(BoundaryType type)
    {
        if(type == null)
            return false;
        switch(type)
        {
            case VISIBLE_TRANSPARENT, VISIBLE_SOLID, VISIBLE_TRANSPARENT_SOLID-> {return true;}
            default -> {return false;}
        }
    }

    public static boolean isTransparent(BoundaryType type)
    {
        if(type == null)
            return true;
        switch(type)
        {
            case TRANSPARENT, VISIBLE_TRANSPARENT_SOLID, VISIBLE_TRANSPARENT-> {return true;}
            default -> {return false;}
        }
    }
}

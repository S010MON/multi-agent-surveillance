package app.model;

import app.model.furniture.FurnitureType;

public enum Type
{
    WALL,
    SHADE,
    GLASS,
    TOWER,
    PORTAL,
    GUARD_SPAWN,
    INTRUDER_SPAWN,
    TARGET,
    BORDER,
    SIREN,
    INTRUDER,
    GUARD;

    public static Type of(FurnitureType furnitureType)
    {
        switch(furnitureType)
        {
            case WALL -> {return Type.WALL;}
            case SHADE -> {return Type.SHADE;}
            case GLASS -> {return Type.GLASS;}
            case TOWER -> {return Type.TOWER;}
            case PORTAL -> {return Type.PORTAL;}
            case GUARD_SPAWN -> {return Type.GUARD_SPAWN;}
            case INTRUDER_SPAWN -> {return Type.INTRUDER_SPAWN;}
            case TARGET -> {return Type.TARGET;}
            case BORDER -> {return Type.BORDER;}
            case SIREN -> {return Type.SIREN;}
        }
        return null;
    }
}

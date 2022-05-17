package app.model;

public abstract class TypeInformation
{
    public static boolean isSolid(Type type)
    {
        if(type == null)
            return false;

        switch(type)
        {
            case WALL, GLASS, BORDER -> { return true; }
            default -> { return false; }
        }
    }
}
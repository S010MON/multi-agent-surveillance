package app.model;

public abstract class TypeInformation
{
    public static boolean isSolid(Type type)
    {
        // switch cases apparently don't support null,
        // but wanted to keep switch-case cause cleaner and easier to add stuff
        if(type == null)
        {
            return false;
        }

        switch(type)
        {
            case WALL, GLASS, BORDER -> { return true; }
            default -> { return false; }
        }
    }
}
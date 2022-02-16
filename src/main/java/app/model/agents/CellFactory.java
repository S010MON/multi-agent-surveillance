package app.model.agents;

public abstract class CellFactory
{
    public static Cell make(CellType type)
    {
        return create(type);
    }

    private static Cell create(CellType type)
    {
        switch(type)
        {
            case PHERAMONE -> {return new PheramoneCell();}
            case BOOLEAN -> {return new BooleanCell();}
        }
        return null;
    }
}

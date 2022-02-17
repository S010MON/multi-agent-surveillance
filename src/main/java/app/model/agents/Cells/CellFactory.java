package app.model.agents.Cells;

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
            case PHEROMONE -> {return new PheromoneCell();}
            case BOOLEAN -> {return new BooleanCell();}
        }
        return null;
    }
}

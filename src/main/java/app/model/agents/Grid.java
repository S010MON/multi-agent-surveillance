package app.model.agents;

public class Grid
{
    protected int rowSize;
    protected int colSize;
    protected Cell[][] grid;
    protected int cellSize = 1;
    protected CellType type;

    Boolean boolDefault = false;
    Double doubleDefault = 0.0;

    public Grid(double length, double width, CellType type)
    {
        rowSize = (int)(length / cellSize);
        colSize = (int)(width / cellSize);
        this.type = type;

        grid = new Cell[rowSize][colSize];
        initializeAllCellsDefault();
    }

    public void initializeAllCellsDefault()
    {
        for(int row = 0; row < rowSize; row++)
        {
            for(int col = 0; col < colSize; col++)
            {
                grid[row][col] = CellFactory.make(type);
            }
        }
    }

    public Cell getCellAt(int row, int col)
    {
        if(type == CellType.BOOLEAN)
        {
            return (BooleanCell)grid[row][col];
        }
        if(type == CellType.PHERAMONE)
        {
            return (PheramoneCell)grid[row][col];
        }
        return null;
    }
}

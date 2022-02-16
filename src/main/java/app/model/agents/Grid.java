package app.model.agents;

import app.model.agents.Cells.*;

public class Grid
{
    protected int rowSize;
    protected int colSize;
    protected Cell[][] grid;
    protected double cellSize = 1.0;
    protected CellType type;


    public Grid(double length, double width, CellType type)
    {
        rowSize = (int)(length / cellSize);
        colSize = (int)(width / cellSize);
        this.type = type;

        grid = new Cell[rowSize][colSize];
        initializeAllCells();
    }

    public void initializeAllCells()
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
        switch (type)
        {
            case BOOLEAN -> {return (BooleanCell)grid[row][col];}
            case PHERAMONE -> {return (PheramoneCell)grid[row][col];}
        }
        return null;
    }
}

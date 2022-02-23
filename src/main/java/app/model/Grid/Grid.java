package app.model.Grid;

import app.controller.linAlg.Vector;
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

        grid = initializeAllCells();
    }

    public Cell[][] initializeAllCells()
    {
        Cell[][] gridInitialization = new Cell[rowSize][colSize];

        for(int row = 0; row < rowSize; row++)
        {
            for(int col = 0; col < colSize; col++)
            {
                gridInitialization[row][col] = CellFactory.make(type);
            }
        }
        return gridInitialization;
    }

    public Cell getCellAt(int row, int col)
    {
        return grid[row][col];
    }

    public Cell getCellAt(Vector position)
    {
        int row = getCellRow(position);
        int col = getCellCol(position);

        return grid[row][col];
    }

    public int getCellRow(Vector position)
    {
        return (int)(position.getY() / cellSize);
    }

    public int getCellCol(Vector position)
    {
        return (int)(position.getX() / cellSize);
    }

    public int getRowDimension()
    {
        return grid.length;
    }

    public int getColDimension()
    {
        return grid[0].length;
    }

    public double getCellSize()
    {
        return cellSize;
    }
}

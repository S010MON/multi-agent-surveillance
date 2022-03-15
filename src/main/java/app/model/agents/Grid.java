package app.model.agents;

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
        //Addition of single cell to accomodate for 0 cells
        rowSize = (int)(Math.ceil(length / cellSize) + 1);
        colSize = (int)(Math.ceil(width / cellSize) + 1);
        this.type = type;

        grid = initializeAllCells();
    }

    public Grid(double length, double width, CellType type, double cellSize)
    {
        //Addition of single cell to accomodate for 0 cells
        this.cellSize = cellSize;
        rowSize = (int) (Math.ceil(length / cellSize) + 1);
        colSize = (int) (Math.ceil(width / cellSize) + 1);
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

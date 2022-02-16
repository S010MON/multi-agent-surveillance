package app.model.agents.ACO;

import app.controller.linAlg.Vector;

public class AcoOverlay
{
    private int rowSize;
    private int colSize;
    private Cell[][] grid;
    private double cellSize = 1.0;

    public AcoOverlay(double length, double width)
    {
        this.rowSize = (int)(length / cellSize);
        this.colSize = (int)(width / cellSize);

        grid = new Cell[rowSize][colSize];
        initializeAllCells();
    }

    public AcoOverlay(double length, double width, double cellSize)
    {
        this.cellSize = cellSize;
        this.rowSize = (int)(length / cellSize);
        this.colSize = (int)(width / cellSize);

        grid = new Cell[rowSize][colSize];
        initializeAllCells();
    }

    public void initializeAllCells()
    {
        for(int i = 0; i< grid.length; i++)
        {
            for(int j = 0; j < grid[i].length; j++)
            {
                grid[i][j] = new Cell();
            }
        }
    }

    public void updateAgent(AcoAgent agent)
    {
        int row = getCellRow(agent.getPosition());
        int col = getCellCol(agent.getPosition());
        grid[row][col].updatePheramone(agent.releasePheramone());
    }

    public void evaporationProcess()
    {
        for(int row = 0; row < grid.length; row ++)
        {
            for(int col = 0; col < grid[row].length; col ++)
            {
                grid[row][col].evaporation();
            }
        }
    }

    public Cell getCell(Vector position)
    {
        int row = (int)(position.getY() / cellSize);
        int col = (int)(position.getX() / cellSize);

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
}

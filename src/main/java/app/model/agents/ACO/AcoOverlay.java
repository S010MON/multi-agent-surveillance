package app.model.agents.ACO;

import app.controller.linAlg.Vector;

import java.util.ArrayList;

public class AcoOverlay
{
    private int rowSize;
    private int colSize;
    private Cell[][] grid;
    private double cellSize = 1.0;

    private ArrayList<AcoAgent> agents = new ArrayList<>();

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

    public void addAgent(AcoAgent agent)
    {
        agents.add(agent);

        int row = getCellRow(agent.getPosition());
        int col = getCellCol(agent.getPosition());
        Cell currentCell = grid[row][col];

        currentCell.addAgent(agent);
        currentCell.pheramonePlacement();
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

    public void removeAgent(AcoAgent agent)
    {
        int row = getCellRow(agent.getPosition());
        int col = getCellCol(agent.getPosition());
        grid[row][col].removeAgent();
    }

    public void placeAgent(Vector movement, AcoAgent agent)
    {
        int row = getCellRow(agent.getPosition());
        int col = getCellCol(agent.getPosition());

        int updatedRow = row + (int)movement.getY();
        int updatedCol = col + (int)movement.getX();

        Cell cell = grid[updatedRow][updatedCol];
        cell.addAgent(agent);
        cell.pheramonePlacement();
    }

    public void updateAgent(Vector movement, AcoAgent agent)
    {
        removeAgent(agent);
        placeAgent(movement, agent);
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

    public int getRowDimension()
    {
        return grid.length;
    }

    public int getColDimension()
    {
        return grid[0].length;
    }
}

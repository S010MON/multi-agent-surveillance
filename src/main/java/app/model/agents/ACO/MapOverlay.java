package app.model.agents.ACO;

import app.controller.linAlg.Vector;

import java.util.ArrayList;

public class MapOverlay
{
    private int rowSize;
    private int colSize;
    private Cell[][] grid;
    private double cellSize = 1.0;

    private ArrayList<AcoAgent> agents = new ArrayList<>();

    public MapOverlay(double length, double width)
    {
        this.rowSize = (int)(length / cellSize);
        this.colSize = (int)(width / cellSize);

        grid = new Cell[rowSize][colSize];
    }

    public void addAgent(AcoAgent agent)
    {
        agents.add(agent);
    }

    public Cell getCell(Vector position)
    {
        int row = (int)(position.getX() / rowSize);
        int col = (int)(position.getY() / colSize);

        return grid[row][col];
    }

    public int getCellRow(Vector position)
    {
        return (int)(position.getY() / rowSize);
    }

    public int getCellCol(Vector position)
    {
        return (int)(position.getX() / colSize);
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
}

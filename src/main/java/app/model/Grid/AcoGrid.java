package app.model.Grid;

import app.model.agents.ACO.AcoAgent;
import app.model.agents.Cells.Cell;
import app.model.agents.Cells.CellType;
import app.model.agents.Cells.PheromoneCell;

//TODO Use singleton pattern to insist on only one world
public class AcoGrid extends Grid
{
    public AcoGrid()
    {
        super(1000, 1000, CellType.PHEROMONE, 20);
    }

    public AcoGrid(double length, double width)
    {
        super(length, width, CellType.PHEROMONE);
    }


    public AcoGrid(double length, double width, double cellSize)
    {
        super(length, width, CellType.PHEROMONE, cellSize);
    }

    //TODO Evaporation needs to be changed into group system, not individual
    public void updateAgent(AcoAgent agent)
    {
        int row = getCellRow(agent.getPosition());
        int col = getCellCol(agent.getPosition());

        PheromoneCell cell = (PheromoneCell) super.getCellAt(row, col);
        cell.updatePheromone(agent.releaseMaxPheromone());
    }

    public void evaporationProcess()
    {
        for(int row = 0; row < grid.length; row ++)
        {
            for(int col = 0; col < grid[row].length; col ++)
            {
                PheromoneCell cell = (PheromoneCell) super.getCellAt(row, col);
                cell.evaporation();
            }
        }
    }

    //TODO Rewrite method to toString
    public void print()
    {
        for(Cell[] line : grid)
        {
            for(Cell cell: line)
            {
                System.out.print(cell.toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}

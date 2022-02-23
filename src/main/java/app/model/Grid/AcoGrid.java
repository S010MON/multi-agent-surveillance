package app.model.Grid;

import app.model.agents.ACO.AcoAgent;
import app.model.agents.Cells.CellType;
import app.model.agents.Cells.PheromoneCell;

//TODO Use singleton pattern to insist on only one world
public class AcoGrid extends Grid
{
    public AcoGrid(double length, double width)
    {
        super(length, width, CellType.PHEROMONE);
    }

    public AcoGrid(double length, double width, double cellSize)
    {
        super(length, width, CellType.PHEROMONE);
        super.cellSize = cellSize;
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

    public void displayGridState()
    {
        for(int i = 0; i < getRowDimension(); i++)
        {
            for(int j = 0; j < getColDimension(); j++)
            {
                System.out.print(grid[i][j].toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}

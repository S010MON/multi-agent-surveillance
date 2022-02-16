package app.model.agents.ACO;

import app.model.agents.Cells.CellType;
import app.model.agents.Cells.PheramoneCell;
import app.model.agents.Grid;

public class AcoGrid extends Grid
{
    public AcoGrid(double length, double width)
    {
        super(length, width, CellType.PHERAMONE);
    }

    public AcoGrid(double length, double width, double cellSize)
    {
        super(length, width, CellType.PHERAMONE);
        super.cellSize = cellSize;
    }

    public void updateAgent(AcoAgent agent)
    {
        int row = getCellRow(agent.getPosition());
        int col = getCellCol(agent.getPosition());

        PheramoneCell cell = (PheramoneCell) super.getCellAt(row, col);
        cell.updatePheramone(agent.releaseMaxPheramone());
    }

    public void evaporationProcess()
    {
        for(int row = 0; row < grid.length; row ++)
        {
            for(int col = 0; col < grid[row].length; col ++)
            {
                PheramoneCell cell = (PheramoneCell) super.getCellAt(row, col);
                cell.evaporation();
            }
        }
    }


}

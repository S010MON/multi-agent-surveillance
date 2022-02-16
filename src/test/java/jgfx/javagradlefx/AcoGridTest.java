package jgfx.javagradlefx;

import app.controller.linAlg.Vector;
import app.model.agents.ACO.AcoAgent;
import app.model.Grid.AcoGrid;
import app.model.agents.Cells.PheramoneCell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcoGridTest
{

    @Test
    void testUpdateAgent()
    {
        Vector agentPosition = new Vector(10.2, 12.3);
        AcoGrid grid = new AcoGrid(50.3, 25.7, 1);
        AcoAgent agent = new AcoAgent(agentPosition, new Vector(1, 0), 10);

        grid.updateAgent(agent);
        PheramoneCell occupiedCell = (PheramoneCell)grid.getCellAt(agentPosition);

        assertEquals(occupiedCell.currentPheramoneValue(), agent.releaseMaxPheramone());
    }

    @Test
    void testEvaporationProcess()
    {
        Vector agentPosition = new Vector(10.2, 12.3);
        AcoGrid grid = new AcoGrid(50.3, 25.7, 1);
        AcoAgent agent = new AcoAgent(agentPosition, new Vector(1, 0), 10);
        grid.updateAgent(agent);

        PheramoneCell cell = (PheramoneCell) grid.getCellAt(agentPosition);
        double currentPheramone = cell.currentPheramoneValue();

        grid.evaporationProcess();
        double pheramoneAfterEvaporation = cell.currentPheramoneValue();

        assertTrue(currentPheramone > pheramoneAfterEvaporation);
    }
}

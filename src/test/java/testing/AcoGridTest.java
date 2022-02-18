package testing;

import app.controller.linAlg.Vector;
import app.model.agents.ACO.AcoAgent;
import app.model.Grid.AcoGrid;
import app.model.agents.Cells.PheromoneCell;
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
        PheromoneCell occupiedCell = (PheromoneCell)grid.getCellAt(agentPosition);

        assertEquals(occupiedCell.currentPheromoneValue(), agent.releaseMaxPheramone());
    }

    @Test
    void testEvaporationProcess()
    {
        Vector agentPosition = new Vector(10.2, 12.3);
        AcoGrid grid = new AcoGrid(50.3, 25.7, 1);
        AcoAgent agent = new AcoAgent(agentPosition, new Vector(1, 0), 10);
        grid.updateAgent(agent);

        PheromoneCell cell = (PheromoneCell) grid.getCellAt(agentPosition);
        double currentPheromone = cell.currentPheromoneValue();

        grid.evaporationProcess();
        double pheromoneAfterEvaporation = cell.currentPheromoneValue();

        assertTrue(currentPheromone > pheromoneAfterEvaporation);
    }
}

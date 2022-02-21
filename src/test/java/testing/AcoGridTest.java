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
        AcoAgent.initializeWorld(50.3, 25.7);

        Vector agentPosition = new Vector(10.2, 12.3);
        AcoAgent agent = new AcoAgent(agentPosition, new Vector(1, 0), 10);
        AcoGrid grid = AcoAgent.accessWorld();

        PheromoneCell occupiedCell = (PheromoneCell)grid.getCellAt(agentPosition);

        assertEquals(occupiedCell.currentPheromoneValue(), agent.releaseMaxPheramone());
    }

    @Test
    void testEvaporationProcess()
    {
        AcoAgent.initializeWorld(50.3, 25.7);

        Vector agentPosition = new Vector(10.2, 12.3);
        AcoAgent agent = new AcoAgent(agentPosition, new Vector(1, 0), 10);
        AcoGrid grid = AcoAgent.accessWorld();

        PheromoneCell cell = (PheromoneCell) grid.getCellAt(agentPosition);
        double currentPheromone = cell.currentPheromoneValue();

        grid.evaporationProcess();
        double pheromoneAfterEvaporation = cell.currentPheromoneValue();

        assertTrue(currentPheromone > pheromoneAfterEvaporation);
    }

    @Test
    void testAgentMovementWithinGrid()
    {
        AcoAgent.initializeWorld(20, 20);

        Vector agentPosition = new Vector(10, 10);
        AcoAgent agent = new AcoAgent(agentPosition, new Vector(1, 0), 10);
        AcoAgent.accessWorld().displayGridState();

        agent.updateLocation(new Vector(9, 9));
        AcoAgent.accessWorld().displayGridState();

    }
}

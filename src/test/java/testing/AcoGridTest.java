package testing;

import app.controller.linAlg.Vector;
import app.model.agents.ACO.AcoAgent360Vision;
import app.model.agents.ACO.AcoGrid;
import app.model.agents.Cells.PheromoneCell;
import app.model.agents.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcoGridTest
{
    private final boolean display = false;

    @Test
    void testUpdateAgent()
    {
        AcoGrid world = new AcoGrid(50.3, 25.7, 1.0);
        AcoAgent360Vision.initializeWorld(world);

        Vector agentPosition = new Vector(10.2, 12.3);
        AcoAgent360Vision agent = new AcoAgent360Vision(agentPosition, new Vector(1, 0), 10, Team.GUARD);
        AcoGrid grid = AcoAgent360Vision.accessWorld();

        PheromoneCell occupiedCell = (PheromoneCell)grid.getCellAt(agentPosition);

        assertEquals(occupiedCell.currentPheromoneValue(), agent.releaseMaxPheromone());
    }

    @Test
    void testEvaporationProcess()
    {
        AcoGrid world = new AcoGrid(50.3, 25.7, 1);
        AcoAgent360Vision.initializeWorld(world);

        Vector agentPosition = new Vector(10.2, 12.3);
        AcoAgent360Vision agent = new AcoAgent360Vision(agentPosition, new Vector(1, 0), 10, Team.GUARD);
        AcoGrid grid = AcoAgent360Vision.accessWorld();

        PheromoneCell cell = (PheromoneCell) grid.getCellAt(agentPosition);
        double currentPheromone = cell.currentPheromoneValue();

        grid.evaporationProcess();
        double pheromoneAfterEvaporation = cell.currentPheromoneValue();

        assertTrue(currentPheromone > pheromoneAfterEvaporation);
    }

    @Test
    void testAgentMovementWithinGrid()
    {
        AcoGrid world = new AcoGrid(20, 20, 1);
        AcoAgent360Vision.initializeWorld(world);

        Vector agentPosition = new Vector(10, 10);
        AcoAgent360Vision agent = new AcoAgent360Vision(agentPosition, new Vector(1, 0), 10, Team.GUARD);

        if(display) AcoAgent360Vision.accessWorld().print();

        Vector newPosition = new Vector(9, 9);
        agent.updateLocation(newPosition);

        if(display) AcoAgent360Vision.accessWorld().print();

        PheromoneCell currentCell = (PheromoneCell) AcoAgent360Vision.accessWorld().getCellAt(newPosition);
        assertTrue(currentCell.currentPheromoneValue() < agent.releaseMaxPheromone());

    }
}

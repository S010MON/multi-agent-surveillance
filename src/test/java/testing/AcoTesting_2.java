package testing;

import app.controller.linAlg.Vector;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Team;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Set;

public class AcoTesting_2
{
    private Vector position = new Vector(30, 40);
    private Vector direction = new Vector(1, 0);
    private int radius = 10;

    @Test
    public void testEvaporation()
    {
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        //agent.worldEvaporationProcess();
        Set<GraphCell> vertexSet = AcoAgent.getWorld().vertexSet();
        vertexSet.forEach((GraphCell cell) -> assertNotEquals(cell.getPheromone(), agent.getMaxPheromone()));
    }
}

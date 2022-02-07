package jgfx.javagradlefx;

import app.controller.AcoAgent;
import app.controller.GraphicsEngine;
import app.controller.RayTracing;
import app.controller.Vector;
import app.model.Agent;
import app.model.MapTemp;
import org.junit.jupiter.api.Test;
import org.jgrapht.*;

public class VisionToGraphTest
{
    GraphicsEngine graphicsEngine = new RayTracing();

    @Test
    void Test4PointCellDetection()
    {
        //Agent setup
        Vector agentsPosition = new Vector(200, 200);
        Vector agentsDirection = new Vector(1,0);
        Agent agent = new AcoAgent(agentsPosition, agentsDirection, 10);

        //No obstacle setup
        MapTemp map = new MapTemp(agent, null);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));
    }
}

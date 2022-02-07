package jgfx.javagradlefx;


import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.RayTracing;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.Agent;
import org.junit.jupiter.api.Test;
import org.jgrapht.*;

public class VisionToGraphTest
{
    GraphicsEngine graphicsEngine = new RayTracing();

    @Test
    void Test4PointCellDetectionNoObstacles()
    {
        //Agent setup
        Vector agentsPosition = new Vector(200, 200);
        Vector agentsDirection = new Vector(1,0);
        Agent agent = new AcoAgent(agentsPosition, agentsDirection, 10);

        //No obstacle setup
        Map map = new Map(agent, null);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));
    }
}
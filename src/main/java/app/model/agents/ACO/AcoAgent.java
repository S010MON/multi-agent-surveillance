package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Grid.AcoGrid;
import app.model.agents.AgentImp;
import app.model.agents.Cells.PheromoneCell;

import java.util.HashMap;

public class AcoAgent extends AgentImp
{
    //TODO Place actual max pheramone value
    private double maxPheramone = 10;
    private static AcoGrid world;
    private HashMap<Integer, PheromoneCell> agentMap = new HashMap<>();

    public AcoAgent(Vector position, Vector direction, double radius, Vector targetDirection)
    {
        super(position, direction, radius, targetDirection);
        world.updateAgent(this);
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
        world.updateAgent(this);
    }

    public double releaseMaxPheramone()
    {
        return maxPheramone;
    }

    public static void initializeWorld(double width, double height)
    {
        world = new AcoGrid(height, width);
    }
    public static AcoGrid accessWorld()
    {
        return world;
    }
}

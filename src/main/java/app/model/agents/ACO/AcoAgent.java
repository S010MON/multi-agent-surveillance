package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.agents.AgentImp;

import java.util.HashMap;

public class AcoAgent extends AgentImp
{
    //TODO Place actual max pheramone value
    private double maxPheramone = 10;
    private HashMap<Integer, Cell> agentMap = new HashMap<>();

    public AcoAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
    }

    public double releasePheramone()
    {
        return maxPheramone;
    }

    public double accessPheramoneQuantity()
    {
        return maxPheramone;
    }
}

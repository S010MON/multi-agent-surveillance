package app.model.agents.ACO;

import app.controller.linAlg.Vector;

public class Vertice
{
    private Vector agentRelativePosition;
    private double pheramoneQuantity;

    public Vertice()
    {
        agentRelativePosition = new Vector();
    }

    public Vertice(Vector agentPosition)
    {
        agentRelativePosition = agentPosition;
    }

    public void updateCellPheramoneQuantity(double updatedQuantity)
    {
        pheramoneQuantity = updatedQuantity;
    }

    public Vector accessCellPosition()
    {
        return agentRelativePosition;
    }
}
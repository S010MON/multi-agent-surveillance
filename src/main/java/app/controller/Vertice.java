package app.controller;

public class Vertice
{
    private Vector agentRelativePosition;
    private double pheramoneQuantity;

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

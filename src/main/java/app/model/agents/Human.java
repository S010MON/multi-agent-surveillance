package app.model.agents;

import app.controller.linAlg.Vector;

public class Human extends AgentImp
{
    private Vector nextMove;

    public Human(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        nextMove = new Vector();
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
    }

    @Override
    public Vector move()
    {
        Vector temp = position.add(nextMove);
        nextMove = new Vector();
        return temp;
    }

    public void walk(Vector nextMove)
    {
        this.nextMove = nextMove.scale(maxWalk);
    }

    public void sprint(Vector nextMove)
    {
        this.nextMove = nextMove.scale(maxSprint);
    }
}

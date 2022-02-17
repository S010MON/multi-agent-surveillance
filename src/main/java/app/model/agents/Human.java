package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.map.Move;

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
    public Move move()
    {
        Vector temp = position.add(nextMove);
        nextMove = new Vector();
        return new Move(new Vector(), temp);
    }

    public void walk(Vector nextMove)
    {
        this.nextMove = nextMove.scale(MAX_WALK);
    }

    public void run(Vector nextMove)
    {
        this.nextMove = nextMove.scale(MAX_RUN);
    }
}

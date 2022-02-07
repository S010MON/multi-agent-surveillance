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
    public void move()
    {
        position = position.add(nextMove);
        nextMove = new Vector();
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

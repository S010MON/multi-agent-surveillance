package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;

public class Human extends AgentImp
{
    private Vector nextMove;

    public Human(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
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
        Vector temp = nextMove;
        nextMove = new Vector();
        return new Move(new Vector(), temp);
    }

    public void walk(Vector nextMove)
    {
        this.nextMove = nextMove.scale(maxWalk);
    }

    public void sprint(Vector nextMove)
    {
        this.nextMove = nextMove.scale(45);
    }

    public void rotateTo(double degrees)
    {
        this.direction = new Vector(0, 1).rotate(degrees);
    }

    public void rotateLeft()
    {
        this.direction = direction.rotate(90);
    }

    public void rotateRight()
    {
        this.direction = direction.rotate(-90);
    }
}

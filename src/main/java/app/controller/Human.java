package app.controller;

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

    public void setNextMove(Vector nextMove)
    {
        this.nextMove = nextMove;
    }
}

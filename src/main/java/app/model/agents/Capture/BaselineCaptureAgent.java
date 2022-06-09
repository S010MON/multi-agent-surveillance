package app.model.agents.Capture;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.Agent;
import app.model.agents.AgentImp;

import java.util.ArrayList;

public class BaselineCaptureAgent extends AgentImp
{
    private final int MAX_TICS_WITHOUT_SIGHT = 1;
    private int counter = 0;

    public BaselineCaptureAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    public BaselineCaptureAgent(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
    }

    @Override
    public Move move()
    {
        System.out.println("Base Moving");
        ArrayList<Vector> availableMoves = findAvailableMoves();
        Vector move = new Vector();

        if(isTypeSeen(Type.INTRUDER))
            move = bestMove(availableMoves);
        else
            counter++;

        Vector change = move.sub(position);
        direction = change.normalise();
        return new Move(direction, change);
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
    }

    @Override
    public Agent nextState()
    {
        if(maxTicsReached())
            return new AcoAgent(this);
        return this;
    }

    private ArrayList<Vector> findAvailableMoves()
    {
        ArrayList<Vector> newLocations = new ArrayList<>();
        newLocations.add(new Vector(position.getX(), position.getY() + getMaxWalk())); // North
        newLocations.add(new Vector(position.getX() + getMaxWalk(), position.getY())); // East
        newLocations.add(new Vector(position.getX(), position.getY() - getMaxWalk())); // West
        newLocations.add(new Vector(position.getX() - getMaxWalk(), position.getY())); // South
        return newLocations;
    }

    private Vector bestMove(ArrayList<Vector> moves)
    {
        double shortestDist = Double.MAX_VALUE;
        Vector closestPoint = new Vector();
        for(Vector location : moves)
        {
            if(location.dist(typePosition) < shortestDist)
            {
                shortestDist = location.dist(typePosition);
                closestPoint = location;
            }
        }
        return closestPoint;
    }

    private boolean maxTicsReached()
    {
        return counter >= MAX_TICS_WITHOUT_SIGHT;
    }
}

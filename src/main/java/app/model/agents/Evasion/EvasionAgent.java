package app.model.agents.Evasion;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.WallFollow.WallFollowAgent;

public class EvasionAgent extends AgentImp
{
    private final int MAX_TICS_WITHOUT_SIGHT = 100;
    private int counter = 0;
    private Vector closestGuard;
    private Vector guardDirection;

    private EvasionStrategy strategy;
    private boolean guardInView = true;
    private double randomness = 0.3;

    public EvasionAgent(Vector position, Vector direction, double radius, Type type, EvasionStrategy strategy)
    {
        super(position, direction, radius, type);
        this.strategy = strategy;
    }

    public EvasionAgent(Vector position, Vector direction, double radius, Type type)
    {
        this(position, direction, radius, type, EvasionStrategy.RANDOMDIRECTED);
    }

    public EvasionAgent(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
        updateKnowledge();
    }

    @Override
    public Move move()
    {
        updateKnowledge();
        switch(strategy)
        {
            case DIRECTED -> {
                return moveDirected();
            }
            case RANDOMDIRECTED -> {
                return moveRandomDirected();
            }
            case RANDOM -> {
                return moveRandom();
            }
        }
        return null;
    }

    private void updateKnowledge()
    {
        Vector closestSeenGuard = closestTypePos(Type.GUARD);
        if(closestSeenGuard != null)
        {
            counter = 0;
            if(closestGuard == null)
            {
                closestGuard = closestSeenGuard;
                guardDirection = closestGuard.sub(position);
            }
            else if(closestSeenGuard.dist(position) < closestGuard.dist(position))
            {
                closestGuard = closestSeenGuard;
                guardDirection = closestGuard.sub(position);
            }
        }
        else
            counter++;
    }

    private Move moveRandom()
    {
        int theta = (int) (Math.random() * 360);
        Vector randomDirection = new Vector(0, 1).rotate(theta);

        return new Move(randomDirection, randomDirection.scale(maxSprint));
    }

    private Move moveRandomDirected()
    {
        if(guardDirection == null)
        {
            return moveRandom();
        }
        // randomness should be a number between 0 and 1
        int theta = (int) (Math.random() * 360);
        Vector randomDirection = new Vector(0, 1).rotate(theta);

        Vector flippedDirection = guardDirection.rotate(180);

        Vector mixedDirection = flippedDirection.scale(1 - randomness).add(randomDirection.scale(randomness));

        return new Move(guardDirection, mixedDirection.scale(maxSprint));
    }

    public Move moveDirected()
    {
        Vector flippedDirection = guardDirection.rotate(180);

        // Keep looking back to keep the agent in view
        return new Move(guardDirection, flippedDirection.scale(maxSprint));
    }

    private boolean maxTicsReached()
    {
        return (counter > MAX_TICS_WITHOUT_SIGHT);
    }

    @Override public Agent nextState()
    {
        if(maxTicsReached())
            return new WallFollowAgent(this);

        return this;
    }
}

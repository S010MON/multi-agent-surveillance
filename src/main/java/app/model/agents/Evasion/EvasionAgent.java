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
    protected Vector closestGuard;
    protected Vector guardDirection;

    private final int MAX_MOVES = 20;
    private int moveCounter;
    private EvasionStrategy strategy;
    private boolean guardInView = true;
    private final double RANDOMNESS = 0.3;
    private final double MAX_RANDOMNESS = 0.8;
    private boolean checkIfLegalMove = true;

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
                return moveRandomDirected(RANDOMNESS);
            }
            case RANDOM -> {
                return moveRandom();
            }
        }
        return null;
    }

    protected void updateKnowledge()
    {
        moveCounter = 0;
        Vector closestSeenGuard = closestTypePos(Type.GUARD);
        if(closestSeenGuard != null)
        {
            counter = 0;
            if(closestGuard == null && closestSeenGuard != null)
            {
                closestGuard = closestSeenGuard;
                guardDirection = closestGuard.sub(position).normalise();
            }
            else if(closestSeenGuard.dist(position) < closestGuard.dist(position))
            {
                closestGuard = closestSeenGuard;
                guardDirection = closestGuard.sub(position).normalise();
            }
        }
        else
            counter++;
    }

    private Move moveRandom()
    {
        int theta = (int) (Math.random() * 360);
        Vector randomDirection = new Vector(0, 1).rotate(theta);

        if(!checkIfLegalMove | noWallDetected(randomDirection, moveLength))
            return new Move(randomDirection, randomDirection.scale(moveLength));
        else if(moveCounter < MAX_MOVES)
        {
            moveCounter++;
            return moveRandom();
        }
        else
            return new Move(direction, new Vector(0,0));
    }

    private Move moveRandomDirected(double randomness)
    {
        if(guardDirection == null)
        {
            return moveRandom();
        }
        // randomness should be a number between 0 and 1
        int theta = (int) (Math.random() * (randomness/2) * 360);
        if(Math.random() < 0.5)
            theta = -theta;

        Vector flippedDirection = guardDirection.rotate(180);

        Vector mixedDirection = flippedDirection.rotate(theta);

        if(!checkIfLegalMove || noWallDetected(mixedDirection, moveLength))
            return new Move(guardDirection, mixedDirection.scale(moveLength));
        else if(moveCounter < MAX_MOVES)
        {
            moveCounter++;
            randomness = Math.max(MAX_RANDOMNESS, randomness+0.05);
            return moveRandomDirected(randomness);
        }
        else
            return new Move(direction, new Vector(0,0));
    }

    public Move moveDirected()
    {
        if(guardDirection == null)
        {
            return moveRandom();
        }
        Vector flippedDirection = guardDirection.rotate(180);

        // Keep looking back to keep the agent in view
        if(!checkIfLegalMove | noWallDetected(flippedDirection, moveLength))
            return new Move(guardDirection, flippedDirection.scale(moveLength));
        else
        {
            moveCounter++;
            return moveRandomDirected(0.1);
        }
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

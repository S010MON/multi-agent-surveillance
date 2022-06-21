package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;

public class AcoMomentumSpiralAvoidance extends AcoMomentum
{
    public AcoMomentumSpiralAvoidance(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    public AcoMomentumSpiralAvoidance(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
    }

    @Override
    public Move makeMove()
    {
        Vector movement;
        if(randomGenerator.nextDouble() < momentumHeuristic && momentumContinuityPossible())
        {
            movement = momentumContinuity;
        }
        else
        {
            avoidSpiral();
            movement = possibleMovements.get(randomGenerator.nextInt(possibleMovements.size()));
            turnQueue.add(movement);
        }

        direction = movement.normalise();
        previousPosition = position;
        previousMove = new Move(direction, movement);
        return new Move(direction, movement);
    }
}

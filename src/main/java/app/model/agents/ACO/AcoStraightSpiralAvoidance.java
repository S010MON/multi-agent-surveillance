package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;

public class AcoStraightSpiralAvoidance extends AcoStraight
{
    public AcoStraightSpiralAvoidance(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    @Override
    public Move makeMove()
    {
        Vector movement;
        avoidSpiral();
        if(randomGenerator.nextDouble() < momentumHeuristic && momentumContinuityPossible())
        {
            movement = momentumContinuity;
        }
        else
        {
            movement = possibleMovements.get(randomGenerator.nextInt(possibleMovements.size()));
            turnQueue.add(movement);
        }

        direction = movement.normalise();
        previousPosition = position;
        previousMove = new Move(direction, movement);
        return new Move(direction, movement);
    }
}

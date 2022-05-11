package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import lombok.Setter;

public class AcoMomentum extends AcoAgent
{
    @Setter protected double momentumHeuristic = 1.0;
    @Setter protected Vector momentumContinuity = new Vector();


    public AcoMomentum(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    @Override
    protected void initializeWorld()
    {
        super.initializeWorld();
        momentumContinuity = direction.scale(distance);
    }

    @Override
    protected void successfulMovement()
    {
        world.leaveVertex(previousPosition, maxPheromone);
        world.add_or_adjust_Vertex(position);

        momentumContinuity = previousMove.getDeltaPos();
        possibleMovements.clear();
        shortTermMemory.clear();
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
            movement = possibleMovements.get(randomGenerator.nextInt(possibleMovements.size()));
        }

        direction = movement.normalise();
        previousPosition = position;
        previousMove = new Move(direction, movement);
        return new Move(direction, movement);
    }

    protected boolean momentumContinuityPossible()
    {
        return possibleMovements.contains(momentumContinuity);
    }
}

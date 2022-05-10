package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Universe;
import lombok.Getter;
import lombok.Setter;

public class AcoStraight extends AcoAgent
{
    @Getter private static int acoAgentCount;
    @Getter private static int acoMoveCount;

    @Setter private double momentumHeuristic = 1.0;
    @Setter private Vector momentumContinuity = new Vector();


    public AcoStraight(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    @Override
    protected void initializeWorld()
    {
        Universe.init(type, distance);
        world = new AcoWorld(Universe.getMemoryGraph(type));
        world.add_or_adjust_Vertex(position);

        pheromoneSenseDirections();
        momentumContinuity = direction.scale(distance);
        tgtDirection = direction.copy();
        previousMove = new Move(direction, new Vector());
        previousPosition = position;
        acoAgentCount ++;
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

    private boolean momentumContinuityPossible()
    {
        return possibleMovements.contains(momentumContinuity);
    }
}
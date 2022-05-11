package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Universe;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class AcoRanking extends AcoAgent
{
    @Getter private static int acoAgentCount;
    @Getter private static int acoMoveCount;
    @Getter private ArrayList<Double> moveRanking = new ArrayList<>();
    @Setter private double stochasticHeuristic = 0.20;

    public AcoRanking(Vector position, Vector direction, double radius, Type type)
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
        tgtDirection = direction.copy();
        previousMove = new Move(direction, new Vector());
        previousPosition = position;
        acoAgentCount ++;
    }

    @Override
    public void explorationToViableMovement(Vector explorationDirection)
    {
        Ray cardinalRay = detectCardinalPoint(explorationDirection.getAngle());
        if(moveEvaluation(cardinalRay))
        {
            possibleMovements.add(explorationDirection.scale(distance));
            moveRanking.add(cardinalRay.length());
        }
        else
        {
            world.setVertexAsObstacle(position, explorationDirection.scale(distance));
        }
        nextBestOptionHandling(explorationDirection);
    }

    @Override
    public Move makeMove()
    {
        Vector move;
        double randomValue = randomGenerator.nextDouble();

        //Introduce stochastic element to avoid getting stuck
        if(randomValue < stochasticHeuristic)
        {
            move = possibleMovements.get(randomGenerator.nextInt(possibleMovements.size()));
        }
        // Move ranking procedure
        else
        {
            move = rankingProcedure();
        }
        previousPosition = position;
        previousMove = new Move(direction, move);
        direction = move.normalise();
        return new Move(direction, move);
    }

    private Vector rankingProcedure()
    {
        double longestMoveDistance = Double.MIN_VALUE;
        Vector longestMove = possibleMovements.get(0);

        for(int i = 0; i < possibleMovements.size(); i++)
        {
            if(longestMoveDistance < moveRanking.get(i))
            {
                longestMoveDistance = moveRanking.get(i);
                longestMove = possibleMovements.get(i);
            }
        }
        return longestMove;
    }

    @Override
    protected boolean selectNextPossibleMove()
    {
        int index = possibleMovements.indexOf(previousMove.getDeltaPos());
        possibleMovements.remove(index);
        moveRanking.remove(index);

        return !possibleMovements.isEmpty();
    }

    @Override
    protected void successfulMovement()
    {
        world.leaveVertex(previousPosition, maxPheromone);
        world.add_or_adjust_Vertex(position);

        possibleMovements.clear();
        moveRanking.clear();
        shortTermMemory.clear();
    }

    @Override
    protected void evaporateProcess()
    {
        acoMoveCount ++;
        if(acoMoveCount >= acoAgentCount)
        {
            world.evaporateWorld();
            acoMoveCount = acoMoveCount - acoAgentCount;
        }
    }
}

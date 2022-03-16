package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.map.Move;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;


public class AcoAgentLimitedVision extends AcoAgent360Vision
{
    @Getter private ArrayList<Vector> pheromoneDirections = new ArrayList<Vector>();
    private Vector directionBias = new Vector();
    private Stack<Vector> visualDirectionsToExplore = new Stack<>();
    @Getter private ArrayList<Vector> possibleMovements = new ArrayList<>();

    public AcoAgentLimitedVision(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        pheromoneSenseDirections();
    }

    /*
    1. Agent accesses smells in 360 degrees
    2. Determines directions of lowest pheromones
    3. Determines if directions are viable through vision ~ (Randomly) (If not viable ...)
    4. Moves in that direction
     */
    @Override
    public Move move()
    {
        if(visualDirectionsToExplore.empty() && possibleMovements.isEmpty())
        {
            Move nextMove = pheromonesDetection();
            return nextMove;
        }
        else if(visualDirectionsToExplore.empty() && !possibleMovements.isEmpty())
        {
            return makeMove();
        }
        else
        {
            return visibleExploration();
        }
    }

    public Move visibleExploration()
    {
        explorationToViableMovement();
        nextExplorationVisionDirection();

        previousMove = new Move(position, new Vector());
        return new Move(position, new Vector());
    }

    public Move pheromonesDetection()
    {
        ArrayList<Double> pheromoneValues = accessAvaliableCellPheromones(pheromoneDirections);
        directionsToVisiblyExplore(pheromoneValues);
        direction = visualDirectionsToExplore.peek().normalise();

        previousMove = new Move(position, new Vector());
        return new Move(position, new Vector());
    }

    public Move makeMove()
    {
        //Randomly or with bias select move
        //Take into account agent clash
        Vector move = possibleMovements.get(randomGenerator.nextInt(possibleMovements.size()));
        previousMove = new Move(position, move.scale(cellSize));
        possibleMovements.clear();

        return new Move(position, move.scale(cellSize));
    }

    public void directionsToVisiblyExplore(ArrayList<Double> pheromoneValues)
    {
        double minValue = Double.MAX_VALUE;
        visualDirectionsToExplore = new Stack<>();

        for(int i = 0; i < pheromoneValues.size(); i++)
        {
            Double pheromoneValue = pheromoneValues.get(i);

            if(pheromoneValue != null && pheromoneValue == minValue)
            {
                visualDirectionsToExplore.add(pheromoneDirections.get(i));
            }
            else if(pheromoneValue != null && pheromoneValues.get(i) < minValue)
            {
                visualDirectionsToExplore.clear();
                minValue = pheromoneValues.get(i);
                visualDirectionsToExplore.add(pheromoneDirections.get(i));
            }
        }
    }

    public void pheromoneSenseDirections()
    {
        for(int cardinalAngle: cardinalAngles)
        {
            pheromoneDirections.add(angleToGridMovementLink(cardinalAngle));
        }
    }

    public void explorationToViableMovement()
    {
        Ray cardinalRay = detectCardinalPoint(direction.getAngle());
        visualDirectionsToExplore.pop();
        if(movePossible(cardinalRay))
        {
            possibleMovements.add(direction);
        }
    }

    public void nextExplorationVisionDirection()
    {
        if(!visualDirectionsToExplore.empty())
        {
            Vector directionToExplore = visualDirectionsToExplore.peek();
            direction = directionToExplore.normalise();
        }
    }

    public boolean movePossible(Ray cardinalRay)
    {
        if(cardinalRay.rayLength() > cellSize + epsilon)
        {
            return true;
        }
        return false;
    }

    @Override
    public Ray detectCardinalPoint(double targetCardinalAngle)
    {
        for(Ray ray: view)
        {
            if(approximateAngleRange(ray.angle(), targetCardinalAngle))
            {
                return ray;
            }
        }
        throw new RuntimeException("Cardinal point not found");
    }

    public int getSizeOfDirectionsToVisiblyExplore()
    {
        int count = 0;
        while(!visualDirectionsToExplore.empty())
        {
            visualDirectionsToExplore.pop();
            count ++;
        }
        return count;
    }
}

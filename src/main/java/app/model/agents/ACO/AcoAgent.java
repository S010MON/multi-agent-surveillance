package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Grid.AcoGrid;
import app.model.agents.AgentImp;
import app.model.agents.Cells.PheromoneCell;
import app.model.map.Move;

import java.util.ArrayList;
import java.util.HashMap;

public class AcoAgent extends AgentImp
{
    //TODO Place actual max pheramone value
    private double maxPheramone = 10;
    private static AcoGrid world;
    private double cellSize = world.getCellSize();

    private HashMap<Integer, PheromoneCell> agentMap = new HashMap<>();

    private int[] cardinalAngles = {0, 90, 180, 270};
    private double epsilon = 0.5;

    public AcoAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        world.updateAgent(this);
    }

    // 1. Agent looks around
    // 2. Determine cardinal rays
    // 3. Determine positions to move to that aren't blocked (Wall or agent)
    // 4. Determine pheramone value at each position
    // 5. Determine best move and execute it.
    // 6. After all agents have gone, perform evaporation procedure
    @Override
    public Move move()
    {
        Ray[] cardinalRays = detectCardinalPoints();
        return null;
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
        world.updateAgent(this);
    }

    public ArrayList<Vector> determineAvaliableMovements(Ray[] cardinalRays)
    {
        ArrayList<Vector> possibleMovements = new ArrayList<>();

        for(int i = 0; i < cardinalRays.length; i++)
        {
            if(cardinalRays[i].rayLength() >= cellSize)
            {
                possibleMovements.add(angleToGridMovementLink(cardinalAngles[i]));
            }
        }
        return null;
    }

    public Vector angleToGridMovementLink(int angle)
    {
        switch(angle)
        {
            case 0:
                return new Vector(1, 0);
            case 90:
                return new Vector(0, 1);
            case 180:
                return new Vector(-1, 0);
            case 270:
                return new Vector(0, -1);
            default:
                return null;
        }
    }

    public Ray[] detectCardinalPoints()
    {
        Ray[] cardinalRays = new Ray[cardinalAngles.length];

        for(int i = 0; i < cardinalAngles.length; i++)
        {
            cardinalRays[i] = detectCardinalPoint(cardinalAngles[i]);
        }
        return cardinalRays;
    }

    public Ray detectCardinalPoint(double targetCardinalAngle)
    {
        int upperBound = view.size() - 1;
        int lowerBound = 0;

        while(lowerBound <= upperBound)
        {
            int midPoint = calculateMidPoint(upperBound, lowerBound);
            double currentAngle = view.get(midPoint).angle();

            if(approximateAngleRange(currentAngle, targetCardinalAngle))
            {
                System.out.println("Approximate Ray found, angle: " + currentAngle);
                return view.get(midPoint);
            }
            else if(currentAngle < targetCardinalAngle)
            {
                lowerBound = midPoint + 1;
            }
            else if(currentAngle > targetCardinalAngle)
            {
                upperBound = midPoint - 1;
            }
        }
        return null;
    }

    public int calculateMidPoint(int upperBound, int lowerBound)
    {
        return lowerBound + (upperBound - lowerBound)/2;
    }

    public boolean approximateAngleRange(double detectedAngle, double targetAngle)
    {
        if(detectedAngle < (targetAngle + epsilon) && detectedAngle > (targetAngle - epsilon))
        {
            return true;
        }
        return false;
    }

    public double releaseMaxPheramone()
    {
        return maxPheramone;
    }

    public static void initializeWorld(double width, double height)
    {
        world = new AcoGrid(height, width);
    }
    public static AcoGrid accessWorld()
    {
        return world;
    }

    public int[] getCardinalAngles()
    {
        return cardinalAngles;
    }
}

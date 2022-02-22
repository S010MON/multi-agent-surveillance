package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Grid.AcoGrid;
import app.model.agents.AgentImp;
import app.model.agents.Cells.PheromoneCell;

import java.util.HashMap;

public class AcoAgent extends AgentImp
{
    //TODO Place actual max pheramone value
    private double maxPheramone = 10;
    private static AcoGrid world;

    private HashMap<Integer, PheromoneCell> agentMap = new HashMap<>();

    private double epsilon = 1.0;

    public AcoAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        world.updateAgent(this);
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
        world.updateAgent(this);
    }

    public Ray detectCardialPoint(double targetCardinalAngle)
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
                lowerBound = midPoint - 1;
            }
            else if(currentAngle > targetCardinalAngle)
            {
                upperBound = midPoint - 1;
            }
        }
        throw new RuntimeException("Cardinal point not contained within visual field");
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
}

package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Cells.PheromoneCell;
import app.model.agents.MemoryGraph;
import app.model.agents.Team;
import app.model.agents.WallFollow.WfWorld;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class AcoAgent extends AgentImp
{
    //General
    @Getter @Setter private static int distance = 20;
    @Getter private static MemoryGraph<GraphCell, DefaultEdge> world = new AcoWorld(distance);
    @Getter private static int acoAgentCount;
    @Getter private static int acoMoveCount;

    @Getter private double maxPheromone = 2;
    private Random randomGenerator = new Random(1);
    private int[] cardinalAngles = {0, 90, 180, 270};

    @Getter private ArrayList<Vector> possibleMovements = new ArrayList<>();
    private Stack<Vector> visualDirectionsToExplore = new Stack<>();
    @Getter private ArrayList<Vector> pheromoneDirections = new ArrayList<Vector>();
    @Getter @Setter private double visionDistance = 100.0;
    @Getter protected Move previousMove;


    private Vector targetDirection;
    protected double epsilon = 0.3;

    public AcoAgent(Vector position, Vector direction, double radius, Team team)
    {
        super(position, direction, radius, team);
        targetDirection = direction;


        pheromoneSenseDirections();
        acoAgentCount ++;
        acoMoveCount ++;
    }

    @Override
    public Move move()
    {
        return null;
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
    }

    // Smell //
    public void smellPheromonesToVisualExplorationDirection()
    {
        ArrayList<Double> aggregatePheromones = accessAvailableCellAggregatePheromones();
        minimumPheromonesToDirections(aggregatePheromones);
    }

    public void minimumPheromonesToDirections(ArrayList<Double> pheromoneValues)
    {
        double minValue = Double.MAX_VALUE;
        visualDirectionsToExplore = new Stack<>();

        for(int i = 0; i < pheromoneValues.size(); i++)
        {
            Double pheromoneValue = pheromoneValues.get(i);

            if(pheromoneValue == minValue)
            {
                visualDirectionsToExplore.add(pheromoneDirections.get(i));
            }
            else if(pheromoneValues.get(i) < minValue)
            {
                visualDirectionsToExplore.clear();
                minValue = pheromoneValues.get(i);
                visualDirectionsToExplore.add(pheromoneDirections.get(i));
            }
        }
    }

    public ArrayList<Double> accessAvailableCellAggregatePheromones()
    {
        ArrayList<Double> cellPheromoneValues = new ArrayList<>();

        for(Vector movement : pheromoneDirections)
        {
            double aggregatePheromone = world.aggregateCardinalPheromones(position, movement);
            cellPheromoneValues.add(aggregatePheromone);
        }
        return cellPheromoneValues;
    }


    // Vision //
    /*
    1. Agent accesses smells in 360 degrees
    2. Determines directions of lowest pheromones
    3. Determines if directions are viable through vision ~ (Randomly) (If not viable ...)
    4. Moves in that direction
     */

    public Move visibleExploration()
    {
        explorationToViableMovement();
        nextExplorationVisionDirection();

        previousMove = new Move(position, new Vector());
        return new Move(position, new Vector());
    }

    public void explorationToViableMovement()
    {
        Ray cardinalRay = detectCardinalPoint(direction.getAngle());
        Vector currentDirection = visualDirectionsToExplore.pop();
        if(moveEvaluation(cardinalRay))
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

    public boolean moveEvaluation(Ray cardinalRay)
    {
        double rayLength = cardinalRay.rayLength();
        return(rayLength > distance + epsilon && rayLength < visionDistance + epsilon);
    }

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

    public boolean approximateAngleRange(double detectedAngle, double targetAngle)
    {
        return detectedAngle < (targetAngle + epsilon) && detectedAngle > (targetAngle - epsilon);
    }


    //World (SWARM memory)
    public void acceptWorld(WfWorld wfWorld)
    {
        world = wfWorld;
    }

    //Setup
    public void pheromoneSenseDirections()
    {
        for(int cardinalAngle: cardinalAngles)
        {
            pheromoneDirections.add(angleToGraphMovementLink(cardinalAngle));
        }
    }

    public Vector angleToGraphMovementLink(int angle)
    {
        return switch (angle)
                {
                    case 0 -> new Vector(0, distance);
                    case 90 -> new Vector(distance, 0);
                    case 180 -> new Vector(0, -distance);
                    case 270 -> new Vector(-distance, 0);
                    default -> null;
                };
    }
}

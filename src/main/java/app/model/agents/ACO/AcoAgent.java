package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.GraphCell;
import app.model.agents.MemoryGraph;
import app.model.agents.Team;
import app.model.agents.Universe;
import app.model.agents.WallFollow.WfWorld;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class AcoAgent extends AgentImp
{
    @Getter private static int acoAgentCount;
    @Getter private static int acoMoveCount;
    private static Random randomGenerator = new Random(1);

    @Getter private final double maxPheromone = 2;
    private final double epsilon = 0.3;
    private final int[] cardinalAngles = {0, 90, 180, 270};

    @Setter private double movementHeuristic = 1.0;
    @Setter private Vector movementContinuity = new Vector();
    @Getter private ArrayList<Vector> possibleMovements = new ArrayList<>();
    @Getter private ArrayList<Vector> pheromoneDirections = new ArrayList<>();
    @Getter private Stack<Vector> visualDirectionsToExplore = new Stack<>();
    @Getter private HashMap<Integer, Vector> shortTermMemory = new HashMap<>();
    @Getter @Setter private double visionDistance = 30.0;
    @Getter @Setter private int distance = 20;
    @Getter @Setter protected Move previousMove;


    public AcoAgent(Vector position, Vector direction, double radius, Team team)
    {
        super(position, direction, radius, team);
        initializeWorld();
    }

    @Override
    public Move move()
    {
        /*Short term memory after failed move*/
        if(moveFailed)
        {
            return shortTermMemory();
        }
        /*Detect pheromones and translate to directions to explore*/
        else if(visualDirectionsToExplore.isEmpty() && possibleMovements.isEmpty())
        {
            return smellPheromones();
        }
        /*Explore areas visually*/
        else if(!visualDirectionsToExplore.isEmpty())
        {
            return visibleExploration();
        }
        /*Make move*/
        else
        {
            return makeMove();
        }
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
        evaporateProcess();

        if(detectChangeInPosition())
        {
            successfulMovement();
        }
    }

    /* Setup */
    private void pheromoneSenseDirections()
    {
        for(int cardinalAngle: cardinalAngles)
        {
            pheromoneDirections.add(angleToGraphMovementLink(cardinalAngle));
        }
    }

    private Vector angleToGraphMovementLink(int angle)
    {
        return switch (angle)
                {
                    case 0 -> new Vector(0, -distance);
                    case 90 -> new Vector(distance, 0);
                    case 180 -> new Vector(0, distance);
                    case 270 -> new Vector(-distance, 0);
                    default -> null;
                };
    }

    private void initializeWorld()
    {
        Universe.init(team, distance);
        world = new AcoWorld(Universe.getMemoryGraph(team));

        pheromoneSenseDirections();
        world.add_or_adjust_Vertex(position);
        movementContinuity = direction.scale(distance);
        tgtDirection = direction.copy();
        previousMove = new Move(position, new Vector());
        acoAgentCount ++;
    }

    /* Memory */
    private Move shortTermMemory()
    {
        // Case 1: possibleMoves not empty -> select next move and make it
        if(selectNextPossibleMove())
        {
            return makeMove();
        }
        else
        {
            //Case 2: possibleMovesEmpty -> Remove this pheromone sensing direction (Condition to be added)
            return revaluateSenses();
        }
    }

    private void relyOnMemory()
    {
        shortTermMemory.forEach((k, v) -> possibleMovements.add(v));
    }

    private boolean selectNextPossibleMove()
    {
        possibleMovements.remove(previousMove.getDeltaPos());
        return !possibleMovements.isEmpty();
    }

    private Move revaluateSenses()
    {
        Vector previousMovement = previousMove.getDeltaPos();
        shortTermMemory.put(previousMovement.hashCode(), previousMovement);
        return new Move(position, new Vector());
    }

    /* Movement */
    private void successfulMovement()
    {
        world.leaveVertex(previousMove.getEndDir(), maxPheromone);
        world.add_or_adjust_Vertex(position);

        movementContinuity = previousMove.getDeltaPos();
        possibleMovements.clear();
        shortTermMemory.clear();
    }

    private boolean detectChangeInPosition()
    {
        return (!previousMove.getEndDir().equals(position));
    }

    public Move makeMove()
    {
        Vector movement;
        if(randomGenerator.nextDouble() < movementHeuristic && movementContinuityPossible())
        {
            movement = movementContinuity;
        }
        else
        {
            movement = possibleMovements.get(randomGenerator.nextInt(possibleMovements.size()));
        }

        direction = movement.normalise();
        previousMove = new Move(position, movement);
        return new Move(position, movement);
    }

    private boolean movementContinuityPossible()
    {
        return possibleMovements.contains(movementContinuity);
    }

    /* Smell */
    public Move smellPheromones()
    {
        smellPheromonesToVisualExplorationDirection();
        previousMove = new Move(position, new Vector());
        return new Move(position, new Vector());
    }

    public void smellPheromonesToVisualExplorationDirection()
    {
        ArrayList<Double> aggregatePheromones = accessAvailableCellAggregatePheromones();
        minimumPheromonesToDirections(aggregatePheromones);
        direction = visualDirectionsToExplore.peek().normalise();
        if(visualDirectionsToExplore.isEmpty())
        {
            relyOnMemory();
        }
    }

    public void minimumPheromonesToDirections(ArrayList<Double> pheromoneValues)
    {
        double minValue = Double.MAX_VALUE;
        visualDirectionsToExplore = new Stack<>();

        for(int i = 0; i < pheromoneValues.size(); i++)
        {
            Double pheromoneValue = pheromoneValues.get(i);
            Vector pheromoneDirection = pheromoneDirections.get(i);
            boolean movementInMemory = shortTermMemory.get(pheromoneDirection.hashCode()) != null;

            if(!movementInMemory && pheromoneValue == minValue)
            {
                visualDirectionsToExplore.add(pheromoneDirection);
            }
            else if(!movementInMemory && pheromoneValues.get(i) < minValue)
            {
                visualDirectionsToExplore.clear();
                minValue = pheromoneValues.get(i);
                visualDirectionsToExplore.add(pheromoneDirection);
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

    /* Vision */
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
            possibleMovements.add(direction.scale(distance));
        }
        else
        {
            world.setVertexAsObstacle(position, currentDirection);
        }
        nextBestOptionHandling(currentDirection);
    }

    private void nextBestOptionHandling(Vector currentDirectionExplored)
    {
        shortTermMemory.put(currentDirectionExplored.hashCode(), currentDirectionExplored);

        if(!possibleMovements.isEmpty())
        {
            shortTermMemory.clear();
        }
    }

    private void nextExplorationVisionDirection()
    {
        if(!visualDirectionsToExplore.empty())
        {
            Vector directionToExplore = visualDirectionsToExplore.peek();
            direction = directionToExplore.normalise();
        }
    }

    public boolean moveEvaluation(Ray cardinalRay)
    {
        double rayLength = cardinalRay.length();
        return(rayLength > visionDistance + epsilon);
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

    private boolean approximateAngleRange(double detectedAngle, double targetAngle)
    {
        return detectedAngle < (targetAngle + epsilon) && detectedAngle > (targetAngle - epsilon);
    }

    //World (SWARM memory)
    private void evaporateProcess()
    {
        acoMoveCount ++;
        if(acoMoveCount >= acoAgentCount)
        {
            world.evaporateWorld();
            acoMoveCount = acoMoveCount - acoAgentCount;
        }
    }
}

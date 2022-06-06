package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.TypeInformation;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.Type;
import app.model.agents.Capture.CaptureAgent;
import app.model.agents.Universe;
import app.model.agents.WallFollow.WallFollowAgent;
import app.model.agents.WallFollow.WfWorld;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class AcoAgent extends AgentImp
{
    @Getter private static int acoAgentCount;
    @Getter private static int acoMoveCount;
    protected static Random randomGenerator = new Random(1);

    @Getter protected final double maxPheromone = 2;
    private final double epsilon = 0.5;
    private final int[] cardinalAngles = {0, 90, 180, 270};
    @Getter protected ArrayList<Vector> possibleMovements = new ArrayList<>();
    @Getter private ArrayList<Vector> pheromoneDirections = new ArrayList<>();
    @Getter private Stack<Vector> visualDirectionsToExplore = new Stack<>();
    @Getter protected HashMap<Integer, Vector> shortTermMemory = new HashMap<>();
    @Getter @Setter private double visionDistance = 25.0;
    @Getter @Setter protected Move previousMove;
    protected Vector previousPosition;
    @Getter protected Queue<Vector> turnQueue = new LinkedList<>();


    public AcoAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        initializeWorld();
        acoAgentCount ++;
    }

    public AcoAgent(Agent other)
    {
        super(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        this.direction = directionOfAngle(direction.getAngle());
        copyOver(other);
        if(other.getWorld() != null)
            this.world = new AcoWorld(other.getWorld().getG());
        else
            initializeWorld();
        acoAgentCount ++;
    }

    protected void copyOver(Agent other)
    {
        super.copyOver(other);

        if(other.getWorld() != null)
        {
            this.world = new AcoWorld(other.getWorld().getG());
            world.add_or_adjust_Vertex(position);

            pheromoneSenseDirections();
            tgtDirection = direction.copy();
            previousMove = new Move(direction, new Vector());
            previousPosition = position;
        }
        else
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
    protected void pheromoneSenseDirections()
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
                    case 0 -> new Vector(0, -moveLength);
                    case 90 -> new Vector(moveLength, 0);
                    case 180 -> new Vector(0, moveLength);
                    case 270 -> new Vector(-moveLength, 0);
                    default -> null;
                };
    }

    protected void initializeWorld()
    {
        Universe.init(type, (int)moveLength);
        world = new AcoWorld(Universe.getMemoryGraph(type));
        world.add_or_adjust_Vertex(position);

        pheromoneSenseDirections();
        tgtDirection = direction.copy();
        previousMove = new Move(direction, new Vector());
        previousPosition = position;
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

    protected boolean selectNextPossibleMove()
    {
        possibleMovements.remove(previousMove.getDeltaPos());
        return !possibleMovements.isEmpty();
    }

    private Move revaluateSenses()
    {
        Vector previousMovement = previousMove.getDeltaPos();
        shortTermMemory.put(previousMovement.hashCode(), previousMovement);
        previousPosition = position;
        return new Move(direction, new Vector());
    }

    /* Movement */
    protected void successfulMovement()
    {
        world.leaveVertex(previousPosition, maxPheromone);
        world.add_or_adjust_Vertex(position);

        possibleMovements.clear();
        shortTermMemory.clear();
    }

    private boolean detectChangeInPosition()
    {
        return (!previousPosition.equals(position));
    }

    public Move makeMove()
    {
        avoidSpiral();
        Vector movement = possibleMovements.get(randomGenerator.nextInt(possibleMovements.size()));

        if(movement != previousMove.getDeltaPos())
        {
            turnQueue.add(movement);
        }
        direction = movement.normalise();
        previousPosition = position;
        previousMove = new Move(direction, movement);
        return new Move(direction, movement);
    }

    protected void avoidSpiral()
    {
        if(turnQueue.size() == 4)
        {
            Vector headOfQueue = turnQueue.remove();
            if(possibleMovements.contains(headOfQueue) && possibleMovements.size() >= 2)
            {
                possibleMovements.remove(headOfQueue);
                turnQueue.clear();
            }
        }
    }

    /* Smell */
    public Move smellPheromones()
    {
        smellPheromonesToVisualExplorationDirection();
        previousPosition = position;
        previousMove = new Move(direction, new Vector());
        return new Move(direction, new Vector());
    }

    public void smellPheromonesToVisualExplorationDirection()
    {
        ArrayList<Double> aggregatePheromones = accessAvailableCellAggregatePheromones();
        minimumPheromonesToDirections(aggregatePheromones);
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
                visualDirectionsToExplore.add(pheromoneDirection.normalise());
            }
            else if(!movementInMemory && pheromoneValues.get(i) < minValue)
            {
                visualDirectionsToExplore.clear();
                minValue = pheromoneValues.get(i);
                visualDirectionsToExplore.add(pheromoneDirection.normalise());
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
        explorationToViableMovements();

        previousPosition = position;
        previousMove = new Move(direction, new Vector());
        return new Move(direction, new Vector());
    }

    public void explorationToViableMovements()
    {
        ArrayList<Vector> directionStillToExplore = new ArrayList<>();


        while(!visualDirectionsToExplore.isEmpty())
        {
            Vector currentExplorationDirection = visualDirectionsToExplore.pop();

            try
            {
                explorationToViableMovement(currentExplorationDirection);
            }
            catch(Exception e)
            {
                directionStillToExplore.add(currentExplorationDirection);
            }
        }

        // Directions not within current field of view
        if (!directionStillToExplore.isEmpty())
        {
            visualDirectionsToExplore.addAll(directionStillToExplore);
            nextExplorationVisionDirection();
        }
    }

    public void explorationToViableMovement(Vector explorationDirection)
    {
        Ray cardinalRay = detectCardinalPoint(explorationDirection.getAngle());
        if(moveEvaluation(cardinalRay))
        {
            possibleMovements.add(explorationDirection.scale(moveLength));
        }
        else
        {
            world.setVertexAsObstacle(position, explorationDirection.scale(moveLength));
        }
        nextBestOptionHandling(explorationDirection);
    }

    protected void nextBestOptionHandling(Vector currentDirectionExplored)
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
        ArrayList<Ray> solidAndNullView = filterSolidAndNullOnly(view);
        for (Ray ray : solidAndNullView)
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
    protected void evaporateProcess()
    {
        acoMoveCount ++;
        if(acoMoveCount >= acoAgentCount)
        {
            world.evaporateWorld();
            acoMoveCount = acoMoveCount - acoAgentCount;
        }
    }

    private Vector directionOfAngle(double angle)
    {
        Vector direction = new Vector(0, -1);
        return direction.rotate(angle);
    }

    @Override
    public Agent nextState()
    {
        Agent newAgent = super.nextState();
        if(this != newAgent)
            acoAgentCount--;
        return newAgent;
    }
}

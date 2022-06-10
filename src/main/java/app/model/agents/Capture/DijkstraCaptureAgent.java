package app.model.agents.Capture;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.Move;
import app.model.Type;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.ACO.AcoWorld;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Universe;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DijkstraCaptureAgent extends AgentImp
{
    private final int MAX_TICS_WITHOUT_SIGHT = 10;
    private final int MAX_MOVES_BEFORE_RETURN = 5;
    private final int MAX_POSITIONS_HELD = 3;
    private final double SCALE_TARGET = 1.5;
    @Getter @Setter private VectorSet beliefSet = new VectorSet();
    @Getter private VectorSet shortTermMemory = new VectorSet();
    private Queue<Vector> intruderPreviousPos = new LinkedList<>();
    private Queue<Vector> previousPositions =  new LinkedList<>();
    private Move previousMove;
    private int counter = 0;

    /**
     * Constructor for capture agent.
     *
     * @param position    Our agents position.
     * @param direction   Our agents direction.
     * @param radius      Our radius.
     * @param type        Our type. (Guard).
     * @param intruderPos The position of the intruder to capture.
     */
    public DijkstraCaptureAgent(Vector position, Vector direction, double radius, Type type, Vector intruderPos)
    {
        super(position, direction, radius, type);
        beliefSet.add(intruderPos);
        intruderPreviousPos.add(intruderPos);
        initializeWorld();
    }

    public DijkstraCaptureAgent(Vector position, Vector direction, double radius, Type type, double moveLength)
    {
        super(position, direction, radius, type);
        this.moveLength = moveLength;
        initializeWorld();
    }

    public DijkstraCaptureAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        initializeWorld();
    }

    public DijkstraCaptureAgent(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
    }

    protected void copyOver(Agent other)
    {
        super.copyOver(other);

        if(other.getWorld() != null)
        {
            this.world = new AcoWorld(other.getWorld().getG());
            world.add_or_adjust_Vertex(position);

            previousMove = new Move(direction, new Vector());
            previousPositions.add(position);
        }
        else
            initializeWorld();
    }

    @Override
    public Move move()
    {
        if(!maxTicsReached())
        {
            updateKnowledge();
            if(moveFailed){
                return shortTermMemory();
            }else {
                shortTermMemory.clear();
            }
        }
        setShortTermMemory();
        return nextMove(findTarget());
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
    }

    /**
     * Updates the internal state of the agent.
     */
    public void updateKnowledge(){
        if(isTypeSeen(Type.INTRUDER))
        {
            updatePositionHistory();
            beliefSet.clear();
            beliefSet.add(closestTypePos(Type.INTRUDER));
        }
        else
        {
            updateBeliefSet();
            intruderPreviousPos.clear();
        }
        updatePrevPositions();
        updateCounter();
    }

    public Move shortTermMemory()
    {
        shortTermMemory.remove(position.add(previousMove.getDeltaPos()));

        // Remove possible moves that we have been in recently when we do not see the intruder.
        if(beliefSet.size() != 1)
            checkPrevPositions(shortTermMemory);

        Vector wantedMove = closestLocationInArray(new ArrayList<>(shortTermMemory), findTarget());
        Vector deltaPos = wantedMove.sub(position);
        Vector direction = deltaPos.normalise();
        previousMove = new Move(direction, deltaPos);
        return new Move(direction, deltaPos);
    }

    public void setShortTermMemory()
    {
        shortTermMemory.addAll(findAllPossiblePositions(position));
    }

    /**
     * Will loop through belief set and add all the next possible positions to the set.
     * Only unique positions are stored.
     */
    public void updateBeliefSet()
    {
        VectorSet newLocations = new VectorSet();
        for(Vector location : beliefSet)
        {
            newLocations.addAll(findAllPossiblePositions(location));
        }
        checkLocationsNotInObstacle(newLocations);
        beliefSet.addAll(newLocations);
        checkLocationsVisible(beliefSet);
    }

    /**
     * Returns the 4 cardinal locations reachable in one move.
     *
     * @param location Centre location to calculate from.
     *
     * @return The set of 4 vectors.
     */
    public VectorSet findAllPossiblePositions(Vector location)
    {
        VectorSet newLocations = new VectorSet();
        newLocations.add(new Vector(location.getX(), location.getY() + getMaxWalk())); // North
        newLocations.add(new Vector(location.getX() + getMaxWalk(), location.getY())); // East
        newLocations.add(new Vector(location.getX(), location.getY() - getMaxWalk())); // West
        newLocations.add(new Vector(location.getX() - getMaxWalk(), location.getY())); // South
        return newLocations;
    }


    /**
     * Removes vectors that are visible from beliefSet.
     *
     * @param locations Potential intruder locations.
     */
    public void checkLocationsVisible(VectorSet locations)
    {
        for(Ray r : view)
        {
            locations.removeIf(location -> Intersection.hasLimitedIntersection(r, location, 1));
        }
    }

    public void checkLocationsNotInObstacle(VectorSet locations)
    {
        for(Vector v: locations)
        {
            GraphCell cell = world.getVertexAt(v);
            if(cell != null && cell.getObstacle())
                locations.remove(v);
        }
    }

    /**
     * Picks a location in the belief set to use as a target for the next move.
     *
     * @return the vector location to target.
     */
    public Vector findTarget()
    {
        //Using heuristics decides on target for next move.
        // If our belief set is size 1, then we have our target.
        if(beliefSet.size() == 1 && intruderPreviousPos.size() > 0)
            return checkMomentum(new ArrayList<>(intruderPreviousPos));

        // Finds the centre of the belief region.
        Vector centre = findCentreOfRegion(new ArrayList<>(beliefSet));

        // Targets location closest to the centre.
        return closestLocationInArray(new ArrayList<>(beliefSet), centre);
    }

    /**
     * This method checks if the intruder is moving in a straight line, and calculates a target
     * point in front of the intruder to try and intercept.
     *
     * @param intruderHistory The previous actual positions of the intruder.
     *
     * @return The target for our next move.
     */
    public Vector checkMomentum(ArrayList<Vector> intruderHistory)
    {
        int arrLength = intruderHistory.size();
        if(arrLength == MAX_POSITIONS_HELD)
        {
            if(intruderHistory.get(arrLength-1).equals(intruderHistory.get(0)))
                return intruderHistory.get(arrLength-1);

            Line line = new Line(intruderHistory.get(0), intruderHistory.get(arrLength-1));
            for(int i = 1; i < arrLength - 1; i++)
            {
                if(!line.liesOn(intruderHistory.get(i)))
                    return intruderHistory.get(arrLength-1);
            }

            Vector intruderDirection = findDirection(intruderHistory.get(arrLength-1),
                    intruderHistory.get(0));
            double distBetween = intruderHistory.get(arrLength-1).dist(position) * SCALE_TARGET;
            return intruderHistory.get(arrLength-1).add(intruderDirection.scale(distBetween));
        }
        return intruderHistory.get(arrLength-1);
    }


    @Override
    public Agent nextState()
    {
        if(maxTicsReached())
            return new AcoAgent(this);

        return this;
    }

    /**
     * Finds the direction the intruder is travelling in.
     *
     * @param head Where the intruder is now.
     * @param tail Where the intruder was when it started moving in a straight line.
     *
     * @return The direction vector that the intruder is following.
     */
    private Vector findDirection(Vector head, Vector tail)
    {
        Vector diff = head.sub(tail);
        return diff.normalise();
    }

    /**
     * Finds centre of the belief region denoted by the belief set.
     *
     * @param beliefRegion The belief set.
     * @return The centre of the region as a vector.
     */
    private Vector findCentreOfRegion(ArrayList<Vector> beliefRegion)
    {
        double totalX = 0;
        double totalY = 0;
        for(Vector location : beliefRegion)
        {
            totalX += location.getX();
            totalY += location.getY();
        }
        return new Vector(totalX / beliefRegion.size(), totalY / beliefRegion.size());
    }

    /**
     * Finds the closest vector to given point.
     *
     * @param locations Array of locations.
     * @param pos       Position to compare to.
     * @return Closest vector to pos from locations.
     */
    private Vector closestLocationInArray(ArrayList<Vector> locations, Vector pos)
    {
        double shortestDist = Double.MAX_VALUE;
        Vector closestPoint = new Vector(0,0);
        for(Vector location : locations)
        {
            if(location.dist(pos) < shortestDist)
            {
                shortestDist = location.dist(pos);
                closestPoint = location;
            }
        }

        return closestPoint;
    }

    /**
     * Chooses the cardinal movement which gets the agent closer to the target according to Dijkstras Algorithm.
     *
     * @param target the vector location to move towards.
     * @return The next Move.
     */
    private Move nextMove(Vector target)
    {
        //get GraphCell containing target
        GraphCell targetCell = world.getVertexAt(target);
        GraphCell currentCell = world.getVertexAt(position);

        if(targetCell != null && currentCell != null)
        {

            // Calculate Dijkstra path
            List<GraphCell> currentPathToNextVertex = DijkstraShortestPath.findPathBetween(world.G,
                    currentCell, targetCell).getVertexList();

            // get direction of right move
            GraphCell nextVertex = currentPathToNextVertex.get(0);
            if(nextVertex.equals(world.getVertexAt(position)))
            {
                currentPathToNextVertex.remove(nextVertex);
                nextVertex = currentPathToNextVertex.get(0);
            }
            direction = world.G.getNeighbourDir(world.getVertexAt(position), nextVertex);
            previousMove = new Move(direction, direction.normalise().scale(moveLength));
            return previousMove;
        }
        else
        {
            VectorSet possibleMoves = findAllPossiblePositions(position);

            // Remove possible moves that we have been in recently when we do not see the intruder.
            if(beliefSet.size() != 1)
                checkPrevPositions(possibleMoves);

            Vector wantedMove = closestLocationInArray(new ArrayList<>(possibleMoves), target);
            Vector changeInPos = wantedMove.sub(position);
            direction = changeInPos.normalise();
            previousMove = new Move(direction, changeInPos);
            return previousMove;
        }
    }

    private boolean maxTicsReached()
    {
        return (counter > MAX_TICS_WITHOUT_SIGHT);
    }

    private void updateCounter()
    {
        if(beliefSet.size() > 1)
            counter++;
        else if(beliefSet.size() == 1)
            counter = 0; // Reset counter when we see the intruder.
    }

    private void checkPrevPositions(VectorSet moves)
    {
        moves.removeIf(l -> previousPositions.contains(l) && moves.size() > 1);
    }

    private void updatePrevPositions()
    {
        previousPositions.add(position);
        if(previousPositions.size() > MAX_MOVES_BEFORE_RETURN)
            previousPositions.remove();
    }

    private void updatePositionHistory()
    {
        intruderPreviousPos.add(closestTypePos(Type.INTRUDER));
        if(intruderPreviousPos.size() > MAX_POSITIONS_HELD)
            intruderPreviousPos.remove();
    }

    protected void initializeWorld()
    {
        Universe.init(type, (int)moveLength);
        world = new AcoWorld(Universe.getMemoryGraph(type));
        world.add_or_adjust_Vertex(position);

        previousMove = new Move(direction, new Vector());
        previousPositions.add(position);
    }
}


package app.model.agents.Evasion;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Universe;
import app.model.agents.WallFollow.WfWorld;
import app.model.agents.World;
import app.model.boundary.Boundary;
import lombok.Getter;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class IntelligentEvasionAgent extends EvasionAgent
{
    private GraphicsEngine grEng = new GraphicsEngine();
    private ArrayList<GraphCell> pathToNextVertex = new ArrayList<>();
    private GraphCell targetVertex = null;
    @Getter private World world;
    private Map map;
    private ArrayList<Boundary> wallBoundaries;
    private final List<Vector> directions = Arrays.asList(new Vector(0,1),
            new Vector(1,0),
            new Vector(0,-1),
            new Vector(-1,0));

    public IntelligentEvasionAgent(Vector position, Vector direction, double radius, Type type, EvasionStrategy strategy)
    {
        super(position, direction, radius, type, strategy);
        initializeWorldGraph();
    }

    public IntelligentEvasionAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type, EvasionStrategy.INTELLIGENT);
        initializeWorldGraph();
    }

    public IntelligentEvasionAgent(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
        initializeWorldGraph();
        updateKnowledge();
    }

    private void initializeWorldGraph()
    {
        if (!Universe.isIsPerfectUniverse())
        {
            Universe.createPerfectUniverse(Universe.getMap().createFullGraph());
        }
        world = new WfWorld(Universe.getMemoryGraph(type));
        if (map != null)
        {
            map = Universe.getMap();
        }
        if (wallBoundaries != null)
        {
            wallBoundaries = grEng.collectWallBoundaries(map);
        }
    }

    /*
    Idea based on: http://lisc.mae.cornell.edu/LISCpresentations/SE_07_25_18.pdf
        main idea is to minimize observability of the evasion agent to the guard based on guard's predicted path/strategy

    Problems / questions:
        - how to check closed corner vs open space?
     */

    @Override protected Move moveIntelligent()
    {
        /*
        0. check if current target hiding place is still hidden from guard (if not clear path)
        1. if no current path to follow
            2. get predicted guard path
            3. get possible vertices
            4. calculate scores and pick best score vertex
            5. calculate shortest path to that vertex and save the path
        6. make move based on path if path exists, rotate otherwise (stay put)
         */
        if (targetVertex != null && !isHiddenPosition(targetVertex.getPosition(),closestGuard))
        {
            targetVertex = null;
            pathToNextVertex.clear();
        }
        if (pathToNextVertex.isEmpty())
        {
            List<GraphCell> predictedGuardPath = getPredictedGuardPath();
            HashMap<GraphCell,Integer> possibleVertices = getPossibleVertices(predictedGuardPath);
            setBestScoreVertexPath(possibleVertices);
        }
        if (!pathToNextVertex.isEmpty())
        {
            return getMoveBasedOnPath();
        }
        return new Move(rotateAgentRight(),new Vector(0,0));
    }

    /**
     * Calculate shortest path between guard's position and current evasion agent's position.
     */
    private List getPredictedGuardPath()
    {
        GraphCell guardVertex = world.getVertexAt(new Vector(closestGuard.getX(),closestGuard.getY()));
        GraphPath predictedPath = null;
        if (guardVertex != null && world.getVertexAt(position) != null){
            predictedPath = DijkstraShortestPath.findPathBetween(world.G, guardVertex, world.getVertexAt(position));
        }
        if (predictedPath != null)
        {
            return predictedPath.getVertexList();
        }
        ArrayList<GraphCell> guardVertexList = new ArrayList();
        guardVertexList.add(guardVertex);
        return guardVertexList;
    }

    /**
     * Get HashMap of vertices that are hidden from at least one point on the guard's predicted path.
     * @param predictedPath list of vertices on the predicted path of the guard
     * @return HashMap where keys are vertices that have at least one ray to guard's path with no direct line of sight
     *         and values are nr of such rays the vertex has
     */
    private HashMap<GraphCell,Integer> getPossibleVertices(List<GraphCell> predictedPath)
    {
        HashMap<GraphCell, Integer> possibleVertices = new HashMap<>();
        for (GraphCell vertex : predictedPath)
        {
            Vector guardPos = new Vector(vertex.getX(), vertex.getY());
            ArrayList<GraphCell> hiddenPositions = getHiddenPositions(guardPos);
            for (GraphCell pos : hiddenPositions)
            {
                if (possibleVertices.containsKey(pos))
                {
                    possibleVertices.put(pos, possibleVertices.get(pos) + 1);
                }
                else
                {
                    possibleVertices.put(pos,1);
                }
            }
        }
        return possibleVertices;
    }

    /**
     * Gets all the position vertices that are hidden from the guard's position.
     * @param guardPos guard agent's (possible) position
     * @return list of vertices that hold the hidden positions
     */
    private ArrayList<GraphCell> getHiddenPositions(Vector guardPos)
    {
        ArrayList<GraphCell> hiddenPositions = new ArrayList<>();
        for (Ray r : view)
        {
            Vector wallPoint = r.getV();
            if (isHiddenPosition(wallPoint, guardPos))
            {
                GraphCell wallPointVertex = world.getVertexAt(wallPoint);
                if (wallPointVertex != null)
                {
                    GraphCell vertex = world.getNonObstacleNeighbour(wallPointVertex);
                    if (!hiddenPositions.contains(vertex))
                    {
                        hiddenPositions.add(vertex);
                    }
                }
            }
        }
        return hiddenPositions;
    }

    /**
     * Checks if a possible position has no direct line of sight between guard's (predicted) position.
     * @param posToCheck
     * @param guardPos
     * @return
     */
    public boolean isHiddenPosition(Vector posToCheck, Vector guardPos)
    {
        Ray lineOfSight = new Ray(posToCheck,guardPos);
        double lineLength = lineOfSight.length();
        Vector intersection = grEng.getIntersection(lineOfSight, wallBoundaries);
        double actualSightLength = lineLength;
        if (intersection != null)
        {
            actualSightLength = new Ray(posToCheck, intersection).length();
        }
        return actualSightLength < lineLength;
    }

    /**
     * Set the path to the best (minimum) scored vertex
     * @param vertexMap possible vertices HashMap
     */
    private void setBestScoreVertexPath(HashMap<GraphCell,Integer> vertexMap)
    {
        double minScore = Double.MAX_VALUE;
        List<GraphCell> pathList = new ArrayList<>();
        for (GraphCell vertex : vertexMap.keySet())
        {
            GraphPath shortestPath = DijkstraShortestPath.findPathBetween(world.G, world.getVertexAt(position), vertex);
            double score = calculateVertexScore(vertexMap.get(vertex),shortestPath);
            if (score < minScore)
            {
                pathList = shortestPath.getVertexList();
            }
        }
        if (!pathList.isEmpty())
        {
            pathToNextVertex.addAll(pathList);
            targetVertex = pathToNextVertex.get(pathToNextVertex.size()-1);
        }
    }

    /**
     * Calculate score based on
     * 1. shortest path length,
     * 2. how many hidden lines of sight target vertex has,
     * score = shortest path length / hidden lines of sight
     * @return score
     */
    private double calculateVertexScore(int nrHiddenLines, GraphPath pathToVertex)
    {
        double score = Double.MAX_VALUE;
        if (pathToVertex != null)
        {
            score = pathToVertex.getLength();
        }
        score = score / nrHiddenLines;
        return score;
    }

    public Move getMoveBasedOnPath()
    {
        GraphCell nextVertex = pathToNextVertex.get(0);
        if (nextVertex.equals(world.getVertexAt(position)))
        {
            pathToNextVertex.remove(0);
            if (!pathToNextVertex.isEmpty())
            {
                nextVertex = pathToNextVertex.get(0);
            }

        }
        Vector nextDir = world.G.getNeighbourDir(world.getVertexAt(position), nextVertex);
        if (nextDir.equals(direction))
        {
            double deltaX = nextVertex.getX() - world.getVertexAt(position).getX();
            double deltaY = nextVertex.getY() - world.getVertexAt(position).getY();
            pathToNextVertex.remove(nextVertex);
            return new Move(direction, new Vector(deltaX,deltaY));
        }
        else
        {
            if (rotateAgentLeft().equals(nextDir))
            {
                return new Move(rotateAgentLeft(),new Vector(0,0));
            }
            else if (rotateAgentRight().equals(nextDir))
            {
                return new Move(rotateAgentRight(),new Vector(0,0));
            }
            else
            {
                return new Move(rotateAgentLeft(),new Vector(0,0));
            }
        }
    }

    public Vector rotateAgentLeft()
    {
        for (int i = 0; i <= directions.size(); i++)
        {
            if (direction.equals(directions.get(i)))
            {
                if (i == directions.size()-1)
                {
                    return directions.get(0);
                }
                else
                {
                    return directions.get(i+1);
                }
            }
        }
        throw new RuntimeException("None of the 4 cardinal directions were reached when rotating agent.");
    }

    public Vector rotateAgentRight()
    {
        for (int i = 0; i <= directions.size(); i++)
        {
            if (direction.equals(directions.get(i)))
            {
                if (i == 0)
                {
                    return directions.get(3);
                }
                else
                {
                    return directions.get(i-1);
                }
            }
        }
        throw new RuntimeException("None of the 4 cardinal directions were reached when rotating agent.");
    }

}

package app.model.agents.Evasion;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.Cells.GraphCell;
import app.model.boundary.Boundary;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IntelligentEvasionAgent extends EvasionAgent
{
    private GraphicsEngine grEng = new GraphicsEngine();

    public IntelligentEvasionAgent(Vector position, Vector direction, double radius, Type type, EvasionStrategy strategy)
    {
        super(position, direction, radius, type, strategy);
    }

    private IntelligentEvasionAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type, EvasionStrategy.INTELLIGENT);
    }

    private IntelligentEvasionAgent(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
        updateKnowledge();
    }

    /*
    Idea based on: http://lisc.mae.cornell.edu/LISCpresentations/SE_07_25_18.pdf
        main idea is to minimize observability of the evasion agent to the guard based on guard's predicted path/strategy
    The evasion strategy / algorithm:
        if guard / capture agent detected:
            calculate shortest path between me and guard
        get currently explored points within a range
            for each of the points calculate if there is direct line of sight between that point and guard agent
                (do that for points on the shortest path as well)
            use heuristics to pick the best point to move to
                score consist of dijkstra's shortest path length, lines of sight to the predicted guard path,
                direction of the agent, if point is close to a closed corner
        move to the picked point
        if wall encountered
            if nr of ticks passed since last seeing guard
                do wall following based on target direction
            else
                do wall following away from guard's path
     */

    /*
    Problems / questions:
        - lines of sight calculation to shortest path?
        - how to check closed corner vs open space?
        - how to select direction for wall following (direction agent?)
     */

    private Move moveIntelligent()
    {
        /*
        1. update knowledge
        2. get predicted guard path
        3. get possible vertices
        4. calculate scores and pick best score vertex
        5. calculate shortest path to that vertex
        6. move there
        7. WHAT HAPPENS NEXT? switch between wall following?
         */
        updateKnowledge();
        List<GraphCell> predictedGuardPath = getPredictedGuardPath();
        HashMap<GraphCell,Integer> possibleVertices = getPossibleVertices(predictedGuardPath);

        return new Move(direction, new Vector(0,0));
    }

    /**
     * Calculate shortest path between guard's position and current evasion agent's position.
     */
    private List getPredictedGuardPath()
    {
        GraphCell guardVertex = world.getVertexAt(new Vector(closestGuard.getX(),closestGuard.getY()));
        GraphPath predictedPath = DijkstraShortestPath.findPathBetween(world.G, guardVertex, world.getVertexAt(position));
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
        ArrayList<Boundary> boundaries = new ArrayList<>();
        if (world.map != null)
        {
            boundaries = grEng.collectBoundaries(getWorld().map);
        }
        else
        {
            throw new RuntimeException("Evasion agent does not have access to the map!");
        }
        for (Ray r : view)
        {
            Vector wallPoint = r.getV();
            Ray lineOfSight = new Ray(wallPoint,guardPos);
            double lineLength = lineOfSight.length();
            Vector intersection = grEng.getIntersection(lineOfSight, boundaries);
            double actualSightLength = lineLength;
            if (intersection != null)
            {
                actualSightLength = new Ray(wallPoint, intersection).length();
            }
            if (actualSightLength < lineLength)
            {
                GraphCell vertex = world.getNonObstacleNeighbour(world.getVertexAt(wallPoint));
                hiddenPositions.add(vertex);
            }
        }
        return hiddenPositions;
    }

}

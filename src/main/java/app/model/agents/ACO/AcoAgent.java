package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.GraphCell;
import app.model.agents.MemoryGraph;
import app.model.agents.Team;
import app.model.agents.WallFollow.WfWorld;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.DefaultEdge;

import java.util.Random;
import java.util.Set;

public class AcoAgent extends AgentImp
{
    //General
    @Getter private static MemoryGraph<GraphCell, DefaultEdge> world = new AcoWorld();
    @Getter private static int acoAgentCount;
    @Getter private static int acoMoveCount;

    @Getter private double maxPheromone = 2;
    private Random randomGenerator = new Random(1);
    private int[] cardinalAngles = {0, 90, 180, 270};

    private Vector targetDirection;
    protected double epsilon = 0.3;

    public AcoAgent(Vector position, Vector direction, double radius, Team team)
    {
        super(position, direction, radius, team);
        targetDirection = direction;

        world.addVertex(position, false, true, maxPheromone);
        world.setEdge(maxWalk);
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
        world.addVertex(position, false, true, maxPheromone);
    }

    // Vision //

    public void explorationToViableMovement()
    {
        Ray cardinalRay = detectCardinalPoint(direction.getAngle());
        Vector currentDirection = visualDirectionsToExplore.pop();
        if(movePossible(cardinalRay))
        {
            possibleMovements.add(direction);
        }
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

    public boolean movePossible(Ray cardinalRay)
    {
        return (cardinalRay.rayLength() > maxWalk + epsilon);
    }

    public void worldEvaporationProcess()
    {
        Set<GraphCell> vertexSet = world.vertexSet();
        vertexSet.forEach((GraphCell cell) -> cell.evaporate());
    }

    public void acceptWorld(WfWorld wfWorld)
    {
        world = wfWorld;
    }
}

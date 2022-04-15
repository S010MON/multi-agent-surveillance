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


    private Vector targetDirection;
    protected double epsilon = 0.3;

    public AcoAgent(Vector position, Vector direction, double radius, Team team)
    {
        super(position, direction, radius, team);
        targetDirection = direction;


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


    /*
    Smell utilizes concept of diffusion, such that an aggregate pheromone value is determined
    for cardinal directions
     */
    public ArrayList<Double> accessAggregateCellPheromones()
    {
        return null;
    }




    // Vision //
    /*
    1. Agent accesses smells in 360 degrees
    2. Determines directions of lowest pheromones
    3. Determines if directions are viable through vision ~ (Randomly) (If not viable ...)
    4. Moves in that direction
     */


    //World (SWARM memory)
    public void acceptWorld(WfWorld wfWorld)
    {
        world = wfWorld;
    }
}

package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Universe;
import lombok.Getter;

public class AcoMid extends AcoStraight
{
    @Getter private static int acoAgentCount;
    @Getter private static int acoMoveCount;

    public AcoMid(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        momentumHeuristic = 0.5;
    }

    @Override
    protected void initializeWorld()
    {
        Universe.init(type, distance);
        world = new AcoWorld(Universe.getMemoryGraph(type));
        world.add_or_adjust_Vertex(position);

        pheromoneSenseDirections();
        momentumContinuity = direction.scale(distance);
        tgtDirection = direction.copy();
        previousMove = new Move(direction, new Vector());
        previousPosition = position;
        acoAgentCount ++;
    }

    @Override
    protected void evaporateProcess()
    {
        acoMoveCount ++;
        if(acoMoveCount >= acoAgentCount)
        {
            world.evaporateWorld();
            acoMoveCount = acoMoveCount - acoAgentCount;
        }
    }
}

package app.model.agents.Evasion;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.WallFollow.WallFollowAgent;

import java.util.Random;

public class EvasionRandom extends AgentImp
{
    protected Random random = new Random(1);
    protected final int MAX_TICS_WITHOUT_SIGHT = 100;
    protected int counter = 0;

    public EvasionRandom(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    public EvasionRandom(Agent other)
    {
        super(other);
    }

    @Override
    public Move move()
    {
        if(moveFailed)
        {
            direction = direction.rotate(90);
        }
        else
        {
            updateKnowledge();
        }
        return nextMove();
    }

    protected void updateKnowledge()
    {
        Vector closestGuardSeen = closestTypePos(Type.GUARD);
        if(closestGuardSeen != null)
        {
            counter = 0;
        }
        else
        {
            counter ++;
        }
    }

    protected Move nextMove()
    {
        int upperBound = (int) direction.getAngle() + 90;
        int lowerBound = (int) direction.getAngle() - 90;
        int theta = (int) random.nextInt(upperBound - lowerBound) + lowerBound;

        Vector randomMove = new Vector(0, moveLength).rotate(theta);

        direction = randomMove.normalise();
        return new Move(randomMove.normalise(), randomMove);
    }

    @Override
    public Agent nextState()
    {
        if(counter > MAX_TICS_WITHOUT_SIGHT)
        {
            return new WallFollowAgent(this);
        }
        return this;
    }
}

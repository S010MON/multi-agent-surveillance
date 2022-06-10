package app.model.agents.Evasion;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;

public class EvasionDirected extends EvasionRandom
{
    protected Vector intelligentDirection;
    protected boolean intelligenceFlag = false;

    public EvasionDirected(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    public EvasionDirected(Agent other)
    {
        super(other);
    }

    @Override
    public Move move()
    {
        if(moveFailed)
        {
            direction = direction.rotate(90);
            return super.nextMove();
        }
        else
        {
            updateKnowledge();
            return nextMove();
        }
    }

    @Override
    protected Move nextMove()
    {
        if(intelligenceFlag)
        {
            return new Move(intelligentDirection.normalise(), intelligentDirection);
        }
        return super.nextMove();
    }

    @Override
    protected void updateKnowledge()
    {
        Vector closestGuardSeen = closestTypePos(Type.GUARD);

        if(closestGuardSeen != null)
        {
            counter = 0;
            Vector safeDirection = position.sub(closestGuardSeen);
            intelligentDirection = safeDirection.normalise().scale(moveLength);
            intelligenceFlag = true;
        }
        else
        {
            counter ++;
            intelligenceFlag = false;
        }
    }
}

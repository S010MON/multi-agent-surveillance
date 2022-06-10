package app.model.agents.Evasion;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;


import java.util.ArrayList;

public class EvasionDistanceMax extends EvasionRandom
{
    private Vector maximizingMove = new Vector();
    private boolean maximizeFlag = false;
    private boolean assumptionFlag = false;

    private ArrayList<Vector> failedMoves = new ArrayList<>();

    private ArrayList<Vector> possibleMoves = new ArrayList<>();
    public EvasionDistanceMax(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        setup();
    }

    public EvasionDistanceMax(Agent other)
    {
        super(other);
        setup();
    }

    private void setup()
    {
        possibleMoves.add(new Vector(0, moveLength));
        possibleMoves.add(new Vector(moveLength, moveLength));
        possibleMoves.add(new Vector(0, -moveLength));
        possibleMoves.add(new Vector(moveLength, moveLength));
        possibleMoves.add(new Vector(moveLength, 0));
        possibleMoves.add(new Vector(-moveLength, moveLength));
        possibleMoves.add(new Vector(-moveLength, 0));
        possibleMoves.add(new Vector(moveLength, -moveLength));
    }

    @Override
    public Move move()
    {
        if(moveFailed)
        {
            failedMoves.add(maximizingMove);
        }
        updateKnowledge();
        return nextMove();
    }

    @Override
    protected Move nextMove()
    {
        if(maximizeFlag)
        {
            return new Move(direction, maximizingMove);
        }
        else
        {
            Vector assumedMove = direction.scale(-moveLength);
            return new Move(direction, assumedMove);
        }
    }

    @Override
    protected void updateKnowledge()
    {
        Vector closestGuardSeen = closestTypePos(Type.GUARD);
        if(moveFailed && closestGuardSeen != null)
        {
            ArrayList<Double> scores= determineMoveScores(closestGuardSeen);
            determineNextBestMove(scores);
            maximizeFlag = true;
        }
        else if(closestGuardSeen != null)
        {
            ArrayList<Double> scores= determineMoveScores(closestGuardSeen);
            counter = 0;
            determineMaximizingMove(scores);
            direction = closestGuardSeen.sub(position).normalise();
            maximizeFlag = true;
            assumptionFlag = false;
            failedMoves.clear();
        }
        else
        {
            counter ++;
            maximizeFlag = false;
            assumptionFlag = true;
        }
    }

    private ArrayList<Double> determineMoveScores(Vector guardPosition)
    {
        ArrayList<Double> scores = new ArrayList<>();
        for(Vector move: possibleMoves)
        {
            Vector possiblePosition = position.add(move);
            double distance = possiblePosition.dist(guardPosition);
            scores.add(distance);
        }
        return scores;
    }

    private void determineMaximizingMove(ArrayList<Double> scores)
    {
        double maximum = 0;
        for(int i=0; i < possibleMoves.size(); i++)
        {
            if(scores.get(i) > maximum)
            {
                maximizingMove = possibleMoves.get(i);
                maximum = scores.get(i);
            }
        }
    }

    private void determineNextBestMove(ArrayList<Double> scores)
    {
        double maximum = 0;
        for(int i=0; i< possibleMoves.size(); i++)
        {
            if(!failedMoves.contains(possibleMoves.get(i)) && scores.get(i) > maximum)
            {
                maximum = scores.get(i);
                maximizingMove = possibleMoves.get(i);
            }
        }
    }
}

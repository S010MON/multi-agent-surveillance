package app.model.agents.Capture;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.Team;

import java.util.ArrayList;

public class Capture extends AgentImp
{
    private final int MAX_TICS_WITHOUT_SIGHT = 100;
    VectorSet beliefSet;
    ArrayList<Vector> positionHistory;
    Vector direction;
    int counter;




    public Capture(Vector position, Vector direction, double radius, Team team)
    {
        super(position, direction, radius, team);
        this.beliefSet.add(direction);
        this.positionHistory.add(direction);
        this.direction = direction;

        initializeCapturing();
    }

    //Both - work as if this method exists (Implement each method separately!)
    public void initializeCapturing()
    {

        // This method loops in capture until end state is reached (Intruder captured or tics maxed)
    }


    //mah
    // This method goes through the belief set and adds any vectors which the intruder could reach after the next move.
    public void findAllPossiblePositions()
    {
//        double offset = intruder.getMaxWalk();
        double offset = this.getMaxWalk();

        Vector original = direction;

        beliefSet.add(new Vector(original.getX() , original.getY() + offset));
        beliefSet.add(new Vector(original.getX() + offset, original.getY()));
        beliefSet.add(new Vector(original.getX() , original.getY() - offset));
        beliefSet.add(new Vector(original.getX() - offset, original.getY()));
    }

    public void addPositionHistory(Vector vector){
        positionHistory.add(vector);
    }

    //matt
    public Vector findTarget()
    {
        //Using heuristics decides on target for next move.
        return null;
    }

    //matt
    public Vector nextMove (Vector target)
    {
        //Decides on the best next move towards the target position. (Use A* or dijkstra etc.)
        Vector nextMove = null;
        return nextMove;
    }



    public boolean isCompelete()
    {
        return (counter > MAX_TICS_WITHOUT_SIGHT);
    }

    //Matt
    public void updateCounter()
    {
        counter = 0;
    }


    //mahshid
    public boolean isVisible()
    {
        //This method checks if we still see the intruder. (One of the rays intersects with it)
        //Access the view (array of rays)
        return false;
    }

}

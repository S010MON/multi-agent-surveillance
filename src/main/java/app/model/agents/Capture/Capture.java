package app.model.agents.Capture;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.Move;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Capture extends AgentImp

{
    @Getter @Setter private int MAX_TICS_WITHOUT_SIGHT = 100;
    private VectorSet beliefSet;
    private ArrayList<Vector> positionHistory;
    int counter;

    public Capture(Vector position, Vector direction, double radius, Team team, Vector intruderPos)
    {
        super(position, direction, radius, team);
        this.beliefSet = new VectorSet();
        this.beliefSet.add(intruderPos);
        this.positionHistory = new ArrayList<>();
        this.positionHistory.add(intruderPos);

        initializeCapturing();
    }

    //Both - work as if this method exists (Implement each method separately!)
    public void initializeCapturing()
    {
        // This method loops in capture until end state is reached (Intruder captured or tics maxed)
        while(this.isComplete()){
            addPositionHistory();
            findAllPossiblePositions(this.getDirection(), this.getMaxWalk());
        }
    }


    //mah
    public void findAllPossiblePositions(Vector centerPoint , double offset)
    {
        beliefSet.add(new Vector(centerPoint.getX() , centerPoint.getY() + offset));
        beliefSet.add(new Vector(centerPoint.getX() + offset, centerPoint.getY()));
        beliefSet.add(new Vector(centerPoint.getX() , centerPoint.getY() - offset));
        beliefSet.add(new Vector(centerPoint.getX() - offset, centerPoint.getY()));
    }

    public void addPositionHistory(){
        positionHistory.add(direction);
    }

    //matt
    public Vector findTarget()
    {
        //Using heuristics decides on target for next move.
        // If our belief set is size 1, then we have our target.
        if(this.beliefSet.size() == 1)
            return this.beliefSet.get(0);

        double shortestDist = Double.MAX_VALUE;
        Vector closestPoint = null;
        for(Vector location : this.beliefSet)
        {
            if(location.dist(this.position) < shortestDist)
            {
                shortestDist = location.dist(this.position);
                closestPoint = location;
            }
        }

        // Currently returning the closest point in the belief set to us. (Will improve this and add more heuristics)
        return closestPoint;
    }

    //matt
    public Move nextMove(Vector target)
    {
        //Decides on the best next move towards the target position. (Use A* or dijkstra etc.)
        return null;
    }



    public boolean isComplete()
    {
        return (this.counter > this.MAX_TICS_WITHOUT_SIGHT);
    }

    //Matt
    public void updateCounter()
    {
        // Increases the counter if the size of the belief set is greater than 1. (a tic has passed without seeing the intruder).
        if(this.beliefSet.size() > 1)
            this.counter++;
        else if(this.beliefSet.size() == 1)
            counter = 0; // Reset counter when we see the intruder.
    }


    //mahshid
    public boolean isVisible()
    {
        //This method checks if we still see the intruder. (One of the rays intersects with it)
        //Access the view (array of rays)
        return false;
    }

}

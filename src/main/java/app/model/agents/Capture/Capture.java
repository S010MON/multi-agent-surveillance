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

    Agent intruder;
    VectorSet beliefSet;
    ArrayList<Vector> positionHistory;
    int counter;



    public Capture(Vector position, Vector direction, double radius, Team team, Agent intruder)
    {
        super(position, direction, radius, team);
        this.intruder = intruder;
        this.beliefSet.add(intruder.getPosition());
        this.positionHistory.add(intruder.getPosition());
    }

    //mah
    public void findPossiblePosition(){

    }

    public void addPositionHistory(Vector vector){
        positionHistory.add(vector);
    }

    //matt
    public Vector findTarget(){
        return null;
    }

    //matt
    public Vector nextMove (Vector target){
        Vector nextMove = null;
        return nextMove;
    }



    public boolean isCompelete(){
        return (counter > 1000);
    }


    public void nullCounter(){
        counter = 0;
    }


    //mahshid
    public boolean isVisible(){
//        access to view
        return false;
    }

}

package app.model.agents.Cells;

import app.controller.linAlg.Vector;
import lombok.Getter;
import lombok.Setter;

public class GraphCell
{
    //General
    @Getter private int x;
    @Getter private int y;
    @Getter private String XY;

    //WF
    @Getter @Setter Boolean obstacle;
    @Getter @Setter private Boolean occupied;

    //ACO
    @Getter @Setter private double pheromone;
    private double evaporationConstant = 0.001;

    public GraphCell(Vector position)
    {
        setGeneralInfo(position);
    }

    public GraphCell(Vector position, boolean obstacle, boolean occupied, double pheromone)
    {
        setGeneralInfo(position);
        this.obstacle = obstacle;
        this.occupied = occupied;
        this.pheromone = pheromone;
    }

    public void setGeneralInfo(Vector position)
    {
        this.x = (int)position.getX();
        this.y = (int)position.getY();
        this.XY = x + " " + y;
    }

    public void evaporate()
    {
        pheromone = pheromone * (1 - evaporationConstant);
    }

    public void updatePheromone(double pheromoneValue)
    {
        pheromone += pheromoneValue;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj.getClass() == this.getClass())
        {
            return (this.x == ((GraphCell) obj).getX() && this.y == ((GraphCell) obj).getY());
        }
        return false;
    }
}

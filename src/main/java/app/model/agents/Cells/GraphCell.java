package app.model.agents.Cells;

import lombok.Getter;
import lombok.Setter;

public class GraphCell
{
    //General
    @Getter private int x;
    @Getter private int y;
    private String XY;

    //WF
    @Getter @Setter Boolean obstacle;
    private Boolean occupied;

    //ACO
    @Getter @Setter private double pheromone;
    private double evaporationConstant = 0.001;

    public GraphCell(int x, int y)
    {
        setGeneralInfo(x, y);
    }

    public GraphCell(int x, int y, boolean obstacle, boolean occupied)
    {
        setGeneralInfo(x, y);
        this.obstacle = obstacle;
        this.occupied = occupied;
    }

    public void setGeneralInfo(int x, int y)
    {
        this.x = x;
        this.y = y;
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

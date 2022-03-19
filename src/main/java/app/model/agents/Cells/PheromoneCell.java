package app.model.agents.Cells;

public class PheromoneCell implements Cell
{
    private double pheromone;
    private double evaporationConstant = 0.001;

    public PheromoneCell()
    {
        this.pheromone = 0.0;
    }

    public double currentPheromoneValue()
    {
        return pheromone;
    }

    public void updatePheromone(double maxPheromone)
    {
        pheromone += maxPheromone;
    }

    public void evaporation()
    {
        pheromone = pheromone * (1 - evaporationConstant);
    }

    public String toString()
    {
        return Double.toString(pheromone);
    }
}

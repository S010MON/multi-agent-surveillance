package app.model.agents.Cells;

public class PheromoneCell implements Cell
{
    private double pheramone;
    private double evaporationConstant = 0.001;

    public PheromoneCell()
    {
        this.pheramone = 0.0;
    }

    public double currentPheromoneValue()
    {
        return pheramone;
    }

    public void updatePheromone(double maxPheramone)
    {
        pheramone += maxPheramone;
    }

    public void evaporation()
    {
        pheramone = pheramone * (1 - evaporationConstant);
    }

    public String toString()
    {
        return Double.toString(pheramone);
    }
}

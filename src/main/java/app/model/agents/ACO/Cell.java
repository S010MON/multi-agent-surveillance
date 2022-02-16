package app.model.agents.ACO;

public class Cell
{
    private double pheramone;
    private double evaporationConstant = 0.001;

    public Cell()
    {
        this.pheramone = 0.0;
    }

    public double currentPheramoneValue()
    {
        return pheramone;
    }

    public void updatePheramone(double maxPheramone)
    {
        pheramone += maxPheramone;
    }

    public void evaporation()
    {
        pheramone = pheramone * (1 - evaporationConstant);
    }

}

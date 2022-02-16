package app.model.agents.Cells;

public class PheramoneCell implements Cell
{
    private double pheramone;
    private double evaporationConstant = 0.001;

    public PheramoneCell()
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

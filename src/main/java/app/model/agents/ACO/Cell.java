package app.model.agents.ACO;

public class Cell
{
    private AcoAgent agent;
    private double pheramone;

    public Cell(AcoAgent agent)
    {
        this.agent = agent;
        this.pheramone = 0.0;
    }

    public Cell()
    {
        this.agent = null;
        this.pheramone = 0.0;
    }

    public double cellPheramoneValue()
    {
        return pheramone;
    }

    public void addAgent(AcoAgent agent)
    {
        this.agent = agent;
    }

    public void removeAgent()
    {
        agent = null;
    }

    //TODO Update with correct pheramone equation
    public void pheramonePlacement()
    {
        if(agent == null)
        {
            throw new RuntimeException("No agent present within this cell");
        }
        pheramone = agent.releasePheramone();
    }
}

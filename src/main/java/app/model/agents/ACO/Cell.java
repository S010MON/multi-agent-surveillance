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

    public void addAgent(AcoAgent agent)
    {
        this.agent = agent;
    }

    public void removeAgent()
    {
        this.agent = null;
    }

    public void pheramonePlacement()
    {
        if(this.agent != null)
        {
            this.agent.releasePheramone();
        }
        throw new RuntimeException("No agent present within this cell");
    }
}

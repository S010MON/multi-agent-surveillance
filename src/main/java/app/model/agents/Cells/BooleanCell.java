package app.model.agents.Cells;

public class BooleanCell implements Cell
{
    private boolean explored;
    private boolean obstacle;

    public void explored()
    {
        explored = true;
    }

    public void obstacle()
    {
        obstacle = true;
    }

    public boolean getExploredState()
    {
        return explored;
    }

    public boolean getObstacleState()
    {
        return obstacle;
    }
}

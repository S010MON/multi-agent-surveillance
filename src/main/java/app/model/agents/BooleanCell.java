package app.model.agents;

public class BooleanCell implements Cell
{
    private boolean explored;
    private boolean obstacle;

    public boolean getExploredState()
    {
        return explored;
    }
}

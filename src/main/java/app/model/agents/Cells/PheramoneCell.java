package app.model.agents.Cells;

public class PheramoneCell implements Cell
{
    private double value;

    public double getValue()
    {
        return value;
    }

    public void updateValue(double newValue)
    {
        value = newValue;
    }
}

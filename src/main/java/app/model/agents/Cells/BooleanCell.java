package app.model.agents.Cells;

import lombok.Getter;
import lombok.Setter;

public class BooleanCell implements Cell
{

    @Getter @Setter private Boolean obstacle;  // permanent obstacle
    private Boolean occupied;  // cell temporarily occupied by an agent or intruder
    private int row = -1;
    private int col = -1;
    @Getter private int x;
    @Getter private int y;
    private String XY;

    public BooleanCell()
    {
        obstacle = null;
        occupied = null;
    }

    public BooleanCell(int x, int y)
    {
        this.x = x;
        this.y = y;
        XY = x + " " + y;
        obstacle = false;
        occupied = false;
    }

    public boolean equalsXY(int otherX, int otherY)
    {
        return this.x == otherX && this.y == otherY;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o.getClass() == this.getClass())
            return equalsXY(((BooleanCell) o).getX(), ((BooleanCell) o).getY());
        return false;
    }

    @Override
    public String toString()
    {
        return XY;
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

}

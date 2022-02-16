package jgfx.javagradlefx;

import app.model.agents.BooleanCell;
import app.model.agents.Cell;
import app.model.agents.CellType;
import app.model.agents.Grid;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GridTest
{
    @Test
    void testBooleanCell()
    {
        Grid grid = new Grid(100, 100, CellType.BOOLEAN);
        Cell cell = grid.getCellAt(10, 10);
        System.out.println(cell.getClass());
    }
}

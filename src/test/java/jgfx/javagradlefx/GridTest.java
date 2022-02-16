package jgfx.javagradlefx;

import app.model.agents.*;
import app.model.agents.Cells.BooleanCell;
import app.model.agents.Cells.CellType;
import app.model.agents.Cells.PheramoneCell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest
{
    @Test
    void testPheramoneCell()
    {
        Grid grid = new Grid(100, 100, CellType.PHERAMONE);
        PheramoneCell cell = (PheramoneCell)grid.getCellAt(10, 10);
        double value = cell.getValue();

        assertEquals(value, 0.0);
    }

    @Test
    void testBooleanCell()
    {
        Grid grid = new Grid(100, 100, CellType.BOOLEAN);
        BooleanCell cell = (BooleanCell) grid.getCellAt(10, 10);
        boolean value = cell.getExploredState();

        assertEquals(value, false);
    }
}

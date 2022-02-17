package jgfx.javagradlefx;

import app.controller.linAlg.Vector;
import app.model.Grid.Grid;
import app.model.Grid.AcoGrid;
import app.model.agents.Cells.BooleanCell;
import app.model.agents.Cells.CellType;
import app.model.agents.Cells.PheromoneCell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest
{
    @Test
    void testDimensionsWholeNumbers()
    {
        Grid grid = new AcoGrid(50, 50, 1);
        int rows = grid.getRowDimension();
        int cols = grid.getColDimension();

        assertEquals(rows, 50);
        assertEquals(cols, 50);
    }

    @Test
    void testDimensionsFractional()
    {
        AcoGrid grid = new AcoGrid(50.3, 25.7, 1);
        int rows = grid.getRowDimension();
        int cols = grid.getColDimension();

        assertEquals(rows, 50);
        assertEquals(cols, 25);
    }

    @Test
    void testPheromoneCell()
    {
        Grid grid = new Grid(100, 100, CellType.PHEROMONE);
        PheromoneCell cell = (PheromoneCell)grid.getCellAt(10, 10);
        double value = cell.currentPheromoneValue();

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

    @Test
    void testCellRow()
    {
        Grid grid = new Grid(50.3, 25.7, CellType.PHEROMONE);
        int row = grid.getCellRow(new Vector(12.5, 5.9));
        assertEquals(row, 5);
    }

    @Test
    void testCellCol()
    {
        Grid grid = new Grid(50.3, 25.7, CellType.PHEROMONE);
        int col = grid.getCellCol(new Vector(12.5, 5.9));
        assertEquals(col, 12);
    }
}

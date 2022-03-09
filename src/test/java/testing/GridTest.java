package testing;

import app.controller.linAlg.Vector;
import app.model.Grid.Grid;
import app.model.Grid.AcoGrid;
import app.model.agents.Cells.BooleanCell;
import app.model.agents.Cells.Cell;
import app.model.agents.Cells.CellType;
import app.model.agents.Cells.PheromoneCell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest
{

    @Test
    void testGetCellAtBoundaryStart()
    {
        Grid grid = new AcoGrid(50, 50, 1);
        Cell cell = grid.getCellAt(0, 0);
        assertNotNull(cell);
    }

    @Test
    void testGetCellAtBoundaryFinish()
    {
        Grid grid = new AcoGrid(52.3, 56.8, 1);
        int rows = grid.getRowDimension();
        int cols = grid.getColDimension();

        assertEquals(rows, 54);
        assertEquals(cols, 58);
    }

    @Test
    void testDimensionsWholeNumbers()
    {
        Grid grid = new AcoGrid(50, 50, 1);
        int rows = grid.getRowDimension();
        int cols = grid.getColDimension();

        //Accomodate for zero cell
        assertEquals(rows, 51);
        assertEquals(cols, 51);
    }

    @Test
    void testDimensionsFractional()
    {
        AcoGrid grid = new AcoGrid(50.3, 25.7, 1);
        int rows = grid.getRowDimension();
        int cols = grid.getColDimension();

        assertEquals(rows, 52);
        assertEquals(cols, 27);
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

        assertFalse(value);
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

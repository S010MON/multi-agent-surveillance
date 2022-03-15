package testing;

import app.model.agents.Cells.BooleanCell;
import app.model.agents.Cells.CellType;
import app.model.agents.Grid;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoverageTest
{
    // No cells explored
    @Test void testZeroCoverage()
    {
        Grid grid = new Grid(1000, 1000, CellType.BOOLEAN, 10);
        assertEquals(0, grid.getCellsExplored());
    }

    // Some cells explored
    @Test void testPartCoverage()
    {
        Grid grid = new Grid(1000, 1000, CellType.BOOLEAN, 10);
        // Make random cells explored
        BooleanCell c1 = (BooleanCell) grid.getGrid()[12][45];
        c1.isExplored();
        BooleanCell c2 = (BooleanCell) grid.getGrid()[15][25];
        c2.isExplored();
        BooleanCell c3 = (BooleanCell) grid.getGrid()[22][41];
        c3.isExplored();
        BooleanCell c4 = (BooleanCell) grid.getGrid()[44][44];
        c4.isExplored();
        BooleanCell c5 = (BooleanCell) grid.getGrid()[7][31];
        c5.isExplored();
        BooleanCell c6 = (BooleanCell) grid.getGrid()[31][67];
        c6.isExplored();
        BooleanCell c7 = (BooleanCell) grid.getGrid()[88][23];
        c7.isExplored();
        BooleanCell c8 = (BooleanCell) grid.getGrid()[6][90];
        c8.isExplored();
        BooleanCell c9 = (BooleanCell) grid.getGrid()[84][77];
        c9.isExplored();
        BooleanCell c10 = (BooleanCell) grid.getGrid()[50][91];
        c10.isExplored();

        assertEquals(10, grid.getCellsExplored());
    }

    // All cells explored
    @Test void testFullCoverage()
    {
        Grid grid = new Grid(1000, 1000, CellType.BOOLEAN, 10);
        for(int i = 0; i < grid.getGrid().length; i++)
        {
            for(int j = 0; j < grid.getGrid()[0].length; j++)
            {
                BooleanCell bc = (BooleanCell) grid.getGrid()[i][j];
                bc.isExplored();
            }
        }
        assertEquals(101 * 101, grid.getCellsExplored());
    }
}

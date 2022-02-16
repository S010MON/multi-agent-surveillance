package jgfx.javagradlefx;

import app.model.agents.ACO.AcoOverlay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcoOverlayTest
{
    @Test
    void testDimensionsWholeNumbers()
    {
        double length = 50.0;

        AcoOverlay grid = new AcoOverlay(50, 50, 1);
        int rows = grid.getRowDimension();
        int cols = grid.getColDimension();

        assertEquals(rows, 50);
        assertEquals(cols, 50);
    }

    @Test
    void testDimensionsFractional()
    {
        AcoOverlay grid = new AcoOverlay(50.3, 25.7, 1);
        int rows = grid.getRowDimension();
        int cols = grid.getColDimension();

        assertEquals(rows, 50);
        assertEquals(cols, 25);
    }
}

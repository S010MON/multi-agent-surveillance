package jgfx.javagradlefx;

import app.controller.linAlg.Vector;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.ACO.AcoOverlay;

import app.model.agents.ACO.Cell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void testCellRow()
    {
        AcoOverlay grid = new AcoOverlay(50.3, 25.7, 1);
        int row = grid.getCellRow(new Vector(12.5, 5.9));
        assertEquals(row, 5);
    }

    @Test
    void testCellCol()
    {
        AcoOverlay grid = new AcoOverlay(50.3, 25.7, 1);
        int col = grid.getCellCol(new Vector(12.5, 5.9));
        assertEquals(col, 12);
    }

    @Test
    void testUpdateAgent()
    {
        Vector agentPosition = new Vector(10.2, 12.3);
        AcoOverlay grid = new AcoOverlay(50.3, 25.7, 1);
        AcoAgent agent = new AcoAgent(agentPosition, new Vector(1, 0), 10);

        grid.updateAgent(agent);
        Cell occupiedCell = grid.getCell(agentPosition);

        assertEquals(occupiedCell.currentPheramoneValue(), agent.accessPheramoneQuantity());
    }

    @Test
    void testEvaporationProcess()
    {
        Vector agentPosition = new Vector(10.2, 12.3);
        AcoOverlay grid = new AcoOverlay(50.3, 25.7, 1);
        AcoAgent agent = new AcoAgent(agentPosition, new Vector(1, 0), 10);
        grid.updateAgent(agent);

        double currentPheramone = grid.getCell(agentPosition).currentPheramoneValue();
        grid.evaporationProcess();
        double pheramoneAfterEvaporation = grid.getCell(agentPosition).currentPheramoneValue();

        assertTrue(currentPheramone > pheramoneAfterEvaporation);
    }
}

package app.model;

import app.controller.Beam;
import app.controller.Vector;

import java.util.ArrayList;

public class MapTemp
{
    public ArrayList<Beam> beams;

    /**
     * Temporary map for testing ray drawing, will be swapped out for proper one once made
     */
    public MapTemp()
    {
        beams = new ArrayList<>();
        createBeams();
    }

    // Temp method to create a set of rays - GraphicsEngine should generate this
    private void createBeams()
    {
        Vector origin = new Vector(400,250);
        Vector end = new Vector(400, 0);
        Beam beam = new Beam(origin, end);
        for(int i = 0; i < 100; i++)
        {
            beams.add(beam.rotate(i * 5.0));

        }
    }
}

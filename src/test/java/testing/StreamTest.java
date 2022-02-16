package testing;

import app.controller.linAlg.Vector;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamTest
{
    @Test void testMin()
    {
        ArrayList<Vector> V = new ArrayList<>();
        Vector o = new Vector(0,0);
        V.add(new Vector(10,10));
        V.add(new Vector(7,10));
        V.add(new Vector(10,7));
        V.add(new Vector(3,3));
        V.add(new Vector(10,11));

        Vector min = V.stream()
                .min(Comparator.comparing(e -> e.dist(o)))
                .get();

        assertEquals(3,min.getX());
        assertEquals(3,min.getY());
    }
}




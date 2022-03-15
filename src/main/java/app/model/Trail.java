package app.model;

import app.controller.linAlg.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Trail implements Comparable
{
    private Vector loc;
    private long time;

    @Override public int compareTo(Object o)
    {
        if(!(o instanceof Trail))
            throw new ClassCastException();

        Trail other = (Trail) o;

        if(this.time < other.time)
            return -1;
        else if(this.time > other.time)
            return 1;
        return 0;
    }
}

package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import lombok.Getter;

public class SoundVector extends Vector
{
    @Getter double amplitude;

    public SoundVector(double x, double y, double amplitude)
    {
        super(x, y);
        this.amplitude = amplitude;
    }
}

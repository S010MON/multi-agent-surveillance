package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import lombok.Getter;

public class SoundVector extends Vector
{
    @Getter double amplitude;       /** How loud the sound is in dB */
    @Getter double frequency;       /** The sound's frequency in hz */

    public SoundVector(double x, double y, double amplitude, double frequency)
    {
        super(x, y);
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    public SoundVector(Vector direction, double amplitude, double frequency)
    {
        super(direction.getX(), direction.getY());
        this.amplitude = amplitude;
        this.frequency = frequency;
    }
}

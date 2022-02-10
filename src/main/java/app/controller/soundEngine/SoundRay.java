package app.controller.soundEngine;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;

public abstract class SoundRay extends Ray {
    protected double amplitude;

    public SoundRay(Vector u, Vector direction, double amplitude){
        super(u,direction);
        this.amplitude = amplitude;
    }

    /*
    * Calculates the amplitude of a sound after some distance d
    * Abstract because this allows for different dropoff types for sound (linear, square, cubic)
    * */
    public abstract double amplitudeAfter(double d);
}

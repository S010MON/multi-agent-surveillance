package app.controller.soundEngine;

import app.controller.linAlg.Vector;

public class LinearSoundRay extends SoundRay{

    public LinearSoundRay(Vector u, Vector direction, double amplitude) {
        super(u, direction, amplitude);
    }

    @Override
    public double amplitudeAfter(double d) {
        // this could be put this input a config
        double dropOff = 0.01;
        return Math.max(this.amplitude - d * dropOff,0.0);
    }
}

package app.controller.soundEngine;


import app.controller.linAlg.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SoundSourceBase implements SoundSource{
    @Getter private Vector position;
    private double amplitude;

    @Override
    public double soundLevelFrom(Vector listener) {
            // this could be put this input a config
            double dropOff = 1.0;
            double dist = listener.dist(this.position);
            return Math.max(this.amplitude - dist * dropOff,0.0);
    }
}

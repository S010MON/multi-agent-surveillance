package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import lombok.Getter;

import java.util.Objects;

public class Sound {
    @Getter private SoundSource soundSource;
    @Getter private double amplitude;
    @Getter private Vector direction;
    private Vector listenerPos;

    // origin can either be the position of the sound source or the last diffraction corner
    public Sound(SoundSource soundSource, Vector listenerPos, Vector origin, int diffractionCount){
        this.soundSource = soundSource;
        this.listenerPos = listenerPos;

        direction = origin.sub(listenerPos);
        amplitude = soundSource.soundLevelFrom(listenerPos, diffractionCount);
    }

    // if the new amplitude is higher than the old one update the origin of the sound and the diffractioncount
    // TODO find more descriptive name
    public void update(Vector origin, int diffractionCount){
        double newAmp = soundSource.soundLevelFrom(listenerPos, diffractionCount);

        if(Double.compare(newAmp, amplitude) > 0) {
            direction = origin.sub(listenerPos);
            amplitude = newAmp;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Sound sound = (Sound) o;
        return Double.compare(sound.amplitude, amplitude) == 0 &&
                soundSource.equals(sound.soundSource) &&
                direction.equals(sound.direction) &&
                listenerPos.equals(sound.listenerPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(soundSource, amplitude, direction, listenerPos);
    }
}

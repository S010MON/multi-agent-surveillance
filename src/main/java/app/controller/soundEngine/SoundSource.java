package app.controller.soundEngine;


import app.controller.linAlg.Vector;

public interface SoundSource {
    double soundLevelFrom(Vector position);
    Vector getPosition();
}

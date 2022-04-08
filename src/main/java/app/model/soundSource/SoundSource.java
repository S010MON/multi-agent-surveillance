package app.model.soundSource;


import app.controller.linAlg.Vector;

public interface SoundSource
{
    double soundLevelFrom(Vector position, int diffractionCount);

    Vector getPosition();
}

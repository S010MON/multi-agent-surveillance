package app.model.soundSource;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import app.model.soundBoundary.SoundBoundary;
import app.model.soundBoundary.SoundBoundaryImp;

public abstract class SoundSourceFactory {
    public static SoundSource make(SoundSourceType f, Vector a, double amplitude)
    {
        // right now they all refer to the same boundary type, but this way it allows for more flexibility later on if we decide to implement weirder sound boundaries
        switch (f)
        {
            case SIREN -> { return new SoundSourceBase(a, amplitude);}
        }
        return null;
    }
}

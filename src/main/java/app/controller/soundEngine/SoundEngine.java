package app.controller.soundEngine;

import app.model.Map;

public interface SoundEngine {
    double compute(Map map, SoundSource source);
}

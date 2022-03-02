package app.controller.soundEngine;

import app.model.Map;
import app.model.agents.Agent;

import java.util.ArrayList;

public interface SoundEngine {
    double compute(Map map, Agent agent);
    ArrayList<SoundSource> computeSources(Map map, Agent agent);
}

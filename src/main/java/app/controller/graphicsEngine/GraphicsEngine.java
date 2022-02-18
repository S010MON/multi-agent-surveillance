package app.controller.graphicsEngine;

import app.model.agents.Agent;
import app.model.Map;

import java.util.ArrayList;

public interface GraphicsEngine
{
    ArrayList<Ray> compute(Map map, Agent agent);
}

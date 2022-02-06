package app.controller;

import app.model.Agent;
import app.model.Map;

import java.util.ArrayList;

public interface GraphicsEngine
{
    ArrayList<Beam> compute(Map map, Agent agent);
}

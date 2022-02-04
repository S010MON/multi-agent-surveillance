package app.controller;

import app.model.Agent;
import app.model.MapTemp;

import java.util.ArrayList;

public interface GraphicsEngine
{
    ArrayList<Beam> compute(MapTemp map, Agent agent);
}

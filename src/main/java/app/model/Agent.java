package app.model;

import app.controller.Vector;

public interface Agent extends Placeable
{
    void move();

    Vector getDirection();
}

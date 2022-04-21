package app.model;

import app.controller.linAlg.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Move
{
    // Splits the move into a change in direction of the agent (endDir) and a change in physical position (deltaPos)
    private Vector endDir;
    private Vector deltaPos;
}

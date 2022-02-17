package app.model.map;

import app.controller.linAlg.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Move
{
    private Vector endDir;
    private Vector deltaPos;
}

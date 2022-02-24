package app.model.furniture;

import app.controller.linAlg.Vector;
import app.model.boundary.Boundary;
import app.model.texture.Texture;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class Portal extends FurnitureBase{

    private Vector teleportTo;
    private Double teleportRotation;

    public Portal()
    {
        super();
        this.teleportTo = null;
        this.teleportRotation = null;
    }
}

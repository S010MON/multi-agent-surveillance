package app.controller.soundEngine;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SoundRay {
    @Getter private Vector start;
    @Getter private Vector end;

    public void draw(GraphicsContext gc)
    {
        // for illustration for now
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }
}

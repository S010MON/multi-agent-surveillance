package app.model.soundSource;

import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import javafx.scene.canvas.GraphicsContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SoundSourceBase implements SoundSource
{
    @Getter private Vector position;
    @Setter @Getter private ArrayList<SoundRay> rays;

    public SoundSourceBase(Vector position)
    {
        this.position = position;
        this.rays = new ArrayList<>();
    }

    public void draw(GraphicsContext gc)
    {
        rays.forEach(r -> r.draw(gc));
    }
}

package app.model.soundSource;


import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface SoundSource
{
    Vector getPosition();

    void setRays(ArrayList<SoundRay>  rays);

    ArrayList<SoundRay> getRays();

    void draw(GraphicsContext gc);
}

package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class SoundVector extends Vector
{
    @Getter private double amplitude;       /** How loud the sound is in dB */
    @Getter private double frequency;       /** The sound's frequency in hz */
    private Color colour = Color.RED;
    protected final double LINE_WIDTH = 2;

    public SoundVector(double x, double y, double amplitude, double frequency)
    {
        super(x, y);
        Vector v = new Vector(x, y).normalise();
        super.x = v.getX();
        super.y = v.getY();
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    public SoundVector(Vector direction, double amplitude, double frequency)
    {
        super(direction.getX(), direction.getY());
        Vector v = new Vector(direction.getX(), direction.getY()).normalise();
        x = v.getX();
        y = v.getY();
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    public void draw(GraphicsContext gc, Vector position)
    {
        Vector u = position;
        Vector v = u.add(this.scale(100));

        gc.setStroke(colour);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeLine(u.getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                u.getY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                v.getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                v.getY() * Info.getInfo().zoom + Info.getInfo().offsetY);
    }
}

package app.model.agents;

import app.controller.linAlg.Vector;
import app.controller.graphicsEngine.Ray;
import app.model.map.Move;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class AgentImp implements Agent
{
    @Getter @Setter protected double maxWalk = 5;
    @Getter @Setter protected double maxSprint = 10;
    @Getter protected Vector position;
    @Getter protected Vector direction;
    protected double radius;
    protected ArrayList<Ray> view;

    // TODO getter for raduios

    public AgentImp(Vector position, Vector direction, double radius)
    {
        this.direction = direction;
        this.position = position;
        this.radius = radius;
        view = new ArrayList<>();
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
    }

    @Override
    public Move move()
    {
        double x = Math.random() * maxWalk;
        if(Math.random() > 0.5)
            x = x * -1;
        double y = Math.random() * maxWalk;
        if(Math.random() > 0.5)
            y = y * -1;

        return new Move(new Vector(), new Vector(x, y));
    }

    @Override
    public ArrayList<Ray> getView()
    {
        return view;
    }

    @Override
    public void updateView(ArrayList<Ray> view)
    {
        this.view = view;
    }

    @Override
    public double getHearing()
    {
        return 0;
    }

    @Override
<<<<<<< HEAD
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public double getMaxWalk() { return maxWalk; }

    @Override
    public double getMaxSprint() { return maxSprint; }

    @Override
    public double getRadius() { return radius; }

    @Override
    public void setMaxWalk(double walkSpeed) { this.maxWalk = walkSpeed; }

    @Override
    public void setMaxSprint(double sprintSpeed) { this.maxSprint = sprintSpeed; }

    @Override
=======
>>>>>>> 624915619887ef090108e40e6515480dcdcc7928
    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.BLACK);
        gc.fillOval((position.getX()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetX,
                    (position.getY()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetY,
                       radius,
                       radius);
    }

    @Override
    public boolean isHit(Ray ray)
    {
        return false;
    }

    @Override
    public Vector intersection(Ray ray)
    {
        return null;
    }

    @Override
    public boolean validMove(Vector startPoint, Vector endPoint) {
        return false;
    }
}

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
        double dx = ray.getU().getX() - ray.getV().getX();
        double dy = ray.getU().getY() - ray.getV().getY();
        double dr = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double D = ray.getU().getX() * ray.getV().getY() - ray.getV().getX() * ray.getU().getY();

        double incidence = Math.pow(radius,2) * Math.pow(dr,2) - Math.pow(D, 2);
        return incidence >= 0;
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

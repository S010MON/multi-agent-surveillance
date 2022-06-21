package app.model.agents.Evasion;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;

public class HideyAgent extends AgentImp
{

    public Vector hideySpot = null;
    public HashSet<Vector> H = new HashSet<>();
    public ArrayList<Line> L = new ArrayList<>();

    public HideyAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    public HideyAgent(Agent other)
    {
        super(other);
    }

    @Override
    public Move move()
    {
        // Have reached hidey spot
        if((hideySpot != null && position.dist(hideySpot) < 50) || moveFailed)
        {
            hideySpot = null;
            return randomMove();
        }
        // Still have a way to to go to hidey spot
        else if(hideySpot != null)
        {
            Vector endDir = position.rotate(180);
            Vector deltaPos = position.sub(hideySpot).normalise().scale(maxSprint);
            return new Move(endDir, deltaPos);
        }

        // Start Algorithm
        Vector t = findAgentPos(view);
        H.clear();
        ArrayList<Vector> V = gatherPoints(view);
        L.addAll(generateLines(view));

        // Find all hidey spots (H)
        for(Vector v: V)
        {
            Line t_vision = new Line(t, v);

            for(Line l: L)
            {
                Vector intersection = null;
                if(t_vision.a != null && t_vision.b != null && l.a != null && l.b != null)
                    intersection = Intersection.findIntersection(t_vision, l);

                if(intersection != null && l.liesOn(intersection))
                    H.add(v);
            }
        }

        // Select best hidey spot
        Vector closest = null;
        double min_dist = Double.MAX_VALUE;
        for(Vector h: H)
        {
            if(position.dist(h) < min_dist)
            {
                closest = h;
                min_dist = position.dist(h);
            }

        }

        if(closest == null)
            return randomMove();

        hideySpot = closest;
        Vector endDir = position.sub(t).normalise();
        Vector deltaPos = position.sub(closest).normalise().scale(maxSprint);
        return new Move(endDir, deltaPos);
    }

    private Move randomMove()
    {
        double x = Math.random() * maxSprint;
        double y = Math.random() * maxSprint;
        return new Move(direction.rotate(180), new Vector(x, y));
    }


    private Vector findAgentPos(ArrayList<Ray> view)
    {
        for(Ray r: view)
        {
            if(r.getType() == Type.GUARD)
                return r.getV();
        }
        return null;
    }

    private ArrayList<Ray> removeAgent(ArrayList<Ray> view)
    {
        for(int i = 0; i < view.size(); i++)
        {
            if(view.get(i).getType() == Type.GUARD)
                view.remove(i);
        }
        return view;
    }

    private ArrayList<Line> generateLines(ArrayList<Ray> view)
    {
        ArrayList<Line> lines = new ArrayList<>();
        for(int i = 1; i < view.size(); i++)
        {
            // If last one
            if(i == view.size()-1)
                lines.add(new Line(view.get(0).getV(), view.get(i).getV()));

            // General Case
            else
                lines.add(new Line(view.get(i-1).getV(), view.get(i).getV()));
        }
        return lines;
    }

    private ArrayList<Vector> gatherPoints(ArrayList<Ray> view)
    {
        ArrayList<Vector> V = new ArrayList<>();
        for(Ray r: view)
        {
            V.add(r.getV());
        }
        return V;
    }
}

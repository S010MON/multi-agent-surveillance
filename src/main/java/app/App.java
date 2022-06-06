package app;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.boundary.Boundary;
import app.model.boundary.BoundaryType;
import app.view.FileMenuBar;
import app.view.simulation.Simulation;
import app.view.ScreenSize;
import app.view.mapBuilder.StartMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;

public class App extends Application
{
    private Scene scene;
    @Getter private FileMenuBar fileMenuBar;
    @Getter private StartMenu startMenu;
    @Getter private Stage stage;
    private Simulation simulation;

    @Override
    public void start(Stage stage)
    {
        this.fileMenuBar = new FileMenuBar(this);
        this.stage = stage;
        stage.setTitle("Multi Agent Surveillance");

        startMenu = new StartMenu(this);
        scene = new Scene(startMenu, ScreenSize.width, ScreenSize.height);

        // Shortcuts
        // Undoing
        KeyCombination ctrlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        Runnable r1 = () -> startMenu.getDisplayPane().undo();
        scene.getAccelerators().put(ctrlZ, r1);

        // Clearing
        KeyCombination ctrlBackspace = new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN);
        Runnable r2 = () -> startMenu.getDisplayPane().clear();
        scene.getAccelerators().put(ctrlBackspace, r2);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

    public void gotoSimulation()
    {
        if (simulation == null)
        {
            simulation = new Simulation(this);
            scene.setOnKeyTyped(e -> simulation.handleKey(e));
        }
        scene.setRoot(simulation);
    }

    public void gotoSimulation(String fileName)
    {
        simulation = new Simulation(this, fileName);
        scene.setOnKeyTyped(e -> simulation.handleKey(e));
        scene.setRoot(simulation);
    }

    public void gotoStart()
    {
        scene.setRoot(startMenu);
    }

    public void pause()
    {
        simulation.pauseOrResume();
    }

    public void updateRenderer(int toggle, boolean display)
    {
        if(simulation != null)
            simulation.updateRenderer(toggle, display);
    }

    /**
     *  @return both the non-transparent intersection, and the transparent (but visible) intersections
     *  encountered before it
     */
    private ArrayList<Ray> getIntersections(Ray r, ArrayList<Boundary> boundaries)
    {
        ArrayList<Vector> transparentIntersections = new ArrayList<>();
        Vector nonTransparentIntersection = null;

        ArrayList<Ray> transparentRays = new ArrayList<>();
        Ray nonTransparentRay = null;

        double closestDist = Double.MAX_VALUE;
        for (Boundary obj: boundaries)
        {
            if(obj.isHit(r))
            {
                Vector currentV = obj.intersection(r);
                if(!BoundaryType.isTransparent(obj.getBoundaryType()))
                {
                    double dist = r.getU().dist(currentV);
                    if(dist < closestDist && Double.compare(currentV.sub(r.getU()).getAngle(), r.angle()) == 0)
                    {
                        nonTransparentRay = new Ray(r.getU(), currentV, obj.getType());
                        closestDist = dist;
                    }
                }
                else if(BoundaryType.isVisible(obj.getBoundaryType()) && Double.compare(currentV.sub(r.getU()).getAngle(), r.angle()) == 0)
                {
                    transparentRays.add(new Ray(r.getU(), currentV, obj.getType()));
                }
            }
        }

        ArrayList<Vector> intersections = new ArrayList<>();

        ArrayList<Ray> rays = new ArrayList<>();
        if(nonTransparentRay != null)
            rays.add(nonTransparentRay);

        for(Ray ray: transparentRays)
        {
            if(ray.length() < closestDist)
            {
                rays.add(ray);
            }
        }
        return rays;
    }
}

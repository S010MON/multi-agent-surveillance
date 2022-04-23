package app.view.mapBuilder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Optional;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import app.view.ScreenSize;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Getter;

public class DisplayPane extends Canvas
{
    @Getter private ArrayDeque<MbObject> objects;
    private StartMenu startMenu;
    private Point2D click;
    private MbObject portal;
    private boolean settingTeleport;
    private Rectangle2D selection;
    private final Color backgroundColour = Color.WHITE;
    private double gridSize = 20;
    private ArrayList<GridLine> vLines;
    private ArrayList<GridLine> hLines;

    public DisplayPane(StartMenu startMenu)
    {
        super(ScreenSize.width - 450 ,ScreenSize.height);
        this.startMenu = startMenu;
        objects = new ArrayDeque<>();
        click = null;
        selection = null;
        portal = null;
        settingTeleport = false;

        vLines = createVLines();
        hLines = createHLines();

        setCursor(Cursor.CROSSHAIR);
        setOnMousePressed(this::mousePressed);
        setOnMouseDragged(this::mouseDragged);
        setOnMouseReleased(this::mouseReleased);
        draw();
    }

    public void draw()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(backgroundColour);
        gc.fillRect(0, 0, getWidth(), getHeight());

        // Draw map boundary
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3.0);
        gc.strokeRect(3, 3, startMenu.getMapWidth(), startMenu.getMapHeight());

        vLines.forEach(e -> e.draw(gc));
        hLines.forEach(e -> e.draw(gc));
        objects.forEach(e -> e.draw(gc));

        if(selection != null)
        {
            gc.setFill(Color.rgb(109,29,190, 0.5));
            gc.fillRect(selection.getMinX(), selection.getMinY(), selection.getWidth(), selection.getHeight());
        }
    }

    public void undo()
    {
        objects.removeLast();
        draw();
    }

    public void clear()
    {
        objects.clear();
        draw();
    }

    private void mousePressed(MouseEvent e)
    {
        click = new Point2D(e.getX(), e.getY());
    }

    private void mouseDragged(MouseEvent e)
    {
        if(click == null)
            return;

        double x, y, w, h;
        x = click.getX();
        y = click.getY();
        w = e.getX() - x;
        h = e.getY() - y;

        if(w < 0)
        {
           x = x + w;
           w = Math.abs(w);
        }

        if(h < 0)
        {
            y = y + h;
            h = Math.abs(h);
        }

        selection = new Rectangle2D(x, y, w, h);
        draw();
    }

    private void mouseReleased(MouseEvent e)
    {
        boolean portalSelected = startMenu.getFurniturePane().getCurrentType() == FurnitureType.PORTAL;
        boolean soundSourceSelected = startMenu.getFurniturePane().getCurrentType() == FurnitureType.SIREN;

        if(click == null)
            return;

        if(portalSelected && !settingTeleport)
        {
            portal = new MbObject(selection, FurnitureType.PORTAL);
            objects.add(portal);
            settingTeleport = true;
        }
        else if(portalSelected)
        {
            portal.setTeleportTo(new Vector(e.getX(), e.getY()));
            settingTeleport = false;
        }
        else if(soundSourceSelected)
        {
            Rectangle2D rect = new Rectangle2D(e.getX(), e.getY(), 0, 0);
            MbObject soundSource = new MbObject(rect, FurnitureType.SIREN);

            soundSource.setAmplitude(getAmplitudeFromPopUp());
            objects.add(soundSource);
        }
        else
        {
            objects.add(new MbObject(selection, startMenu.getFurniturePane().getCurrentType()));
        }
        selection = null;
        click = null;
        draw();
    }

    private ArrayList<GridLine> createVLines()
    {
        ArrayList<GridLine> lines = new ArrayList<>();
        for(int x = 0; x < getWidth(); x += gridSize)
        {
            lines.add(new GridLine(x, 0, x, getHeight()));
        }
        return lines;
    }

    private ArrayList<GridLine> createHLines()
    {
        ArrayList<GridLine> lines = new ArrayList<>();
        for(int y = 0; y < getHeight(); y += gridSize)
        {
            lines.add(new GridLine(0, y, getWidth(), y));
        }
        return lines;
    }

    private double getAmplitudeFromPopUp()
    {
        TextInputDialog textField = new TextInputDialog("100");
        textField.setHeaderText("Enter the range of the SoundSource \nif no legal number is entered, it will be set to 100");
        textField.setContentText("range:");
        Optional<String> result = textField.showAndWait();

        if (result.isPresent())
        {
            return Double.parseDouble(result.get());
        } else
        {
            return 100;
        }
    }
}

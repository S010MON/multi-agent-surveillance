package testing;

import app.controller.GameEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.settings.SettingsGenerator;
import app.controller.settings.SettingsObject;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.Team;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VisibleTargetTest
{
    @Test void seesTargetStraight()
    {
        Furniture target = FurnitureFactory.make(new SettingsObject(new Rectangle2D(0,2,10,10), FurnitureType.TARGET));

        Agent a = new AgentImp(new Vector(0,0),new Vector(0,1),1, Team.INTRUDER, new Vector(0,1), target);

        a.getView().add(new Ray(new Vector(0,0), new Vector(0,5)));

        assertTrue(a.seesTarget());
    }

    @Test void seesTargetDiagonal1()
    {
        Furniture target = FurnitureFactory.make(new SettingsObject(new Rectangle2D(0,2,10,10), FurnitureType.TARGET));

        Agent a = new AgentImp(new Vector(0,0),new Vector(0,1),1, Team.INTRUDER, new Vector(0,1), target);

        a.getView().add(new Ray(new Vector(0,0), new Vector(5,5)));

        assertTrue(a.seesTarget());
    }

    @Test void seesTargetDiagonal2()
    {
        Furniture target = FurnitureFactory.make(new SettingsObject(new Rectangle2D(10,10,10,10), FurnitureType.TARGET));

        Agent a = new AgentImp(new Vector(0,0),new Vector(0,1),1, Team.INTRUDER, new Vector(0,1), target);

        a.getView().add(new Ray(new Vector(0,0), new Vector(5,5)));

        assertTrue(a.seesTarget());
    }

    @Test void blockedTargetStraightAhead()
    {
        Furniture target = FurnitureFactory.make(new SettingsObject(new Rectangle2D(0,2,10,10), FurnitureType.TARGET));

        Agent a = new AgentImp(new Vector(0,0),new Vector(0,-1),1, Team.INTRUDER, new Vector(0,1), target);

        a.getView().add(new Ray(new Vector(0,0), new Vector(0,-5)));

        assertFalse(a.seesTarget());
    }
}

package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import app.controller.soundEngine.SoundVector;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.Team;
import app.model.sound.SoundSource;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class SoundSourceTest
{
    @Test void isHeard()
    {
        Vector a_pos = new Vector(10,0);
        Vector a_dir = new Vector();
        double a_rad = 10;
        Team a_team = Team.GUARD;
        Agent agent = new AgentImp(a_pos, a_dir, a_rad, a_team);

        Vector s_pos = new Vector(0, 0);
        double s_amp = 10;
        double s_freq = 100;
        SoundSource soundSource = new SoundSource(s_pos, s_amp, s_freq);
        Vector u = new Vector(0,0);
        Vector v = new Vector(11,0);
        ArrayList<SoundRay> rays = new ArrayList<>();
        rays.add(new SoundRay(u, v));
        soundSource.setRays(rays);

        ArrayList<SoundVector> exp = new ArrayList<>();
        exp.add(new SoundVector(1,0, s_amp, s_freq));
        ArrayList<SoundVector> act = soundSource.heard(agent);
        assertEquals(exp, act);
    }


}

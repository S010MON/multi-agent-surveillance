package app.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Clock implements ActionListener
{
    private GameEngine ge;

    public Clock(GameEngine ge)
    {
        this.ge = ge;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        ge.tick();
    }
}

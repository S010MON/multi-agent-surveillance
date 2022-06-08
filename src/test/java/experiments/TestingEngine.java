package experiments;

import app.controller.GameEngine;
import app.model.Type;
import app.model.Map;

public class TestingEngine extends GameEngine
{
    protected String test_name;

    public TestingEngine(Map map, String test_name)
    {
        super(map);
        this.test_name = test_name;
    }

    public int[] runCoverageTest()
    {
        double prevPercentage = 0;
        double currentPercentage;
        int[] times = new int[100];

        while(!complete() && tics < 100000 )
        {
            tick();

             currentPercentage = map.percentageComplete(Type.GUARD);
            if(currentPercentage != prevPercentage)
            {
                updatePercentageBar(currentPercentage);

                int prevIndex = (int) (prevPercentage * 100);
                int index = (int) (currentPercentage *  100);

                for(int i = prevIndex + 1; i <= index; i++)
                {
                    times[i] =  (int) tics;
                }

                prevPercentage = currentPercentage;
            }
        }
        System.out.println(" done");
        return times;
    }


    protected boolean complete()
    {
        return  map.percentageComplete(Type.GUARD) > 0.85;
    }


    protected void updatePercentageBar(double percent)
    {
        StringBuilder bar = new StringBuilder();
        bar.append(test_name);
        for(double d = 0; d < percent; d += 0.01)
        {
            bar.append("#");
        }
        bar.append(" ").append(percent).append("%");
        System.out.print("\r" + bar);
    }


    /**
     * A test to find the number of tics taken for the guards to capture all the
     * intruders on the map (or a limit of n tics)
     * @return the number of tics taken to capture the intruders
     */
    public long runCaptureTest()
    {
        long limit = 200;

        while(!(map.agentsRemaining(Type.INTRUDER) == 0) && tics < limit)
        {
            tick();
            updateTicsRemaining(tics, limit);
        }
        System.out.println(" - complete");
        return tics;
    }

    /**
     * Determines the quantity of ticks required for a capture agent to lose visual sight of an evading agent
     * for more that an escape limit (Ticks equal or above this indicate an escape)
     * @return The number of ticks until the evading agent has lost the capture agent
     */
    public long runEvasionTest()
    {
        long limit = 200;
        long noVisualDuration = 0;
        long escapeLimit = 20;

        while(noVisualDuration < escapeLimit && tics < limit)
        {
            if(!map.intruderVisual())
            {
                noVisualDuration ++;
            }
            else
            {
                noVisualDuration = 0;
            }

            tick();
            updateTicsRemaining(tics, limit);
        }
        System.out.println(" - complete");
        return tics;
    }


    /**
     * A test to find the number of tics taken for the intruders to reach the goal (or a limit of n tics)
     * @return the number of tics taken to reach the goal
     */
    public long runInflitrationTest()
    {
        long limit = 100;

        while(!map.goalReached() && tics < limit)
        {
            tick();
            updateTicsRemaining(tics, limit);
        }
        System.out.println(" - complete");
        return tics;
    }


    protected void updateTicsRemaining(long tics, long total)
    {
        StringBuilder bar = new StringBuilder();
        double percent = (double) tics / (double) total;
        bar.append(test_name).append(" ");
        for(double d = 0; d < percent; d += 0.01)
        {
            bar.append("#");
        }
        bar.append(" ").append(percent * 100).append("%");
        System.out.print("\r" + bar);
    }
}

package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.PheromoneCell;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AcoAgent360Vision extends AgentImp
{
    protected static AcoGrid world = new AcoGrid();
    private static int AcoAgentCount = 0;
    private static int AcoMoveCount = 0;

    private double maxPheromone = 5;
    @Getter protected double cellSize;
    protected Random randomGenerator = new Random(1);

    @Getter protected Move previousMove;
    protected HashMap<Integer, Vector> shortTermMoveMemory = new HashMap<>();

    protected int[] cardinalAngles = {0, 90, 180, 270};
    protected double epsilon = 0.3;

    public AcoAgent360Vision(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        cellSize = world.getCellSize();
        world.updateAgent(this);

        previousMove = new Move(position, new Vector());
        AcoAgentCount ++;
        AcoMoveCount ++;
    }

    // 1. Agent looks around
    // 2. Determine cardinal rays
    // 3. Determine positions to move to that aren't blocked (Wall or agent)
    // 4. Determine pheromone value at each position
    // 5. Determine best move and execute it.
    // 6. After all agents have gone, perform evaporation procedure
    @Override
    public Move move()
    {
        Ray[] cardinalRays = detectCardinalRays();
        ArrayList<Vector> possibleMovements = determineMovesAllScenarios(cardinalRays);

        ArrayList<Double> cellPheromones = accessAvaliableCellPheromones(possibleMovements);

        ArrayList<Vector> equivalentMinMoves = determineEquivalentMinMoves(cellPheromones, possibleMovements);
        Vector moveVector= selectRandomEquivalentMove(equivalentMinMoves);

        previousMove = new Move(position, moveVector);

        return previousMove;
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
        world.updateAgent(this);

        worldEvaporationProcess();
    }

    public void worldEvaporationProcess()
    {
        AcoMoveCount ++;
        if(AcoMoveCount >= AcoAgentCount)
        {
            world.evaporationProcess();
            AcoMoveCount = AcoMoveCount - AcoAgentCount;
        }
    }

    public ArrayList<Vector> determineMovesAllScenarios(Ray[] cardinalRays)
    {
        ArrayList<Vector> possibleMovements = determineAvailableMovements(cardinalRays);

        if(previousMove.getEndDir().equals(position))
        {
            shortTermMoveMemory.put(previousMove.getDeltaPos().hashCode(), previousMove.getDeltaPos());
            possibleMovements = resolveAgentMovementClash(possibleMovements);
        }
        else
        {
            shortTermMoveMemory.clear();
        }
        return possibleMovements;
    }

    public ArrayList<Vector> resolveAgentMovementClash(ArrayList<Vector> possibleMoves)
    {
        ArrayList<Vector> modifiedPossibleMoves = new ArrayList<>();
        for(Vector move: possibleMoves)
        {
            if(shortTermMoveMemory.get(move.hashCode()) == null)
            {
                modifiedPossibleMoves.add(move);
            }
        }
        return modifiedPossibleMoves;
    }

    public ArrayList<Vector> determineEquivalentMinMoves(ArrayList<Double> cellPheromones, ArrayList<Vector> possibleMovements)
    {
        double minValue = Double.MAX_VALUE;
        ArrayList<Vector> equivalentMoves = new ArrayList<>();

        for(int i = 0; i < cellPheromones.size(); i++)
        {
            Double cellPheromone = cellPheromones.get(i);
            if(cellPheromone != null && cellPheromone == minValue)
            {
                equivalentMoves.add(possibleMovements.get(i));
            }
            else if(cellPheromone != null && cellPheromones.get(i) < minValue)
            {
                equivalentMoves.clear();
                minValue = cellPheromones.get(i);
                equivalentMoves.add(possibleMovements.get(i));
            }
        }
        return equivalentMoves;
    }

    public Vector selectRandomEquivalentMove(ArrayList<Vector> equivalentMoves)
    {
        if(equivalentMoves.size() != 0)
        {
            return equivalentMoves.get(randomGenerator.nextInt(equivalentMoves.size()));
        }
        else
        {
            ArrayList<Vector> rememberedMoves =  new ArrayList<>(shortTermMoveMemory.values());
            int randomIndex =  randomGenerator.nextInt(rememberedMoves.size());
            return rememberedMoves.get(randomIndex);
        }
    }

    public ArrayList<Double> accessAvaliableCellPheromones(ArrayList<Vector> possibleMovements)
    {

        ArrayList<Double> cellPheromoneValues = new ArrayList<>();

        for(Vector movement : possibleMovements)
        {
            Vector resultingPosition = position.add(movement);
            PheromoneCell possibleCell = (PheromoneCell) world.getCellAt(resultingPosition);
            if(possibleCell != null)
            {
                cellPheromoneValues.add(possibleCell.currentPheromoneValue());
            }
            else
            {
                cellPheromoneValues.add(null);
            }
        }
        return cellPheromoneValues;
    }

    public ArrayList<Vector> determineAvailableMovements(Ray[] cardinalRays)
    {
        ArrayList<Vector> possibleMovements = new ArrayList<>();

        for(int i = 0; i < cardinalRays.length; i++)
        {
            if(cardinalRays[i].rayLength() > cellSize + epsilon)
            {
                possibleMovements.add(angleToGridMovementLink(cardinalAngles[i]));
            }
        }
        return possibleMovements;
    }

    public Vector angleToGridMovementLink(int angle)
    {
        return switch (angle)
                {
                    case 0 -> new Vector(0, cellSize);
                    case 90 -> new Vector(cellSize, 0);
                    case 180 -> new Vector(0, -cellSize);
                    case 270 -> new Vector(-cellSize, 0);
                    default -> null;
                };
    }

    public Ray[] detectCardinalRays()
    {
        Ray[] cardinalRays = new Ray[cardinalAngles.length];

        for(int i = 0; i < cardinalAngles.length; i++)
        {
            cardinalRays[i] = detectCardinalPoint(cardinalAngles[i]);
        }
        return cardinalRays;
    }

    public Ray detectCardinalPoint(double targetCardinalAngle)
    {
        int upperBound = view.size() - 1;
        int lowerBound = 0;

        while(lowerBound <= upperBound)
        {
            int midPoint = calculateMidPoint(upperBound, lowerBound);
            double currentAngle = view.get(midPoint).angle();

            if(approximateAngleRange(currentAngle, targetCardinalAngle))
            {
                return view.get(midPoint);
            }
            else if(currentAngle < targetCardinalAngle)
            {
                lowerBound = midPoint + 1;
            }
            else if(currentAngle > targetCardinalAngle)
            {
                upperBound = midPoint - 1;
            }
        }
        System.out.println("Cardinal Point not found");
        return null;
    }

    private int calculateMidPoint(int upperBound, int lowerBound)
    {
        return lowerBound + (upperBound - lowerBound)/2;
    }

    public boolean approximateAngleRange(double detectedAngle, double targetAngle)
    {
        return detectedAngle < (targetAngle + epsilon) && detectedAngle > (targetAngle - epsilon);
    }

    public double releaseMaxPheromone()
    {
        return maxPheromone;
    }

    public static void initializeWorld(AcoGrid newWorld)
    {
        world = newWorld;
    }

    public static AcoGrid accessWorld()
    {
        return world;
    }

    public int[] getCardinalAngles()
    {
        return cardinalAngles;
    }

    public static void clearAcoCounts()
    {
        AcoAgentCount = 0;
        AcoMoveCount = 0;
    }

    public HashMap<Integer, Vector> accessShortTermMemory()
    {
        return shortTermMoveMemory;
    }

    public int getShortTermMemorySize()
    {
        return shortTermMoveMemory.size();
    }
}
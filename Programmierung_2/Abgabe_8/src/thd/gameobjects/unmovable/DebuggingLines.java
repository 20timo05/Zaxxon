package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.TravelPathCalculator;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import java.awt.*;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;


/**
 * A class that displays lines on the GameView. These help visualize how the isometric movement works,
 * e.g. where GameObjects (de)spawn, how the player moves, ...
 *
 * @see GameObject
 */
public class DebuggingLines extends GameObject {
    private final Position[] outerScreenCoordinates;
    private final Position[][] spawnLines;
    private final Position[][] travelPathBoundaryLines;
    private final double[][] playerMovementAreaCoords;

    /**
     * Initializes a {@code DebuggingLines} object that displays various calculated Values from {@link TravelPathCalculator}.
     *
     * @param gameView the gameView to display the lines on
     * @param gamePlayManager   reference to the gamePlayManager
     * @see TravelPathCalculator
     */

    public DebuggingLines(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);

        distanceToBackground = 1;

        outerScreenCoordinates = new Position[]{
                TRAVEL_PATH_CALCULATOR.getOuterScreenEntry()[0],
                TRAVEL_PATH_CALCULATOR.getOuterScreenEntry()[1],
                TRAVEL_PATH_CALCULATOR.getOuterScreenExit()[0],
                TRAVEL_PATH_CALCULATOR.getOuterScreenExit()[1]
        };

        spawnLines = new Position[][]{
                TRAVEL_PATH_CALCULATOR.getSpawnLine(),
                TRAVEL_PATH_CALCULATOR.getDespawnLine()
        };

        travelPathBoundaryLines = new Position[][]{
                new Position[]{TRAVEL_PATH_CALCULATOR.getSpawnLine()[0], TRAVEL_PATH_CALCULATOR.getDespawnLine()[0]},
                new Position[]{TRAVEL_PATH_CALCULATOR.getSpawnLine()[1], TRAVEL_PATH_CALCULATOR.getDespawnLine()[1]}
        };


        double[] xCoords = {
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[0].getX(),
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[0].getX(),
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[1].getX(),
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[1].getX()
        };
        double[] yCoords = {
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[0].getY(),
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[0].getY() - GameSettings.MAX_PLAYER_ALTITUDE,
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[1].getY() - GameSettings.MAX_PLAYER_ALTITUDE,
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[1].getY()
        };
        playerMovementAreaCoords = new double[][]{xCoords, yCoords};
    }

    /**
     * Renders {@code DebuggingLines} object as a various lines, dots, ... on {@code gameView}.
     *
     * @see GameView#addLineToCanvas
     */
    @Override
    public void addToCanvas() {
        for (Position dot : outerScreenCoordinates) {
            gameView.addOvalToCanvas(dot.getX(), dot.getY(), 10, 10, 1, true, Color.BLUE);
        }

        for (Position[] line : spawnLines) {
            gameView.addLineToCanvas(line[0].getX(), line[0].getY(), line[1].getX(), line[1].getY(), 5, Color.GREEN);
        }

        for (Position[] line : travelPathBoundaryLines) {
            gameView.addLineToCanvas(line[0].getX(), line[0].getY(), line[1].getX(), line[1].getY(), 2, Color.WHITE);
        }


        gameView.addPolygonToCanvas(playerMovementAreaCoords[0], playerMovementAreaCoords[1], 3, false, Color.red);
    }

    /**
     * String representation of {@code DebuggingLines} object.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "<DebuggingLines />";
    }
}

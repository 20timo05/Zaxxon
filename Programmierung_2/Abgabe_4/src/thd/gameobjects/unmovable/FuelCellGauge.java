package thd.gameobjects.unmovable;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import static thd.gameobjects.unmovable.FuelCellGaugeBlockImages.*;


/**
 * A status bar, that displays the amount of Fuel the Player has left.
 *
 * @see GameObject
 */
public class FuelCellGauge extends GameObject {
    private double fuelLevel;
    /**
     * Initializes a {@code FuelCellGauge} object.
     *
     * @param gameView to display the {@code FuelCellGauge} object on
     */
    public FuelCellGauge(GameView gameView) {
        super(gameView);

        size = 3;
        width = FuelCellGaugeBlockImages.WIDTH * MAX_NUM_FUEL_CELLS;
        height = FuelCellGaugeBlockImages.HEIGHT;
        fuelLevel = 1;

        position.updateCoordinates(
                GameView.WIDTH * 0.45 - width*size / 2,
                GameSettings.GAME_HEIGHT + GameSettings.FOOTER_HEIGHT * 0.5
        );
    }

    /**
     * Renders {@code FuelCellGauge} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        String fuelCellGaugeBlockImage = createBlockImage(fuelLevel);

        gameView.addBlockImageToCanvas(fuelCellGaugeBlockImage, position.getX(), position.getY(), size, rotation);
    }

    @Override
    public void updateStatus() {
        fuelLevel -= 0.0001;
    }

    /**
     * Creates the BlockImage Graphic String dynamically based on the Fuel Level of the Player.
     *
     * @param fuelLevel factor of how full the Fuel Tank is
     * @return the BlockImage Graphic String
     */
    private String createBlockImage(double fuelLevel) {
        if (fuelLevel < 0 || fuelLevel > 1) {
            throw new IllegalArgumentException("fuelLevel should be in [0, 1]");
        }

        int numFuelCells = (int) (MAX_NUM_FUEL_CELLS * fuelLevel);
        int widthOfLastCell = (int) ((MAX_NUM_FUEL_CELLS * fuelLevel % 1) * WIDTH);

        StringBuilder[] rows = new StringBuilder[HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            rows[i] = new StringBuilder();
        }

        // Append full cells
        for (int cellIdx = 0; cellIdx < numFuelCells; cellIdx++) {
            for (int row = 0; row < HEIGHT; row++) {
                rows[row].append(FUEL_CELL[row]);
            }
        }

        // Append partial cell
        for (int row = 0; row < HEIGHT; row++) {
            rows[row].append(FUEL_CELL[row], 0, widthOfLastCell);
        }

        // combine all rows into one coherent BlockImage String
        StringBuilder result = new StringBuilder();
        for (StringBuilder row : rows) {
            result.append(row.toString()).append("\n");
        }

        return result.toString();
    }

    /**
     * String representation of {@code FuelCellGauge} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "FuelCellGauge: " + position;
    }
}

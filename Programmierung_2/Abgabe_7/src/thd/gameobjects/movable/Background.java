package thd.gameobjects.movable;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

import java.util.Vector;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;
import thd.gameobjects.base.Vector2d;

/**
 * The Background GameObject moves along with the other GameObjects, but is
 * solely for design purposes.
 */
public class Background extends GameObject implements ShiftableGameObject, ActivatableGameObject {
  private final String backgroundWallBlockImage;
  /**
   * Creates a new {@code Background} GameObject.
   * 
   * @param gameView                 GameView to show the game object on.
   * @param gamePlayManager          reference to the gamePlayManager
   * @param backgroundWallBlockImage the pregenerated BlockImage String for the
   *                                 background Wall
   */
  public Background(GameView gameView, GamePlayManager gamePlayManager, String backgroundWallBlockImage) {
    super(gameView, gamePlayManager, 0, 0);

    this.backgroundWallBlockImage = backgroundWallBlockImage;
    size = Math.floor(GameSettings.MAX_PLAYER_ALTITUDE / (9 * height));

    Vector2d newTargetPosition = new Vector2d(targetPosition);
    newTargetPosition
        .add(new Vector2d(-TRAVEL_PATH_CALCULATOR.getDistanceToDespawnLine(), GameSettings.MOVEMENT_ANGLE_IN_RADIANS));
    targetPosition.updateCoordinates(newTargetPosition);
  }

  /**
     * Renders {@code Background} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
      gameView.addBlockImageToCanvas(backgroundWallBlockImage, position.getX(), position.getY(), size, rotation);
    }


  @Override
  public boolean tryToActivate(Object info) {
    if (info instanceof Background) {
      return ((Background) info).calcInterpolation() > 0.5;
    }
    return false;
  }
}

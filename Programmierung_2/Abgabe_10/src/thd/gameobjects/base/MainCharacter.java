package thd.gameobjects.base;

import thd.gameobjects.movable.ZaxxonFighterLaserShot;

/**
 * Mandatory methods that {@link thd.gameobjects.movable.ZaxxonFighter} has to implement.
 */
public interface MainCharacter {

    /**
     * A method that should shoot a {@link ZaxxonFighterLaserShot} straight ahead.
     */
    void shoot();
}

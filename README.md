# Zaxxon C64 Remake - Java Edition

## Project Overview

This project is a Java-based remake of the classic C64 game "Zaxxon" (Synapse version). It was developed as part of the "Programmieren 2" course for the Bachelor's degree in "Artificial Intelligence" at the Deggendorf Institute of Technology. The game recreates core mechanics like isometric navigation, combat, and resource management, built upon a base architecture provided for the course.

A key achievement is the **dynamic wall generation system**, which translates 2D textual descriptions of walls into complex isometric 3D `BlockImage` graphics and their corresponding hitboxes on-the-fly. The design also emphasizes deriving game world dimensions and object movements from a few core constants for adaptability.

## Core Features

*   **Isometric 3D Perspective & Player Control:** Faithful Zaxxon view with altitude and lateral fighter movement.
*   **Combat & Levels:** Engage enemies across three distinct levels with shooting mechanics.
*   **Dynamic Wall System:** Walls are defined via 2D char maps and dynamically rendered as isometric `BlockImages` with hitboxes.
*   **Diverse Game Objects:** Includes various enemies (Enemy Shooters, Gun Emplacements, Vertical Rockets) and obstacles (Energy Barriers, Radar Towers, Fuel Tanks).
*   **Game Mechanics:** Collision detection, scoring, lives system, and fuel management.
*   **Difficulty Levels & UI:** "Easy"/"Standard" settings, height/fuel status bars, and score display.

## Gameplay

Pilot the Zaxxon fighter through the enemy fortress, destroying targets for points and collecting fuel. Avoid collisions with walls and enemy fire while managing altitude.

**Controls:**
*   **Arrow Keys Up/Down (or W/S):** Change altitude.
*   **Arrow Keys Left/Right (or A/D):** Move laterally.
*   **Spacebar:** Fire.

## Technical Highlights

*   **Parametric Design:** Most positional and movement calculations are based on a small set of fundamental constants (e.g., `GameSettings.MOVEMENT_ANGLE_IN_RADIANS`), ensuring world consistency.
*   **Dynamic Wall Generation:** A custom system (`WallPreprocessingService.java`, `WallBuildingService.java`, `WallBlockGraphicUtils.java`) converts 2D 'x' character descriptions from `Level.java` into detailed isometric `BlockImage` strings and hitboxes for rendering.
*   **Isometric Calculations:** `TravelPathCalculator.java` handles isometric projection and path definitions.

## Project Structure

*   `src/thd/game/bin`: Main entry point (`StartGame.java`).
*   `src/thd/game/level`: Level definitions and difficulty.
*   `src/thd/game/managers`: Core game logic controllers.
*   `src/thd/gameobjects`: Base classes, movable (player, enemies), and unmovable (UI) objects.
*   `src/thd/game/utilities`:
    *   `GameView.java`: **Professor-provided library** for rendering, input, sound, and timing.
    *   Utilities for wall generation, isometric math (`TravelPathCalculator.java`), file access, etc.
*   `src/thd/screens`: Start and end screens.

## Simplifications from Original Zaxxon

The following were omitted for project scope:
*   Free space combat sequences.
*   Boss battles.

## How to Run

1.  Ensure a compatible Java Development Kit (JDK) is installed.
2.  Compile all `.java` files.
3.  Run `src.thd.game.bin.StartGame.java`.
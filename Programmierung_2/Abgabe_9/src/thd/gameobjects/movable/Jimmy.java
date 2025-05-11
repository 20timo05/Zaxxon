package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;

// Diese Klasse muss nicht mit Javadoc kommentiert werden.
public class Jimmy extends GameObject {

    private enum State {RUNNING, JUMPING, HOVERING}
    private State currentState;
    private String blockImage;

    private enum RunningState {
        RUNNING_1(JimmyBlockImages.RUNNING_1),
        RUNNING_2(JimmyBlockImages.RUNNING_2),
        RUNNING_3(JimmyBlockImages.RUNNING_3),
        RUNNING_4(JimmyBlockImages.RUNNING_4),
        RUNNING_5(JimmyBlockImages.RUNNING_5);

        final String display;

        RunningState(String display) {
            this.display = display;
        }
    }
    private RunningState runningState;

    private enum JumpingState {
        JUMPING_1(JimmyBlockImages.STANDARD, 0),
        JUMPING_2(JimmyBlockImages.JUMPING, 50),
        JUMPING_3(JimmyBlockImages.JUMPING, 30),
        JUMPING_4(JimmyBlockImages.JUMPING, 0),
        JUMPING_5(JimmyBlockImages.JUMPING, 0),
        JUMPING_6(JimmyBlockImages.JUMPING, -30),
        JUMPING_7(JimmyBlockImages.JUMPING, -50),
        JUMPING_8(JimmyBlockImages.STANDARD, 0);

        final String display;
        final int up;

        JumpingState(String display, int up) {
            this.display = display;
            this.up = up;
        }
    }
    private JumpingState jumpingState;

    public Jimmy(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);
        currentState = State.RUNNING;
        size = 5;
        height = 21 * size;
        width = 15 * size;
        speedInPixel = 5;
        distanceToBackground = 10000;
        resetPosition();

        runningState = RunningState.values()[0];
        jumpingState = JumpingState.values()[0];
    }

    private void resetPosition() {
        position.updateCoordinates(-width, GameView.HEIGHT - height);
    }

    @Override
    public void updateStatus() {
        switch (currentState) {
            case RUNNING -> {
                blockImage = runningState.display;
                position.right(speedInPixel);

                if (gameView.timer(80, 0, this)) {
                    switchToNextRunningState();
                }
            }
            case JUMPING -> {
                blockImage = jumpingState.display;
                position.right(speedInPixel);

                if (gameView.timer(80, 0, this)) {
                    switchToNextJumpingState();
                    position.up(jumpingState.up);
                }
            }
            case HOVERING -> {
                blockImage = JimmyBlockImages.STANDARD;
                position.right(speedInPixel);
            }
        }
        if (position.getX() > GameView.WIDTH) {
            resetPosition();
            switchToNextState();
            resetStates();
        }
    }

    private void switchToNextState() {
        int nextState = (currentState.ordinal() + 1) % State.values().length;
        currentState = State.values()[nextState];
    }

    private void switchToNextRunningState() {
        int nextState = (runningState.ordinal() + 1) % RunningState.values().length;
        runningState = RunningState.values()[nextState];
    }

    private void switchToNextJumpingState() {
        int nextState = (jumpingState.ordinal() + 1) % JumpingState.values().length;
        jumpingState = JumpingState.values()[nextState];
    }

    private void resetStates() {
        runningState = RunningState.values()[0];;
        jumpingState = JumpingState.values()[0];
    }

    @Override
    public void addToCanvas() {
        gameView.addBlockImageToCanvas(blockImage, position.getX(), position.getY(), size, rotation);
    }
}

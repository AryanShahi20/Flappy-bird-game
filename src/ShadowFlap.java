import bagel.*;

/**
 * Code for SWEN20003 Project 1, Semester 2, 2021
 * ShadowFlap is a game similar to flappy bird but with 2 different levels.
 * Play the game by pressing space to fly and 's' to shoot in level 1.
 * Aim of the game is to go through the pipes without colliding
 * or leaving the window boundaries.
 * constants are a mixture of spec sheet and demo videos
 *
 * @author Aryan Shahi
 * @version 1.0
 */
public class ShadowFlap extends AbstractGame {
    private final Level0 level0 = new Level0();
    private final Level1 level1 = new Level1();

    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private static boolean nextLevel = false;

    /**
     * Instantiates a new Shadow flap.
     */
    public ShadowFlap() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, "ShadowFlap");
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.isDown(Keys.ESCAPE)) {
            Window.close();
        }

        if(!nextLevel){

            level0.update(input);

        } else {

            level1.update(input);

        }
    }

    /**
     * Set next level.
     *
     * @param value the value nextLevel is set to
     */
    public static void setNextLevel(boolean value){
        nextLevel = value;
    }

}



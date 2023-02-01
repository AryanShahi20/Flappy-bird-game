import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is the first level (level 0) of ShadowFlap.java
 *
 * @author Aryan
 * @version 1.0
 */
public class Level0 extends Level{
    private final Bird bird = new Bird(0);

    private boolean spacePressed = false;
    private int score = 0;

    private final Image background = new Image("res/level-0/background.png");

    private static final int SCORE_GAP = 75;

    private static final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private static final String FAIL_MSG = "GAME OVER";
    private String scoreMessage = "FINAL SCORE: ";

    private static final Point SCORE_START = new Point(100, 100);
    private final ArrayList<PipeSet> pipes = new ArrayList<>();
    private double frameCount = 0;

    private int life = bird.getLife();

    private static final String LVL_MSG = "LEVEL-UP!";
    private static final int LEVEL_UP_FRAMES = 150;
    private int levelFrameCount = 0;

    private static final int LVL0_MAX_SCORE = 10;

    /**
     * performs a level state Update.
     * runs all the logic for level 0
     *
     * @param input the input
     */
    public void update(Input input){
        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        // Game begins when player presses space
        if (input.wasPressed(Keys.SPACE)) {
            spacePressed = true;
        }

        // Player hasn't begun so keep showing instruction screen
        if (!spacePressed) {
            printCenteredMsg(INSTRUCTION_MSG);

        } else {

            // add pipes after the given interval
            if(PipeSet.addPipe(frameCount)){
                frameCount = 0;
                pipes.add(new PipeSet(0));
            }

            PipeSet.scaleTime(input);

            // completed level 0
            if (score == LVL0_MAX_SCORE) {
                printCenteredMsg(LVL_MSG);

                // setting up level 1 after level up screen is rendered
                if(levelFrameCount==LEVEL_UP_FRAMES){
                    ShadowFlap.setNextLevel(true);
                    PipeSet.resetTimeScale();
                }

                levelFrameCount++;

            // no hp game lost
            } else if (life == 0) {
                printDoubleMessage(FAIL_MSG,scoreMessage,SCORE_GAP);

            // level hasn't been lost or won so continue playing
            } else {

                bird.update(input);
                for (PipeSet pipeSet : pipes) {
                    pipeSet.update();
                }
                bird.updateLife(life);

                if(checkScore(pipes, bird)){
                    score++;
                }

                collisionDetection();

                if(boundDetection(bird)){
                    life--;
                    bird.reset();
                }

                if (life == 0) {
                    scoreMessage += score;
                }

                getFont().drawString("SCORE: " + score,
                        SCORE_START.x, SCORE_START.y);

                frameCount++;
            }

        }

    }

    /**
     * Checks to see whether the bird collides with the pipes and
     * if it does remove a hit point
     */
    public void collisionDetection() {
        Iterator<PipeSet> iterator = pipes.iterator();

        while (iterator.hasNext()){
            PipeSet pipeSet = iterator.next();
            Rectangle birdHitbox = bird.getHitbox();
            Rectangle topHitbox = pipeSet.getTopBox();
            Rectangle bottomHitbox = pipeSet.getBottomBox();

            boolean test1 = birdHitbox.intersects(topHitbox);
            boolean test2 = birdHitbox.intersects(bottomHitbox);

            if(test1 || test2 ){
                life--;
                iterator.remove();
            }
        }
    }

}

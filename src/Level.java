import bagel.Font;
import bagel.Input;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;

/**
 * Provides a base for level 0 and level 1 used in
 * ShadowFlap.java
 *
 * @author Aryan
 * @version 1.0
 */
public abstract class Level {
    private final Font font = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private static final int FONT_SIZE = 48;

    /**
     * Prints out 2 messages with the first being centered
     * and the second being put below in accordance with
     * the gap.
     *
     * @param msg1 the first message
     * @param msg2 the second message
     * @param gap  the gap between the 2 messages
     */
    protected void printDoubleMessage(String msg1, String msg2, int gap) {

        printCenteredMsg(msg1);

        Point msg2Start = getFontStart(msg2);
        font.drawString(msg2, msg2Start.x, msg2Start.y + gap);

    }

    /**
     * Print a centered msg.
     *
     * @param message the message to be printed
     */
    protected void printCenteredMsg(String message){
        Point messageStart = getFontStart(message);
        font.drawString(message, messageStart.x, messageStart.y);
    }

    /**
     * Gets font starting point, so it is printed
     * in the centre of the screen.
     *
     * @param message the message to be printed
     * @return the font starting point
     */
    protected Point getFontStart(String message) {
        double x = (Window.getWidth() / 2.0) - (font.getWidth(message) / 2.0);
        double y = (Window.getHeight() / 2.0) + (FONT_SIZE / 2.0);

        return new Point(x, y);
    }

    /**
     * Collision detection implemented accordingly in level 0 and 1.
     */
    protected abstract void collisionDetection();

    /**
     * level update implemented accordingly in level 0 and 1.
     */
    protected abstract void update(Input input);

    /**
     * Gets the font.
     *
     * @return the font
     */
    protected Font getFont(){
        return font;
    }

    /**
     * Checks to see if the bird goes out of bounds.
     *
     * @param bird the bird
     * @return the boolean which is true if bird out of bounds
     */
    protected boolean boundDetection(Bird bird){

        boolean test1 = (bird.getPoint().y < 0);
        boolean test2 = (bird.getPoint().y > Window.getHeight());

        return test1 || test2;
    }


    /**
     * Checks to see if the bird has passed a pipe set if it has
     * a score is added.
     *
     * @param pipes the pipes
     * @param bird  the bird
     * @return the boolean which is true if bird has scored
     */
    protected boolean checkScore(ArrayList<PipeSet> pipes, Bird bird){

        for (PipeSet pipeSet : pipes) {
            double d1 = bird.getPoint().x;
            double d2 = pipeSet.getTopBox().right();
            double errorMargin = PipeSet.getPipeSpeed();

            boolean scored = ((d1 - d2) <= errorMargin && (d1 - d2) >= 0);

            if (scored) {
                return true;
            }
        }

        return false;
    }



}

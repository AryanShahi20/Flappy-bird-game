import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Stores all attributes and methods of both birds level 0
 * and level 1 used in ShadowFlap.java
 *
 * @author Aryan
 * @version 1.0
 */
public class Bird {
    private Image birdUp;
    private Image birdDown;
    private int life;

    private double velocity = 0;
    private int frameCount = 0;

    private static final double MAX_VELOCITY = 10;
    private static final double UPWARDS_VELOCITY = 6;
    private static final double GRAVITY = 0.4;
    private static final int FLAP_SPEED = 10;

    private static final double BIRD_X = 200;
    private static final double BIRD_Y = 350;
    private Point birdPoint = new Point(BIRD_X, BIRD_Y);

    private boolean acquired = false;

    private LifeBar health;
    private static final int LVL_0_HEARTS = 3;
    private static final int LVL_1_HEARTS = 6;

    /**
     * Instantiates a new Bird.
     *
     * @param level the level of the bird to be instantiated
     */
    public Bird(int level) {

        if(level == 0) {
            birdUp = new Image("res/level-0/birdWingUp.png");
            birdDown = new Image("res/level-0/birdWingDown.png");
            health = new LifeBar(LVL_0_HEARTS);
            life = LVL_0_HEARTS;
        }
        if(level == 1) {
            birdUp = new Image("res/level-1/birdWingUp.png");
            birdDown = new Image("res/level-1/birdWingDown.png");
            health = new LifeBar(LVL_1_HEARTS);
            life = LVL_1_HEARTS;
        }
    }

    /**
     * Gets bird point
     *
     * @return the point corresponding to the bird
     */
    public Point getPoint() {
        return new Point(birdPoint.x, birdPoint.y);
    }

    /**
     * Reset the bird to original position.
     */
    public void reset(){
        birdPoint = new Point(BIRD_X,BIRD_Y);
        velocity = 0;
    }

    /**
     * Gets hitbox of the bird.
     *
     * @return the hitbox of the bird
     */
    public Rectangle getHitbox() {
        return new Rectangle(birdUp.getBoundingBoxAt(birdPoint));
    }

    /** Adjusts the birds y-coordinate depending on whether space has been
     * pressed or not and limits bird falling speed
     */
    private Point birdMove() {
        double newY;
        // Makes sure bird doesn't fall faster than 10 pixels per frame
        if (velocity < MAX_VELOCITY) {
            newY = birdPoint.y + velocity;
        } else {
            newY = birdPoint.y + MAX_VELOCITY;
        }

        return new Point(birdPoint.x, newY);
    }

    /**
     * performs a bird state Update.
     *
     * @param input the input to determine if bird needs to go up
     */
    public void update(Input input) {
        frameCount++;
        // Makes sure bird "flaps" every 10 frames
        if (frameCount % FLAP_SPEED == 0) {
            birdUp.draw(birdPoint.x, birdPoint.y);
        } else {
            birdDown.draw(birdPoint.x, birdPoint.y);
        }
        // Adjusts velocity when space is pressed
        if (input.wasPressed(Keys.SPACE)) {
            velocity = -UPWARDS_VELOCITY;
        } else {
            velocity += GRAVITY;
        }
        birdPoint = birdMove();
    }

    /**
     * Set acquired.
     *
     * @param value the value acquired is set to
     */
    public void setAcquired(boolean value){
        acquired = value;
    }

    /**
     * Get acquired boolean.
     *
     * @return the boolean
     */
    public boolean getAcquired(){
        return acquired;
    }

    /**
     * Get bird info point.
     *
     * @return the point
     */
    public Point getBirdInfo(){
        return new Point(birdUp.getBoundingBoxAt(birdPoint).right(), birdPoint.y);
    }

    /**
     * Get life int.
     *
     * @return the int
     */
    public int getLife(){
        return life;
    }

    /**
     * Update life so print out birds current hp.
     *
     * @param life how much health the bird has
     */
    public void updateLife(int life){
        health.updateLife(life);
    }

}




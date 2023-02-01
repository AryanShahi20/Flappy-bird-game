import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The PipeSet class for both levels in ShadowFlap.java
 *
 * @author Aryan
 * @version 1.0
 */
public class PipeSet{
    private static final Image PLASTIC_IMAGE = new Image("res/level/plasticPipe.png");
    private static final Image STEEL_IMAGE = new Image("res/level-1/steelPipe.png");
    private final Image FIRE = new Image("res/level-1/flame.png");
    private Image pipeImage;

    private static final int PIPE_GAP = 168;
    private double topPipeY;
    private double bottomPipeY;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();

    private static double spawnGap = 100;
    private static double pipeSpeed = 5;

    private static int timeScale = 1;
    private static final int MIN_TIMESCALE = 1;
    private static final int MAX_TIMESCALE = 5;
    private static final double TIME_FACTOR = 0.5;
    private static final double SCALING = 1 + TIME_FACTOR;

    private static final double INITIAL_SPAWN_GAP = 100;
    private static final double INITIAL_PIPE_SPEED = 5;

    private static final int HIGH_GAP = 100;
    private static final int MID_GAP = 300;
    private static final int LOW_GAP = 500;

    private static final int FLAME_INTERVAL = 20;
    private static final int FLAME_TIME = 30;
    private static final int FLAME_GAP = 10;
    private int flameFrameCount = 0;
    private int activeFlameCount = 0;
    private boolean hasFlame = false;
    private boolean flameActive = false;

    private boolean isSteel;


    /**
     * Instantiates a new Pipe set.
     *
     * @param level the level of the pipe set
     */
    public PipeSet(int level) {
        spawnPipe(level);
    }

    /**
     * Render the pipe set and flames if a steel pipe.
     */
    public void renderPipeSet() {
        pipeImage.draw(pipeX, topPipeY);
        pipeImage.draw(pipeX, bottomPipeY, ROTATOR);

        if(hasFlame && flameActive){
            FIRE.draw(pipeX, topPipeY + (pipeImage.getHeight() / 2.0) + FLAME_GAP);
            FIRE.draw(pipeX, bottomPipeY - (pipeImage.getHeight() / 2.0) - FLAME_GAP, ROTATOR);
            activeFlameCount++;
            // to allow for flame to come and go in intervals
            if(activeFlameCount == FLAME_TIME){
                flameActive = false;
                activeFlameCount = 0;
            }
        }
    }

    /**
     * Update the state of the pipes and move them along the screen.
     */
    public void update() {

        if(!flameActive && hasFlame){
            flameFrameCount++;
        }
        if (flameFrameCount == FLAME_INTERVAL){
            flameActive = true;
            flameFrameCount = 0;
        }
        renderPipeSet();
        pipeX -= pipeSpeed;
    }

    /**
     * Gets top box.
     *
     * @return the top box
     */
    public Rectangle getTopBox() {
        return pipeImage.getBoundingBoxAt(new Point(pipeX, topPipeY));

    }

    /**
     * Gets bottom box.
     *
     * @return the bottom box
     */
    public Rectangle getBottomBox() {
        return pipeImage.getBoundingBoxAt(new Point(pipeX, bottomPipeY));

    }

    /**
     * Gets top flame box.
     *
     * @return the top flame box
     */
    public Rectangle getTopFlameBox() {
        return pipeImage.getBoundingBoxAt(new Point(pipeX, topPipeY +
                FLAME_GAP + (FIRE.getHeight() / 2.0)));

    }

    /**
     * Gets bot flame box.
     *
     * @return the bot flame box
     */
    public Rectangle getBotFlameBox() {
        return pipeImage.getBoundingBoxAt(new Point(pipeX, bottomPipeY -
                FLAME_GAP - (FIRE.getHeight() / 2.0)));

    }

    /**
     * Add pipe boolean.
     *
     * @param frameCount the frame count to see if it is time to spawn pipe
     * @return the boolean indicating if pipe should be spawned
     */
    public static boolean addPipe(double frameCount){
        return frameCount >= spawnGap;

    }

    /**
     * Scales time.
     *
     * @param input the input to decide whether to scale time up or down
     */
    public static void scaleTime(Input input){

        // lower timescale
        if(input.wasPressed(Keys.K)){

            if(timeScale > MIN_TIMESCALE){
                timeScale--;
                spawnGap *= SCALING;
                pipeSpeed /= SCALING;
            }
        }
        // increase timescale
        if(input.wasPressed(Keys.L)){

            if(timeScale < MAX_TIMESCALE){
                timeScale++;
                spawnGap /= SCALING;
                pipeSpeed *= SCALING;
            }
        }
    }

    /**
     * Spawns pipes for both levels.
     *
     * @param level the level the pipe is being spawned for
     */
    public void spawnPipe(int level){
        int random;

        if (level == 0) {
            this.pipeImage = PLASTIC_IMAGE;
            random = ThreadLocalRandom.current().nextInt(0, 2 + 1);

            // high gap
            if (random == 0) {

                setPipeCoordinates(HIGH_GAP);

            // mid gap
            } else if (random == 1) {

                setPipeCoordinates(MID_GAP);

            // low gap
            } else if (random == 2) {

                setPipeCoordinates(LOW_GAP);

            }
        }

        if (level == 1){
            double gapStart = (ThreadLocalRandom.current().nextDouble() * (500 - 100)) + 100;
            random = ThreadLocalRandom.current().nextInt(0, 1 + 1);

            // plastic pipe
            if (random == 0){

                pipeImage = PLASTIC_IMAGE;
                isSteel = false;

            // steel pipe
            } else if (random == 1){

                pipeImage = STEEL_IMAGE;
                hasFlame = true;
                isSteel = true;

            }
            setPipeCoordinates(gapStart);

        }

    }

    public void setPipeCoordinates(double start){
        topPipeY = start - (pipeImage.getHeight() / 2.0);
        bottomPipeY = start + PIPE_GAP + (pipeImage.getHeight() / 2.0);
    }

    /**
     * Get pipe speed double.
     *
     * @return the double
     */
    public static double getPipeSpeed(){
        return pipeSpeed;
    }

    /**
     * Get spawn gap double.
     *
     * @return the double
     */
    public static double getSpawnGap(){
        return spawnGap;
    }

    /**
     * Reset time scale.
     */
    public static void resetTimeScale(){
        timeScale = 1;
        spawnGap = INITIAL_SPAWN_GAP;
        pipeSpeed = INITIAL_PIPE_SPEED;
    }

    /**
     * Is active flame boolean.
     *
     * @return the boolean
     */
    public boolean isActiveFlame(){
        return hasFlame && flameActive;
    }

    /**
     * Get is steel boolean.
     *
     * @return the boolean
     */
    public boolean getIsSteel(){
        return isSteel;
    }

}

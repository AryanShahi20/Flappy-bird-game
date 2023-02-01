import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The weapon class for all level 1 weapons
 *
 * @author Aryan
 * @version 1.0
 */
public class Weapon {

    private static final Image ROCK_IMAGE = new Image("res/level-1/rock.png");
    private static final Image BOMB_IMAGE = new Image("res/level-1/bomb.png");
    public DrawOptions scale = new DrawOptions().setScale(0.5,0.5);
    private Image weapon;

    private static final double MIN_Y = 100;
    private static final double MAX_Y = 500;
    private static double movementSpeed = 5;
    private static final double SHOOT_SPEED = 5;
    private double weaponX = Window.getWidth();
    private double weaponY = (ThreadLocalRandom.current().nextDouble() * (MAX_Y - MIN_Y)) + MIN_Y;
    private static double spawnRate;

    private boolean acquired = false;
    private boolean breakSteel;

    private boolean shot = false;
    private double shotY;
    private boolean remove;

    private double shotFrames = 0;

    private static boolean firstWeapon = true;

    private static int timeScale = 1;
    private static final int MIN_TIMESCALE = 1;
    private static final int MAX_TIMESCALE = 5;
    private static final double TIME_FACTOR = 0.5;
    private static final double SCALING = 1 + TIME_FACTOR;

    private static final int ROCK_DISTANCE = 25;
    private static final int BOMB_DISTANCE = 50;

    private final int random = ThreadLocalRandom.current().nextInt(0, 1 + 1);

    /**
     * Instantiates a new Weapon and randomly decides whether it is a rock or a bomb
     */
    public Weapon(){

        if(random == 0){
            weapon = ROCK_IMAGE;
            breakSteel = false;
        }
        if(random == 1){
            weapon = BOMB_IMAGE;
            breakSteel = true;
        }
    }

    /**
     * implements the shooting logic for each weapon
     */
    public void shoot(){
        shotFrames++;
        // this weapon is a rock
        if(random == 0){
            if(shotFrames == ROCK_DISTANCE){
                shot = false;
                remove = true;
            }
        }
        // this weapon is a bomb
        if(random == 1){
            if(shotFrames == BOMB_DISTANCE){
                shot = false;
                remove = true;
            }
        }

        weaponX += SHOOT_SPEED;
        weapon.draw(weaponX, shotY, scale);

    }

    /**
     * Spawn weapon boolean.
     *
     * @param weaponFrames the weapon frames to check when to spawn
     * @return the boolean indicating it is ready to spawn
     */
    public static boolean spawnWeapon(double weaponFrames){
        spawnRate = PipeSet.getSpawnGap();

        if(weaponFrames >= (PipeSet.getSpawnGap()/2.0) && firstWeapon){
            firstWeapon = false;
            return true;
        } else {
            return weaponFrames >= spawnRate;
        }
    }

    /**
     * Update the weapon before being shot.
     */
    public void update() {

        if(acquired){
            weapon.draw(weaponX, weaponY);
        } else if (!shot && !remove) {
            weapon.draw(weaponX, weaponY);
            weaponX -= movementSpeed;
        }
    }

    /**
     * Gets weapon hitbox.
     *
     * @return the hitbox
     */
    public Rectangle getBox() {
        return weapon.getBoundingBoxAt(new Point(weaponX, weaponY));
    }

    /**
     * Set acquired.
     *
     * @param value the value of acquired
     */
    public void setAcquired(boolean value){
        acquired = value;
    }

    /**
     * Set shot.
     *
     * @param value the value of shot
     */
    public void setShot(boolean value){
        shot = value;
    }

    /**
     * Set shot y.
     *
     * @param value the value of shot y
     */
    public void setShotY(double value){
        shotY = value;
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
     * Get shot boolean.
     *
     * @return the boolean
     */
    public boolean getShot(){
        return shot;
    }

    /**
     * Set bird info.
     *
     * @param birdInfo the bird info needed to spawn weapon on its beak
     */
    public void setBirdInfo(Point birdInfo){
        weaponX = birdInfo.x;
        weaponY = birdInfo.y;
    }

    /**
     * Get remove boolean.
     *
     * @return the boolean
     */
    public boolean getRemove(){return remove;}

    /**
     * Get break steel boolean.
     *
     * @return the boolean
     */
    public boolean getBreakSteel(){
        return breakSteel;
    }

    /**
     * Scale time.
     *
     * @param input the input to see whether to scale up or down
     */
    public static void scaleTime(Input input){

        // lower timescale
        if(input.wasPressed(Keys.K)){

            if(timeScale > MIN_TIMESCALE){
                timeScale--;
                spawnRate *= SCALING;
                movementSpeed /= SCALING;
            }
        }
        // increase timescale
        if(input.wasPressed(Keys.L)){

            if(timeScale < MAX_TIMESCALE){
                timeScale++;
                spawnRate /= SCALING;
                movementSpeed *= SCALING;
            }
        }
    }

    public static void setFirstWeapon(){
        firstWeapon = true;
    }


}

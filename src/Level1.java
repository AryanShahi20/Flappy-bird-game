import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is the second level (level 1) for ShadowFlap.java
 */
public class Level1 extends Level{
    private final Bird bird = new Bird(1);

    private boolean spacePressed = false;
    private int score = 0;

    private final Image background = new Image("res/level-1/background.png");

    private static final int SCORE_GAP = 75;

    private static final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private static final String FAIL_MSG = "GAME OVER";
    private static final String WIN_MSG = "CONGRATULATIONS!";
    private String scoreMessage = "FINAL SCORE: ";

    private static final Point SCORE_START = new Point(100, 100);

    private final ArrayList<PipeSet> pipes = new ArrayList<>();
    private final ArrayList<Weapon> weapons = new ArrayList<>();
    private double frameCount = 0;
    private double weaponFrameCount = 0;

    private int life = bird.getLife();

    private static final int LVL1_START_GAP = 68;
    private static final String SHOOT_MSG = "PRESS 'S' TO SHOOT";

    private static final int LVL1_MAX_SCORE = 30;


    /**
     * performs a level 1 state Update.
     * runs all the logic for level 1
     *
     * @param input the input
     */
    public void update(Input input){

        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        if (input.wasPressed(Keys.SPACE)) {
            spacePressed = true;
        }

        if (!spacePressed) {
            printDoubleMessage(INSTRUCTION_MSG,SHOOT_MSG,LVL1_START_GAP);

        // level 1 has begun
        } else {

            // my own cool take on timescale I reckon
            // I implemented something similar to demo in level 0
            // this method here makes it so u actually get a break
            // before launching into a different time-scale
            // stops pipe spawning too close to each other
            // also makes weapons not spawn on pipes
            // also my weapon frequency is high makes it very fun on high timescales
            if (input.wasPressed(Keys.K) || input.wasPressed(Keys.L)){
                frameCount = 0;
                weaponFrameCount = 0;
                Weapon.setFirstWeapon();
            }

            if(PipeSet.addPipe(frameCount)){
                frameCount = 0;
                pipes.add(new PipeSet(1));
            }

            if(Weapon.spawnWeapon(weaponFrameCount)){
                weaponFrameCount = 0;
                weapons.add(new Weapon());
            }

            PipeSet.scaleTime(input);

            Weapon.scaleTime(input);

            if (score == LVL1_MAX_SCORE) {
                printCenteredMsg(WIN_MSG);

            } else if (life == 0) {
                printDoubleMessage(FAIL_MSG,scoreMessage,SCORE_GAP);

            } else {

                bird.update(input);
                for (PipeSet pipeSet : pipes) {
                    pipeSet.update();
                }
                for (Weapon weapon : weapons) {
                    weapon.update();
                }
                bird.updateLife(life);

                if(checkScore(pipes, bird)){
                    score++;
                }

                acquireWeapon();

                carryWeapon();

                if(input.wasPressed(Keys.S) && bird.getAcquired()) {
                    useWeapon();
                }

                weaponPipeCollision();

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
                weaponFrameCount++;

                removeUnnecessaryWeapon();
            }

        }

    }

    /**
     * Checks to see whether the bird collides with the pipes or if
     * it collides with the flames from steel pipes and
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

            if(test1 || test2){
                life--;
                iterator.remove();
            }

            // if no collision with pipe check to see if collision with flames
            else if(pipeSet.isActiveFlame()) {
                Rectangle topFlameHitbox = pipeSet.getTopFlameBox();
                Rectangle botFlameHitbox = pipeSet.getBotFlameBox();

                boolean test3 = birdHitbox.intersects(topFlameHitbox);
                boolean test4 = birdHitbox.intersects(botFlameHitbox);

                if(test3 || test4){
                    life--;
                    iterator.remove();
                }
            }
        }
    }

    /**
     * acknowledges bird has acquired weapon and weapon has
     * been acquired if both hit boxes intersect and the
     * bird is not already carrying a weapon
     */
    public void acquireWeapon(){

        for (Weapon weapon : weapons) {

            Rectangle box1 = weapon.getBox();
            Rectangle box2 = bird.getHitbox();

            if(box1.intersects(box2)){

                if(!bird.getAcquired() && !weapon.getShot()) {
                    weapon.setAcquired(true);
                    bird.setAcquired(true);
                }
            }
        }
    }

    /**
     * Allows the weapon to be carried on the beak
     * of the bird as long as it has not been shot
     */
    public void carryWeapon(){

        if(bird.getAcquired()){

            for (Weapon weapon : weapons) {

                if(weapon.getAcquired()){
                    weapon.setBirdInfo(bird.getBirdInfo());
                }
            }
        }
    }

    /**
     * Uses the weapon and updates all booleans accordingly allowing
     * the weapon to impact a pipe and the bird to pick up another one
     */
    public void useWeapon(){

        for (Weapon weapon : weapons) {

            if(weapon.getAcquired()){
                bird.setAcquired(false);
                weapon.setAcquired(false);
                weapon.setShot(true);
                weapon.setShotY(bird.getPoint().y);
            }
        }
    }

    /**
     * Checks to see if weapon collides with a pipe
     * and if it does implement the logic correctly taking into account
     * whether it is a steel pipe and whether the weapon can break steel pipes
     * adds 1 point to the score if pipe is broken
     */
    public void weaponPipeCollision(){
        Iterator<Weapon> iterator1 = weapons.iterator();
        // for every weapon
        while (iterator1.hasNext()){
            Weapon weapon = iterator1.next();
            // if the weapon got shot
            if(weapon.getShot()){
                weapon.shoot();
                Iterator<PipeSet> iterator2 = pipes.iterator();
                // for every pipe set
                while (iterator2.hasNext()){
                    PipeSet pipeSet = iterator2.next();
                    Rectangle box1 = pipeSet.getTopBox();
                    Rectangle box2 = pipeSet.getBottomBox();
                    Rectangle box3 = weapon.getBox();
                    // weapon is removed regardless of pipe cause collision
                    if(box3.intersects(box1) || box3.intersects(box2)){
                        iterator1.remove();
                        // if it can break steel remove pipe or if it can't break steel
                        // and pipe is not steel remove pipe
                        if(weapon.getBreakSteel() ||
                                (!weapon.getBreakSteel() && !pipeSet.getIsSteel())){
                            iterator2.remove();
                            score++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes unnecessary weapons.
     */
    public void removeUnnecessaryWeapon(){
        Iterator<Weapon> iterator = weapons.iterator();

        while (iterator.hasNext()) {
            Weapon weapon = iterator.next();
            if(weapon.getRemove()){
                iterator.remove();
            }
        }
    }


}

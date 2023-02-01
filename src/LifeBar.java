import bagel.Image;

/**
 * The life bar for the bird class.
 *
 * @author Aryan
 * @version 1.0
 */
public class LifeBar {
    private final Image NO_LIFE_IMAGE = new Image("res/level/noLife.png");
    private final Image FULL_LIFE_IMAGE = new Image("res/level/fullLife.png");
    private static final int LIFE_START_Y = 15;
    private static final int LIFE_START_x = 100;
    private static final int HEART_GAP = 50;
    private final int maxLife;

    /**
     * Performs a life bar update.
     *
     * @param life the life of the bird
     */
    public void updateLife(int life) {
        int lifeStartX = LIFE_START_x;

        for(int i=0; i<maxLife; i++){

            if(i < life) {
                FULL_LIFE_IMAGE.drawFromTopLeft(lifeStartX, LIFE_START_Y);
            } else {
                NO_LIFE_IMAGE.drawFromTopLeft(lifeStartX, LIFE_START_Y);
            }

            lifeStartX += HEART_GAP;
        }
    }

    /**
     * Instantiates a new Life bar.
     *
     * @param maxHearts the max hearts for the life bar
     */
    public LifeBar(int maxHearts){
        maxLife = maxHearts;
    }
}

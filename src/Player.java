/**
 * Created by daneb on 6/4/2017.
 */
public class Player implements GameObject {
    private Rectangle playerRectangle;
    private int speed = 10;
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;

    //0=right, 1=left, 2=up, 3=down
    private int direction = 0;

    public Player(Sprite sprite){
        this.sprite = sprite;
        if (sprite != null && sprite instanceof AnimatedSprite){
            animatedSprite = (AnimatedSprite) sprite;
        }
        updateDirection();
        playerRectangle = new Rectangle(32, 16, 16, 32);
        playerRectangle.generateGraphics(0xFF00FF90);
    }

    private void updateDirection(){
        if (animatedSprite != null) {
            animatedSprite.setAnimationRange(direction * 2, direction * 2 + 2);
        }
    }

    public void update(Game game){ //this method runs
        KeyboardListener keyboardListener = game.getKeyListener();
        boolean didMove = false;
        int newdirection = direction;

        if (keyboardListener.left()){
            playerRectangle.x -= speed;
            didMove = true;
            newdirection = 1;
        }

        if (keyboardListener.right()){
            playerRectangle.x += speed;
            newdirection = 0;
            didMove = true;
        }
        if (keyboardListener.up()){
            playerRectangle.y -= speed;
            newdirection = 2;
            didMove = true;
        }

        if (keyboardListener.down()){
            playerRectangle.y += speed;
            newdirection = 3;
            didMove = true;
        }

        if (newdirection != direction) {
            direction = newdirection;
            updateDirection();
        }

        if (!didMove){
            animatedSprite.reset();
        }

        updateCamera(game.getRenderer().getCamera());

        if (didMove) {
            animatedSprite.update(game);
        }
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom){
        if (animatedSprite != null) {
            renderer.renderSprite(animatedSprite, playerRectangle.x, playerRectangle.y, xZoom-1, yZoom-1, false);
        }
        else if (sprite != null) {
            renderer.renderSprite(sprite, playerRectangle.x, playerRectangle.y, xZoom-1, yZoom-1, false);
        }
        else {
            renderer.renderRectangle(playerRectangle, xZoom-1, yZoom-1, false);
        }
    }

    public void updateCamera(Rectangle camera){
        camera.x = playerRectangle.x - (camera.w / 2);
        camera.y = playerRectangle.y - (camera.h / 2);
    }

    public boolean handleMouseClick(Rectangle rectangle, Rectangle camera, int x, int y){return false;}
}

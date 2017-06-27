import java.awt.image.BufferedImage;

/**
 * Created by daneb on 6/15/2017.
 */
public class AnimatedSprite extends Sprite implements GameObject {
    private Game game;
    private BufferedImage image;
    private int speed;
    private Sprite[] sprites;
    private int currentSprite = 0;
    int counter = 0;

    private int startSprite = 0;
    private int endSprite;

    public int x, y; //might have to make by pixel instead of by tile

    //speed represents amount of frames pass before sprite changes
    public AnimatedSprite(BufferedImage[] image, int speed, int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
        sprites = new Sprite[image.length];
        this.speed = speed;
        this.startSprite = image.length-1;

        for (int i = 0; i < image.length; i++) {
            sprites[i] = new Sprite(image[i]);
        }

    }

    public AnimatedSprite(SpriteSheet sheet, Rectangle[] positions, int speed){
        sprites = new Sprite[positions.length];
        this.speed = speed;
        for (int i = 0; i < sprites.length-1; i++) {
            sprites[i] = sheet.getSprite(positions[i].x, positions[i].y);
        }
        endSprite = sprites.length-1;
        x = positions[0].w;
        y = positions[0].h; //using first width and height of position array for location on Frame
    }

    public void reset(){
        counter = 0;
        currentSprite = startSprite;
    }

    public AnimatedSprite(SpriteSheet sheet, int speed){
        sprites = sheet.getLoadedSprites();
        this.speed = speed;
        this.endSprite = sprites.length-1;
    }

    //render specifically dealt with layer class
    public void render(RenderHandler renderHandler, int xZoom, int yZoom){
        renderHandler.renderSprite(this, x*(16*xZoom), y*(16*yZoom), xZoom, yZoom, false);
    }

    public void update(Game game){
        counter++;
        if (counter >= speed){
            counter = 0;
            incrementSprite();
        }
    }

    public void setAnimationRange(int startSprite, int endSprite){
        this.startSprite = startSprite;
        this.endSprite = endSprite;
        reset();
    }

    public int getWidth(){
        return sprites[currentSprite].getWidth();
    }

    public int getHeight(){
        return sprites[currentSprite].getHeight();
    }

    public int[] getPixels(){
        return sprites[currentSprite].getPixels();
    }

    public void incrementSprite(){
        currentSprite++;
        if (currentSprite >= endSprite){
            currentSprite = startSprite;
        }
    }

    public boolean handleMouseClick(Rectangle rectangle, Rectangle camera, int x, int y){return false;}
}

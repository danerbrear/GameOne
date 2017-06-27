import java.awt.image.BufferedImage;

/**
 * Created by daneb on 5/21/2017.
 */
public class Sprite {
    protected int width, height;
    protected int[] pixels;

    public Sprite(SpriteSheet spriteSheet, int startX, int startY, int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width * height];
        spriteSheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width); //gets certain section of the spritesheet

    }

    public Sprite(BufferedImage image){
        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
    }

    public Sprite(){}

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int[] getPixels(){return pixels;}

}

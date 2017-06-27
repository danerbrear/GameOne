import java.awt.image.BufferedImage;

/**
 * Created by daneb on 5/21/2017.
 */
public class SpriteSheet {
    private int[] pixels;
    private BufferedImage image;
    public final int SIZEX;
    public final int SIZEY;
    private Sprite[] loadedSprites = null;
    private boolean spritesLoaded = false;
    private int spriteSizeX;
    private int spriteSizeY;

    public SpriteSheet(BufferedImage sheetImage){
        this.image = sheetImage;
        SIZEX = sheetImage.getWidth();
        SIZEY = sheetImage.getHeight();

        pixels = new int[SIZEX * SIZEY];
        pixels = sheetImage.getRGB(0, 0, SIZEX, SIZEY, pixels, 0, SIZEX);
    }

    public int[] getPixels(){
        return pixels;
    }

    public BufferedImage getImage(){
        return image;
    }

    public Sprite getSprite(int x, int y){
        if (spritesLoaded){
            int spriteID = x + y * (SIZEX / spriteSizeX); //latter part sets each "block" on sprite sheet as one unit
            if (spriteID < loadedSprites.length) {
                return loadedSprites[spriteID];
            }
            else {
                System.out.println("Loaded a sprite out of bounds");
            }
        }
        else {
            System.out.println("No loaded Sprite");
        }
        return null;
    }


    public void loadSprites(int spriteSizeX, int spriteSizeY){
        this.spriteSizeX = spriteSizeX;
        this.spriteSizeY = spriteSizeY;
        loadedSprites = new Sprite[(SIZEX / spriteSizeX) * (SIZEY / spriteSizeY)];
        int spriteID = 0;
        for (int y = 0; y < SIZEY; y += spriteSizeY) {
            for (int x = 0; x < SIZEX; x += spriteSizeX) {
                loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
                spriteID++;
            }
        }
        spritesLoaded = true;
    }

    public Sprite[] getLoadedSprites(){return loadedSprites;}

}

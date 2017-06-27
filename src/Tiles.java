import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by daneb on 5/27/2017.
 */
public class Tiles {
    private SpriteSheet spriteSheet;
    private ArrayList<Tile>  tilesList = new ArrayList<Tile>();

    public Tiles(File tilesFile, SpriteSheet spriteSheet){
        this.spriteSheet = spriteSheet;
        try {
            Scanner scanner = new Scanner(tilesFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (!line.startsWith("//")){
                    String[] splitString = line.split("-");
                    String tileName = splitString[0];
                    int SpriteX = Integer.parseInt(splitString[1]);
                    int SpriteY = Integer.parseInt(splitString[2]);
                    Tile tile = new Tile(tileName, spriteSheet.getSprite(SpriteX, SpriteY));
                    tilesList.add(tile);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderTile(int tileID, RenderHandler renderHandler, int xPosition, int yPosition, int xZoom, int yZoom){
        if (tileID >= 0 && tilesList.size() > tileID){
            renderHandler.renderSprite(tilesList.get(tileID).sprite, xPosition, yPosition, xZoom, yZoom, false);
        }
        else {
            System.out.println("TileID" + tileID + "is not within range - " + tilesList.size());
        }
    }

    public int size()
    {
        return tilesList.size();
    }

    public Sprite[] getSprites(){
        Sprite[] sprites = new Sprite[size()];

        for(int i = 0; i < sprites.length; i++)
            sprites[i] = tilesList.get(i).sprite;

        return sprites;
    }

    class Tile {
        private String tileName;
        private Sprite sprite;

        public Tile(String tileName, Sprite sprite){
            this.tileName = tileName;
            this.sprite = sprite;


        }
    }
}

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by daneb on 5/27/2017.
 */
public class Map {
    private Tiles tileSet;
    private int fillTileID = -1;
    private ArrayList<MappedTile> mappedTiles = new ArrayList<MappedTile>();
    private File mapFile;
    private HashMap<Integer, String> comments = new HashMap<Integer, String>();

    public Map(File mapFile, Tiles tileSet){
        this.tileSet = tileSet;
        this.mapFile = mapFile;
        try{
            Scanner scanner = new Scanner(mapFile);
            int currentLine = 0;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (!line.startsWith("//")){
                    if (line.contains(":")) {
                       String [] splitString = line.split(":");
                       if (splitString[0].equalsIgnoreCase("Fill")){
                           fillTileID = Integer.parseInt(splitString[1]);
                           continue;
                       }
                    }
                    String[] splitString = line.split("-");
                    if (splitString.length >= 3){
                        MappedTile mappedTile = new MappedTile(Integer.parseInt(splitString[0]), Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]));
                        mappedTiles.add(mappedTile);
                    }
                }
                else {
                    comments.put(currentLine, line);
                }
                currentLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setTile(int tileX, int tileY, int id){
        boolean foundTile = false;
        for (int i = 0; i < mappedTiles.size(); i++) {
            MappedTile mappedTile = mappedTiles.get(i);
            if (mappedTile.x == tileX && mappedTile.y == tileY){
                mappedTile.id = id;
                foundTile = true;
                break;
            }
        }

        if (!foundTile){
            mappedTiles.add(new MappedTile(id, tileX, tileY));
        }
    }

    public void removeTile(int tileX, int tileY){
        for (int i = 0; i < mappedTiles.size(); i++) {
            MappedTile mappedTile = mappedTiles.get(i);
            if (mappedTile.x == tileX && mappedTile.y == tileY){
                mappedTiles.remove(i);
            }
        }
    }

    public void renderMap(RenderHandler renderer, int xZoom, int yZoom){
        int xIncrement = 16 * xZoom;
        int yIncrement = 16 * yZoom;

        if (fillTileID >= 0){
            Rectangle camera = renderer.getCamera();

            for (int y = camera.y - yIncrement - (camera.y % yIncrement); y < camera.y + camera.h; y += yIncrement) {
                for (int x = camera.x - xIncrement - (camera.x % xIncrement); x < camera.x + camera.w; x += xIncrement) {
                    tileSet.renderTile(fillTileID, renderer, x, y, xZoom, yZoom);
                }
            }
        }

        for (int tileIndex = 0; tileIndex < mappedTiles.size(); tileIndex++) {
            MappedTile mappedTile = mappedTiles.get(tileIndex);
            tileSet.renderTile(mappedTile.id, renderer, (mappedTile.x * 16 * xZoom), (mappedTile.y * 16 * yZoom), xZoom, yZoom);
        }
    }

    public void saveMap(){
       try {
           int currentLine = 0;

           if (mapFile.exists()) {
               mapFile.delete();
           }
           mapFile.createNewFile();

           PrintWriter printWriter = new PrintWriter(mapFile);
           if (fillTileID >= 0) {
               if (comments.containsKey(currentLine)) {
                   printWriter.println(comments.get(currentLine));
                   currentLine++;
               }
               printWriter.println("Fill:" + fillTileID);
           }

           for (int i = 0; i < mappedTiles.size(); i++) {
               if (comments.containsKey(currentLine)){
                   printWriter.println(comments.get(currentLine));
               }

               MappedTile tile = mappedTiles.get(i);
               printWriter.println(tile.id + "-" + tile.x + "-" + tile.y);
               currentLine++;
           }

           printWriter.close();
       } catch (IOException e){
           e.printStackTrace();
       }
    }

    //TileID in the Tileset and the position of the tile in the map
    class MappedTile{
        public int id, x, y;

        public MappedTile(int id, int x, int y){
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }
}

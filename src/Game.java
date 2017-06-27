import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game extends JFrame implements Runnable
{
    public static int alpha = 0xFFF700F7;
//    public static int beta = 0xFF7F007F;

    private Canvas canvas = new Canvas();
    private RenderHandler renderer;
    private BufferedImage grass;
    private Rectangle testRectangle = new Rectangle(30, 90, 100, 100);

    private SpriteSheet playerSheet;
    private SpriteSheet sheet;

    private Tiles tiles;
    private Map map;

    private int selectedTileID = 13;

    private Sprite smallTree;
    private Sprite downLeftBeach;
    private Sprite water;
    private Sprite sand;

    private KeyboardListener keyListener = new KeyboardListener(this);
    private MouseEventListener mouseEventListener = new MouseEventListener(this);

    private GameObject[] objects;
    private Player player;
    private AnimatedSprite animatedSprite;

    private int xZoom = 3;
    private int yZoom = 3;


    String fileName = "res/SpriteSheet5.png";

    public Game()
    {
        //Make our program shutdown when we exit out.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set the position and size of our frame.
        setBounds(0,0, 1000, 800);

        //Put our frame in the center of the screen.
        setLocationRelativeTo(null);

        //Add our graphics compoent
        add(canvas);

        //Make our frame visible.
        setVisible(true);

        //Create our object for buffer strategy.
        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());

        //first sprite sheet
        BufferedImage sheetImage = loadImage(fileName);
        sheet = new SpriteSheet(sheetImage);
        sheet.loadSprites(16, 16);

        //player animations
        BufferedImage playerImage = loadImage("res/(2anim)CharacterSheet.png");
        playerSheet = new SpriteSheet(playerImage);
        playerSheet.loadSprites(32, 48);
        AnimatedSprite playerAnimations = new AnimatedSprite(playerSheet, 7);

        //Load Tiles
        tiles = new Tiles(new File("res/Tiles"), sheet);

        //Load Map
        map = new Map(new File("res/Map"), tiles);

        //sand = sheet.getSprite(3, 0);
        testRectangle.generateGraphics(3, 1234);

        //mouse sprite animation
        Rectangle[] mousePositions = new Rectangle[18];
        mousePositions[0] = new Rectangle(4, 3, 11, 16);
        mousePositions[1] = new Rectangle(5, 4, 11, 16);
        mousePositions[2] = new Rectangle(7, 4, 11, 16);
        mousePositions[3] = new Rectangle(7, 3, 11, 16);
        mousePositions[4] = new Rectangle(6, 3, 1, 1);
        mousePositions[5] = new Rectangle(6, 4, 1, 1);
        mousePositions[6] = new Rectangle(6, 3, 1, 1);
        mousePositions[7] = new Rectangle(7, 4, 1, 1);
        mousePositions[8] = new Rectangle(5, 4, 1, 1);
        mousePositions[9] = new Rectangle(4, 3, 1, 1);
        mousePositions[10] = new Rectangle(4, 3, 1, 1);
        mousePositions[11] = new Rectangle(4, 3, 1, 1);
        mousePositions[12] = new Rectangle(4, 3, 1, 1);
        mousePositions[13] = new Rectangle(4, 3, 1, 1);
        mousePositions[14] = new Rectangle(4, 3, 1, 1);
        mousePositions[15] = new Rectangle(4, 3, 1, 1);
        mousePositions[16] = new Rectangle(4, 3, 1, 1);
        mousePositions[17] = new Rectangle(4, 3, 1, 1);

        //Load SDK GUI
        GUIButton[] buttons = new GUIButton[tiles.size() / 2];
        GUIButton[] buttonsTwo = new GUIButton[tiles.size() / 2];
        Sprite[] tileSprites = tiles.getSprites();
        Sprite[] firstHalf = new Sprite[tileSprites.length / 2];
        Sprite[] secondHalf = new Sprite[tileSprites.length / 2];
        for (int i = 0; i < secondHalf.length; i++) {
            secondHalf[i] = tileSprites[i+(tileSprites.length / 2)];
        }
        for (int i = 0; i < firstHalf.length; i++) {
            firstHalf[i] = tileSprites[i];
        }
        for(int i = 0; i < buttons.length; i++) {
            Rectangle tileRectangle = new Rectangle(0, (i*(16*xZoom + 2)), (16*xZoom), (int)(16*yZoom));
            buttons[i] = new SDKButton(this, i, firstHalf[i], tileRectangle);
        }
        for(int i = 0; i < buttons.length; i++) {
            Rectangle tileRectangle = new Rectangle(0, (i*(16*xZoom + 2)), (16*xZoom), (int)(16*yZoom));
            buttonsTwo[i] = new OffsetSDK(this, i + secondHalf.length, secondHalf[i], tileRectangle);
        }
        GUI gui = new GUI(buttons, 5, 5, true);
        GUI guiTwo = new GUI(buttonsTwo, 5, 5, true);

        //load objects
        objects = new GameObject[4];
        player = new Player(playerAnimations);
        animatedSprite = new AnimatedSprite(sheet, mousePositions, 30);
        objects[0] = gui; //uncomment if i want the first half of sprites
        objects[1] = guiTwo;
        objects[2] = animatedSprite;
        objects[3] = player;


        //add listeners
        canvas.addKeyListener(keyListener);
        canvas.addFocusListener(keyListener);
        canvas.addMouseListener(mouseEventListener);
        canvas.addMouseMotionListener(mouseEventListener);
    }

    private BufferedImage loadImage(String path){
        try {
            BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path)); // wrong format
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);
            return formattedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //using for AnimatedSprite atm
    private BufferedImage loadTileImage(String path,int tileX,int tileY){
        try {
            BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path)); // wrong format
            BufferedImage subImage = loadedImage.getSubimage((tileX*16), (tileY*16), 16, 16);
            return subImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void leftClick(int x, int y) {
       Rectangle mouseRectangle = new Rectangle(x, y, 1, 1);
       boolean stoppedChecking = false;
        for (int i = 0; i < objects.length; i++) {
               if (!stoppedChecking) {
                   stoppedChecking = objects[i].handleMouseClick(mouseRectangle, renderer.getCamera(), xZoom, yZoom);
               }
        }

        if (!stoppedChecking) {
            x = (int) Math.floor((x + renderer.getCamera().x) / (16.0 * xZoom));
            y = (int) Math.floor((y + renderer.getCamera().y) / (16.0 * yZoom));
            map.setTile(x, y, selectedTileID);
        }

        System.out.println(x + ", " + y);
    }

    public void rightClick(int x, int y) {
        x = (int)Math.floor((x + renderer.getCamera().x)/(16.0 * xZoom));
        y = (int)Math.floor((y + renderer.getCamera().y)/(16.0 * yZoom));

        map.removeTile(x, y);

        System.out.println(x + ", " + y);
    }

    public void changeTile(int tileID){
        selectedTileID = tileID;
    }


    public void update() {
        for (int i = 0; i < objects.length; i++) {
            objects[i].update(this);
        }
    }


    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        super.paint(graphics);

        //tiles.renderTile(1, renderer, 300, 100, 4, 4);

        map.renderMap(renderer, (int)xZoom,(int)yZoom);

        for (int i = 0; i < objects.length; i++) {
            objects[i].render(renderer, xZoom, yZoom);
        }

        //renderer.renderRectangle(testRectangle, 1, 1);

        renderer.render(graphics);

        graphics.dispose();
        bufferStrategy.show();

        renderer.clear();
    }

    public void run() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        int i = 0;
        int x = 0;

        long lastTime = System.nanoTime(); //long 2^63
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
        double changeInSeconds = 0;

        while(true) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            while(changeInSeconds >= 1) {
                update();
                changeInSeconds = 0;
            }

            render();
            lastTime = now;
        }
    }

    public static void main(String[] args)
    {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    public int getSelectedTile()
    {
        return selectedTileID;
    }

    public KeyboardListener getKeyListener(){
        return keyListener;
    }

    public RenderHandler getRenderer(){return renderer;}

    public MouseEventListener getMouseEventListener(){return mouseEventListener;}

    public SpriteSheet getSheet(){return sheet;}

    public void handleCTRL(boolean[] keys){
        if (keys[KeyEvent.VK_S]){
            map.saveMap();
        }
    }

}
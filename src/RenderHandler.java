import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class RenderHandler
{
    private BufferedImage view;
    private int[] pixels;
    private Rectangle camera;

    public RenderHandler(int width, int height) {
        //Create a BufferedImage that will represent our view.
        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        camera = new Rectangle(0, 0, width, height);
        camera.x = -30;
        camera.y = -100;

        //Create an array for pixels
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    }

    //renders our array of pixels to the screen
    public void render(Graphics graphics) {
        graphics.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }

    public void renderImage(BufferedImage image, int xPos, int yPos, int xZoom, int yZoom, boolean fixed){
       int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

       renderArray(imagePixels, image.getWidth(), image.getHeight(), xPos, yPos, xZoom, yZoom, fixed);
    }

    public void renderArray(int[] renderPixels, int renderWidth, int renderHeight, int xPos, int yPos, int xZoom, int yZoom, boolean fixed){
        for (int i = 0; i < renderHeight; i++) {
            for (int x = 0; x < renderWidth; x++) {
                for (int yZoomPos = 0; yZoomPos < yZoom; yZoomPos++){
                    for (int xZoomPos = 0; xZoomPos < xZoom; xZoomPos++){
                        setPixel(renderPixels[x + i * renderWidth], ((x * xZoom) + xPos + xZoomPos), ((i * yZoom) + yPos + yZoomPos), fixed);
                    }
                }
            }
        }
    }

    public void renderSprite(Sprite sprite, int Xpos, int Ypos, int XZoom, int YZoom, boolean fixed){
        renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), Xpos, Ypos, XZoom, YZoom, fixed);
    }

    private void setPixel(int pixel, int x, int y, boolean fixed) {
        int pixelIndex = 0; //if 0 pixel at top left, 6:15 of video
        if (!fixed) {
            if (camera.x <= x && camera.y <= y && x <= camera.x + camera.w && y <= camera.y + camera.h) {
                pixelIndex = (int)((x - camera.x) + (y - camera.y) * view.getWidth());
            }
        }
        else {
            if (x >= 0 && y >= 0 && x <= camera.w && y <= camera.h){
                pixelIndex = (int)(x + y * view.getWidth());
            }
        }
        if (pixels.length > pixelIndex && pixel != Game.alpha) {
            pixels[pixelIndex] = pixel;
        }
    }

    public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom, boolean fixed){
        int[] rectanglePixels = rectangle.getPixels();
        if (rectanglePixels != null){
            renderArray(rectanglePixels, rectangle.w, rectangle.h, rectangle.x, rectangle.y, xZoom, yZoom, fixed);
        }
    }

    public void renderRectangle(Rectangle rectangle, Rectangle offset, int xZoom, int yZoom, boolean fixed){
        int[] rectanglePixels = rectangle.getPixels();
        if(rectanglePixels != null)
            renderArray(rectanglePixels, rectangle.w, rectangle.h, rectangle.x, rectangle.y, xZoom, yZoom, fixed);
    }
    public Rectangle getCamera(){return camera;}

    public void clear(){
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0; //sets movement trail to black
        }
    }
}
public class Rectangle {
    public int x, y, w, h;
    private int[] pixels;

    public Rectangle(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Rectangle(){
        this(0,0,0,0);
    }

    public void generateGraphics(int borderWidth, int color){
        pixels = new int[w*h];

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = Game.alpha;
        }
        
        for (int y = 0; y < borderWidth; y++) {
            for (int x = 0; x < w; x++) {
                pixels[x + y * w] = color;
            }
        }
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < borderWidth; x++) {
                pixels[x + y * h] = color;
            }
        }
        for (int y = 0; y < h; y++) {
            for (int x = w - borderWidth; x < w; x++) {
                pixels[x+y*w] = color;
            }
        }
        for (int y = h - borderWidth; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[x + y * w] = color;
            }
        }
    }

    public void generateGraphics(int color)
    {
        pixels = new int[w*h];
        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                pixels[x + y * w] = color;
    }

    public String toString(){
        return "[" + x + ", " + y + ", " + w + ", " + h + "]";
    }

    public boolean intersects(Rectangle otherRectangle){
        if (x > otherRectangle.x + otherRectangle.w || otherRectangle.x > x + w){
            return false;
        }
        if (y > otherRectangle.y + otherRectangle.h || otherRectangle.y > y + h){
            return false;
        }
        return true;
    }

    public int[] getPixels(){
        return pixels;
    }
}

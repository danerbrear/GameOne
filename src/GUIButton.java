/**
 * Created by daneb on 6/24/2017.
 */
public abstract class GUIButton implements GameObject {
    protected Sprite sprite;
    protected Rectangle rect;
    protected boolean fixed;

    public GUIButton(Sprite sprite, Rectangle rect, boolean fixed){
        this.rect = rect;
        this.sprite = sprite;
        this.fixed = fixed;
        System.out.println(rect);
    }

    //called every time physically possible
    public void render(RenderHandler renderHandler, int xZoom, int yZoom){}

    public void render(RenderHandler renderer, int xZoom, int yZoom, Rectangle interfaceRect){
        renderer.renderSprite(sprite, rect.x + interfaceRect.x, rect.y + interfaceRect.y, xZoom, yZoom, fixed);
    }

    //call at 60 fps
    public void update(Game game){

    }

    //call whenever mouse is clicked on canvas
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom){
//        System.out.println("Mouse: " + mouseRectangle);
//        System.out.println("Button: " + rect);
        if (mouseRectangle.intersects(rect)){
            activate();
            return true;
        }
        return false;
    }

    public abstract void activate();
}

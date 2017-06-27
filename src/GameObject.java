/**
 * Created by daneb on 6/4/2017.
 */
public interface GameObject {
    //called every time physically possible
    public void render(RenderHandler renderHandler, int xZoom, int yZoom);

    //call at 60 fps
    public void update(Game game);

    //call whenever mouse is clicked on canvas
    //return true to stop chcecking other clicks
    public boolean handleMouseClick(Rectangle mouseRectangle, Rectangle camera, int xZoom, int yZoom);
}

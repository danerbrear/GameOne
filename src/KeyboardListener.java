import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by daneb on 6/4/2017.
 */
public class KeyboardListener implements KeyListener, FocusListener {
    public boolean keys[] = new boolean[120];
    private Game game;

    public KeyboardListener(Game game){
        this.game = game;
    }

    public void keyPressed(KeyEvent event){
        int keyCode = event.getKeyCode();
        if (keyCode < keys.length){
            keys[keyCode] = true;
        }
        if (keys[KeyEvent.VK_CONTROL]){
            game.handleCTRL(keys);
        }
    }

    public void keyReleased(KeyEvent event){
        int keyCode = event.getKeyCode();
        if (keyCode < keys.length) {
            keys[keyCode] = false;
        }
    }


    public void focusLost(FocusEvent event){
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    public boolean up(){
        //keys[KeyEvent.VK_W] = true;
        return keys[KeyEvent.VK_W];
    }

    public boolean down(){return keys[KeyEvent.VK_S];
    }

    public boolean left(){
        return keys[KeyEvent.VK_A];
    }

    public boolean right(){
        return keys[KeyEvent.VK_D];
    }

    public void keyTyped(KeyEvent event){}

    public void focusGained(FocusEvent event){}
}

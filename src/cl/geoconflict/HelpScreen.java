/**
 * 
 */
package cl.geoconflict;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;

/**
 * @author Fernando Valencia F.
 *
 */
public class HelpScreen extends Screen {

	public HelpScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        Graphics g = this.game.getGraphics();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	if(inBounds(event, 100, 330, 128, 64) ) {
//                  if(Settings.soundEnabled)
                  	//TODO
            		game.setScreen(new MainMenuScreen(game));
                }
            	if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
//                    if(Settings.soundEnabled);
                    // TODO
                }
            }
        }
		
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.back, 100, 330);
        
        if(Settings.soundEnabled){
            g.drawPixmap(Assets.sound, 0, g.getHeight()-64, 0, 0, 64, 64);
        } else {
            g.drawPixmap(Assets.sound, 0, g.getHeight()-64, 64, 0, 64, 64);
        }
	}

	@Override
	public void pause() {
		Settings.save(this.game.getFileIO());
	}

	@Override
	public void resume() {
		Settings.load(this.game.getFileIO());
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

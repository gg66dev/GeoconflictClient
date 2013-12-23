/**
 * 
 */
package cl.geoconflict.screen;


import cl.geoconflict.Assets;
import cl.geoconflict.Settings;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

/**
 * @author Fernando Valencia F.
 *
 */
public class LoadingScreen extends Screen {
    
	public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
    	//activa GPS
    	
    	
    	Graphics g = game.getGraphics();
        Assets.background = g.newPixmap("background.png", PixmapFormat.RGB565);
        Assets.design = g.newPixmap("design.png", PixmapFormat.RGB565);
        Assets.start = g.newPixmap("start.png", PixmapFormat.RGB565);
        Assets.sound = g.newPixmap("sound.png", PixmapFormat.RGB565);
        Assets.login = g.newPixmap("login.png", PixmapFormat.RGB565);
        Assets.help = g.newPixmap("help.png", PixmapFormat.RGB565);
        Assets.ranking = g.newPixmap("ranking.png", PixmapFormat.RGB565);
        Assets.back = g.newPixmap("back.png", PixmapFormat.RGB565);
        Assets.register = g.newPixmap("register.png", PixmapFormat.RGB565);
        Assets.exit = g.newPixmap("exit.png", PixmapFormat.RGB565);
        
        Settings.load(game.getFileIO());
        game.setScreen(new StartScreen(game));
    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
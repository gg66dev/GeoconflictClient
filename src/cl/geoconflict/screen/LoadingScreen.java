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
        
        //crear partida - buscar partida - unirse partida
//        Assets.create_match = g.newPixmap("create_match.png", PixmapFormat.RGB565);
//        Assets.join_match = g.newPixmap("join_match.png", PixmapFormat.RGB565);
//        Assets.create_map = g.newPixmap("create_map.png", PixmapFormat.RGB565);
//        Assets.tenMinBlack = g.newPixmap("opciones-partida/10min-black.png", PixmapFormat.ARGB4444);
//        Assets.tenMinRed = g.newPixmap("opciones-partida/10min-red.png", PixmapFormat.ARGB4444);
//        Assets.fifteenMinBlack = g.newPixmap("opciones-partida/15min-black.png", PixmapFormat.ARGB4444);
//        Assets.fifteenMinRed = g.newPixmap("opciones-partida/15min-red.png", PixmapFormat.ARGB4444);
//        Assets.twentyMinBlack = g.newPixmap("opciones-partida/20min-black.png", PixmapFormat.ARGB4444);
//        Assets.twentyMinRed = g.newPixmap("opciones-partida/20min-red.png", PixmapFormat.ARGB4444);
//        Assets.hughLayerBlack = g.newPixmap("opciones-partida/hugh-layer-black.png", PixmapFormat.ARGB4444);
//        Assets.hughLayerRed = g.newPixmap("opciones-partida/hugh-layer-red.png", PixmapFormat.ARGB4444);
//        Assets.mediumLayerBlack = g.newPixmap("opciones-partida/medium-layer-black.png", PixmapFormat.ARGB4444);
//        Assets.mediumLayerRed = g.newPixmap("opciones-partida/medium-layer-red.png", PixmapFormat.ARGB4444);
//        Assets.smallLayerBlack = g.newPixmap("opciones-partida/small-layer-black.png", PixmapFormat.ARGB4444);
//        Assets.smallLayerRed = g.newPixmap("opciones-partida/small-layer-red.png", PixmapFormat.ARGB4444);
//        Assets.sdUp = g.newPixmap("opciones-partida/sd-up.png", PixmapFormat.ARGB4444);
//        Assets.empezar = g.newPixmap("empezar.png", PixmapFormat.ARGB4444);
//        Assets.slice = g.newPixmap("slice.png", PixmapFormat.ARGB4444);
        
        //arma-mapa
//        Assets.animationArma = g.newPixmap("animations/animacionArma2.png", PixmapFormat.ARGB4444);
//        Assets.lifeplayer = g.newPixmap("arma-mapa/life.png", PixmapFormat.ARGB4444);
//        Assets.radarSimple = g.newPixmap("arma-mapa/radar.png", PixmapFormat.ARGB4444);
//        Assets.ammo = g.newPixmap("arma-mapa/ammo.png", PixmapFormat.ARGB4444);
//        Assets.geogrilla = g.newPixmap("arma-mapa/geogrilla.png", PixmapFormat.ARGB4444);
//        Assets.playerWhite = g.newPixmap("arma-mapa/playerWhite.png", PixmapFormat.ARGB4444);
//        Assets.simpleAmmo = g.newPixmap("arma-mapa/simpleAmmo2.png", PixmapFormat.ARGB4444);
//        Assets.playerWhite = g.newPixmap("arma-mapa/playerWhite.png", PixmapFormat.ARGB4444);
//        Assets.playerGreen = g.newPixmap("arma-mapa/playerGreen.png", PixmapFormat.ARGB4444);

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
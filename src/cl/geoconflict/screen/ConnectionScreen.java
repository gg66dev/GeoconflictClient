/**
 * 
 */
package cl.geoconflict.screen;


import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.Settings;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.esotericsoftware.kryonet.Client;

/**
 * @author Fernando Valencia F.
 *
 */
public class ConnectionScreen extends Screen {
	private Client client;
	private GameStates gamestates;


	public ConnectionScreen(Game game, Client client, GameStates gamestates) {
        super(game);
        this.client = client;
        this.gamestates = gamestates;
    }

    @Override
    public void update(float deltaTime) {
    	Graphics g = game.getGraphics();
//    	Assets.background.dispose();
//        Assets.design.dispose();
        Assets.start.dispose();
//        Assets.sound.dispose();
        Assets.login.dispose();
        Assets.help.dispose();
        Assets.ranking.dispose();
//        Assets.back.dispose();
        Assets.register.dispose();
        
        //crear partida - buscar partida - unirse partida
        Assets.create_match = g.newPixmap("create_match.png", PixmapFormat.RGB565);
        Assets.join_match = g.newPixmap("join_match.png", PixmapFormat.RGB565);
        Assets.create_map = g.newPixmap("create_map.png", PixmapFormat.RGB565);
        Assets.tenMinBlack = g.newPixmap("opciones-partida/10min-black.jpg", PixmapFormat.ARGB4444);
        Assets.tenMinRed = g.newPixmap("opciones-partida/10min-red.jpg", PixmapFormat.ARGB4444);
        Assets.fifteenMinBlack = g.newPixmap("opciones-partida/15min-black.jpg", PixmapFormat.ARGB4444);
        Assets.fifteenMinRed = g.newPixmap("opciones-partida/15min-red.jpg", PixmapFormat.ARGB4444);
        Assets.twentyMinBlack = g.newPixmap("opciones-partida/20min-black.jpg", PixmapFormat.ARGB4444);
        Assets.twentyMinRed = g.newPixmap("opciones-partida/20min-red.jpg", PixmapFormat.ARGB4444);
        Assets.hughLayerBlack = g.newPixmap("opciones-partida/hugh-layer-black.png", PixmapFormat.ARGB4444);
        Assets.hughLayerRed = g.newPixmap("opciones-partida/hugh-layer-red.png", PixmapFormat.ARGB4444);
        Assets.mediumLayerBlack = g.newPixmap("opciones-partida/medium-layer-black.png", PixmapFormat.ARGB4444);
        Assets.mediumLayerRed = g.newPixmap("opciones-partida/medium-layer-red.png", PixmapFormat.ARGB4444);
        Assets.smallLayerBlack = g.newPixmap("opciones-partida/small-layer-black.png", PixmapFormat.ARGB4444);
        Assets.smallLayerRed = g.newPixmap("opciones-partida/small-layer-red.png", PixmapFormat.ARGB4444);
        Assets.sdUp = g.newPixmap("opciones-partida/sd-up.jpg", PixmapFormat.ARGB4444);
        Assets.empezar = g.newPixmap("empezar.png", PixmapFormat.ARGB4444);
        Assets.slice = g.newPixmap("slice.png", PixmapFormat.ARGB4444);
        
        //arma-mapa
        Assets.animationArma = g.newPixmap("animations/animacionArma.jpg", PixmapFormat.ARGB4444);
        Assets.lifeplayer = g.newPixmap("arma-mapa/life.png", PixmapFormat.ARGB4444);
        Assets.radarSimple = g.newPixmap("arma-mapa/radar.jpg", PixmapFormat.ARGB4444);
        Assets.ammo = g.newPixmap("arma-mapa/ammo.jpg", PixmapFormat.ARGB4444);
        Assets.geogrilla = g.newPixmap("arma-mapa/geogrilla.jpg", PixmapFormat.ARGB4444);
        Assets.playerWhite = g.newPixmap("arma-mapa/playerWhite.jpg", PixmapFormat.ARGB4444);
        Assets.simpleAmmo = g.newPixmap("arma-mapa/simpleAmmo.jpg", PixmapFormat.ARGB4444);
        Assets.playerWhite = g.newPixmap("arma-mapa/playerWhite.png", PixmapFormat.ARGB4444);
        Assets.playerGreen = g.newPixmap("arma-mapa/playerGreen.png", PixmapFormat.ARGB4444);
        Assets.redArrow = g.newPixmap("arma-mapa/redArrow.png", PixmapFormat.ARGB4444);
        
        Settings.load(game.getFileIO());
        game.setScreen(new MenuScreen(this.game, this.client, this.gamestates));
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
/**
 * 
 */
package cl.geoconflict.screen;

import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.Settings;
import cl.geoconflict.gameplay.Clock;
import cl.geoconflict.gameplay.Player;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

/**
 * @author Fernando Valencia F.
 * 
 */
public class ConnectionScreen extends Screen {

	public ConnectionScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();

		if (GameStates.initMatch) {
			/*Carga Juego */
			// arma-mapa
			Assets.animationArma = g.newPixmap("animations/animacionArma.jpg",
					PixmapFormat.ARGB4444);
			Assets.lifeplayer = g.newPixmap("arma-mapa/life.png",
					PixmapFormat.ARGB4444);
			Assets.radarSimple = g.newPixmap("arma-mapa/radar.jpg",
					PixmapFormat.ARGB4444);
			Assets.ammo = g.newPixmap("arma-mapa/ammo.jpg",
					PixmapFormat.ARGB4444);
			Assets.geogrilla = g.newPixmap("arma-mapa/geogrilla.jpg",
					PixmapFormat.ARGB4444);
			Assets.playerWhite = g.newPixmap("arma-mapa/playerWhite.jpg",
					PixmapFormat.ARGB4444);
			Assets.simpleAmmo = g.newPixmap("arma-mapa/simpleAmmo.jpg",
					PixmapFormat.ARGB4444);
			Assets.playerWhite = g.newPixmap("arma-mapa/playerWhite.png",
					PixmapFormat.ARGB4444);
			Assets.playerGreen = g.newPixmap("arma-mapa/playerGreen.png",
					PixmapFormat.ARGB4444);
			Assets.redArrow = g.newPixmap("arma-mapa/redArrow.png",
					PixmapFormat.ARGB4444);
			Settings.load(game.getFileIO());

			// logica del juego se puede usar GameStates
			// se crea mapa se asocia a Player y a gps
			Player player = new Player(20, this.game);
			Clock clockMatch = new Clock(GameStates.timeMatch); // p: tiempo
																// partida
			ArmaScreen arma = new ArmaScreen(game);
			MapaScreen mapa = new MapaScreen(game);
			arma.setMapaScreen(mapa, clockMatch, player);
			mapa.setArmaScreen(arma, clockMatch, player);
			game.setScreen(mapa);
		} else if (GameStates.createMap) {
			/*Carga Creador de Mapas */
			Assets.geogrilla = g.newPixmap("arma-mapa/geogrilla.jpg",
					PixmapFormat.ARGB4444);
			Assets.b_drop = g.newPixmap("creador-mapa/drop.png",
					PixmapFormat.ARGB4444);
			Assets.b_h_add = g.newPixmap("creador-mapa/h_add.png",
					PixmapFormat.ARGB4444);
			Assets.b_h_subtract = g.newPixmap("creador-mapa/h_subtract.png",
					PixmapFormat.ARGB4444);
			Assets.b_new_c = g.newPixmap("creador-mapa/new_c.png",
					PixmapFormat.ARGB4444);
			Assets.b_save = g.newPixmap("creador-mapa/save.png",
					PixmapFormat.ARGB4444);
			Assets.b_w_add = g.newPixmap("creador-mapa/w_add.png",
					PixmapFormat.ARGB4444);
			Assets.b_w_subtract = g.newPixmap("creador-mapa/w_subtract.png",
					PixmapFormat.ARGB4444);
			Settings.load(game.getFileIO());
			game.setScreen(new CrearMapaScreen(game));
		}
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
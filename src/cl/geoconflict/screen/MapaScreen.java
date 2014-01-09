package cl.geoconflict.screen;

import java.util.Enumeration;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.activity.MenuScreen_activity;
import cl.geoconflict.animation.PlayerPoint;
import cl.geoconflict.gameplay.Clock;
import cl.geoconflict.gameplay.Map;
import cl.geoconflict.gameplay.Match;
import cl.geoconflict.gameplay.Position;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.minlog.Log;

public class MapaScreen extends Screen {

	ArmaScreen arma = null;
	Clock clockMatch = null; // p: tiempo partida
	Map map;

	PlayerPoint playerPoints[];

	public MapaScreen(Game game) {
		super(game);
		Assets.simpleAmmo.scale(90, 90);
		Assets.playerWhite.scale(140, 40);
		Assets.playerGreen.scale(140, 40);
		Assets.redArrow.scale(20, 20);

		// se crea una instancia de mapa
		this.map = new Map(this.game.getGraphics().getHeight(), this.game
				.getGraphics().getWidth());

		// se crean los sprites
		Log.debug("Posiciones: " + GameStates.getPositions().size());
		this.playerPoints = new PlayerPoint[GameStates.getPositions().size()];
		Enumeration<String> enumKey = GameStates.getPositions().keys();
		int cont = 0;
		while (enumKey.hasMoreElements()) {
			// Las posiciones se identifican con el nombre de usuario del
			// jugador
			String key = enumKey.nextElement();
			this.playerPoints[cont] = new PlayerPoint(Assets.playerWhite, key,
					20, 20, 1, 6);
			Position pos = this.map.getPosition(
					GameStates.getPositions().get(key)[0], GameStates
							.getPositions().get(key)[1]);
			this.playerPoints[cont++].setPosition((int) pos.x, (int) pos.y);
			Log.debug("Posiciones: (" + GameStates.getPositions().get(key)[0]
					+ ", " + GameStates.getPositions().get(key)[1] + " )");
		}
	}

	public void setArmaScreen(ArmaScreen armascreen, Clock clock) {
		this.arma = armascreen;
		this.clockMatch = clock;

		Assets.lifeplayer.scale(10, 50);
	}

	@Override
	public void update(float deltaTime) {
		if( !GameStates.initMatch ){
			// FIXME
			Intent i = new Intent(this.game.getActivity(),	MenuScreen_activity.class);
			this.game.getActivity().startActivity(i);
			this.game.getActivity().finish();
		}
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		// disminuye tiempo
		clockMatch.update(deltaTime);
		
		// cuando se actualiza GPS se envia log y lat al servidor
		Match.getPlayer().updatePosition();

		// se actualiza la posicion de los jugadores
		if (GameStates.newPosition) {
			Enumeration<String> enumKey = GameStates.getPositions().keys();
			while (enumKey.hasMoreElements()) {
				// Las posiciones se identifican con el nombre de usuario del
				// jugador
				String key = enumKey.nextElement();
				Position pos = this.map.getPosition(GameStates.getPositions()
						.get(key)[0], GameStates.getPositions().get(key)[1]);
				for (PlayerPoint pp : this.playerPoints) {
					if (pp.getId().equals(key)) {
						pp.setPosition((int) pos.x, (int) pos.y);
						pp.update(deltaTime);
						Log.debug("Actualizada posicion de " + pp.getId());
						break;
					}
				}
			}
			GameStates.newPosition = false;
		}

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 400, 90, 90)) {
					if (arma != null)
						game.setScreen(arma);
				}
			}
			
		}
		
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawRect(0, 0, g.getWidth() + 10, g.getHeight() + 10, Color.BLACK);

		// mostrar grilla
		Assets.geogrilla.scale(g.getWidth() - 30, g.getHeight() - 30);
		g.drawPixmap(Assets.geogrilla, 0, 0);

		// puntaje
		g.drawTextRotate("Puntaje", 50, 100, Color.WHITE, 20, 90);
		g.drawTextRotate(Match.getPlayer().getScore(), 50, 120, Color.WHITE, 20, 90);

		// tiempo
		g.drawTextRotate("Tiempo", -50, 100, Color.WHITE, 20, 90);
		g.drawTextRotate(clockMatch.getTime(), -50, 120, Color.WHITE, 20, 90);

		// latitud y longitud , orientacion (debug)
		g.drawTextRotate("lat:" + Match.getPlayer().getLatitud(), 150, 100, Color.GREEN,20, 90);
		g.drawTextRotate("log:" + Match.getPlayer().getLongitud(), 150, 120, Color.GREEN,20, 90);
		g.drawTextRotate("dir:" + Match.getPlayer().getDirection(), 150, 140, Color.GREEN,20, 90);

		// vida
		for (int i = 0; i < Match.getPlayer().getHealth(); i++)
			g.drawPixmap(Assets.lifeplayer, 170 + (i * 13), 420);

		// prueba de giro jugador 0
		playerPoints[0].setRotation( Match.getPlayer().getDirection() );
		
		// mostrar jugadores
		for(int i = 0; i < GameStates.getPositions().size() ; i++)
			playerPoints[i].draw(g);

		// municion
		g.drawPixmap(Assets.simpleAmmo, 0, 400);
		
		
		//dibujar mapa si esta cargado
		if(GameStates.mapLoaded){
			for(int i =0; i < GameStates.collList.size() ; i++){
				GameStates.collList.get(i).draw(g);
			}
			
		}
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
//		Assets.simpleAmmo.dispose();
//		Assets.playerWhite.dispose();
//		Assets.playerGreen.dispose();
//		Assets.redArrow.dispose();
//		Assets.geogrilla.dispose();
	}
}

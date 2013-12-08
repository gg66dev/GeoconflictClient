package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.gameplay.Clock;
import cl.geoconflict.gameplay.Map;
import cl.geoconflict.gameplay.Player;

import cl.geoconflict.network.Network.RequestLeaveRoom;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;


public class UnirsePartidaScreen extends Screen {

	Client client;
	GameStates gamestates;
	Pixmap timePx;

	public UnirsePartidaScreen(Game game, Client client, GameStates gamestates) {
		super(game);
		this.client = client;
		this.gamestates = gamestates;

		Assets.tenMinRed.scale(70, 70);
		Assets.fifteenMinRed.scale(70, 70);
		Assets.twentyMinRed.scale(70, 70);
		Assets.tenMinBlack.scale(70, 70);
		Assets.fifteenMinBlack.scale(70, 70);
		Assets.twentyMinBlack.scale(70, 70);
		Assets.hughLayerRed.scale(280, 70);
		Assets.hughLayerBlack.scale(280, 70);
		timePx = Assets.tenMinRed;

	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				// back
				if (inBounds(event, 10, 400, Assets.back.getWidth(),
						Assets.back.getHeight())) {
					// if(Settings.soundEnabled)
					RequestLeaveRoom rlr = new RequestLeaveRoom();
					rlr.nameRoom = gamestates.currMatch;
					rlr.namePlayer = gamestates.username;
					client.sendTCP(rlr);
				}
			}
		}

		// define icono de tiempo
		if(gamestates.timeChange){
			if (gamestates.timeMatch == 10 && gamestates.team.equals("red"))
				timePx = Assets.tenMinRed;
			if (gamestates.timeMatch == 15 && gamestates.team.equals("red"))
				timePx = Assets.fifteenMinRed;
			if (gamestates.timeMatch == 20 && gamestates.team.equals("red"))
				timePx = Assets.twentyMinRed;
			if (gamestates.timeMatch == 10 && gamestates.team.equals("black"))
				timePx = Assets.tenMinBlack;
			if (gamestates.timeMatch == 15 && gamestates.team.equals("black"))
				timePx = Assets.fifteenMinBlack;
			if (gamestates.timeMatch == 20 && gamestates.team.equals("black"))
				timePx = Assets.twentyMinBlack;
			gamestates.timeChange = false;
		}
		
		// se inicia la partida
		if (gamestates.initMatch) {
			// se crea mapa se asocia a Player y a gps
			Map map = new Map();
			gamestates.gps.addObserver(map);
			Clock clockMatch = new Clock(15); // p: tiempo partida
			Player player = new Player(20, map, gamestates.gps); // p:
																	// ammo,mapa,
																	// gps
			ArmaScreen arma = new ArmaScreen(game, client, gamestates);
			MapaScreen mapa = new MapaScreen(game, client, gamestates);
			arma.setMapaScreen(mapa, clockMatch, player);
			mapa.setArmaScreen(arma, clockMatch, player);
			this.game.setScreen(mapa);
		}

		// cierre de partida, salen a mainMenu
		if (gamestates.closeRoom) {
			gamestates.closeRoom = false;
			gamestates.roomJoined = false;
			gamestates.listReceived = false;
			gamestates.array.clear();//limpia lista de salas
			this.game.setScreen(new MenuScreen(this.game, this.client,
					this.gamestates));
		}
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.background, 0, 0);

		if (gamestates.hasTeam && gamestates.team.equals("red")) {
			g.drawPixmap(Assets.hughLayerRed, 30, 100);
			g.drawText(gamestates.username, 50, 130, Color.WHITE, 30);
			// muestra miembros del equipo
			int j = 0;
			for (int i = 0; i < gamestates.teamRed.size(); i++) { // tamaño de
																	// equipo
				// ignora su hombre
				if (!gamestates.username.equals(gamestates.teamRed.get(i))) {
					g.drawPixmap(Assets.smallLayerRed, 130, 180 + j * 50);
					g.drawText(gamestates.teamRed.get(i), 150, 210 + j * 50,
							Color.WHITE, 30);// layer con nombre jugadores del
												// equipo
					j++;
				}
			}
		}
		if (gamestates.hasTeam && gamestates.team.equals("black")) {
			g.drawPixmap(Assets.hughLayerBlack, 30, 100);
			g.drawText(gamestates.username, 50, 130, Color.WHITE, 30);
			int j = 0;
			// muestra miembros del equipo
			for (int i = 0; i < gamestates.teamBlack.size(); i++) { // tamaño de
																	// equipo
				if (!gamestates.username.equals(gamestates.teamBlack.get(i))) {
					g.drawPixmap(Assets.smallLayerBlack, 130, 180 + j * 50);
					g.drawText(gamestates.teamBlack.get(i), 150, 210 + j * 50,
							Color.WHITE, 30);// nombre de jugadore equipo
					j++;
				}
			}
		}

		// back
		g.drawPixmap(Assets.back, 10, 400);
		// tiempo
		g.drawPixmap(timePx, 190, 390);
		// texto de esperar partida
		g.drawText("Espere a que empieze ", 20, 360, Color.BLACK, 30);
		g.drawText("la partida", 20, 380, Color.BLACK, 30);
		// titulo
		g.drawText("Partida", 10, 50, Color.BLACK, 50);
		// nombre de la sala
		g.drawText("'" + gamestates.currMatch + "'", 70, 90, Color.BLACK, 30);
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
		// TODO Auto-generated method stub

	}

}

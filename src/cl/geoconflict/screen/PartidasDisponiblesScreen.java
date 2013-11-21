package cl.geoconflict.screen;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.MenuScreen;
import cl.geoconflict.gui.ButtonGUI;
import cl.geoconflict.gui.ScrollBar;
import cl.geoconflict.network.Network.RequestJoinRoom;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class PartidasDisponiblesScreen extends Screen {

	ScrollBar scrollbar;
	GameStates gamestates;
	Client client;
	boolean activeTouch;

	public PartidasDisponiblesScreen(Game game, Client client,
			GameStates gamestates) {
		super(game);
		this.client = client;
		this.gamestates = gamestates;
		scrollbar = new ScrollBar(10, 100, 300, 200);
		// esta variable bloque acciones mientras se entrega una respuesa
		/*
		 * gamestates.loading = true; scrollbar = null; activeTouch = false;
		 */

		/* como respuesta se recive entes de esta clase */
		if (this.gamestates.array.size() > 0) {
			// guarda lista de salas y luego las asigna al scrollbar
			for (int i = 0; i < this.gamestates.array.size(); i++) {
				scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
				scrollbar.GetElement(i).setName(this.gamestates.array.get(i));
			}
			// como hay opciones scrollbar activa
			activeTouch = true;
		}
		// no hay salas disponibles
		if (this.gamestates.array.size() == 0) {
			scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
			scrollbar.GetElement(0).setName("No hay salas");
		}
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		// si noy salas desactiva touch
		if (activeTouch) {
			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				if (event.type == TouchEvent.TOUCH_UP) {
					// acciona botones
					if (inBounds(event, 10, 100, 300, 200)) {
						int index = scrollbar.SelectElement(event);
						if (index > -1 && index < gamestates.array.size()) {
							gamestates.currMatch = gamestates.array.get(index);
							//envia request para unirse a sala
							RequestJoinRoom rjr = new RequestJoinRoom(); 
							rjr.nameRoom = gamestates.array.get(index);
							rjr.username = gamestates.username;
							client.sendTCP(rjr);
						}
					}
				}
				if (inBounds(event, 10, 100, 300, 200)) {
					// desplaza posicion del scrollbar
					scrollbar.update(deltaTime, event);
				}
			}
		}

		//boton back
		int len = touchEvents.size();
		int height = g.getHeight();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 10, height - Assets.back.getHeight() - 30,
						Assets.back.getWidth(), Assets.back.getHeight())) {
					gamestates.listReceived = false;
					//borra lista de salas guardadas y prepara por si se selecciona denuevo
					gamestates.array = new ArrayList<String>();
					this.game.setScreen(new MenuScreen(this.game,client,gamestates));
				}
			}
		}
		
		//si recibe mensaje que acepte entra a sala
		if(gamestates.roomJoined)
			game.setScreen(new UnirsePartidaScreen(game,client, gamestates));
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.background, 0, 0);
		g.drawText("Partidas", 10, 50, Color.BLACK, 50);
		g.drawText("Disponibles", 30, 80, Color.BLACK, 50);
		scrollbar.draw(g);
		g.drawPixmap(Assets.back, 10, 400);
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

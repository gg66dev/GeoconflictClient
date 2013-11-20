package cl.geoconflict.screen;

import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.MenuScreen;
import cl.geoconflict.gui.ButtonGUI;
import cl.geoconflict.gui.ScrollBar;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

public class PartidasDisponiblesScreen extends Screen {

	
	ScrollBar scrollbar;
	GameStates gamestates;
	Client client;
	
	public PartidasDisponiblesScreen(Game game, Client client, GameStates gamestates) {
		super(game);
		this.client = client;

		scrollbar = new ScrollBar(10, 100, 300, 200);

		// se tiene que contectar al servidor para ver partidas disponibles mas
		// cercanas
		// **como ejemplo**
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
		scrollbar.GetElement(0).setName("adm1 3/6");
		
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
		scrollbar.GetElement(1).setName("adm2 6/6");
		
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
		scrollbar.GetElement(2).setName("adm3 1/6");
		
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
		scrollbar.GetElement(3).setName("adm4 1/6");
		
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
		scrollbar.GetElement(4).setName("adm5 1/6");
		
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
		scrollbar.GetElement(5).setName("adm6 2/6");
		
		scrollbar.add(new ButtonGUI(Assets.mediumLayerRed, 255, 50));
		scrollbar.GetElement(6).setName("adm7 5/6");
		
		scrollbar.setTabulation(0);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {

				if (inBounds(event, 0, 200, 500, 200)) {
					int index = scrollbar.SelectElement(event);

					switch (index) {
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
					case 5:
						break;
					case 6:
						break;
					case 7:
						break;
					default:
					}
				}
				if (inBounds(event, 0, 200, 500, 200)) {
					// desplaza posicion del scrollbar
					scrollbar.update(deltaTime, event);
				}

			}
			if (inBounds(event, 10, 400, Assets.back.getWidth(), Assets.back.getHeight())) {
				this.game.setScreen(new MenuScreen(this.game, this.client, this.gamestates));
			}
			

		}
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.background, 0, 0);

		g.drawText("Partidas", 10, 50, Color.BLACK, 50);
		g.drawText("Disponibles", 30, 80, Color.BLACK, 50);

		
		scrollbar.draw(g);
		
		g.drawPixmap(Assets.back,10, 400);
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

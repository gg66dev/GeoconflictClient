package cl.geoconflict.screen;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import cl.geoconflict.Assets;
import cl.geoconflict.Settings;
import cl.geoconflict.gameplay.Collisionable;
import cl.geoconflict.gui.GameButton;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;

public class CrearMapaScreen extends Screen {

	GameButton drop;
	GameButton new_c;
	GameButton save;

	// Contador del tiempo de presionado del boton
	int count = 0;
	//cuando se crea un nuevo colisionable
	boolean newCollisionable = false;
	//cuando se selecciona un colisionable ya creado
	boolean selected = false;
	Collisionable c = null;
	ArrayList<Collisionable> collList;
	int currSelected;
	
	public CrearMapaScreen(Game game) {
		super(game);
		Graphics g = game.getGraphics();

		save = new GameButton(50, 64, Assets.b_save);
		new_c = new GameButton(64, 50, Assets.b_new_c);
		drop = new GameButton(64, 50, Assets.b_drop);

		save.setPosition(g.getWidth() - 50, g.getHeight() - 64);
		new_c.setPosition((g.getWidth() / 2) - 25, g.getHeight() - 64);
		drop.setPosition(50, g.getHeight() - 64);

		Assets.geogrilla.scale(g.getWidth() - 30, g.getHeight() - 30);
		collList = new ArrayList<Collisionable>();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, save.getX(), save.getY(), save.getW(),
						save.getH())) {
					save.isPressed(true);
					//guarda mapa en un temp antes de ponerle nombre
					Settings.saveMap(game.getFileIO(), ".temp", collList);
					//cambia a otra actividad y sale de GeoConflictGame
					game.getSaveMapActivity();
				}
				if (inBounds(event, new_c.getX(), new_c.getY(), new_c.getW(),
						new_c.getH())) {
					new_c.isPressed(true);
					// crea colisionable
					if (!newCollisionable) {
						c = new Collisionable(new_c.getX(), new_c.getY(), 40,
								40);
						newCollisionable = true;
					}
				}
				if (inBounds(event, drop.getX(), drop.getY(), drop.getW(),
						drop.getH())) {
					drop.isPressed(true);
				}
				// revisa la lista de coliisionables para ver si vuelve a
				// seleccionar uno
				
				int j = 0;
				for (Collisionable x : collList) {
					if (x.inBounds(event)) {
						drop.isPressed(true);
						selected = true;
						c = collList.get(j);
						currSelected = j;
						break;
					}
					j++;
				}

			}
			if (event.type == TouchEvent.TOUCH_UP) {
				// guarda en lista de collisionables
				if (newCollisionable)
					collList.add(c);
				save.isPressed(false);
				new_c.isPressed(false);
				drop.isPressed(false);
				newCollisionable = false;
				selected = false;
			
				//ve si colisiona con el drop
				if(c != null && collList.size() > 0 && isBounds(drop,c)){
					//lo quita de la lista
					collList.remove(currSelected);
				}
			}
			if (event.type == TouchEvent.TOUCH_DRAGGED) {
				if (newCollisionable || selected) {
					c.setPosition(event.x, event.y);
				}
			}
		}
		
	}

	private boolean isBounds(GameButton drop, Collisionable c) {
		int e = 10;
		if(c.x  > drop.getX() - e && c.x < drop.getX()+ drop.getW() + e 
				&& c.y  > drop.getY() -e && c.y  < drop.getY()+ drop.getH() + e ) 
			return true;
		else
			return false;
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.clear(Color.BLACK);

		// mostrar grilla
		g.drawPixmap(Assets.geogrilla, 0, 0);

		drop.draw(g);
		new_c.draw(g);
		save.draw(g);

		if (c != null && newCollisionable) {
			c.draw(g);
		}

		// dibuja colisionables de la lista
		for (Collisionable x : collList) {
			x.draw(g);
		}

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		Assets.geogrilla.dispose();
		Assets.b_drop.dispose();
		Assets.b_new_c.dispose();
		Assets.b_save.dispose();
	}

}

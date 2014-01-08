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
	GameButton h_add;
	GameButton h_subtract;
	GameButton new_c;
	GameButton save;
	GameButton w_add;
	GameButton w_substract;

	// Contador del tiempo de presionado del boton
	int count = 0;
	//cuando se crea un nuevo colisionable
	boolean newCollisionable = false;
	//cuando se selecciona un colisionable ya creado
	boolean selected = false;
	Collisionable c = null;
	ArrayList<Collisionable> collList;

	public CrearMapaScreen(Game game) {
		super(game);
		Graphics g = game.getGraphics();

		save = new GameButton(50, 64, Assets.b_save);
		h_add = new GameButton(50, 64, Assets.b_h_add);
		h_subtract = new GameButton(50, 65, Assets.b_h_subtract);
		w_add = new GameButton(50, 64, Assets.b_w_add);
		w_substract = new GameButton(50, 64, Assets.b_w_subtract);
		new_c = new GameButton(64, 50, Assets.b_new_c);
		drop = new GameButton(64, 50, Assets.b_drop);

		save.setPosition(g.getWidth() - 50, 0);
		h_add.setPosition(g.getWidth() - 50, 74);
		h_subtract.setPosition(g.getWidth() - 50, 148);
		w_add.setPosition(g.getWidth() - 50, 222);
		w_substract.setPosition(g.getWidth() - 50, 292);
		new_c.setPosition(g.getWidth() / 2, g.getHeight() - 64);
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
				if (inBounds(event, h_add.getX(), h_add.getY(), h_add.getW(),
						h_add.getH())) {
					h_add.isPressed(true);
				}
				if (inBounds(event, h_subtract.getX(), h_subtract.getY(),
						h_subtract.getW(), h_subtract.getH())) {
					h_subtract.isPressed(true);
				}
				if (inBounds(event, w_add.getX(), w_add.getY(), w_add.getW(),
						w_add.getH())) {
					w_add.isPressed(true);
				}
				if (inBounds(event, w_substract.getX(), w_substract.getY(),
						w_substract.getW(), w_substract.getH())) {
					w_substract.isPressed(true);
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
						selected = true;
						c = collList.get(j);
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
				h_add.isPressed(false);
				h_subtract.isPressed(false);
				w_add.isPressed(false);
				w_substract.isPressed(false);
				new_c.isPressed(false);
				drop.isPressed(false);
				newCollisionable = false;
				selected = false;
			}
			if (event.type == TouchEvent.TOUCH_DRAGGED) {
				if (newCollisionable || selected) {
					c.setPosition(event.x, event.y);
				}
			}
		}

	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.clear(Color.BLACK);

		// mostrar grilla
		g.drawPixmap(Assets.geogrilla, 0, 0);

		drop.draw(g);
		h_subtract.draw(g);
		new_c.draw(g);
		save.draw(g);
		w_add.draw(g);
		w_substract.draw(g);
		h_add.draw(g);

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
		Assets.b_h_add.dispose();
		Assets.b_h_subtract.dispose();
		Assets.b_new_c.dispose();
		Assets.b_save.dispose();
		Assets.b_w_add.dispose();
		Assets.b_w_subtract.dispose();
	}

}

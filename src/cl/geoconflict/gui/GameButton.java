package cl.geoconflict.gui;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

/*
 * Se encarga de dibujar los botones
 * y la animacion cuando se presionan
 * 
 * */
public class GameButton {

	int x, y, h, w;
	boolean isPressed = false;
	Pixmap img;

	public GameButton(int w, int h, Pixmap img) {
		this.w = w;
		this.h = h;
		this.img = img;
		x = 0;
		y = 0;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}
	public void isPressed(boolean b) {
		isPressed = b;
	}

	public void draw(Graphics g) {
		if (isPressed)
			g.drawPixmap(img, x, y, 0, 0, w, h);
		else
			g.drawPixmap(img, x, y, 0, h, w, h);
	}

}

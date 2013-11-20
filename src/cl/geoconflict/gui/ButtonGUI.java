package cl.geoconflict.gui;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class ButtonGUI implements ElementGUI {

	Pixmap pixmap;
	private int w;
	private int h;
	String name;
	
	boolean drawName = false;
	
	public  ButtonGUI(Pixmap pixmap,int w, int h){
		this.pixmap = pixmap;
		this.w = w;
		this.h = h;
		name = "";
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return h;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return w;
	}
	
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void draw(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		
		g.drawPixmap(pixmap,  x,  y);
	}

	@Override
	public void draw(Graphics g) {}

	@Override
	public void draw(Graphics g, int dx, int dy, int srcX , int srcY, int srcW, int srcH) {
		// TODO Auto-generated method stub
		g.drawPixmap(pixmap, dx, dy, srcX, srcY, srcW, srcH);
	
		if(drawName)
			g.drawText(name, dx, dy + h/2, Color.WHITE, 30);
	
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}
	public void setDraw(boolean b){
		drawName = b;
	}
		


	
	
}

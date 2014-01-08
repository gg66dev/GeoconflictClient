package cl.geoconflict.gameplay;

import android.graphics.Color;


import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;

public class Collisionable {
	public int x;
	public int y;
	public int w;
	public int h;
	public boolean isSelected = false;

	public Collisionable(int x, int y , int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	
	public void draw(Graphics g){
		g.drawRect(x, y, w, h, Color.RED);
	}


	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public boolean inBounds(TouchEvent event) {
		if(event.x > x && event.x < x + w - 1 && 
		           event.y > y && event.y < y + h - 1) 
		            return true;
		        else
		            return false;
	}

	public String toString() {
		return ""+x+"-"+y+"-"+w+"-"+h;
	}
	
}

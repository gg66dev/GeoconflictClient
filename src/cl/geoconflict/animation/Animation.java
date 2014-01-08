package cl.geoconflict.animation;

import cl.geoconflict.gameplay.Match;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

//animacion del arma
public class Animation {

	Pixmap pixmap;
	int frameindex;
	int width;
	int height;
	float speed;
	boolean isStop = true;
	int x;
	int y;
	int numRows;
	float lapse = 0;
		
	public Animation(Pixmap pixmap, int widthFrame, int heightFrame, int numRows){
		this.pixmap = pixmap;
		this.width = widthFrame;
		this.height = heightFrame;
		x = 10;
		y = 10;
		this.numRows = numRows;
		frameindex = numRows;
	}
	
	public void shoot(){
		Match.getPlayer().restAmmo();
		isStop = false;
	}
	
	public void update(float dt){
		if(!isStop){
			if(frameindex < 0){
				isStop = true;
				frameindex = numRows;
			}
			if(lapse > 0.09f ){
				frameindex--;
				lapse = 0;
			}
			lapse += dt;
		}
	}

	public void draw(Graphics g){
		g.drawPixmap(pixmap, x, y, width*frameindex ,0 , width , height );
	}
	
	public boolean isStop(){
		return isStop;
	}
}

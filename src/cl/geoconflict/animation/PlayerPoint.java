package cl.geoconflict.animation;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class PlayerPoint {

	Pixmap pixmap;
	int width;
	int height;
	float speed;
	int x, y;
	int numRows;
	int numCols;
	float lapse = 0;
	int row, col; //funcionan de index frame
	
	
	public PlayerPoint(Pixmap pixmap, int widthFrame, int heightFrame, int numRows, int numCols){
		this.pixmap = pixmap;
		this.width = widthFrame;
		this.height = heightFrame;
		x = 10;
		y = 10;
		this.numRows = numRows;
		this.numCols = numCols;
		row = 0;
		col = 0;
	}
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	public void update(float dt){
		
		
		if(lapse > 0.09f ){
			col++;
			lapse = 0;
			if(col > numCols){
				col = 0;
				row++;
				if(row > numRows)
					row = 0;
			}
		}
		lapse += dt;
	}
	
	
	public void draw(Graphics g){
		
		g.drawPixmap(pixmap, x, y, width*col ,height*row , width , height );
		
	}
	
}

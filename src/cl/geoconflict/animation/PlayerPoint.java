package cl.geoconflict.animation;

import cl.geoconflict.Assets;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class PlayerPoint {
	private Pixmap pixmap;
	private String id;
	private int width;
	private int height;
	private int x, y;
	private int numRows;
	private int numCols;
	private float lapse = 0;
	private int row, col; //funcionan de index frame
	public float rotate = 0.0f;
	
	public PlayerPoint(Pixmap pixmap, String id, int widthFrame, int heightFrame, int numRows, int numCols){
		this.pixmap = pixmap;
		this.id = id;
		this.width = widthFrame;
		this.height = heightFrame;
		this.x = 10;
		this.y = 10;
		this.numRows = numRows;
		this.numCols = numCols;
		this.row = 0;
		this.col = 0;
	}
	
	/**
	 * pensado solo para mostrar la orientacion en el mapa.
	 * como la pantalla en modo mapa se pone en horizontal, para
	 * que el norte quede apuntando hacia las estadisticas de la 
	 * pantalla (hacia arriba) se hace un ajuste de 180
	 * **/
	public void setRotation(float rotation){
		if(  rotation + 180   > 360 )
			this.rotate = rotation - 180  ;
		else
			this.rotate = rotation + 180;
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
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
		//orientacion es mostrada como una fecha roja
		g.drawPixmap(Assets.redArrow, x, y, rotate );
	}
	
}

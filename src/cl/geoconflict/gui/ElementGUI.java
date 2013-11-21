package cl.geoconflict.gui;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

//interfaces de la que salen los botones o laminas de texto
//pensada para agregar elementos al scrollbar
public interface ElementGUI {

	//x,y,w,h
	public int getHeight();
	public int getWidth();
	
	//funcion update
	public void update(float dt);
	public void setName(String name);
	public void setDraw(boolean b);

	
	//funcion draw
	public void draw(Graphics g,int dx,int dy); //referente a un objeto
	public void draw(Graphics g); //referencia propia
	//deibujo parcial referente a otro objeto
	//par; recX y RexY, es el origen del rectangulo de la seleccion
	public void draw(Graphics g,int dx,int dy, int srcX , int srcY, int srcW, int srcH);
	public void setPixmapLayer(Pixmap mediumLayerBlack); 
	
}

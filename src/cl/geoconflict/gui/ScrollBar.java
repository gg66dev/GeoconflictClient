package cl.geoconflict.gui;

import java.util.ArrayList;

import cl.geoconflict.Assets;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pixmap;

public class ScrollBar {

	ArrayList<ElementGUI> arrayElement = null;
	int size; //cantidad de elementos
	int length; // largo del scroll en pixeles
	int spaceBetween = 10; // espacio entre elementos
	
	int x,y,h,w;
	int dy = 0; //desplazamiento del scroll
	int lastinputY = 0;
	
	Pixmap slice;
	boolean showSlice = false;
	
	int tabspace;//espacio de tabulacion del elemento
	
	//posicion y el largo que tomara en la pantalla
	//el ancho dependera de los elementos
	public ScrollBar(int x, int y, int w , int h){
		
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
		
		this.arrayElement = new ArrayList<ElementGUI>();
		size = 0; 
		length = 0;
		
		slice = Assets.slice;
		
		tabspace = 10;
	}

	//en pixeles
	public void setSpaceBetween(int sp){
		spaceBetween = sp;
	}
	
	public void setTabulation(int ts){
		tabspace = ts;
	}
	
	public void add(ElementGUI e){
		
		length += (e.getHeight() + spaceBetween);
		arrayElement.add(e);
		size++;
	}
	
	public void remove(int i){
		length -= (arrayElement.get(i).getHeight() + spaceBetween);
		arrayElement.remove(i);
		size--;
	}
	
	public int size(){
		return size;
	}
	
	public ElementGUI GetElement(int i){
		return arrayElement.get(i);
	}
	
	public void update(float dt,TouchEvent event){
		
		if(event.type == TouchEvent.TOUCH_DRAGGED){
			
			showSlice = true;
			
			//limita borde superior
			if(dy <= 0){
				
				//limita borde inferior
				if(dy   <= -length + h )
					dy = -length + h;
				
				//movimiento scrollbar
				if(event.y - lastinputY > 0)
					dy += 4;
				if(event.y - lastinputY < 0)
					dy -= 4;
				lastinputY = event.y;
			}
			else
				dy = 0;
		}
		else
			showSlice = false;
		
	}
	
	
	public void draw(Graphics g){
		
		int x,y;
		int elementBefore = 0; //altura de los elementos anteriores a i
		
		x = this.x + tabspace;
		elementBefore = this.y;
		for(int i = 0; i < size ; i++){
			
			int h = arrayElement.get(i).getHeight();
			
			//calcula la posicion donde los elementos van dibujados
			y = elementBefore + spaceBetween + dy;
	
			
			if(y  < this.y){//disminute imagen limite superior
				arrayElement.get(i).draw(g,x,this.y ,0, this.y - y , arrayElement.get(i).getWidth(),h);
			}
			else{
				if(y  > this.y + this.h){//disminuye imagen limite inferior
					arrayElement.get(i).draw(g,x, this.y + this.h ,0,  (this.y + this.h) - y, arrayElement.get(i).getWidth(), h ); 
					arrayElement.get(i).setDraw(false);
				}
				else{ // imagen completa
					arrayElement.get(i).draw(g,x,y ,0, 0, arrayElement.get(i).getWidth(),h);
					arrayElement.get(i).setDraw(true);
				}
			}
			
			elementBefore += h + spaceBetween;
		}
		
		
		
		//slice
		if(showSlice)
			g.drawPixmap(slice, w - 20 , (int)(this.y - dy*0.4));
		
	}

	
	public int SelectElement(TouchEvent event){
		
		int y;
		int touchY = event.y;
		int touchX = event.x;
		
		//input fuera del marco del scroll
		if( this.y > touchY || touchY > this.y + this.h)
			return -1;
		
		
		int elementBefore = this.y;
		for(int i = 0; i < size ; i++){
			
			int h = arrayElement.get(i).getHeight();
			int w = arrayElement.get(i).getWidth();
			
			//calcula la posicion donde los elementos van dibujados
			y = elementBefore  + spaceBetween + dy;
	
			if( (y <= touchY) && (touchY <= y + h ) &&
					(x <= touchX) && (touchX <= x + w ))
				return (i);
			
			
			elementBefore += h + spaceBetween;
		}
		
		return 0;
	}
	
}

package cl.geoconflict.gui;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint.Style;
import android.view.KeyEvent;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Input.TouchEvent;

public class TextBox {
	protected Game game;
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	
	protected String text;
	final protected int maxLength = 10;
	
	protected boolean selected;
	
	public TextBox(Game game, int x, int y, int w, int h){
		this.game = game;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = "";
		this.selected = false;
	}

	public int getX(){			return	x;	}
	public int getY(){			return	y;	}
	public int getWidth(){		return	w;	}
	public int getHeight(){		return	h;	}
	public String getText(){	return text;}
	public boolean getSelected(){ return this.selected;}
	
	protected void setText(List<Input.KeyEvent> keyList){
		if(keyList.size() != 0){
			Input.KeyEvent event = keyList.get(0);
			
			if( text.length() < maxLength)
				
	        	//solo acepta letas y numeros
	        	if((event.keyCode >= KeyEvent.KEYCODE_0 && event.keyCode <= KeyEvent.KEYCODE_9) 
	        			|| (event.keyCode >= KeyEvent.KEYCODE_A && event.keyCode <= KeyEvent.KEYCODE_Z) 
	        			|| event.keyCode == KeyEvent.KEYCODE_AT){
	        		if ( event.type == Input.KeyEvent.KEY_DOWN ){
	        			text = text + event.keyChar;
	        		}
	        	}
			
			if(text.length() > 0 
					&& event.type == Input.KeyEvent.KEY_DOWN 
					&& event.keyCode ==  KeyEvent.KEYCODE_DEL)
				text = text.substring(0, text.length() -1);
				
		}
		
	}
	
	public void draw(){
		Graphics g = this.game.getGraphics();
		
		//draw el user box
		if( this.selected ){
			g.drawRect(x, y, w, h, Color.argb( 100, 255,  255,  255), Style.FILL);
		}else{
			g.drawRect(x, y, w, h, Color.argb( 100,  150,  150, 150), Style.FILL);
		}
		g.drawRect(x, y, w, h, Color.rgb(0,  0,  0), Style.STROKE);
				
		//dibuja texto
		// TODO posicion talvez varie con la pantalla --revisar luego
		
		g.drawText(text,x + 2, y + 30, Color.rgb(1,  1,  1), 30);
	}
	
	public void update( List<TouchEvent> touchEvents ) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	//preciona el userBox
            	
            	if(inBounds(event, this.x, this.y, this.getWidth(), this.getHeight())) {
            		if( this.selected == false ){
            			this.selected = true;
            		}
	            }else{
	            	if( this.selected == true ){
	            		this.selected = false;
	            	}
	            }
            }
        }
        
        //obtener lista solo cuando softkeyboard esta activado
        if( this.selected ){
        	List<Input.KeyEvent> keyList = game.getInput().getKeyEvents();
        	this.setText(keyList);
        	
        	//si se preciona back o enter se deselecciona los box
            if(keyList.size() != 0){
            	if(keyList.get(0).keyCode == 4 || keyList.get(0).keyCode == 66){
            		this.selected = false;
            	}
            }
        }
	}
	
	protected boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }
}

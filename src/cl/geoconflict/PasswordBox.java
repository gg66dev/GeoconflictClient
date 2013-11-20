package cl.geoconflict;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint.Style;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;

public class PasswordBox extends TextBox {

	int lapse = 10;
	
	public PasswordBox(Game game, int x, int y, int w, int h) {
		super(game, x, y, w, h);
	}

	@Override
	protected void setText(List<Input.KeyEvent> keyList){
		super.setText(keyList);
		
		if(keyList.size() == 0)
			lapse--;
		else
			lapse = 10;
		
	}
	
	//dibuja la cadena de caracteres como asteriscos, el ultimo caracter como normal y luego lo vuelve asterisco
	@Override
	public void draw(){
		String text = "";
		int len = getText().length();		
		for(int i = 0; i < len;i++){
			//ultimo caracter se muestra (solo un lapso de tiempo)
			if(lapse > 0 && i == len - 1 && this.selected)
				text = text + getText().charAt(len - 1);
			else
				text = text + "*";
		}
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
		g.drawText(text,this.x + 2, this.y + 30, Color.rgb(1,  1,  1), 30);
	}
}

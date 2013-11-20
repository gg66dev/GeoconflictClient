package cl.geoconflict.screen;

import cl.geoconflict.Assets;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;

public class ResultadosScreen extends Screen {

	public ResultadosScreen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        //g.drawPixmap(Assets.logo, 32, 28);
        
        
        //botones de prueba
        //g.drawPixmap(Assets.b_resultadosScreen, 20, 280);
        
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

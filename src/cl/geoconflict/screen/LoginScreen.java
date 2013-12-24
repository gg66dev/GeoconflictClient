/**
 * 
 */
package cl.geoconflict.screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.util.Log;

import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.Settings;
import cl.geoconflict.gui.PasswordBox;
import cl.geoconflict.gui.TextBox;
import cl.geoconflict.network.Network;
import cl.geoconflict.network.NetworkListener;
import cl.geoconflict.network.Network.RequestLogin;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

/**
 * @author Fernando Valencia F.
 *
 */
public class LoginScreen extends Screen {
	
	List<Input.KeyEvent> keyList;
	TextBox userBox;
	PasswordBox passBox;
	Client client;
	NetworkListener nl;
	
	/*clase que guarda variables intermediarias entre
	las screen y el networklistener*/ 
	GameStates gamestates;
	
	public LoginScreen(Game game) {
		super(game);
		
		int width = this.game.getGraphics().getWidth();
		userBox = new TextBox(this.game, 15, 50, width-30, 40);
		passBox = new PasswordBox(this.game, 15, 165, width-30, 40);
		
		//crea cliente y hace coneccion
		client = new Client();
		gamestates = new GameStates();
		//crea gps - gps se activa con el framework-input
		//gamestates.gps = new PositionGPS(game);
    	
		Network.register(client);
		this.nl = new NetworkListener();
		this.nl.init(gamestates,client);//parametro juego
		client.addListener(nl);
		client.start();
		try {
			client.connect(30000,"**.**.**.**",Network.portTCP, Network.portUDP);
			//*cambiar timeout a 5000
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> keyList = this.game.getInput().getTouchEvents();
		List<TouchEvent> copy = new ArrayList<TouchEvent>();
		Graphics g = this.game.getGraphics();
		for( TouchEvent e : keyList){
			copy.add(e);
			
			if(e.type == TouchEvent.TOUCH_UP) {
            	int height = g.getHeight();
            	int width = g.getWidth();
                if(inBounds(e, 10, height-Assets.back.getHeight()-30, 
                		Assets.back.getWidth(), Assets.back.getHeight())) {
                	this.game.setScreen( new MainMenuScreen(this.game) );
                }else if (inBounds(e, width-Assets.login.getWidth()-10, height-Assets.login.getHeight()-30,
                		Assets.login.getWidth(), Assets.login.getHeight())){
                	
                	//se apreta boton login
                	if( this.userBox.getText() != null
                			&& this.userBox.getText().replaceAll(" ", "").length() != 0
                			&& this.passBox.getText() != null
                			&& this.passBox.getText().replaceAll(" ", "").length() != 0 ){
                		
                		if( this.client.isConnected()){
                    		Log.d("depuracion", "login");
                    		gamestates.username = this.userBox.getText();
                    		gamestates.passwd = this.passBox.getText(); 
                    		
                    		RequestLogin rl =  new RequestLogin();
                    		rl.loginInfo = gamestates.getJSONLogin(game.getFileIO());
                    		
                    		gamestates.username = this.userBox.getText();
                    		client.sendTCP(rl);
                    	}
                	}
                }
            }
		}
		
		
		/*debido a que el servidor puede demorar en responder la confirmacion
		*va fuera del bloque del touchevent
		*/
		//si se logueo sigue con la siguiente ventana
    	if(this.gamestates.logged){
    		this.game.setScreen( new ConnectionScreen(this.game, this.client,this.gamestates) );
    	}
    	//si el servidor manda mensaje de error se desconecta
    	if(this.gamestates.error){
    		if( this.client.isConnected() )
    			this.client.close();
    	}
		
		
		//actualiza GUI
		this.userBox.update( keyList );
		this.passBox.update( copy );
		
		if( this.userBox.getSelected() || this.passBox.getSelected() ){
			this.game.getInput().showkeyboard();
		}else{
			this.game.getInput().hidekeyboard();
		}
	}
	
	@Override
	public void present(float deltaTime) {
		Graphics g = this.game.getGraphics();
		
		int width = g.getWidth();
		int height = g.getHeight();
		
		//dibujar editext en el canvas
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.back, 10, height-Assets.back.getHeight()-30);
		g.drawPixmap(Assets.login, width-Assets.login.getWidth()-10, height-Assets.login.getHeight()-30);
		
		g.drawText("Login", 25, 40, Color.rgb(255,  0,  0), 30);
		g.drawText("Password", 25, 155, Color.rgb(255,  0,  0), 30);
		userBox.draw();
		passBox.draw();
	}

	@Override
	public void pause() {
		Settings.save(game.getFileIO());

	}

	@Override
	public void resume() {
		Settings.load(game.getFileIO());

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}

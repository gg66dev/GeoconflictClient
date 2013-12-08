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
import cl.geoconflict.network.Network.RequestRegisterUser;
import cl.geoconflict.network.RegisterListener;

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
public class RegisterScreen extends Screen {
	
	List<Input.KeyEvent> keyList;
	TextBox userBox;
	TextBox mailBox;
	PasswordBox passBox;
	PasswordBox repBox;
	
	Client client;
	GameStates gamestates;
	
	
	public RegisterScreen(Game game) {
		super(game);
		int width = this.game.getGraphics().getWidth();
		userBox = new TextBox(this.game, 15, 50, width-30, 40);
		passBox = new PasswordBox(this.game, 15, 135, width-30, 40);
		repBox = new PasswordBox(this.game, 15, 220, width-30, 40);
		mailBox = new TextBox(this.game, 15, 305, width-30, 40);
	
		//crea cliente y se conecta
		client = new Client();
		gamestates = new GameStates();
		Network.register(client);
		RegisterListener rl = new RegisterListener();
		rl.init(client, gamestates);
		client.addListener(rl);
		client.start();
		try {
			//*cambiar timeout a 5000
			client.connect(30000, "**.**.**.**",Network.portTCP,Network.portUDP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> keyList = this.game.getInput().getTouchEvents();
		List<TouchEvent> copy1 = new ArrayList<TouchEvent>();
		List<TouchEvent> copy2 = new ArrayList<TouchEvent>();
		List<TouchEvent> copy3 = new ArrayList<TouchEvent>();
		Graphics g = this.game.getGraphics();
		for( TouchEvent e : keyList){
			copy1.add(e);
			copy2.add(e);
			copy3.add(e);
			
			if(e.type == TouchEvent.TOUCH_UP) {
            	int height = g.getHeight();
            	int width = g.getWidth();
                if(inBounds(e, 10, height-Assets.back.getHeight()-30, 
                		Assets.back.getWidth(), Assets.back.getHeight())) {
                	this.game.setScreen( new MainMenuScreen(this.game) );
                }else if (inBounds(e, width-Assets.register.getWidth()-10, height-Assets.register.getHeight()-30,
                		Assets.register.getWidth(), Assets.register.getHeight())){
                	if( this.userBox.getText() != null
            				&& this.passBox.getText() != null
            				&& this.repBox.getText() != null
            				&& this.mailBox.getText() != null
            				&& this.userBox.getText().replaceAll(" ", "").length() != 0
            				&& this.passBox.getText().replaceAll(" ", "").length() != 0
            				&& this.repBox.getText().replaceAll(" ", "").length() != 0
            				&& this.mailBox.getText().replaceAll(" ", "").length() != 0
            				&& this.passBox.getText().equals(this.repBox.getText())){
                		if( this.client.isConnected()){
                    		Log.d("depuracion", "register");
                    		//ingresa variables a gamestates
                    		gamestates.passwd = this.passBox.getText(); 
                    		gamestates.username = this.repBox.getText(); 
                    		gamestates.mail = this.mailBox.getText(); 
                    		
                    		RequestRegisterUser rru = new RequestRegisterUser();
                    		rru.regInfo = gamestates.getJSONRegister(game.getFileIO());
                    		client.sendTCP(rru);
                    	}
                	}
                }
			}
		}
		
		
		
		/*
		 * Como la respuesta puede demorar la verificacion se hace
		 * fuera del touchevent
		 * */
    	if( gamestates.registered){
    		if( this.client.isConnected() )
        		this.client.close();
    		this.game.setScreen( new MainMenuScreen(this.game) );
    	}
    	
    	//mostrar mensajes y mantenerse en la vista con los campos vacios
    	//...
    	if( gamestates.error)
    		this.client.close();
			
		
		
		this.userBox.update( keyList );
		this.mailBox.update( copy1 );
		this.passBox.update( copy2 );
		this.repBox.update( copy3 );
		
		if( this.userBox.getSelected() || this.passBox.getSelected() 
				|| this.mailBox.getSelected() || this.repBox.getSelected() ){
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
		g.drawPixmap(Assets.register, width-Assets.register.getWidth()-10, 
				height-Assets.register.getHeight()-30);
		
		g.drawText("Username", 25, 40, Color.rgb(255,  0,  0), 30);
		g.drawText("Password", 25, 125, Color.rgb(255,  0,  0), 30);
		g.drawText("Repeat password", 25, 210, Color.rgb(255,  0,  0), 30);
		g.drawText("Email", 25, 295, Color.rgb(255,  0,  0), 30);
		userBox.draw();
		mailBox.draw();
		passBox.draw();
		repBox.draw();
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

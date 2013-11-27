/**
 * 
 */
package cl.geoconflict.screen;

import java.util.List;

import org.apache.sling.commons.json.JSONObject;


import cl.geoconflict.Assets;
import cl.geoconflict.GameStates;
import cl.geoconflict.Settings;
import cl.geoconflict.network.Network.RequestCreateRoom;
import cl.geoconflict.network.Network.RequestListRoom;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.esotericsoftware.kryonet.Client;

/**
 * @author Fernando Valencia F.
 *
 */
public class MenuScreen extends Screen{

	Client client;
	GameStates gameStates;

	
	public MenuScreen(Game game, Client client, GameStates gameStates) {
		super(game);
		this.client = client;
		this.gameStates = gameStates;
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
//                    if(Settings.soundEnabled);
                    // TODO
                }
                if(inBounds(event, 100, 180, 128, 64) ) {
//                    if(Settings.soundEnabled)
                	RequestCreateRoom rcr =  new RequestCreateRoom();
                	rcr.userNameRoom = this.gameStates.username;
                	this.client.sendTCP(rcr);
                }
                if(inBounds(event, 100, 260, 128, 64) ) {
//                    if(Settings.soundEnabled)
                	RequestListRoom rcr =  new RequestListRoom();
                	rcr.list = new JSONObject();
                	this.client.sendTCP(rcr);
                }
                if(inBounds(event, 100, 340, 128, 64) ) {
                	// TODO crear mapa
//                    if(Settings.soundEnabled)
                }
            }
        }
        
        if(this.gameStates.roomAcepted){
        	this.game.setScreen(new CrearPartidaScreen(this.game, this.client, this.gameStates));
        }
        if(this.gameStates.listReceived){
        	this.game.setScreen(new PartidasDisponiblesScreen(this.game, this.client, this.gameStates));
        }
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.design, 32, 28);
        g.drawPixmap(Assets.create_match, 100, 180);
        g.drawPixmap(Assets.join_match, 100, 260);
        g.drawPixmap(Assets.create_map, 100, 340);
        if(Settings.soundEnabled){
            g.drawPixmap(Assets.sound, 0, g.getHeight()-64, 0, 0, 64, 64);
        } else {
            g.drawPixmap(Assets.sound, 0, g.getHeight()-64, 64, 0, 64, 64);
        }
		
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
		
	}

}

package cl.geoconflict.network;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.esotericsoftware.kryonet.Client;

import cl.geoconflict.network.Network.*;


public class Game  {

	/*
	 * Una de las fases del game loop sera informar al servidor sobre el estado 
	 * del jugador y pedirle al servidor la informacion de la partida(que incluira 
	 * la de los demas jugadores)
	 * */
	class ClientRunnable implements Runnable{

		Game player;
		int i  = 30; //lo empleo para detener la partida (forma temporal) 
		
		
		void setPlayer(Game player){
			this.player = player;
		}
		
		public void run() {
			
			while(true){
				//envia informacion de usuario
				//informacion es enviada en un objeto json
				RequestPlayerInfo rui = new RequestPlayerInfo();
				rui.playerInfo = getJSonPlayer();
				rui.nameRoom = player.currRoom; //indica a la sala que actualizara
				client.sendUDP(rui);
			
				//envia peticion para recibir informacion de la partida
				//(la informacion la recive en el listener como un objeto json)
				RequestMatchInfo rmu = new RequestMatchInfo();
				rmu.nameRoom = player.currRoom; //indica la sala de la que quiere la info
				client.sendUDP(rmu);
				
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				i--;
				if(i<0) break;
			}
		}
		
	}
	
	//datos del jugador
	private Thread game;
	private Client client;
	private float positionX;
	private float positionY;
	private int hp;
	private String namePlayer;
	private String currRoom; //nombre del actual match
	private String team;
	
	
	public Game(Client client){
		ClientRunnable clientrunnable = new ClientRunnable();
		clientrunnable.setPlayer(this);
		game = new Thread(clientrunnable);
		positionX  = 10;
		positionY  = 10;
		hp = 500;
		team = "uno";
		currRoom = "none";
		this.client = client;
	}
	
	public void init(){
		game.start();
	}

	public void stop(){
		//game.stop();
	}
	
	public Client getClient(){
		return client;
	}
	public void setUserName(String name){
		namePlayer = name;
	}
	public String getUserName(){
		return namePlayer;
	}
	public void setRoom(String room){
		System.out.println("se asigno la room: " + room);
		currRoom = room;
	}
	public String getRoom(){
		return currRoom;
	}
	
	
	//guarda informacion del jugador en un objeto JSon
	private JSONObject getJSonPlayer(){
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("team", team);
			obj.put("positionX", positionX);
			obj.put("positionY", positionY);
			obj.put("namePlayer", namePlayer);
			obj.put("hp", hp);
			obj.put("currRoom", currRoom);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return obj;
	}

	
	/*
	 * En este metodo se actualiza jugadores en el mapa
	 * tiempo de la partida...etc 
	 * 
	 * */
	public void setMatchInfo(JSONObject matchInfo) {
		System.out.println(matchInfo.toString());
		
	}
	
}

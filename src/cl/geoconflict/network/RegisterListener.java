package cl.geoconflict.network;

import cl.geoconflict.GameStates;
import cl.geoconflict.network.Network.RequestAnswer;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class RegisterListener extends Listener {

	Client client;
	GameStates gamestates;
	
	
	public void init(Client client,GameStates gamestates){
		this.client = client;
		this.gamestates = gamestates;
	}
	
	
	public void connected(Connection c){
		//esta conectado
	}
	
	public void disconnected(Connection c){
		//esta desconectado
	}
	
	
	public void received(Connection c, Object o){
		
		//reques de respuesta a registro
		if(o instanceof RequestAnswer){
			RequestAnswer answer = ((RequestAnswer)o);
			
			if(answer.id == RequestAnswer.answer_id.REGISTER && answer.accepted){
				gamestates.registered = true;
			}
			
			
			if(answer.id == RequestAnswer.answer_id.REGISTER && !answer.accepted){
				gamestates.error = true;
			}
		}
		
	}
		
	
	
}

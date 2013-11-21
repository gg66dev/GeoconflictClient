package cl.geoconflict.network;

import java.util.Scanner;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import cl.geoconflict.GameStates;
import cl.geoconflict.network.Network.*;

public class NetworkListener extends Listener {
	private Client client; // se emplea este cliente para enviar msg
	GameStates gamestates;
	private Scanner scanner;

	public void init(GameStates gamestates, Client client) {
		this.client = client;
		this.gamestates = gamestates;
		this.scanner = new Scanner(System.in);
		client.setKeepAliveTCP(8000);
	}

	public void connected(Connection arg0) {
		Log.info("[Client] you have connected.");
	}

	public void disconnected(Connection arg0) {
		Log.info("[Client] you have disconnected.");
	}

	public void received(Connection c, Object o) {
		System.out.println("recivio request");

		if (o instanceof RequestAnswer) {
			Log.info("Request Answer!!");
			RequestAnswer answer = ((RequestAnswer) o);

			// pudo loguear
			if (answer.id == RequestAnswer.answer_id.LOGIN && answer.accepted) {
				Log.info("se a podigo Logear");
				gamestates.logged = true;

			}

			// no se pudo loguear
			if (answer.id == RequestAnswer.answer_id.LOGIN && !answer.accepted) {
				Log.info("no se a podido logear");
				gamestates.error = true;
			}

			// se creo sala
			if (answer.id == RequestAnswer.answer_id.CREATEROOM
					&& answer.accepted) {
				Log.info("A creado una sala!!");
				this.gamestates.roomAcepted = true;
			}

			// no se creo sala
			if (answer.id == RequestAnswer.answer_id.CREATEROOM
					&& !answer.accepted) {
				// caso que no se pueda crear sala
				System.out
						.println("no se puede crear sala - cerrando coneccion");
				c.close();
			}

			// pudo unirse a una sala
			if (answer.id == RequestAnswer.answer_id.JOINROOM
					&& answer.accepted) {
				System.out.println("Se unido a la sala");
				gamestates.roomJoined = true;
			}

			// no se unio a una sala
			if (answer.id == RequestAnswer.answer_id.JOINROOM
					&& !answer.accepted) {
				System.out
						.println("no se a podido unir a la sala,seleccione otra:");
				/* game.setRoom("none"); // borra campo asignado en la peticion */
				RequestListRoom rlr = new RequestListRoom();
				client.sendTCP(rlr);

			}

			// inicio partida
			if (answer.id == RequestAnswer.answer_id.MATCHINIT
					&& answer.accepted) {
				System.out.println("se inicio partida");
				/* game.init(); */
			}

			// no pudo inicio partida
			if (answer.id == RequestAnswer.answer_id.MATCHINIT
					&& !answer.accepted) {
				System.out.println("no se a podido iniciar partida");
			}

		}

		// lista salas disponibles
		if (o instanceof RequestListRoom) {
			RequestListRoom rlr = new RequestListRoom();
			Log.info("Lista de salas!!");
			rlr = (RequestListRoom) o;
			//guarda lista de salas en gamestastes
			this.gamestates.listReceived = true;
			this.gamestates.setListRoom(rlr.list);
		}

		// listas usuarios de sala
		if (o instanceof RequestListUser) {
			Log.info("Lista de usuarios!!");
			RequestListUser rlu = (RequestListUser) o;
			gamestates.newPlayerList = true;
			this.gamestates.setListUsers(rlu.list);

		}

		// recive informacion de la sala
		// listas usuarios de sala
		if (o instanceof RequestMatchInfo) {
			RequestMatchInfo rmi = (RequestMatchInfo) o;
			/* game.setMatchInfo(rmi.matchInfo); */

		}
	}

}

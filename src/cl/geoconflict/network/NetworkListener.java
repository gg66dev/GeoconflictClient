package cl.geoconflict.network;

import org.apache.sling.commons.json.JSONException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import cl.geoconflict.GameStates;
import cl.geoconflict.gameplay.Match;
import cl.geoconflict.network.Network.*;

public class NetworkListener extends Listener {
	private Client client; // se emplea este cliente para enviar msg

	public void init(Client client) {
		this.client = client;
		client.setKeepAliveTCP(8000);
	}

	public void connected(Connection arg0) {
		Log.info("[Client] you have connected.");
	}

	public void disconnected(Connection arg0) {
		Log.info("[Client] you have disconnected.");
	}

	public void received(Connection c, Object o) {
		Log.debug("recivio request");

		if (o instanceof RequestAnswer) {
			Log.info("Request Answer!!");
			RequestAnswer answer = ((RequestAnswer) o);
			// pudo loguear
			if (answer.id == RequestAnswer.answer_id.LOGIN && answer.accepted) {
				Log.info("se a podigo Logear");
				GameStates.logged = true;

			} else if (answer.id == RequestAnswer.answer_id.LOGIN
					&& !answer.accepted) {
				// no se pudo loguear
				Log.info("no se a podido logear");
				GameStates.error = true;
			} else if (answer.id == RequestAnswer.answer_id.REGISTER
					&& answer.accepted) {
				GameStates.registered = true;
				Log.info("registro con exito");
			} else if (answer.id == RequestAnswer.answer_id.REGISTER
					&& !answer.accepted) {
				Log.info("Error en el registro");
				GameStates.error = true;
			}else if (answer.id == RequestAnswer.answer_id.CREATEROOM
					&& answer.accepted) {
				// se creo sala
				Log.info("A creado una sala!!");
				GameStates.roomAcepted = true;
			} else if (answer.id == RequestAnswer.answer_id.CREATEROOM
					&& !answer.accepted) {
				// no se creo sala
				System.out
						.println("no se puede crear sala - cerrando coneccion");
				c.close();
			} else if (answer.id == RequestAnswer.answer_id.JOINROOM
					&& answer.accepted) {
				// pudo unirse a una sala
				System.out.println("Se unido a la sala");
				GameStates.roomJoined = true;
			} else if (answer.id == RequestAnswer.answer_id.JOINROOM
					&& !answer.accepted) {
				// no se unio a una sala
				System.out
						.println("no se a podido unir a la sala,seleccione otra:");
				/* game.setRoom("none"); // borra campo asignado en la peticion */
				RequestListRoom rlr = new RequestListRoom();
				client.sendTCP(rlr);

			} else if (answer.id == RequestAnswer.answer_id.MATCHINIT
					&& answer.accepted) {
				// inicio partida
				Log.info("Se inicia la partida");
				Log.debug("Answer positions: " + answer.positions);
				GameStates.initMatch(Integer.valueOf(answer.numPlayers),
						answer.positions);
			} else if (answer.id == RequestAnswer.answer_id.MATCHINIT
					&& !answer.accepted) {
				// no pudo inicio partida
				Log.info("No se inicia la partida");
			}else if (answer.id == RequestAnswer.answer_id.CLOSEROOM
					&& answer.accepted) {
				GameStates.closeRoom = true;
				GameStates.error = false;
				Log.info("se cerro la sala");
			}else if (answer.id == RequestAnswer.answer_id.CLOSEROOM
					&& !answer.accepted) {
				GameStates.closeRoom = false;
				GameStates.error = true;
				Log.info("no se cerro la sala");
			}
		} else if (o instanceof RequestListRoom) {
			// lista salas disponibles
			RequestListRoom rlr = new RequestListRoom();
			Log.info("Lista de salas!!");
			rlr = (RequestListRoom) o;
			// guarda lista de salas en gamestastes
			GameStates.listReceived = true;
			GameStates.setListRoom(rlr.list);
		} else if (o instanceof RequestRoomUpdate) {
			// peticion de informacion usuario, para actualizar room
			RequestRoomUpdate rru = (RequestRoomUpdate) o;
			Log.info("Se recivio update room!!");
			GameStates.newUpdateRoom = true;
			GameStates.setRoomUpdate(rru.roomInfo);
		} else if (o instanceof RequestCloseRoom) {
			// funciona tando al administrador que cierra sala
			// como jugador que abandona sala
			GameStates.closeRoom = true;
			GameStates.roomAcepted = false;
		} else if (o instanceof RequestChangeTime) {
			// envia cambio de tiempo para actualizar servidor
			// y avisa a los demas jugadores
			Log.info("realizo cambio de tiempo");
			RequestChangeTime rct = (RequestChangeTime) o;
			Log.info("nuevo tiempo es " + rct.timeMatch);
			GameStates.timeMatch = Integer.parseInt(rct.timeMatch);
			GameStates.timeChange = true;
		} else if (o instanceof RequestNewCoord) {
			// se actualiza la posicion de los jugadores
			RequestNewCoord rnc = (RequestNewCoord) o;
			try {
				GameStates.getPositions().put(
						rnc.username,
						new Double[] { (Double) rnc.newCoordInfo.get("x"),
								(Double) rnc.newCoordInfo.get("y") });
				Log.debug("Nueva posicion: " + rnc.username + " ("
						+ rnc.newCoordInfo.get("x") + ", "
						+ rnc.newCoordInfo.get("y") + " )");
				GameStates.newPosition = true;
			} catch (JSONException e) {
				Log.debug(e.toString());
			}
		}else if ( o instanceof RequestResult ){
			GameStates.EndGame();
			// TODO Falsa implementar quien gana
		}else if ( o instanceof RequestGotScore ){
			Log.debug("REQUEST SCORE");
			RequestGotScore rgs = (RequestGotScore ) o;
			int score = (int ) rgs.score;
			Match.getPlayer().addScore(score);
		}else if ( o instanceof RequestDamage){
			RequestDamage rd = (RequestDamage ) o;
			Match.getPlayer().damage();
			if( rd.id == RequestDamage.damage_id.DEAD ){
				Match.getPlayer().setAlive(false);
			}
			Log.debug( Integer.toString( Match.getPlayer().getHealth() ) );
			Log.debug( Match.getPlayer().getAmmo() );
		}else if( o instanceof RequestLoadMap){
			RequestLoadMap rlm = (RequestLoadMap ) o;
			GameStates.mapLoaded = true;
			GameStates.loadMapFromRequest(rlm.map);
			
		}
	}
}

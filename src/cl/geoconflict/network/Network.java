package cl.geoconflict.network;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	public static String SERVER_IP = "186.37.104.48";
	public static int PORT_TCP = 54666;
	public static int PORT_UDP = 54888;
	
    // This registers objects that are going to be sent over the network.
	public static void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
    
        //requiere registrar clase externa
        kryo.register(java.util.LinkedHashMap.class);
        kryo.register(org.apache.sling.commons.json.JSONObject.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(org.apache.sling.commons.json.JSONArray.class);
           
        kryo.register(RequestLogin.class);
        kryo.register(RequestRegisterUser.class);
        kryo.register(RequestAnswer.class);
        kryo.register(RequestAnswer.answer_id.class);
        kryo.register(RequestCreateRoom.class);
        kryo.register(RequestCloseRoom.class);
        kryo.register(RequestJoinRoom.class);
        kryo.register(RequestListRoom.class);
        kryo.register(RequestListPlayer.class);
        kryo.register(RequestMatchInit.class);
        kryo.register(RequestMatchInfo.class);
        kryo.register(RequestPlayerInfo.class);
        kryo.register(RequestShoot.class);
        kryo.register(RequestNewCoord.class);
        kryo.register(RequestRoomUpdate.class);
        kryo.register(RequestLeaveRoom.class);
        kryo.register(RequestChangeTime.class);
    }
	
	/**
	 * Request de login
	 */
    public static class RequestLogin{
    	public JSONObject loginInfo;
    }
    
    /**
     * Request de registro
     */
    public static class RequestRegisterUser{
    	public JSONObject regInfo;
    }
    
    /**
     * Request de respuestas
     */
    public static  class RequestAnswer{
    	public boolean accepted = false;
    	public String msg;
    	//id para no crear una respuesta por cada request
    	public answer_id id;
    	public String numPlayers;
    	public JSONArray positions;
    	public enum answer_id {
    	      LOGIN,REGISTER,CREATEROOM,CLOSEROOM,JOINROOM,MATCHINIT
    	}
    }
    
    /**
     * Request de crear sala, al ser creada la sala tiene por nombre el username del usuario
     */
    public static  class RequestCreateRoom{
    	public String userNameRoom;
    }
    /**
     * Request de cerrar sala
     */
    public static  class RequestCloseRoom{
    	public String userNameRoom;
    }
    /**
     * Request de entrar a la sala
     */
    public static class RequestJoinRoom{
    	public String nameRoom;
    	public String username;
    }
    /**
     * Request de listar salas
     */
    public static class RequestListRoom{
    	public JSONObject list;
    }
    /**
     * Request de listar usuario
     */
    public static class RequestListPlayer{
    	public JSONObject list;
    }
    
    /**
     * Request de iniciar match
     */
    public static class RequestMatchInit{
    	public String nameRoom;
    	public JSONObject origin;
    }
    /**
     * Request de informaci&oacute;n del match
     */
     public static class RequestMatchInfo{
    	 public JSONObject matchInfo;
    	 public String nameRoom;
     } 
     /**
      * Request de informacion del usuario 
      */
     public static class RequestPlayerInfo{
    	 public JSONObject playerInfo;
    	 public String nameRoom;
     }
     /**
      * Request de disparo, directionInfo debe indicar el "angulo" 
      * respecto al eje X (en portrait mode) y el "sentido"
      */
     public static class RequestShoot{
    	 public JSONObject shootInfo;
    	 public String nameRoom;
     }
     //actualiza servidor con nueva coordanada cada vez que llega
     public static class RequestNewCoord{
    	 public JSONObject newCoordInfo;
    	 public String username;
     }
     //actualiza informacion de la sala cuando se esta gestionano la partida
     public static class RequestRoomUpdate{
    	 public JSONObject roomInfo;
    	 public String nameRoom;
     }
     public static class RequestLeaveRoom{
    	 public String nameRoom;
    	 public String namePlayer;
     }
     public static class RequestChangeTime{
    	 public String timeMatch;
    	 public String nameRoom;
     }
}
package cl.geoconflict.network;

import org.apache.sling.commons.json.JSONObject;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	public static final int portTCP = 54666;
	public static final int portUDP = 54888;

    // This registers objects that are going to be sent over the network.
	public static  void register (EndPoint endPoint) {
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
            kryo.register(RequestListUser.class);
            kryo.register(RequestMatchInit.class);
            kryo.register(RequestMatchInfo.class);
            kryo.register(RequestUserInfo.class);
            
    }

    public static class RequestLogin{
    	public JSONObject loginInfo;
    }
    
    public static class RequestRegisterUser{
    	public JSONObject regInfo;
    }
    
    public static  class RequestAnswer{
    	public boolean accepted = false;
    	public String msg;
    	//id para no crear una respuesta por cada request
    	public answer_id id;
    	public enum answer_id {
    		LOGIN,REGISTER,CREATEROOM,CLOSEROOM,JOINROOM,MATCHINIT
    	}
    }
    
    //crear room request
    public static  class RequestCreateRoom{
    	public String userNameRoom;
    }
    
    /**
     * Request de cerrar sala
     */
    public static  class RequestCloseRoom{
    	public String userNameRoom;
    }
    
     public static class RequestJoinRoom{
    	public String nameRoom;
    	public String username;
    }
     
     public static class RequestListRoom{
    	 public JSONObject list;
     }
     
     public static class RequestListUser{
    	 public JSONObject list;
     }
     
     public static class RequestMatchInit{
    	 public String nameRoom;
     }
     
     public static class RequestMatchInfo{
    	 public JSONObject matchInfo;
    	 public String nameRoom;
     } 
     
     public static class RequestUserInfo{
    	 public JSONObject userInfo;
    	 public String nameRoom;
     }
}

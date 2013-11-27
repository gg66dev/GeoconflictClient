package cl.geoconflict.gameplay;

import cl.geoconflict.gps.PositionGPS;

public class Player {
	
	/** Le otorga mayor sensibilidad al GPS, haciendo que el movimiento se vea m&aacute;s r&aacute;pido */
	public static final int SPEED = 3;
	
	
	private int iAmmo;
	private int iScore;
	private Map map;
	private int health;
	private PositionGPS gps;
	private int x;
	private int y;
	private int width;
	private int height;
	private int screenWidth;
	private int screenHeight;
	
	
	public Player(int ammo, Map map, PositionGPS gps){
		health = 10;
		iAmmo = ammo;
		iScore = 0;
		this.map = map;
		this.gps = gps;
		//posicion se corrige cuando se asigna dimencion
		this.x=0;
		this.y=0;
	}
	
	public void setDimencion(int w, int h, int sw, int sh){
		
		this.width = w;
		this.height = h;
		this.screenWidth = sw;
		this.screenHeight = sh;
		this.x = sw/2 - this.width/2;
		this.y = sh/2 - this.height/2;
	}
	
	
	public boolean isLoaded()
	{
		if(iAmmo > 0) 
			return true;
		
		return(false);
	}
	
	public void addScore(int score){
		if(iAmmo > 0)
			iScore+=score;
	}
	
	public void restAmmo(){
		if(iAmmo>0)
			iAmmo--;
	}
	
	
	public String getScore(){
		
		if(iScore < 10)
			return "00000";
		if(iScore < 100)
			return "000"+  Integer.toString(iScore);
		if(iScore < 1000)
			return "00"+  Integer.toString(iScore);
		if(iScore < 10000)
			return "0"+  Integer.toString(iScore);
				
		return Integer.toString(iScore);
			
	}
	
	public String getAmmo(){
		return Integer.toString(iAmmo);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	/** Actualiza el movimiento del jugador en el juego */
	public void update(){
		if( this.gps.getX() != 0 || this.gps.getY() != 0){
			this.x = (int) (this.screenWidth/2 - this.width/2 +
					( ( (int) this.gps.getX() ) - this.map.getInitialX()) * SPEED );
			this.y = (int) (this.screenHeight/2 - this.height/2 + 
					( ( (int) this.gps.getY() ) - this.map.getInitialY()) * SPEED );
		}
	}
}

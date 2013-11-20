package cl.geoconflict.gameplay;

public class Player {
	
	int lifeCount;
	int iAmmo;
	int iScore;
	
	public Player(int ammo){
		lifeCount = 8;
		iAmmo = ammo;
		iScore = 0;
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
	
	
	public int getLifeCount(){
		return lifeCount;
	}
	
}

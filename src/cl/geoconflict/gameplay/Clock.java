package cl.geoconflict.gameplay;

public class Clock {

	String sMinute;
	String sSecond;
	int minute;
	int second;
	float lapse = 0;
	
	public Clock(int timeMatch){
		minute = timeMatch;
		second = 00;
		sMinute = Integer.toString(timeMatch);
		sSecond = "00";
	}
	
	public void update(float dt){
		
		//cuenta regresiva de la partida
		if (minute > 0){
			lapse += dt;
			if(lapse > 1){
				lapse-=1;
				second--;
				if(second < 0){
					minute--;
					second = 59;
				}
			}
		}
		
	}
	
	public String getTime(){
		
		sMinute = Integer.toString(minute);
		sSecond = Integer.toString(second);
		if(second < 10)		
			sSecond = "0"+sSecond;
		if(minute < 10)		
			sMinute = "0"+sMinute;
			
		return sMinute + ":" + sSecond;
	}
	
}

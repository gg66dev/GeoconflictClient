package cl.geoconflict.activity;

import cl.geoconflict.GameStates;
import cl.geoconflict.GeoConflictGame;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainMenuScreen_activity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.mainmenuscreen);
    
	}
	
	public void onClick_register(View v) {
		Intent i = new Intent(MainMenuScreen_activity.this, RegisterScreen_activity.class);
		startActivity(i);
		finish();
	}

	public void onClick_login(View v) {
		Intent i = new Intent(MainMenuScreen_activity.this, LoginScreen_activity.class);
		startActivity(i);
		finish();
	}

	/*
	public void onClick_back(View v) {
		Intent i = new Intent(MainMenuScreen_activity.this, StartScreen_activity.class);
		startActivity(i);
		finish();
	}*/
	
	public void onClick_create_map(View v){
		GameStates.createMap = true;
		Intent i = new Intent(MainMenuScreen_activity.this,
				GeoConflictGame.class);
		startActivity(i);
		finish();
	}
	
	public void onBackPressed() {
		Intent i = new Intent(MainMenuScreen_activity.this, StartScreen_activity.class);
		startActivity(i);
		finish();
	}
	
	public void onClick_sound(View v) {
		
	}
}

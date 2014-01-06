package cl.geoconflict.activity;

import cl.geoconflict.ConnectionManager;
import cl.geoconflict.GameProperties;
import cl.geoconflict.GameStates;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



public class StartScreen_activity extends Activity {
	AnimationDrawable frameAnimation;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.startscreen);

		// llama funcion init de clases staticas
		if (!GameStates.staticClassStarted) {
			GameStates.init(); // inicia cliente
			GameProperties.init(this);// inicia recursos que luego usara el
										// juego
			ConnectionManager.init(this);
			GameStates.staticClassStarted = true;
		}
	}


	public void onClick_start(View v) {
		 Intent i = new Intent(StartScreen_activity.this,
		 MainMenuScreen_activity.class);
		 startActivity(i);
		 finish();
	}

	public void onClick_exit(View v) {
		System.exit(0);
	}

	public void onClick_sound(View v) {

	}

}

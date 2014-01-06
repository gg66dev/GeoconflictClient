package cl.geoconflict.activity;

import cl.geoconflict.ConnectionManager;
import cl.geoconflict.GameStates;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MenuScreen_activity extends Activity {

	Handler createRoomHandler;
	Handler joinMatchHandler;
	ProgressDialog progressDialog;
	boolean result = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menuscreen);
		// importante controla perdida de coneccion
		ConnectionManager.setActivity(this);

		createRoomHandler = new Handler();
		joinMatchHandler = new Handler();

	}

	public void onClick_create_match(View v) {
		result = false;
		ConnectionManager.createMatch();
		CreateRoomWaitTask();
	}

	public void onClick_join_match(View v) {
		result = false;
		ConnectionManager.listMatch();
		JoinMatchWaitTask();
	}

	// retorna a startmenu, no a login
	public void onClick_create_map(View v) {

	}

	// Espera respuesta de creacion de sala
	private void CreateRoomWaitTask() {
		// display the progressbar on the screen
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Creando una nueva partida...");
		progressDialog.show();
		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.roomAcepted) {
						Log.d("debug", "se acepto creacion de sala");
						result = true;
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				createRoomHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUICreateRoom();
						// remember to dismiss the progress dialog on UI thread
						progressDialog.dismiss();
					}
				});

			}
		};

		thread.start();
	}

	// espera lista de salas
	private void JoinMatchWaitTask() {
		// display the progressbar on the screen
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Buscando Salas disponibles...");
		progressDialog.show();
		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.listReceived) {
						result = true;
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				joinMatchHandler.post(new Runnable() {
					@Override
					public void run() {
						updateUIListRoom();
						// remember to dismiss the progress dialog on UI thread
						progressDialog.dismiss();
					}
				});

			}
		};

		thread.start();
	}

	void updateUICreateRoom() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		if (!result) {
			alt_bld.setMessage("Error -- no se pudo crear la Partida");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		} else {
			// pasa a la ventana de crear partida.
			GameStates.currMatch = GameStates.username;
			Intent i = new Intent(MenuScreen_activity.this,
					CrearPartidaScreen_activity.class);
			Log.d("debug", "se iniciara crear partida");
			startActivity(i);
			finish();
		}
	}

	void updateUIListRoom() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		if (!result) {
			alt_bld.setMessage("Error en obtener listas de partidas partidas");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		} else {
			// pasa a la ventana listar partidas.
			Intent i = new Intent(MenuScreen_activity.this,
					PartidasDisponibleScreen_activity.class);
			startActivity(i);
			finish();
		}
	}

	// muestra mensaje de termino de conexion cuando se apreta boton back del
	// smartphone (no de la aplicacion)
	@Override
	public void onBackPressed() {
		if (GameStates.client.isConnected()) {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			alt_bld.setMessage("Terminar Conexion?");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Si",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// termina conexion
							if (GameStates.client.isConnected())
								GameStates.client.close();
							Intent i = new Intent(MenuScreen_activity.this,
									StartScreen_activity.class);
							startActivity(i);
							finish();
						}
					});
			alt_bld.setNegativeButton("No", null);
			alt_bld.show();
		} else {
			Intent i = new Intent(MenuScreen_activity.this,
					StartScreen_activity.class);
			startActivity(i);
			finish();
		}
	}

}

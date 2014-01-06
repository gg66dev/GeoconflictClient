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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class LoginScreen_activity extends Activity {

	/*
	 * clase que guarda variables intermediarias entre las screen y el
	 * networklistener
	 */
	/* obtiene propiedades del framework que se inicializan al principio */
	EditText username;
	EditText password;

	Handler loginhandler;
	ProgressDialog progressDialog;
	boolean result = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loginscreen);
		// importante controla perdida de coneccion
		ConnectionManager.setActivity(this);

		loginhandler = new Handler();
		username = (EditText) findViewById(R.id.etx_username);
		password = (EditText) findViewById(R.id.etx_pass);

		// crea conexion
		ConnectionManager.connect();
	}

	public void onClick_login(View v) {
		result = false;
		String username = this.username.getText().toString();
		String passwd = this.password.getText().toString();

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		if (username.isEmpty() && passwd.isEmpty()) {
			alt_bld.setMessage("no deben haber campos vacios");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		} else  if (GameStates.client.isConnected())  {
			// thread que espera respuesta al registro
			LoginWaitTask(username, passwd);
		} else
		{
			alt_bld.setMessage("no se tiene conección con el servidor");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		}
	}

	public void onClick_back(View v) {
		// termina conexion
		if (GameStates.client.isConnected())
			GameStates.client.close();
		Intent i = new Intent(LoginScreen_activity.this,
				MainMenuScreen_activity.class);
		startActivity(i);
		finish();
	}

	public void onBackPressed() {
		// termina conexion
		if (GameStates.client.isConnected())
			GameStates.client.close();
		Intent i = new Intent(LoginScreen_activity.this,
				MainMenuScreen_activity.class);
		startActivity(i);
		finish();
	}

	private void LoginWaitTask(String username, String passwd) {
		// display the progressbar on the screen
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Autentificando...");
		progressDialog.show();

		// envia mensaje
		GameStates.passwd = passwd;
		GameStates.username = username;
		ConnectionManager.sendLogin();

		
		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					if (GameStates.logged) {
						result = true;
						break;
					}
					if (GameStates.error) {
						result = false;
						break;
					}
					if(!GameStates.client.isConnected()){
						progressDialog.dismiss();
						return;
					}
				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				loginhandler.post(new Runnable() {
					@Override
					public void run() {
						updateUI();
						// remember to dismiss the progress dialog on UI thread
						progressDialog.dismiss();
					}
				});
			}
		};
		thread.start();
	}

	// actulizar segun resultado
	private void updateUI() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		if (!result) {
			alt_bld.setMessage("No existe usuario o código salt no coincide");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		} else {
			alt_bld.setMessage("autentificacion Exitosa");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// pasa a la ventana menu.
							Intent i = new Intent(LoginScreen_activity.this,
									MenuScreen_activity.class);
							startActivity(i);
							finish();
						}
					});
			alt_bld.show();
		}
	}

}

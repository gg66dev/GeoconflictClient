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
import android.widget.EditText;

public class RegisterScreen_activity extends Activity {

	Handler registerhandler;
	ProgressDialog progressDialog;
	boolean result = false;

	EditText username;
	EditText password;
	EditText passwordConfirm;
	EditText email;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.registerscreen);
		// importante controla perdida de coneccion
		ConnectionManager.setActivity(this);
		registerhandler = new Handler();

		// inicia propiedades de juego (inputs, assets..etc)

		// obtiene valores de los campos
		username = (EditText) findViewById(R.id.etx_username);
		password = (EditText) findViewById(R.id.etx_pass);
		passwordConfirm = (EditText) findViewById(R.id.etx_passconfirm);
		email = (EditText) findViewById(R.id.etx_email);

		ConnectionManager.connect();
	}

	public void onClick_register(View v) {

		String username = this.username.getText().toString();
		String passwd = this.password.getText().toString();
		String passwdC = this.passwordConfirm.getText().toString();
		String email = this.email.getText().toString();

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

		if (username.isEmpty() && passwd.isEmpty() && email.isEmpty()) {
			alt_bld.setMessage("no deben haber campos vacios");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		} else if (username.length() < 2) {
			alt_bld.setMessage("Username debe tener mas de dos caracteres");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		} else if (passwd.length() < 4) {
			alt_bld.setMessage("password debe tener mas de cuatro caracteres");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		} else if (!passwd.equals(passwdC)) {
			alt_bld.setMessage("los password deben coincidir");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok", null);
			alt_bld.show();
		} else if (GameStates.client.isConnected()) {
			// ingresa variables a gamestates
			GameStates.passwd = passwd;
			GameStates.username = username;
			GameStates.mail = email;

			ConnectionManager.sendRegister();
			// thread que espera respuesta al registro
			RegisterWaitTask();
		} else {
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
		Intent i = new Intent(RegisterScreen_activity.this,
				MainMenuScreen_activity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void onBackPressed() {
		// termina conexion
		if (GameStates.client.isConnected())
			GameStates.client.close();
		Intent i = new Intent(RegisterScreen_activity.this,
				MainMenuScreen_activity.class);
		startActivity(i);
		finish();
	}

	private void RegisterWaitTask() {

		// display the progressbar on the screen
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Registrando nuevo usuario...");
		progressDialog.show();

		// start the time consuming task in a new thread
		Thread thread = new Thread() {
			public void run() {

				while (true) {
					if (GameStates.registered) {
						Log.d("debug", "se a registrado");
						result = true;
						break;
					}
					if (GameStates.error) {
						result = false;
						break;
					}
					if (!GameStates.client.isConnected()) {
						progressDialog.dismiss();
						return;
					}

				}
				// Now we are on a different thread than UI thread
				// and we would like to update our UI, as this task is completed
				registerhandler.post(new Runnable() {
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
		if (result) {
			alt_bld.setMessage("Registro Exitoso");
			alt_bld.setCancelable(false);
			alt_bld.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// termina conexion
							if (GameStates.client.isConnected())
								GameStates.client.close();
							// vuelve a ventana anterior
							Intent i = new Intent(RegisterScreen_activity.this,
									MainMenuScreen_activity.class);
							startActivity(i);
							finish();
						}
					});
			alt_bld.show();
		} else {
			alt_bld.setMessage("ERROR al registrarse -- usuario ya existe");
		}
		alt_bld.setCancelable(false);
		alt_bld.setPositiveButton("Ok", null);
		alt_bld.show();
	}

}

package com.pnkinc.audiatonico;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private static final int RESULT_SETTINGS = 0;
	private ImageButton botonUnJugador, botonOpciones, botonDosJugadores,botonPuntuaciones;
	private int valorJuego;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		botonUnJugador = (ImageButton)findViewById(R.id.buttonGame);
		botonDosJugadores = (ImageButton)findViewById(R.id.buttonTwoGame);
		botonOpciones = (ImageButton)findViewById(R.id.buttonOptions);
		botonPuntuaciones = (ImageButton)findViewById(R.id.buttonScore);

		showUserSettings();

        botonUnJugador.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	        	valorJuego = 0;
	            Intent intent = new Intent(MainActivity.this, Juego.class);
				intent.putExtra("tipojuego", valorJuego);
				startActivity(intent);
	            overridePendingTransition(R.anim.fade, R.anim.hold);
	        }

        });
        
        botonDosJugadores.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	        	valorJuego = 1;
	            Intent intent = new Intent(MainActivity.this, Juego.class);
				intent.putExtra("tipojuego", valorJuego);
				startActivity(intent);
	            overridePendingTransition(R.anim.fade, R.anim.hold);
	        }

        });
        
        
        botonOpciones.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
				Intent i = new Intent(MainActivity.this, AudiaOptions.class);
				startActivityForResult(i, RESULT_SETTINGS);
				overridePendingTransition(R.anim.fade, R.anim.hold);
	        }

        });
        
        botonPuntuaciones.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
				Intent i = new Intent(MainActivity.this, ScoreTables.class);
				startActivity(i);
				overridePendingTransition(R.anim.fade, R.anim.hold);
	        }

        });
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			showUserSettings();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	finish();
	    	android.os.Process.killProcess(android.os.Process.myPid());
	    }
	    return super.onKeyDown(keyCode, event);
	}

	private void showUserSettings() {
		SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		int posId = preferences.getInt("POSICION_IDIOMA", 0);
		
		if (posId == 1){
			botonUnJugador.setBackgroundResource(R.drawable.boton_singleplayer_estilo);
			botonDosJugadores.setBackgroundResource(R.drawable.boton_twoplayer_estilo);
		}else{
			botonUnJugador.setBackgroundResource(R.drawable.boton_unjugador_estilo);
			botonDosJugadores.setBackgroundResource(R.drawable.boton_dosjugadores_estilo);
		}
	}

}

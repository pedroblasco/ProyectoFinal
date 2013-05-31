package com.pnkinc.audiatonico;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int RESULT_SETTINGS = 1;
	private ImageButton botonUnJugador, botonOpciones, botonAcercaDe;
	private TextView tituloMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tituloMain = (TextView)findViewById(R.id.textView1);
		Typeface fuenteJuego = Typeface.createFromAsset(getAssets(), "fonts/8bitwonder.ttf");
		tituloMain.setTypeface(fuenteJuego);
		
		botonUnJugador = (ImageButton)findViewById(R.id.buttonGame);
		botonOpciones = (ImageButton)findViewById(R.id.buttonOptions);
		botonAcercaDe = (ImageButton)findViewById(R.id.buttonAbout);

		showUserSettings();

        botonUnJugador.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	            Intent intent = new Intent(MainActivity.this, Juego.class);
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
        
        botonAcercaDe.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {

	        }

        });
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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

	private void showUserSettings() {
		SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		int posId = preferences.getInt("POSICION_IDIOMA", 0);
		
		if (posId == 1){
			botonUnJugador.setBackgroundResource(R.drawable.botonsingleplayerestilo);
			botonOpciones.setBackgroundResource(R.drawable.botonoptionsestilo);
			botonAcercaDe.setBackgroundResource(R.drawable.botonaboutestilo);
		}else{
			botonUnJugador.setBackgroundResource(R.drawable.botonunjugadorestilo);
			botonOpciones.setBackgroundResource(R.drawable.botonopcionesestilo);
			botonAcercaDe.setBackgroundResource(R.drawable.botonacercadeestilo);
		}
	}

}

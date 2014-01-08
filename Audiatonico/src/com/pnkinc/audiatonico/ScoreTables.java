package com.pnkinc.audiatonico;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreTables extends Activity{
	private SQLiteDatabase db;
	private TextView maximoRondas, mejoraRondasText, porcenText, maximoRondasNum, mejoraRondasNum, porcenNum, textViewEnc;
	private RelativeLayout relayoutPuntu;
	private ImageButton botonUnJugador, botonDosJugadores, botonFacil, botonMedio, botonDificil;
	private String maxRondasSup, mejoraRondasSup, textoPorcen, textoPuntu, toastAlerta, toastFacil, toastMedia, toastDificil;
	private int dif = 0;
	private int percent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_layout);
		
		Typeface fuenteJuego = Typeface.createFromAsset(getAssets(), "fonts/8bitwonder.ttf");
		botonUnJugador = (ImageButton)findViewById(R.id.botonUnJug);
		botonDosJugadores = (ImageButton)findViewById(R.id.botonDosJug);
		botonFacil = (ImageButton)findViewById(R.id.botonFacil);
		botonMedio = (ImageButton)findViewById(R.id.botonMedio);
		botonDificil = (ImageButton)findViewById(R.id.botonDificil);
		relayoutPuntu = (RelativeLayout)findViewById(R.id.vistaPuntu);
		
		textViewEnc = (TextView)findViewById(R.id.textoPuntu);
		textViewEnc.setTypeface(fuenteJuego);
		textViewEnc.setTextSize(0x00000001, 25);
		
		maximoRondas = (TextView)findViewById(R.id.numRondasText);
		maximoRondas.setTypeface(fuenteJuego);
		maximoRondas.setTextSize(0x00000001, 12);
		
		maximoRondasNum = (TextView)findViewById(R.id.numRondasNum);
		maximoRondasNum.setTypeface(fuenteJuego);
		maximoRondasNum.setTextSize(0x00000001, 25);
		
		mejoraRondasText = (TextView)findViewById(R.id.rondasMejText);
		mejoraRondasText.setTypeface(fuenteJuego);
		mejoraRondasText.setTextSize(0x00000001, 12);
		
		mejoraRondasNum = (TextView)findViewById(R.id.rondasMejNum);
		mejoraRondasNum.setTypeface(fuenteJuego);
		mejoraRondasNum.setTextSize(0x00000001, 25);
		
		porcenText = (TextView)findViewById(R.id.numPorcenText);
		porcenText.setTypeface(fuenteJuego);
		porcenText.setTextSize(0x00000001, 12);
		
		porcenNum = (TextView)findViewById(R.id.numPorcenNum);
		porcenNum.setTypeface(fuenteJuego);
		porcenNum.setTextSize(0x00000001, 35);
		
		relayoutPuntu.setVisibility(View.INVISIBLE);
		
		AudiatonicoSQLiteHelper playDataB = new AudiatonicoSQLiteHelper(this, "DBPlayers", null, 1);
		db = playDataB.getWritableDatabase();

		//db.execSQL("DROP TABLE SinglePlayer");
		db.execSQL("CREATE TABLE IF NOT EXISTS SinglePlayer ("
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, dif INT, rounds INT)");
		
		//db.execSQL("DROP TABLE MultiPlayer");
		db.execSQL("CREATE TABLE IF NOT EXISTS MultiPlayer ("
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR, dif INT, rounds INT)");

		showUserSettings();
		textViewEnc.setText(textoPuntu);
		
        botonUnJugador.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	        	compruebaResul();
	        	singlePlayerEntries(dif);
	        }

        });
        
        botonDosJugadores.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	        	Cursor cur = db.rawQuery("SELECT * FROM MultiPlayer", null);
	    		
	    		if(cur !=null && cur.getCount()>0){
		        	Intent intent = new Intent(ScoreTables.this, ScoreShow.class);
					startActivity(intent);
		            overridePendingTransition(R.anim.fade, R.anim.hold);
	    		}else{
	    			Toast.makeText(getApplicationContext(), toastAlerta, 
	 					   Toast.LENGTH_LONG).show();
	    		}
	        }

        });
        
        botonFacil.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	        	dif = 0;
	        	singlePlayerEntries(dif);
	        }

        });
        
        botonMedio.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	        	dif = 1;
	        	singlePlayerEntries(dif);
	        }

        });
        
        botonDificil.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	        	dif = 2;
	        	singlePlayerEntries(dif);
	        }

        });
	}
	
	public void singlePlayerEntries(int dificultad){
		
		if(dificultad == 1){
			percent = 4;
		}else if (dificultad == 2){
			percent = 5;
		}else{
			percent = 3;
		}
		
		
		Cursor cur = db.rawQuery("SELECT rounds FROM SinglePlayer WHERE dif LIKE '" + dificultad + "' ORDER BY rounds DESC", null);
		
		if(cur !=null && cur.getCount()>0){
			relayoutPuntu.setVisibility(View.VISIBLE);
			mejoraRondasText.setVisibility(View.INVISIBLE);
			mejoraRondasNum.setVisibility(View.INVISIBLE);
			
			cur.moveToFirst();
			String rondas = cur.getString(0);
			int rondasEnt = Integer.parseInt(rondas);
			
			maximoRondas.setText(maxRondasSup);
			maximoRondasNum.setText(" " + rondasEnt);
			
			if(cur.moveToNext()){
				
				String rondasAux = cur.getString(0);
				int rondasAuxEnt = Integer.parseInt(rondasAux);
				rondasEnt -= rondasAuxEnt;
				if(rondasEnt != 0){
					mejoraRondasText.setVisibility(View.VISIBLE);
					mejoraRondasNum.setVisibility(View.VISIBLE);
					
					mejoraRondasText.setText(mejoraRondasSup);
					mejoraRondasNum.setText(" " + rondasEnt);
				}
				
				rondasEnt = Integer.parseInt(rondas);
			}
			
			rondasEnt = rondasEnt * percent;
			
			if(rondasEnt > 100){
				rondasEnt = 100;
			}
			
			if(rondasEnt > 49){
				porcenNum.setTextColor(Color.RED);
			}else if (rondasEnt < 50){
				porcenNum.setTextColor(Color.RED);
			}
			porcenText.setText(textoPorcen);
			porcenNum.setText(" " + (rondasEnt) + "%");
			
			if(dificultad == 1){
				Toast.makeText(getApplicationContext(), toastMedia, 
						   Toast.LENGTH_LONG).show();
			}else if(dificultad == 2){
				Toast.makeText(getApplicationContext(), toastDificil, 
						   Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), toastFacil, 
						   Toast.LENGTH_LONG).show();
			}

		}else{
			Toast.makeText(getApplicationContext(), toastAlerta, 
					   Toast.LENGTH_LONG).show();
		}
	}
	
	private void compruebaResul(){
		Cursor cur = db.rawQuery("SELECT rounds FROM SinglePlayer WHERE dif LIKE '" + 0 + "' ORDER BY rounds DESC", null);
		if(cur !=null && cur.getCount()>0){
			dif = 0;
		}else{
			cur = db.rawQuery("SELECT rounds FROM SinglePlayer WHERE dif LIKE '" + 1 + "' ORDER BY rounds DESC", null);
			if(cur !=null && cur.getCount()>0){
				dif = 1;
			}else{
				cur = db.rawQuery("SELECT rounds FROM SinglePlayer WHERE dif LIKE '" + 2 + "' ORDER BY rounds DESC", null);
				if(cur !=null && cur.getCount()>0){
					dif = 2;
				}else{
					dif=0;
				}
			}
		}
	}
	
	private void showUserSettings() {
		SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		int posId = preferences.getInt("POSICION_IDIOMA", 0);
		if(posId == 0){
			maxRondasSup = "Max. Rondas Superadas: ";
			mejoraRondasSup = "Mejora En Rondas: ";
			textoPorcen = "Porcentaje Auditivo: ";
			textoPuntu = "Puntuaciones";
			toastAlerta = "¡No hay puntuaciones!";
			toastFacil = "Mostrando Resultados Fácil";
			toastMedia = "Mostrando Resultados Media";
			toastDificil = "Mostrando Resultados Difícil";
			botonUnJugador.setBackgroundResource(R.drawable.boton_unjugador_estilo);
			botonDosJugadores.setBackgroundResource(R.drawable.boton_dosjugadores_estilo);
			botonFacil.setBackgroundResource(R.drawable.boton_facil_estilo);
			botonMedio.setBackgroundResource(R.drawable.boton_media_estilo);
			botonDificil.setBackgroundResource(R.drawable.boton_dificil_estilo);
		}else{
			maxRondasSup = "Max. Rounds Beaten: ";
			mejoraRondasSup = "Rounds Improved: ";
			textoPorcen = "Audition Percentage: ";
			textoPuntu = "Scoreboards";
			toastAlerta = "No scores found!";
			toastFacil = "Showing Easy Results";
			toastMedia = "Showing Normal Results";
			toastDificil = "Showing Hard Results";
			botonUnJugador.setBackgroundResource(R.drawable.boton_singleplayer_estilo);
			botonDosJugadores.setBackgroundResource(R.drawable.boton_twoplayer_estilo);
			botonFacil.setBackgroundResource(R.drawable.boton_easy_estilo);
			botonMedio.setBackgroundResource(R.drawable.boton_media_estilo);
			botonDificil.setBackgroundResource(R.drawable.boton_hard_estilo);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if(relayoutPuntu.getVisibility() == View.VISIBLE){
	    		relayoutPuntu.setVisibility(View.INVISIBLE);
	    		moveTaskToBack(false);
	    		return false;
	    	}else{
	    		db.close();
	    		finish();
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}
}

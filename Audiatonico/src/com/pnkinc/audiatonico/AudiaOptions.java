package com.pnkinc.audiatonico;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class AudiaOptions extends Activity {
	
	private Spinner cambiaIdioma, cambiaInstrumento, cambiaDificultad;
	private ImageButton botonBorrar;
	private SQLiteDatabase db;
	private int posId;
	private String alertMsg;
	private ArrayAdapter<?> cambDi, cambIn, cambId;
	boolean flagIn = false, flagDi = false;
	private TextView textIdio, textInst, textDifi, textBbDd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiaoptions);		
		
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
		
		final SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		posId = preferences.getInt("POSICION_IDIOMA", 0);
		
		botonBorrar = (ImageButton)findViewById(R.id.borrarBbDd);
		
		//ADAPTADORES:
	    cambId = ArrayAdapter.createFromResource(this, R.array.idiomas, android.R.layout.simple_spinner_item);
	    cambId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    cambIn = ArrayAdapter.createFromResource(this, R.array.instrumentos, android.R.layout.simple_spinner_item);
	    cambIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    cambDi = ArrayAdapter.createFromResource(this, R.array.dificultad, android.R.layout.simple_spinner_item);
	    cambDi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
	    //SPINNERS: 
		cambiaIdioma = (Spinner) findViewById(R.id.changeLanguage);
		    
	    cambiaInstrumento = (Spinner) findViewById(R.id.setInstrument);
		    
	    cambiaDificultad = (Spinner) findViewById(R.id.setDifficulty);
	    
	    //TEXTVIEWS:
	    textIdio = (TextView)findViewById(R.id.textIdioma);
	    textInst = (TextView)findViewById(R.id.textInstrumento);
	    textDifi = (TextView)findViewById(R.id.textDificultad);
	    textBbDd = (TextView)findViewById(R.id.textBbDd);
	    Typeface fuenteJuego = Typeface.createFromAsset(getAssets(), "fonts/8bitwonder.ttf");
	    textIdio.setTypeface(fuenteJuego);
	    textInst.setTypeface(fuenteJuego);
	    textDifi.setTypeface(fuenteJuego);
	    textBbDd.setTypeface(fuenteJuego);
	    showUserSettings();
	    
	    //Escuchador de idioma.
	    cambiaIdioma.setOnItemSelectedListener(new OnItemSelectedListener() {       
	    	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, 
	        		int position, long id) {
	        		SharedPreferences.Editor editor = preferences.edit();
	        		editor.putInt("POSICION_IDIOMA", position);
	        		editor.commit();
	        		
	        	if(posId != position){
		        	refresh();
	        	}
	        }	
	                             
	        public void onNothingSelected(AdapterView<?> parentView) {
	        	//DO NOTHING.
	        }
	    });       
	    
	    //Escuchador de instrumento.
	    cambiaInstrumento.setOnItemSelectedListener(new OnItemSelectedListener() {		       
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, 
	        		int position, long id) {
	        		SharedPreferences.Editor editor = preferences.edit();
	        		editor.putInt("POSICION_INSTRUMENTO", position);
	        		editor.commit();
	        }	                        
	        
	        public void onNothingSelected(AdapterView<?> parentView) {
	        	//DO NOTHING.
	        }
	    });  
	    
	    //Escuchador de dificultad.
	    cambiaDificultad.setOnItemSelectedListener(new OnItemSelectedListener() {
		       
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, 
	        		int position, long id) {
	        		SharedPreferences.Editor editor = preferences.edit();
	        		editor.putInt("POSICION_DIFICULTAD", position);
	        		editor.commit();
	        }
	                             
	        public void onNothingSelected(AdapterView<?> parentView) {
	        	//DO NOTHING.
	        }
	    });  
	    
        botonBorrar.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	    		AlertDialog.Builder build = new AlertDialog.Builder(AudiaOptions.this)
	            .setMessage(alertMsg)
	            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int whichButton) {
	                    db.execSQL("DELETE FROM SinglePlayer");
	                    db.execSQL("DELETE FROM MultiPlayer");
	                }
	            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	//DO NOTHING.
	                }
	            });
	    		AlertDialog alert = build.create();
	    		alert.show();
	        }

        });
	}
	
	private void showUserSettings() {
		SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		int posSpinner = preferences.getInt("POSICION_IDIOMA", 0);
		if(posSpinner==1){
			cambId = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
		    cambId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		    cambIn = ArrayAdapter.createFromResource(this, R.array.instruments, android.R.layout.simple_spinner_item);
		    cambIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		    cambDi = ArrayAdapter.createFromResource(this, R.array.difficulty, android.R.layout.simple_spinner_item);
		    cambDi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			botonBorrar.setBackgroundResource(R.drawable.boton_delete_estilo);
			textIdio.setText("Language:");
			textInst.setText("Instrument:");
			textDifi.setText("Difficulty:");
			textBbDd.setText("Delete Database:");
			alertMsg = "Are you sure?";
		}else{
			cambId = ArrayAdapter.createFromResource(this, R.array.idiomas, android.R.layout.simple_spinner_item);
		    cambId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    
		    cambIn = ArrayAdapter.createFromResource(this, R.array.instrumentos, android.R.layout.simple_spinner_item);
		    cambIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    
		    cambDi = ArrayAdapter.createFromResource(this, R.array.dificultad, android.R.layout.simple_spinner_item);
		    cambDi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    
			botonBorrar.setBackgroundResource(R.drawable.boton_borrar_estilo);
			textIdio.setText("Idioma:");
			textInst.setText("Instrumento:");
			textDifi.setText("Dificultad:");
			textBbDd.setText("Borrar base de datos:");
			alertMsg = "¿Estás seguro?";
		}
		cambiaIdioma.setAdapter(cambId);
		cambiaInstrumento.setAdapter(cambIn);
		cambiaDificultad.setAdapter(cambDi);
		
		int posIdioma = preferences.getInt("POSICION_IDIOMA", 0);
		int posInstrumento = preferences.getInt("POSICION_INSTRUMENTO", 0);
		int posDificultad = preferences.getInt("POSICION_DIFICULTAD", 0);
		
		cambiaIdioma.setSelection(posIdioma);
		cambiaInstrumento.setSelection(posInstrumento);
		cambiaDificultad.setSelection(posDificultad);
	}	
	
	/*private void cambiaValoresSpinner(int posSpinner){
		
		showUserSettings();
	}*/
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	db.close();
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void refresh(){
		finish();
		startActivity(getIntent());
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}
}

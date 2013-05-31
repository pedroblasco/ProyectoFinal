package com.pnkinc.audiatonico;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class AudiaOptions extends Activity {
	
	private Spinner cambiaIdioma, cambiaInstrumento, cambiaDificultad;
	private int posId;
	private ArrayAdapter<?> cambDi, cambIn, cambId, chanLa, chanIn, chanDi;
	boolean flagIn = false, flagDi = false;
	private TextView textIdio, textInst, textDifi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiaoptions);		
		
		final SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		posId = preferences.getInt("POSICION_IDIOMA", 0);
		
		//ADAPTADORES:
	    chanLa = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
	    chanLa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    chanIn = ArrayAdapter.createFromResource(this, R.array.instruments, android.R.layout.simple_spinner_item);
	    chanIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    chanDi = ArrayAdapter.createFromResource(this, R.array.difficulty, android.R.layout.simple_spinner_item);
	    chanDi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
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
	    Typeface fuenteJuego = Typeface.createFromAsset(getAssets(), "fonts/8bitwonder.ttf");
	    textIdio.setTypeface(fuenteJuego);
	    textInst.setTypeface(fuenteJuego);
	    textDifi.setTypeface(fuenteJuego);
	    
	    cambiaValoresSpinner(posId);
	    
	    //Escuchador de idioma.
	    cambiaIdioma.setOnItemSelectedListener(new OnItemSelectedListener() {       
	    	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, 
	        		int position, long id) {
	    		int i = posId;
	        	if(position==1){
		        		SharedPreferences.Editor editor = preferences.edit();
		        		editor.putString("IDIOMA", "en");
		        		editor.putInt("POSICION_IDIOMA", position);
		        		editor.commit();
		        	}else{
		        		SharedPreferences.Editor editor = preferences.edit();
		        		editor.putString("IDIOMA", "es");
		        		editor.putInt("POSICION_IDIOMA", position);
		        		editor.commit();
	        	}
	        	if(i != position){
	        		i=position;
		        	refresh();
	        	}
	        }	
	                             
	        public void onNothingSelected(AdapterView<?> parentView) {
	        	
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
	        	
	        }
	    });  
	}
	
	private void showUserSettings() {
		SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		int posIdioma = preferences.getInt("POSICION_IDIOMA", 0);
		int posInstrumento = preferences.getInt("POSICION_INSTRUMENTO", 0);
		int posDificultad = preferences.getInt("POSICION_DIFICULTAD", 0);
		
		cambiaIdioma.setSelection(posIdioma);
		cambiaInstrumento.setSelection(posInstrumento);
		cambiaDificultad.setSelection(posDificultad);
	}	
	
	private void cambiaValoresSpinner(int posSpinner){
		if(posSpinner==1){
			cambiaIdioma.setAdapter(chanLa);
			cambiaInstrumento.setAdapter(chanIn);
			cambiaDificultad.setAdapter(chanDi);
			textIdio.setText("Language:");
			textInst.setText("Instrument:");
			textDifi.setText("Difficulty:");
		}else{
			cambiaIdioma.setAdapter(cambId);
			cambiaInstrumento.setAdapter(cambIn);
			cambiaDificultad.setAdapter(cambDi);
			textIdio.setText("Idioma:");
			textInst.setText("Instrumento:");
			textDifi.setText("Dificultad:");
		}
		showUserSettings();
	}
	
	private void refresh(){
		finish();
		startActivity(getIntent());
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}
	
	/*public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
    	switch(parent.getId()){
    	case R.id.changeLanguage:
    		Toast.makeText(parent.getContext(), "Has seleccionado " +
      	          parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    		break;
		case R.id.setInstrument:
			Toast.makeText(parent.getContext(), "Has seleccionado " +
      	          parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
			break;
		case R.id.setDifficulty:
			Toast.makeText(parent.getContext(), "Has seleccionado " +
      	          parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
			break;
		}
    }*/

}

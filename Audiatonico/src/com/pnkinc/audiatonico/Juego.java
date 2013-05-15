package com.pnkinc.audiatonico;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Juego extends Activity 
{
	private int[] arraySounds;
	private int[] arrayRandomSounds;
	private int[] arrayImages;
	private int j = 2;
	private int cont = 0;
	private int idSound = 0;
	private ImageView textoNota, intentosRestantes;
	private TextView textoJuego;
	//private int k=0;
	private int fallos = 0;
	private int juego = 0;
	private Handler handler = new Handler();
	
	RelativeLayout botones1;
	ImageButton botonArr1;
		
	AudioAssistant snd;
	int doc, red, mie, faf, solg, laa, sib, docag;
	int streamID;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		textoJuego = (TextView)findViewById(R.id.textView1);
		textoNota = (ImageView)findViewById(R.id.imageView1);
		intentosRestantes = (ImageView)findViewById(R.id.imageView2);
		botones1 = (RelativeLayout)findViewById(R.id.relayout1);
		botonArr1 = (ImageButton)findViewById(R.id.button1);
		botones1.setVisibility(View.INVISIBLE);
		Typeface fuenteJuego = Typeface.createFromAsset(getAssets(), "fonts/8bitwonder.ttf");
		textoJuego.setTypeface(fuenteJuego);
		textoJuego.setVisibility(View.INVISIBLE);
		
		//Creamos la instancia del SoundManager.
		snd = new AudioAssistant(getApplicationContext());
		
		//Asignamos el volumen multimedia a la aplicación.
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//Cargamos los sonidos y los asignamos a los int para utilizarlos como index o como simples identificadores.
		doc = snd.load(R.raw.notac);
		red = snd.load(R.raw.notad);
		mie = snd.load(R.raw.notae);
		faf = snd.load(R.raw.notaf);
		solg = snd.load(R.raw.notag);
		laa = snd.load(R.raw.notaa);
		sib = snd.load(R.raw.notab);
		docag = snd.load(R.raw.notacag);
		
		arrayImages = new int[] {R.drawable.viewpred, R.drawable.notado, R.drawable.notare, R.drawable.notami, R.drawable.notafa, 
				R.drawable.notasol, R.drawable.notala, R.drawable.notasi, R.drawable.notadoag};
		arraySounds = new int[] {doc, red, mie, faf, solg, laa, sib, docag};
		arrayRandomSounds = new int[j];
		for (int i=0; i < arrayRandomSounds.length; i++){
			arrayRandomSounds[i]=snd.getRandomSound(arraySounds);
		}
	}
	     
//ButtonListener añadido por id en el xml.
public void clickHandler(View v)
{	
	int id = v.getId(); // Reconoce la id de los botones (asignada en el xml) para reproducir los sonidos.
    	
	switch (id)
	{
		case R.id.buttonC:
			idSound = doc;
			snd.play(idSound);
			break;
		    	  
		case R.id.buttonD:
			idSound = red;
			snd.play(idSound);
			break;
		    	   
		case R.id.buttonE:
			idSound = mie;
			snd.play(idSound);
			break;    	  
		       	  
		case R.id.buttonF:
			idSound = faf;
			snd.play(idSound);
			break;    	  
		          	  
		case R.id.buttonG:
			idSound = solg;
			snd.play(idSound);
			break;    	  
		          	  
		case R.id.buttonA:
			idSound = laa;
			snd.play(idSound);
			break;    
		         
		case R.id.buttonB:
			idSound = sib;
			snd.play(idSound);
			break;
			
		case R.id.buttonCag:
			idSound = docag;
			snd.play(idSound);
			break;
			
		case R.id.button1:
			fallos=0;
			cont=0;
			textoJuego.setVisibility(View.VISIBLE);
			textoJuego.setText("RONDA " + (juego+1));
			botonArr1.setVisibility(View.INVISIBLE); 
			arrayRandomSounds = snd.fillArrays(juego, arraySounds, arrayRandomSounds);
			handler.postDelayed(new Runnable() { 
				public void run() {
					textoJuego.setVisibility(View.INVISIBLE);
				} 
			}, 1300);
			snd.playAndShow(arrayImages, arrayRandomSounds, textoNota, botones1);
			break;
		}
	
		if(idSound !=0){
			if(idSound == arrayRandomSounds[cont]){
				Log.d("ACIERTO", "¡Has acertado!");
				textoNota.setImageResource(R.drawable.textoacierto);
				cont++;
				handler.postDelayed(new Runnable() { 
					public void run() {
						textoNota.setImageResource(R.drawable.viewpred);
					}
				}, 700);
			}else{
				Log.d("FALLO", "¡Has fallado!");
				textoNota.setImageResource(R.drawable.notaerror);
				fallos++;
				handler.postDelayed(new Runnable() { 
					public void run() {
						textoNota.setImageResource(R.drawable.viewpred);
					}
				}, 700);
			}
		}
		juego=snd.manageFailures(fallos, intentosRestantes, textoJuego, juego, botones1, botonArr1);
		idSound = 0;
		
		if (cont >= arrayRandomSounds.length){
			juego = snd.roundBeaten(juego, botones1, botonArr1, textoJuego, textoNota);
			cont = 0;		
		}
	}
	
	/*private void playAndShow(){
		if(k < arrayRandomSounds.length){
			handler.postDelayed(new Runnable() { 
				public void run() {
					snd.playForShow(arrayRandomSounds[k], textoNota, arrayImages);
					k++;
					playAndShow(); 
				} 
			}, 1500);
		}
		else{
			handler.postDelayed(new Runnable() { 
				public void run() {
					k=0;
					textoNota.setImageResource(R.drawable.viewpred);
					botones1.setVisibility(View.VISIBLE);
				} 
			}, 1500);
		}
	}*/
}


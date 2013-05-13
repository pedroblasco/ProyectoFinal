package com.pnkinc.audiatonico;

import java.util.Random;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Juego extends Activity 
{
	private int[] arraySounds;
	private int[] arrayRandomSounds;
	private int[] arrayImages;
	private int j = 2;
	private int cont = 0;
	private int idSound = 0;
	int game = 0;
	private ImageView textoNota, intentosRestantes;
	private int k=0;
	int fallos = 0;
	int juego = 0;
	
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
		textoNota = (ImageView)findViewById(R.id.imageView1);
		intentosRestantes = (ImageView)findViewById(R.id.imageView2);
		botones1 = (RelativeLayout)findViewById(R.id.relayout1);
		botonArr1 = (ImageButton)findViewById(R.id.button1);
		botones1.setVisibility(View.INVISIBLE);
		
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
			if (juego==0){
				arrayRandomSounds = new int[j];

				for(int i=0; i<arrayRandomSounds.length; i++){
					arrayRandomSounds[i]=snd.getRandomSound(arraySounds);
				}
			}else{
				j+=2;
				int[] otroArrayAuxiliar = new int[arrayRandomSounds.length];
				System.arraycopy(arrayRandomSounds, 0, otroArrayAuxiliar, 0, arrayRandomSounds.length);
				arrayRandomSounds = new int[j];
				for(int i=0; i<arrayRandomSounds.length; i++){
					if(i < otroArrayAuxiliar.length){
						arrayRandomSounds[i] = otroArrayAuxiliar[i];
					}
					else{
						arrayRandomSounds[i]=snd.getRandomSound(arraySounds);
					}
				}
			}			
			playAndShow();
			break;
		}
	
		if(idSound !=0){
			if(idSound == arrayRandomSounds[cont]){
				Log.d("ACIERTO", "¡Has acertado!");
				textoNota.setImageResource(R.drawable.textoacierto);
				cont++;
			}else{
				Log.d("FALLO", "¡Has fallado!");
				textoNota.setImageResource(R.drawable.notaerror);
				fallos++;
			}
		}
		fallos = snd.manageFailures(fallos, intentosRestantes);
		idSound = 0;
		
		if (cont >= arrayRandomSounds.length){
			botones1.setVisibility(View.INVISIBLE);
			botonArr1.setVisibility(View.VISIBLE);
			cont = 0;
			juego++;
			fallos = 0;
		}
	}
	
	private void playAndShow(){
		if(k < arrayRandomSounds.length){
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() { 
				public void run() {
					snd.playForShow(arrayRandomSounds[k], textoNota, arrayImages);
					k++;
					playAndShow(); 
				} 
			}, 1500);
		}
		else{
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() { 
				public void run() {
					k=0;
					idSound = 0;
					textoNota.setImageResource(R.drawable.viewpred);
					botones1.setVisibility(View.VISIBLE);
					botonArr1.setVisibility(View.INVISIBLE); 
				} 
			}, 1500);
		}
	}
}


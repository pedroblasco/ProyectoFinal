package com.pnkinc.audiatonico;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Juego extends Activity 
{
	private int[] arraySounds, arrayRandomSounds, arrayImages;
	private int j = 2;
	private int cont = 0;
	private int idSound = 0;
	String textRonda, textAcierto, textFallo, textRondaSuperadaI, textRondaSuperadaII, textPierdesJuego;  
	private ImageView textoNota, intentosRestantes, winOrLose;
	private TextView textoJuego;
	private ImageButton repiteSecuencia, botonArr1, botonC, botonD, botonE, botonF, botonG, botonA, botonB, botonCag;
	private RelativeLayout botones1;
	private boolean trueFalse;
	private int fallos = 0;
	private int juego = 0;
	private Handler handler = new Handler();
		
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
		repiteSecuencia = (ImageButton)findViewById(R.id.buttonRepeat);
		intentosRestantes = (ImageView)findViewById(R.id.imageView2);
		winOrLose = (ImageView)findViewById(R.id.imageWinOrLose);
		botones1 = (RelativeLayout)findViewById(R.id.relayout1);
		botonArr1 = (ImageButton)findViewById(R.id.button1);
		botones1.setVisibility(View.INVISIBLE);
		Typeface fuenteJuego = Typeface.createFromAsset(getAssets(), "fonts/8bitwonder.ttf");
		textoJuego.setTypeface(fuenteJuego);
		textoJuego.setVisibility(View.INVISIBLE);
		
		botonC = (ImageButton)findViewById(R.id.buttonC);
		botonD = (ImageButton)findViewById(R.id.buttonD);
		botonE = (ImageButton)findViewById(R.id.buttonE);
		botonF = (ImageButton)findViewById(R.id.buttonF);
		botonG = (ImageButton)findViewById(R.id.buttonG);
		botonA = (ImageButton)findViewById(R.id.buttonA);
		botonB = (ImageButton)findViewById(R.id.buttonB);
		botonCag = (ImageButton)findViewById(R.id.buttonCag);
		
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
		
		arraySounds = new int[] {doc, red, mie, faf, solg, laa, sib, docag};
		
		showUserSettings();
		
	}
	     
//ButtonListener añadido por id en el xml.
public void clickHandler(View v)
{	
	int id = v.getId(); // Reconoce la id de los botones (asignada en el xml) para reproducir los sonidos.
    	
	switch (id)
	{
		case R.id.buttonC:
			idSound = doc;
			snd.play(arrayRandomSounds[0]);
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
		
		case R.id.buttonRepeat:
			snd.playAndShow(arrayImages, arrayRandomSounds, textoNota, botones1);
			fallos++;
			break;
			
		case R.id.button1:
			fallos=0;
			cont=0;
			winOrLose.setVisibility(View.INVISIBLE);
			textoJuego.setTextSize(0x00000001, 35);
			textoJuego.setVisibility(View.VISIBLE);
			textoJuego.setText(textRonda + " " + (juego+1));
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
	
		if(fallos>=1){
			repiteSecuencia.setVisibility(View.INVISIBLE);
		}else{
			repiteSecuencia.setVisibility(View.VISIBLE);
		}
	
		if(idSound !=0){
			trueFalse = snd.compareSounds(idSound, cont, arrayRandomSounds, textoNota, textoJuego, botones1, textAcierto, textFallo);
			if(trueFalse==true){
				cont++;
			}else{
				fallos++;
			}
			idSound=0;
		}
		juego=snd.manageFailures(fallos, intentosRestantes, textoJuego, juego, botones1, botonArr1, winOrLose, textPierdesJuego);
		idSound = 0;
		
		if (cont >= arrayRandomSounds.length){
			juego = snd.roundBeaten(juego, botones1, botonArr1, textoJuego, textoNota, winOrLose, textRondaSuperadaI, textRondaSuperadaII);
			cont = 0;		
		}
	}

	private void showUserSettings() {
		SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		int posId = preferences.getInt("POSICION_IDIOMA", 0);
		int posDif = preferences.getInt("POSICION_DIFICULTAD", 0);
		
		if (posDif==0){
			if(posId==1){
				arrayImages = new int[] {R.drawable.viewpred, R.drawable.notac, R.drawable.notad, R.drawable.notae, 
						R.drawable.notaf, R.drawable.notag, R.drawable.notaa, R.drawable.notab, R.drawable.notacag};
			}else{
				arrayImages = new int[] {R.drawable.viewpred, R.drawable.notado, R.drawable.notare, R.drawable.notami, 
						R.drawable.notafa, R.drawable.notasol, R.drawable.notala, R.drawable.notasi, R.drawable.notadoag};
			}
		}
		
		if (posId == 1){
			textRonda = "Round"; 
			textAcierto = "Correct!";
			textFallo = "Wrong!"; 
			textRondaSuperadaI = "Round"; 
			textRondaSuperadaII = "Beaten!";
			textPierdesJuego = "Game Over!";
			botonC.setBackgroundResource(R.drawable.notacestilo);
			botonD.setBackgroundResource(R.drawable.notadestilo);
			botonE.setBackgroundResource(R.drawable.notaeestilo);
			botonF.setBackgroundResource(R.drawable.notafestilo);
			botonG.setBackgroundResource(R.drawable.notagestilo);
			botonA.setBackgroundResource(R.drawable.notaaestilo);
			botonB.setBackgroundResource(R.drawable.notabestilo);
			botonCag.setBackgroundResource(R.drawable.notacagestilo);
		}else{
			textRonda = "Ronda " + juego; 
			textAcierto = "¡Acierto!";
			textFallo = "¡Fallo!"; 
			textRondaSuperadaI = "¡Ronda"; 
			textRondaSuperadaII = "Superada!";
			textPierdesJuego = "¡Has Perdido!";
			botonC.setBackgroundResource(R.drawable.botondoestilo);
			botonD.setBackgroundResource(R.drawable.botonreestilo);
			botonE.setBackgroundResource(R.drawable.botonmiestilo);
			botonF.setBackgroundResource(R.drawable.botonfaestilo);
			botonG.setBackgroundResource(R.drawable.botonsolestilo);
			botonA.setBackgroundResource(R.drawable.botonlaestilo);
			botonB.setBackgroundResource(R.drawable.botonsiestilo);
			botonCag.setBackgroundResource(R.drawable.botondoagestilo);
		}
	}

}


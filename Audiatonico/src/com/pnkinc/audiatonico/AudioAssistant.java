package com.pnkinc.audiatonico;

import java.util.Random;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AudioAssistant 
{
	
	// Creamos las variables que utilizaremos en los m�todos de la clase.
	// Las dos primeras corresponden al contexto de la aplicaci�n y al objeto de la clase SoundPool 
	// que utilizaremos, respectivamente.
	// La siguiente, rate, equivale a la velocidad a la que se reproducir� el sonido. Como ser� la 
	// velocidad normal, le asignamos 1.0f (dado que el valor ha de ser de tipo float).
	// Las siguientes son, como su propio nombre indica, los vol�menes izquierdo y derecho. Tambi�n
	// las inicializamos a 1.0f, aunque quiz� se les suba en el futuro, dependiendo de c�mo suenen en
	// las pruebas que se realicen de la aplicaci�n.
	private Context soundContext;
	private SoundPool soundPool;
	private float rate = 1.0f;
	private float leftVolume = 1.0f;
	private float rightVolume = 1.0f;
	private Handler handler = new Handler();
	private Random rnd = new Random();
	private int k = 0;
	boolean aciertoFallo=true;
	

	// Constructor de la clase, con los ajustes del manejador de sonidos de la aplicaci�n.
	// Le pasamos el contexto de la aplicaci�n, y le decimos el n�mero de sonidos m�ximo
	// que se solapar�n al reproducirse (8, al menos por el momento), el tipo de stream de audio que 
	// utilizaremos(STREAM_MUSIC en este caso, que es el que normalmente utilizan los juegos) y el
	// srcQuality, que determinar�a la calidad del sonido, pero que mantengo en cero porque, seg�n
	// la API, no tiene efecto por el momento, as� que lo mantengo en el valor por defecto.
	public AudioAssistant(Context appContext)
	{
		soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
		soundContext = appContext;
	}

	// Este m�todo es el utilizado para cargar los sonidos.
	// Le pasamos el contexto de la aplicaci�n, su id (por ejemplo, R.raw.do) y la prioridad del sonido.
	// La prioridad del sonido tampoco tiene efecto por el momento, pero seg�n la API es recomendable
	// mantenerlo en 1 por futuras compatibilidades.	
	// Lo �nico que le pasaremos a �ste m�todo al llamarlo ser� la id del sonido para poder cargarla en una variable.
	public int load(int sound_id)
	{
		return soundPool.load(soundContext, sound_id, 1);
	}

	// Este m�todo ser� el utilizado para reproducir los sonidos que hayamos cargado.
	// Los par�metros con los que funciona dicho m�todo son la id del sonido, el volumen 
	// (tanto el izquierdo como el derecho), la prioridad (que mantenemos con el valor 1),
	// un atributo loop (que dejamos a 0 para que no haya loop de ning�n tipo) y la velocidad
	// a la que se reproduce el sonido. Se utilizar�n las variables ya creadas en esta clase
	// para el volumen izquierdo y derecho y la velocidad y se forzar�n los valores 1 y 0 para 
	// la prioridad y el loop respectivamente. Lo �nico que enviaremos al llamar al m�todo ser� 
	// la id del sonido.
	public int play(int sound_id)
	{
		soundPool.play(sound_id, leftVolume, rightVolume, 1, 0, rate); 
		return sound_id;
	}	
	
	// M�todo para reproducir el sonido y mostrar su imagen asociada.
	public void playForShow(int sound_id, ImageView image, int[] arrayImages)
	{
		image.setImageResource(arrayImages[sound_id]);
		soundPool.play(sound_id, leftVolume, rightVolume, 1, 0, rate);
	}	
	
	// Este m�todo fue creado para parar los sonidos que se reproducen. Funciona con un valor
	// de tipo entero, el cual se recoge en la otra clase en el momento de reproducirlo. De 
	// momento no ha sido probado, dado que los problemas de loops y sonidos que no paraban de
	// reproducirse fueron soulicionados con el m�todo releaseAllSounds (creado justo debajo de �ste).
	// Su funcionamiento es sencillo, utiliza la variable recogida en el momento de reproducir un sonido
	// para detener su reproducci�n. Un ejemplo sencillo es:
	// int pararid = snd.play(sonido1);
	// E inmediatamente desde otro bot�n utilizamos el m�todo:
	// stopSound(pararid);
	public void stopSound(int i){
		soundPool.stop(i);
	}

	// M�todo creado para liberar todos los sonidos cargados previamente en memoria. 
	// Su uso de momento ha sido para pruebas y poco m�s.
	// Dado que en el futuro podr�a resultarme �til, el m�todo permanecer� en la clase.
	public void releaseAllSounds()
	{
		soundPool.release();		
	}
	
	// M�todo creado para que el array que reproducir� los sonidos a repetir de la aplicaci�n coja
	// sonidos al azar del array que contiene todos los sonidos en general.
	public int getRandomSound(int[] array) {
		int auxval = rnd.nextInt(array.length);
		return array[auxval];
	}	
	
	public int[] fillArrays(int gameCont, int[] arraySounds, int[] arrayRandomSounds){
		if(gameCont==0){
			arrayRandomSounds = new int[2];	
			for(int i=0; i<arrayRandomSounds.length; i++){
				arrayRandomSounds[i]=getRandomSound(arraySounds);
			}
		}else{
			int j = arrayRandomSounds.length+2;
			int[] arrayAuxiliar = new int[arrayRandomSounds.length];
			System.arraycopy(arrayRandomSounds, 0, arrayAuxiliar, 0, arrayRandomSounds.length);
			arrayRandomSounds = new int[j];
			for(int i=0; i<arrayRandomSounds.length; i++){
				if(i < arrayAuxiliar.length){
					arrayRandomSounds[i] = arrayAuxiliar[i];
				}else{
					arrayRandomSounds[i]=getRandomSound(arraySounds);
				}
			}
		}
		return arrayRandomSounds;
	}
	
	public int roundBeaten(int juego){
		juego++;
		return juego;
	}
	
	public void playAndShow(final int[] arrayImagenes, final int[] arraySonidosRnd, 
			final ImageView imageNota, final RelativeLayout relayoutAux){
		if(k < arraySonidosRnd.length){
			handler.postDelayed(new Runnable() { 
				public void run() {
					playForShow(arraySonidosRnd[k], imageNota, arrayImagenes);
					k++;
					playAndShow(arrayImagenes, arraySonidosRnd, imageNota, relayoutAux); 
				} 
			}, 1500);
		}else{
			handler.postDelayed(new Runnable() { 
				public void run() {
					k=0;
					imageNota.setImageResource(R.drawable.viewpred);
					relayoutAux.setVisibility(View.VISIBLE);
				} 
			}, 1500);
		}
	}
	
	public boolean compareSounds (int soundIdSent, int arrayIterator, int[] arrayOfSounds){
		if(soundIdSent == arrayOfSounds[arrayIterator]){
			aciertoFallo = true;
		}else{
			aciertoFallo = false;
		}
		return aciertoFallo;
	}
	
	public int[] instrumentChooser(int instrId){
		int[] arrayInstrument;
		int doaux, reaux, miaux, faaux, solaux, laaux, siaux, doagaux;
		
		if(instrId == 1){
			doaux = load(R.raw.guiacdo);
			reaux = load(R.raw.guiacre);
			miaux = load(R.raw.guiacmi);
			faaux = load(R.raw.guiacfa);
			solaux = load(R.raw.guiacsol);
			laaux = load(R.raw.guiacla);
			siaux = load(R.raw.guiacsi);
			doagaux = load(R.raw.guiacdoag);
		}else if(instrId == 2){
			doaux = load(R.raw.guieldo);
			reaux = load(R.raw.guielre);
			miaux = load(R.raw.guielmi);
			faaux = load(R.raw.guielfa);
			solaux = load(R.raw.guielsol);
			laaux = load(R.raw.guiella);
			siaux = load(R.raw.guielsi);
			doagaux = load(R.raw.guieldoag);
		}else if(instrId == 3){
			doaux = load(R.raw.baacdo);
			reaux = load(R.raw.baacre);
			miaux = load(R.raw.baacmi);
			faaux = load(R.raw.baacfa);
			solaux = load(R.raw.baacsol);
			laaux = load(R.raw.baacla);
			siaux = load(R.raw.baacsi);
			doagaux = load(R.raw.baacdoag);
		}else if(instrId == 4){
			doaux = load(R.raw.baeldo);
			reaux = load(R.raw.baelre);
			miaux = load(R.raw.baelmi);
			faaux = load(R.raw.baelfa);
			solaux = load(R.raw.baelsol);
			laaux = load(R.raw.baella);
			siaux = load(R.raw.baelsi);
			doagaux = load(R.raw.baeldoag);
		}else{
			doaux = load(R.raw.piado);
			reaux = load(R.raw.piare);
			miaux = load(R.raw.piami);
			faaux = load(R.raw.piafa);
			solaux = load(R.raw.piasol);
			laaux = load(R.raw.piala);
			siaux = load(R.raw.piasi);
			doagaux = load(R.raw.piadoag);
		}
		
		arrayInstrument = new int[]{doaux, reaux, miaux, faaux, solaux, laaux, siaux, doagaux};
		
		return arrayInstrument;
	}
}


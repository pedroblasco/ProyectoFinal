package com.pnkinc.audiatonico;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Juego extends Activity 
{
	private int[] arraySounds, arrayRandomSounds, arrayImages, arrayAux2Play;
	private int dif = 0, cont = 0, idSound = 0, gameValue = 0, HAS_STARTED = 0, JUST_CREATED = 0, 
			fallos = 0, pow = 0, playerControl = 0, playerNum = 0, juego = 0, dosJug = 1, 
			player1cont = 0, player2cont = 0, player1fin = 0, player2fin = 0;
	String textRonda, textAcierto, textFallo, textRondaSuperadaI, textRondaSuperadaII, 
		textPierdesJuego, textContPlayer, textInput, textNumNot, textPlay, textGameWon,
		guardaPuntu, nomPlayerUno, nomPlayerDos;  
	private ImageView textoNota, intentosRestantes, winOrLose;
	private TextView textoJuego;
	private ImageButton repiteSecuencia, botonArr1, botonC, botonD, botonE, botonF, 
			botonG, botonA, botonB, botonCag;
	private RelativeLayout botones1;
	private boolean trueFalse;
	private Handler handler = new Handler();
	private SQLiteDatabase db;
	
	AudioAssistant snd;
	
	int doc, red, mie, faf, solg, laa, sib, docag;
	int streamID;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		
	    Bundle intent = getIntent().getExtras();
	    gameValue = intent.getInt("tipojuego");
	    
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
	     
		textoJuego = (TextView)findViewById(R.id.textView1);
		textoNota = (ImageView)findViewById(R.id.imageView1);
		repiteSecuencia = (ImageButton)findViewById(R.id.buttonRepeat);
		intentosRestantes = (ImageView)findViewById(R.id.imageView2);
		winOrLose = (ImageView)findViewById(R.id.imageWinOrLose);
		botones1 = (RelativeLayout)findViewById(R.id.relayout1);
		botonArr1 = (ImageButton)findViewById(R.id.button1);
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
		
		if(gameValue == 0){
			botones1.setVisibility(View.INVISIBLE);
		}else{
			botonArr1.setVisibility(View.INVISIBLE);
			botones1.setVisibility(View.INVISIBLE);
			repiteSecuencia.setVisibility(View.INVISIBLE);
			textoJuego.setVisibility(View.VISIBLE);
		}
		
		//Creamos la instancia del SoundManager.
		snd = new AudioAssistant(getApplicationContext());
		
		//Asignamos el volumen multimedia a la aplicación.
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//Cargamos los sonidos y los asignamos a los int para utilizarlos como index o como simples identificadores.
		arraySounds = new int[] {doc, red, mie, faf, solg, laa, sib, docag};
		
		dif = showUserSettings(dif);	
		
		if(gameValue == 1){
			if(playerControl % 2 == 0){
				playerNum = 1;
			}else{
				playerNum = 2;
			}
			
			mensaje2Players();
		}
	}
	     
//ButtonListener añadido por id en el xml.
public void clickHandler(View v)
{	
	int id = v.getId(); // Reconoce la id de los botones (asignada en el xml) para reproducir los sonidos.

	switch (id)
	{
		case R.id.buttonC:
			idSound = snd.play(arraySounds[0]);
			break;
		    	  
		case R.id.buttonD:
			idSound = snd.play(arraySounds[1]);
			break;
		    	   
		case R.id.buttonE:
			idSound = snd.play(arraySounds[2]);
			break;    	  
		       	  
		case R.id.buttonF:
			idSound = snd.play(arraySounds[3]);
			break;    	  
		          	  
		case R.id.buttonG:
			idSound = snd.play(arraySounds[4]);
			break;    	  
		          	  
		case R.id.buttonA:
			idSound = snd.play(arraySounds[5]);
			break;    
		         
		case R.id.buttonB:
			idSound = snd.play(arraySounds[6]);
			break;
			
		case R.id.buttonCag:
			idSound = snd.play(arraySounds[7]);
			break;
		
		case R.id.buttonRepeat:
			snd.playAndShow(arrayImages, arrayRandomSounds, textoNota, botones1);
			fallos++;
			break;
			
		case R.id.button1:
				fallos=0;
				cont=0;
				idSound=0;
				winOrLose.setVisibility(View.INVISIBLE);
				textoJuego.setTextSize(0x00000001, 35);
				botonArr1.setVisibility(View.INVISIBLE); 
			if(gameValue == 0){
				textoJuego.setVisibility(View.VISIBLE);
				textoJuego.setText(textRonda + " " + (juego+1));
				arrayRandomSounds = snd.fillArrays(juego, arraySounds, arrayRandomSounds);
				handler.postDelayed(new Runnable() { 
					public void run() {
						textoJuego.setVisibility(View.INVISIBLE);
					} 
				}, 1300);
				snd.playAndShow(arrayImages, arrayRandomSounds, textoNota, botones1);
				HAS_STARTED = 1;
				break;
			}else{
				handler.postDelayed(new Runnable() { 
					public void run() {
						textoJuego.setVisibility(View.VISIBLE);
						textoJuego.setText(textContPlayer + playerNum);
						handler.postDelayed(new Runnable() { 
							public void run() {
								textoJuego.setText(textPlay);
								handler.postDelayed(new Runnable() { 
									public void run() {
										textoJuego.setVisibility(View.INVISIBLE);
										snd.playAndShow(arrayImages, arrayRandomSounds, textoNota, botones1);
									} 
								}, 1300);
							} 
						}, 1300);
					} 
				}, 1300);
				HAS_STARTED = 1;
				break;
			}
		}
	
	if(gameValue == 1){
		if(playerControl % 2 == 0){
			playerNum = 1;
		}else{
			playerNum = 2;
		}
	}
		
    if(gameValue==1 && JUST_CREATED == 0){
    	arrayAux2Play = new int[(dosJug * 4) + 10];
    	
    }
    
    JUST_CREATED = 1;
	
	if(gameValue == 1 && HAS_STARTED == 0){
		if(id !=0){
			if(idSound !=0){
				arrayAux2Play[pow] = idSound;
			}
			pow++;
			if(pow == (dosJug * 4)){
				arrayRandomSounds = new int[dosJug * 4];
				System.arraycopy(arrayAux2Play, 0, arrayRandomSounds, 0, arrayRandomSounds.length);
				handler.postDelayed(new Runnable() { 
					public void run() {
						textoJuego.setVisibility(View.INVISIBLE);
						botonArr1.setVisibility(View.VISIBLE);
						botones1.setVisibility(View.INVISIBLE);
						idSound=0;
						playerControl++;
					} 
				}, 600);
			}
		}
	}
	
	if(HAS_STARTED !=0){
		if(idSound !=0){
			trueFalse = snd.compareSounds(idSound, cont, arrayRandomSounds);
			if(trueFalse==true){
				textoJuego.setTextColor(Color.parseColor("#18FF08"));
				textoJuego.setText(textAcierto);
				textoJuego.setVisibility(View.VISIBLE);
				handler.postDelayed(new Runnable() { 
					public void run() {
						textoJuego.setTextColor(Color.parseColor("#ffffff"));
						textoJuego.setText("");
						textoJuego.setVisibility(View.INVISIBLE);
						textoNota.setImageResource(R.drawable.viewpred);
					}
				}, 400);
				cont++;
			}else{
				textoJuego.setTextColor(Color.parseColor("#FF1010"));
				textoJuego.setText(textFallo);
				textoJuego.setVisibility(View.VISIBLE);
				handler.postDelayed(new Runnable() { 
					public void run() {
						textoJuego.setTextColor(Color.parseColor("#ffffff"));
						textoJuego.setText("");
						textoJuego.setVisibility(View.INVISIBLE);
						textoNota.setImageResource(R.drawable.viewpred);
					}
				}, 400);
				fallos++;
			}
			idSound=0;
		}
		if(gameValue == 1){
			if(fallos > 2){
				pow = 0;
				dosJug = 1;
				player1cont = 0;
				player2cont = 0;
				JUST_CREATED = 0;
				HAS_STARTED = 0;
			}
		}
		else{
			if(fallos > 2){
				player1cont = 0;
				HAS_STARTED = 0;
			}
		}
		
		//FALLOS:
		if(fallos==1){
			intentosRestantes.setImageResource(R.drawable.heartintentosdos);
		}else if(fallos==2){
			intentosRestantes.setImageResource(R.drawable.heartintentosuno);
		}else if(fallos==3){
			juego=0;
			intentosRestantes.setImageResource(R.drawable.heartintentoscero);
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() { 
				public void run() {
					botones1.setVisibility(View.INVISIBLE);
					textoJuego.setVisibility(View.VISIBLE);
					textoJuego.setTextSize(0x00000001, 25);
					textoJuego.setText(textPierdesJuego);
					winOrLose.setImageResource(R.drawable.skull);
					winOrLose.setVisibility(View.VISIBLE);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() { 
						public void run() {
							if(gameValue == 0){
								botonArr1.setVisibility(View.VISIBLE);
							}else{
								botones1.setVisibility(View.VISIBLE);
								winOrLose.setVisibility(View.INVISIBLE);
								textoJuego.setVisibility(View.INVISIBLE);
							}
						}
					}, 2000);
				}
			}, 1000);
		}else{
			intentosRestantes.setImageResource(R.drawable.heartintentosfull);
		}
		
		idSound = 0;
			
		if (cont >= arrayRandomSounds.length){
			if(gameValue == 1){
				botones1.setVisibility(View.INVISIBLE);
			}
			
			juego = snd.roundBeaten(juego);
			
			handler.postDelayed(new Runnable() { 
				public void run() {
					botones1.setVisibility(View.INVISIBLE);
					textoJuego.setVisibility(View.VISIBLE);
					winOrLose.setImageResource(R.drawable.trophy);
					winOrLose.setVisibility(View.VISIBLE);
					if(gameValue == 0){
						textoJuego.setTextSize(0x00000001, 20);
						textoJuego.setText(textRondaSuperadaI + " " + (juego) + " " + textRondaSuperadaII);
					}else{
						textoJuego.setTextSize(0x00000001, 20);
						textoJuego.setText(textGameWon);
					}
					handler.postDelayed(new Runnable() { 
						public void run() {
							textoNota.setImageResource(R.drawable.viewpred);
							if(gameValue == 0){
								botonArr1.setVisibility(View.VISIBLE);
							}
						}
					}, 2000);
				}
			}, 1000);
			
			if(gameValue == 1){
				pow = 0;
				if(playerNum == 1){
					player1cont++;
					if(player1cont > player1fin){
						player1fin = player1cont;
					}
				}
				else if(playerNum == 2){
					player2cont++;
					if(player2cont > player2fin){
						player2fin = player2cont;
					}
				}
				
				if(juego % 2 == 0){
					dosJug++;
				}
				
				handler.postDelayed(new Runnable() { 
					public void run() {
							mensaje2Players();
						}
				}, 3000);
				idSound = 0;
				HAS_STARTED = 0;
				JUST_CREATED = 0;
			}else{
				player1cont++;
				if(player1cont > player1fin){
					player1fin = player1cont;
				}
			}
			cont = 0;
			HAS_STARTED = 0;
		}
		
		if(fallos>1){
			repiteSecuencia.setVisibility(View.INVISIBLE);
		}else{
			repiteSecuencia.setVisibility(View.VISIBLE);
		}
	}
}
	public void mensaje2Players(){
		textoJuego.setTextSize(0x00000001, 35);
		winOrLose.setVisibility(View.INVISIBLE);
		textoJuego.setText(textContPlayer + playerNum); 
		handler.postDelayed(new Runnable() { 
			public void run() {
				textoJuego.setText(textInput);
		    	handler.postDelayed(new Runnable() { 
		    		public void run() {
		    			textoJuego.setText(dosJug * 4 + textNumNot);
		    	    	handler.postDelayed(new Runnable() { 
		    	    		public void run() {
		    	    			textoJuego.setVisibility(View.INVISIBLE);
		    	    			botones1.setVisibility(View.VISIBLE);
		    	    			botonArr1.setVisibility(View.INVISIBLE);
		    	    		} 
		    	    	}, 1300);
		    		} 
		    	}, 1300);
			} 
		}, 1300);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    /*if (keyCode == KeyEvent.KEYCODE_BACK) {
	        snd.releaseAllSounds();
	    	finish();*/
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			
			    public void onClick(DialogInterface dialog, int which) {
			        switch(which){
			        case DialogInterface.BUTTON_POSITIVE:
			            //Yes button clicked
				        if(gameValue == 0){
		                	if(player1fin > 0){
		                		db.execSQL("INSERT INTO SinglePlayer (dif, rounds) VALUES ('"+dif+"', '"+player1fin+"')");
			                	snd.releaseAllSounds();
			                	finish();
		                	}else{
			                	snd.releaseAllSounds();
			                	finish();
		                	}
		                	break;
			        	}else{
			        		if(player1fin > 0 || player2fin > 0){
				        		new AlertDialog.Builder(Juego.this)
					            .setMessage("¿Desea Guardar La Puntuación?")
					            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int whichButton) {
					                	if(player1fin > 0){
					                		introduceNombreP1();
					                	}else{
					                		introduceNombreP2();
					                	}
						        		
					                }
					            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int whichButton) {
					                	snd.releaseAllSounds();
					                	finish();
					                }
					            }).show();
				        		break;
			        		}else{
			                	snd.releaseAllSounds();
			                	db.close();
			                	finish();
			        		}
				        	break;
			        	}
			           
			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			        	//Do Nothing
			            break;
			        }
			    }
			    
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(" " + guardaPuntu).setPositiveButton("OK", dialogClickListener)
			    .setNegativeButton("Cancel", dialogClickListener).show();
		}
			
	    return super.onKeyDown(keyCode, event);
	}
	
	public void introduceNombreP1(){
		final EditText input = new EditText(this);
		input.setText("");
		AlertDialog.Builder build = new AlertDialog.Builder(Juego.this)
        .setMessage(" " + nomPlayerUno)
        .setView(input)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
                String nombrePlayer = input.getText().toString();
                
                if(nombrePlayer.toString().length() == 0 || nombrePlayer.startsWith(" ")){
                	introduceNombreP1();
                }else{
	                db.execSQL("INSERT INTO MultiPlayer (nombre, dif, rounds) VALUES ('"+nombrePlayer+"', '"+dif+"', '"+player1fin+"')");
	                if(player2fin > 0){
	                	introduceNombreP2();
	                }else{
	                	snd.releaseAllSounds();
	                	db.close();
	                	finish();
	                }
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(player2fin > 0){
                	introduceNombreP2();
                }else{
                	snd.releaseAllSounds();
                	db.close();
                	finish();
                }
            }
        });
		AlertDialog alert = build.create();
		alert.show();
	}
	
	public void introduceNombreP2(){
		final EditText input = new EditText(this);
		AlertDialog.Builder build = new AlertDialog.Builder(Juego.this)
        .setMessage(" " + nomPlayerDos)
        .setView(input)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
                String nombrePlayer = input.getText().toString();
                
                if(nombrePlayer.toString().length() == 0 || nombrePlayer.startsWith(" ")){
                	introduceNombreP2();
                }else{
	                	db.execSQL("INSERT INTO MultiPlayer (nombre, dif, rounds) VALUES ('"+nombrePlayer+"', '"+dif+"', '"+player2fin+"')");
	                	snd.releaseAllSounds();
	                finish();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	snd.releaseAllSounds();
            	db.close();
            	finish();
            }
        });
		AlertDialog alert = build.create();
		alert.show();
	}
	
	private int showUserSettings(int posDif) {
		SharedPreferences preferences = getSharedPreferences("AudiaPrefs",Context.MODE_PRIVATE);
		int posId = preferences.getInt("POSICION_IDIOMA", 0);
		posDif = preferences.getInt("POSICION_DIFICULTAD", 0);
		int posIns = preferences.getInt("POSICION_INSTRUMENTO",  0);
		
		arraySounds = snd.instrumentChooser(posIns);
		
		if (posDif==0){
			if(posId==1){
				arrayImages = new int[] {R.drawable.viewpred, R.drawable.notac, R.drawable.notad, R.drawable.notae, 
						R.drawable.notaf, R.drawable.notag, R.drawable.notaa, R.drawable.notab, R.drawable.notacag};
			}else{
				arrayImages = new int[] {R.drawable.viewpred, R.drawable.notado, R.drawable.notare, R.drawable.notami, 
						R.drawable.notafa, R.drawable.notasol, R.drawable.notala, R.drawable.notasi, R.drawable.notadoag};
			}
		}else if(posDif==1){
			arrayImages = new int[] {R.drawable.viewpred, R.drawable.notadopic, R.drawable.notarepic, R.drawable.notamipic, 
				R.drawable.notafapic, R.drawable.notasolpic, R.drawable.notalapic, R.drawable.notasipic, R.drawable.notadoagpic};
		}else{
			arrayImages = new int[] {R.drawable.viewpred, R.drawable.notamaxdif, R.drawable.notamaxdif, R.drawable.notamaxdif, 
					R.drawable.notamaxdif, R.drawable.notamaxdif, R.drawable.notamaxdif, R.drawable.notamaxdif, 
					R.drawable.notamaxdif};
		}
		
		if (posId == 1){
			textRonda = "Round"; 
			textAcierto = "Correct!";
			textFallo = "Wrong!"; 
			textRondaSuperadaI = "Round"; 
			textRondaSuperadaII = "Beaten!";
			textPierdesJuego = "Game Over!";
			textContPlayer = "Player "; 
			textInput = "Input";
			textNumNot = " Notes";
			textPlay = "Play!";
			textGameWon = "Well Done!";
			guardaPuntu = "Exit?";
			nomPlayerUno = "Player One's Name:";
			nomPlayerDos = "Player Two's Name:";
			botonC.setBackgroundResource(R.drawable.boton_c_estilo);
			botonD.setBackgroundResource(R.drawable.boton_d_estilo);
			botonE.setBackgroundResource(R.drawable.boton_e_estilo);
			botonF.setBackgroundResource(R.drawable.boton_f_estilo);
			botonG.setBackgroundResource(R.drawable.boton_g_estilo);
			botonA.setBackgroundResource(R.drawable.boton_a_estilo);
			botonB.setBackgroundResource(R.drawable.boton_b_estilo);
			botonCag.setBackgroundResource(R.drawable.boton_cag_estilo);
		}else{
			textRonda = "Ronda"; 
			textAcierto = "¡Acierto!";
			textFallo = "¡Fallo!"; 
			textRondaSuperadaI = "¡Ronda"; 
			textRondaSuperadaII = "Superada!";
			textPierdesJuego = "¡Has Perdido!";
			textContPlayer = "Jugador "; 
			textInput = "Introduce";
			textNumNot = " Notas";
			textPlay = "¡Juega!";
			textGameWon = "¡Bien Hecho!";
			guardaPuntu = "¿Salir?";
			nomPlayerUno = "Nombre del Jugador Uno:";
			nomPlayerDos = "Nombre del Jugador Dos:";
			botonC.setBackgroundResource(R.drawable.boton_do_estilo);
			botonD.setBackgroundResource(R.drawable.boton_re_estilo);
			botonE.setBackgroundResource(R.drawable.boton_mi_estilo);
			botonF.setBackgroundResource(R.drawable.boton_fa_estilo);
			botonG.setBackgroundResource(R.drawable.boton_sol_estilo);
			botonA.setBackgroundResource(R.drawable.boton_la_estilo);
			botonB.setBackgroundResource(R.drawable.boton_si_estilo);
			botonCag.setBackgroundResource(R.drawable.boton_doag_estilo);
		}
		
		return posDif;
	}
}


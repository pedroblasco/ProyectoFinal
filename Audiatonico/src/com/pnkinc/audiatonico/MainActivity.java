package com.pnkinc.audiatonico;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button boton1;
		boton1 = (Button) findViewById(R.id.buttonGame);
		
        boton1.setOnClickListener(new View.OnClickListener() {
            
	        public void onClick(View view) {
	            Intent intent = new Intent(MainActivity.this, Juego.class);
	            startActivity(intent);
	        }

        });
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

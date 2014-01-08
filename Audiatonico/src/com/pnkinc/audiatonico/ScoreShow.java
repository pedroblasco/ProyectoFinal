package com.pnkinc.audiatonico;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ScoreShow extends ListActivity{
	private SQLiteDatabase db;
	private SimpleCursorAdapter cursorAdaptador;
	private static final String campos[] = { "nombre", "rounds", BaseColumns._ID };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface fuenteJuego = Typeface.createFromAsset(getAssets(), "fonts/8bitwonder.ttf");
        
		AudiatonicoSQLiteHelper playDataB = new AudiatonicoSQLiteHelper(this, "DBPlayers", null, 1);
		db = playDataB.getWritableDatabase();
        
		Cursor data = db.query("MultiPlayer", campos, null, null, null, null,
				"rounds DESC");

		cursorAdaptador = new SimpleCursorAdapter(this, R.layout.row, data, campos,
				new int[] { R.id.nombre, R.id.rounds });
		
		cursorAdaptador.setViewBinder(new SimpleCursorAdapter.ViewBinder(){

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				if(columnIndex == 0){
					TextView textNom = (TextView) view;
					textNom.setTypeface(fuenteJuego);
					textNom.setTextColor(Color.WHITE);
					textNom.setTextSize(0x00000001, 15);
				}else if(columnIndex == 1){
					TextView textRond = (TextView) view;
					textRond.setTypeface(fuenteJuego);
					textRond.setTextColor(Color.WHITE);
					textRond.setTextSize(0x00000001, 15);
				}
				return false;
			}
			
		});
		
		TextView tView = new TextView(this);
        tView.setText("Ranking:");
        tView.setTypeface(fuenteJuego);
        tView.setTextColor(Color.RED);
        tView.setGravity(Gravity.CENTER);
        tView.setTextSize(0x00000001, 25);
        
		ListView listView = getListView();
		listView.setBackgroundColor(Color.BLACK);
		listView.setCacheColorHint(Color.BLACK);
		listView.setSelector(R.color.color);
		listView.setDivider(null);
		listView.setDividerHeight(5);
		listView.addHeaderView(tView);

		setListAdapter(cursorAdaptador); 
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
    		db.close();
    		finish();
    	}
	    return super.onKeyDown(keyCode, event);
	}
}

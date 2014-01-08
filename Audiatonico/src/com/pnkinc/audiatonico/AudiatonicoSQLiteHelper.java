package com.pnkinc.audiatonico;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.provider.BaseColumns;

public class AudiatonicoSQLiteHelper extends SQLiteOpenHelper{
	public AudiatonicoSQLiteHelper(Context contexto, String nombre, CursorFactory almacen, int version){
		super(contexto, nombre, almacen, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.execSQL("DROP TABLE SinglePlayer");
		db.execSQL("CREATE TABLE SinglePlayer ("
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, dif INT, rounds INT)");
		
		//db.execSQL("DROP TABLE MultiPlayer");
		db.execSQL("CREATE TABLE MultiPlayer ("
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR, dif INT, rounds INT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}

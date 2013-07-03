package com.example.treasury;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
	public static final int MAX_RECORDS_COUNT = 5;
	public static final String DB_NAME = "treasuryDB";		
	public static final String TABLE_NAME = "treasury";	
	public static final String KEY_ID = "_id";
	public static final String AI = "ai";	
	public static final String SIZE = "size";	
	public static final String COURSE = "course";			
	public static final String WINNER = "winner";
	public static final String WINNER_COUNT = "winner_count";	
	public static final String LOSER = "loser";		
	public static final String LOSER_COUNT = "loser_count";	
	public static final String DATE_VICTORY = "date_victory";			
	
	public DbOpenHelper(Context context) {
		super(context, DB_NAME, null, 3);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// sql-запрос для создания таблицы
		final String CREATE_DB = "CREATE TABLE " + TABLE_NAME + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ AI + " TEXT NOT NULL,"
				+ SIZE + " TEXT NOT NULL,"
				+ COURSE + " INTEGER NOT NULL,"
				+ WINNER + " TEXT NOT NULL,"
				+ WINNER_COUNT + " INTEGER NOT NULL,"
				+ LOSER + " TEXT,"
				+ LOSER_COUNT + " INTEGER,"
				+ DATE_VICTORY + " REAL NOT NULL" + ");";
		db.execSQL(CREATE_DB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}

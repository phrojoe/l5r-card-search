package org.frojoe.l5rSearch.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardStorageHelper extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "L5RCARDS"
							 , TABLE_NAME = "CARDS"
							 , TITLE = "TITLE"
							 , LINK = "LINK"
						 	 , IMGURL = "IMGURL"
						 	 , DATA = "DATA";
	
	public static final int TITLE_INDX = 0
						  , LINK_INDX = 1
						  , IMGURL_INDX = 2
						  , DATA_INDX = 3;
	
	public static final String[] ALL_COLUMNS = {TITLE, LINK, IMGURL, DATA};
	
	private static final int DATABASE_VERSION = 2;
	private static final String TABLE_CREATE = 
			"CREATE TABLE " + TABLE_NAME + " (" +
			"TITLE TEXT, " +
			"LINK TEXT, " +
			"IMGURL TEXT, " +
			"DATA TEXT);";
	
	public CardStorageHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//not implemented
	}
	
}

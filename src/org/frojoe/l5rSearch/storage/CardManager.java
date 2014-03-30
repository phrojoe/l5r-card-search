package org.frojoe.l5rSearch.storage;

import java.util.ArrayList;

import org.frojoe.l5rSearch.Card;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CardManager {

	public static final int READ = 0;
	public static final int WRITE = 1;
	
	private SQLiteDatabase db;
	
	public CardManager(CardStorageHelper helper, int mode) {
		switch(mode) {
			case READ:
				db = helper.getReadableDatabase();
				break;
			case WRITE:
				db = helper.getWritableDatabase();
				break;
		}
	}
	
	public boolean saveCard(Card card) throws JSONException {
		deleteCard(card);
		ContentValues values = new ContentValues();
		values.put("TITLE", card.getTitle());
		values.put("LINK", card.getLink());
		values.put("IMGURL", card.getImgUrl());
		JSONObject json = new JSONObject(card.getData());
		values.put("DATA", json.toString());
		long r = db.insert(CardStorageHelper.TABLE_NAME,null,values);
		return r != -1;
	}
	
	public boolean deleteCard(Card card) {
		int r = db.delete(CardStorageHelper.TABLE_NAME, 
				CardStorageHelper.TITLE + "=?",new String[] {card.getTitle()});
		return r > 0;
	}
	
	public boolean hasCard(Card card) {
		if (card != null) {
			Cursor c = db.query(CardStorageHelper.TABLE_NAME,
					new String[] {CardStorageHelper.TITLE}, 
					CardStorageHelper.TITLE + "=?", 
					new String[] {card.getTitle()}, null, null, null);
			boolean hasCard = c.moveToFirst();
			c.close();
			return hasCard;
		} else
			return false;
	}
	
	public ArrayList<Card> getCards() throws JSONException {
		ArrayList<Card> cards = new ArrayList<Card>();
		Cursor cursor = db.query(CardStorageHelper.TABLE_NAME, 
				CardStorageHelper.ALL_COLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			cards.add(cardFromCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return cards;
	}
	
	public void close() {
		db.close();
	}
	
	private Card cardFromCursor(Cursor cursor) throws JSONException {
		Card card = new Card();
		card.setTitle(cursor.getString(CardStorageHelper.TITLE_INDX));
		card.setLink(cursor.getString(CardStorageHelper.LINK_INDX));
		card.setImgUrl(cursor.getString(CardStorageHelper.IMGURL_INDX));
		JSONObject json = 
			new JSONObject(cursor.getString(CardStorageHelper.DATA_INDX));	
		card.setData(json);
		return card;
	}
}

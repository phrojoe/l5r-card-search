package org.frojoe.l5rSearch;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.frojoe.l5rSearch.util.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {

    public static final Pattern CARDID_PATTERN =
            Pattern.compile("cardid=(\\d+)");

	private String title;
	private String link;
	private String imgUrl;
	private Map<String,String> data;
	private Bitmap image;
	
	public Card() {
		data = new TreeMap<String,String>();
	}
	
	public Card(String title, String link, String imgUrl) {
		this.title = title;
		this.link = link;
		this.imgUrl = imgUrl;
		data = new TreeMap<String,String>();
	}
	
	public void clone(Card anotherCard) {
		title = anotherCard.getTitle();
		link = anotherCard.getLink();
		imgUrl = anotherCard.getImgUrl();
		image = anotherCard.getImage();
		data = new TreeMap<String,String>();
		data.putAll(anotherCard.getData());
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
        Matcher m = CARDID_PATTERN.matcher(link);
        if (m.find())
            this.link = "#cardid=" + m.group(1);
        else
		    this.link = link;
	}

	public Map<String,String> getData() {
		return data;
	}

	public void setData(Map<String,String> data) {
		this.data = data;
	}
	
	public void setData(JSONObject json) throws JSONException {
		Iterator keys = json.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			data.put(key, json.getString(key));
		}
	}
	
	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	public void put(String key, String value) {
		data.put(key,value);
	}
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(image, 0);
		out.writeString(title);
		out.writeString(link);
		out.writeString(imgUrl);
		final Bundle bundle = new Bundle();
		for (Entry<String,String> entry : data.entrySet())
			bundle.putString(entry.getKey(),entry.getValue());
		out.writeBundle(bundle);
	}
	
	public static final Parcelable.Creator<Card> CREATOR = 
			new Parcelable.Creator<Card>() {
		public Card createFromParcel(Parcel in) {
			return new Card(in);
		}
		
		public Card[] newArray(int size) {
			return new Card[size];
		}
	};
	
	private Card(Parcel in) {
		image = in.readParcelable(Bitmap.class.getClassLoader());
		title = in.readString();
		link = in.readString();
		imgUrl = in.readString();
		data = new TreeMap<String,String>();
		final Bundle bundle = in.readBundle();
		for (String key : bundle.keySet())
			data.put(key, bundle.getString(key));
	}

	public String createShareLink() {
		return Constants.BASE_URL + link;
	}
}

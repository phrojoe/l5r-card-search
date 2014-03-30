package org.frojoe.l5rSearch;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.util.*;

/**
 * lifted from 
 * developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 */
public class BitmapCache extends Application{

	LruCache<String,Bitmap> mMemoryCache;
	int cacheSize;
    List<String> keys = new ArrayList<String>();
	
	public BitmapCache() {}
	
	public void createCache(Context context) {
		final int memClass = ((ActivityManager) context.getSystemService(
	            Context.ACTIVITY_SERVICE)).getMemoryClass();
		cacheSize = 1024 * 1024 * memClass / 8;
		mMemoryCache = new LruCache<String,Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
	       }
		};
	}
	
	public boolean cacheInit() {
		return mMemoryCache != null;
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
        keys.add(key);
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return (key != null) ? mMemoryCache.get(key) : null;
	}
	
	public void recycle() {
        Bitmap b;
        for (String key : keys) {
            b = mMemoryCache.remove(key);
            b.recycle();
        }
        keys.clear();
    }
	
}

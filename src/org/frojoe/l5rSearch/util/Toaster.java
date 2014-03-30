package org.frojoe.l5rSearch.util;

import android.content.Context;
import android.widget.Toast;

public class Toaster {

	private static final int DURATION = Toast.LENGTH_SHORT;
	private Context context;
	
	public Toaster(Context context) {
		this.context = context;
	}
	
	public void toast(String message) {
		Toast.makeText(context, message, DURATION).show();
	}
}

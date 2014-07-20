package org.frojoe.l5rSearch.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import org.apache.http.client.ClientProtocolException;

import org.frojoe.l5rSearch.Card;
import org.frojoe.l5rSearch.CustomTypefaceSpan;
import org.frojoe.l5rSearch.R;
import org.frojoe.l5rSearch.oracle.CardQuery;
import org.frojoe.l5rSearch.storage.CardManager;
import org.frojoe.l5rSearch.storage.CardStorageHelper;
import org.frojoe.l5rSearch.util.Constants;
import org.frojoe.l5rSearch.util.Toaster;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings("ResourceType")
public class CardActivity extends Activity implements View.OnClickListener {
	
	final static int GOLD_ICON_SIZE = 47;
	
	Card card;
    ArrayList<Card> cards;
    int pos;
	boolean saveEnabled = true;
	boolean allowSave = false;
    ScrollView theMainLayout;
    boolean urlLaunch;

    private ShareActionProvider mShareActionProvider;
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

	final static boolean ICS_COMPATIBLE = android.os.Build.VERSION.SDK_INT >= 
			android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	Typeface tf;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.card);
        theMainLayout = (ScrollView) findViewById(R.id.scroll_view);
		tf = Typeface.createFromAsset(this.getAssets(),
				"fonts/Roboto-Condensed.ttf");
		if (ICS_COMPATIBLE)
			addHomeButton();
	    card = getIntent().getExtras().getParcelable(Constants.INTENT_CARD);
        cards = getIntent().getExtras().getParcelableArrayList(Constants.INTENT_CARDS);
        pos = getIntent().getExtras().getInt(Constants.INTENT_POS);
        urlLaunch = getIntent().getBooleanExtra(Constants.INTENT_URLLAUNCH, false);
		setProgressBarIndeterminateVisibility(true);
        if (card != null) {
            if (card.getData().isEmpty())
                new ConsultTheOracleTask().execute(card);
            else
                populateView();
        } else {
           Toaster toaster = new Toaster(this);
           toaster.toast("Card details failed to load");
        }
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        theMainLayout.setOnTouchListener(gestureListener);
	}
	
	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	if (saveEnabled)
    		inflater.inflate(R.layout.card_menu_details_save, menu);
    	else
    		inflater.inflate(R.layout.card_menu_details_delete, menu);
    	MenuItem shareItem = menu.findItem(R.id.share_card);
    	if (ICS_COMPATIBLE) {
    		mShareActionProvider = 
    			(ShareActionProvider) shareItem.getActionProvider();
    		mShareActionProvider.setShareIntent(createShareIntent());
    	} else {
    		shareItem.setOnMenuItemClickListener(
    				new MenuItem.OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					shareCard(createShareIntent());
					return false;
				}
			});
    	}
        return true;
    }
	
	private void shareCard(Intent shareIntent) {
		startActivity(Intent.createChooser(shareIntent, 
				getText(R.string.share_to)));
	}
	
	private Intent createShareIntent() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, card.createShareLink());
		shareIntent.setType("text/plain");
		return shareIntent;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		return onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
                finish();
                return true;
			case R.id.card_menu_image:
				viewImage();
				return true;
			case R.id.card_menu_save:
				if (allowSave)
					saveCard();
				return true;
			case R.id.card_menu_delete:
				deleteCard();
				return true;
			default:
				return true;
		}
	}
	
	@SuppressLint("NewApi")
	private void addHomeButton() {
		ActionBar actionBar = getActionBar();
        if (actionBar != null)
	        actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void viewImage() {
		Intent intent = new Intent(this, CardImageActivity.class);
		intent.putExtra(Constants.INTENT_IMGURL, card.getImgUrl());
		intent.putExtra(Constants.INTENT_CARDTITLE, card.getTitle());
        intent.putExtra(Constants.INTENT_CARD, card);
        intent.putExtra(Constants.INTENT_POS, pos);
        intent.putExtra(Constants.INTENT_URLLAUNCH, urlLaunch);
        intent.putParcelableArrayListExtra(Constants.INTENT_CARDS, cards);
		startActivity(intent);
	}

	private boolean hasCard() {
		CardStorageHelper helper = 
				new CardStorageHelper(this);
		CardManager cm = new CardManager(helper, CardManager.READ);
		return cm.hasCard(card);
	}
	
	@SuppressLint("NewApi")
	private void saveCard() {
		Toaster toaster = new Toaster(this);
		CardStorageHelper helper = 
				new CardStorageHelper(this);
		CardManager cm = new CardManager(helper, CardManager.WRITE);
		try {
			if (cm.saveCard(card)) {
				toaster.toast("Saved");
				saveEnabled = false;
				if (ICS_COMPATIBLE)
					invalidateOptionsMenu();
			}
			else {
				saveEnabled = true;
				toaster.toast("Save failed");
			}
		} catch (JSONException e) {
			toaster.toast("Save failed");
			e.printStackTrace();
		}
		cm.close();
	}
	
	@SuppressLint("NewApi")
	private void deleteCard() {
		Toaster toaster = new Toaster(this);
		CardStorageHelper helper = 
				new CardStorageHelper(this);
		CardManager cm = new CardManager(helper, CardManager.WRITE);
		if (cm.deleteCard(card)) {
			saveEnabled = true;
			toaster.toast("Deleted");
			if (ICS_COMPATIBLE)
				invalidateOptionsMenu();
		} else {
			saveEnabled = false;
			toaster.toast("Delete failed");
		}
		cm.close();
	}
	
	@SuppressLint("NewApi")
	private void populateView() {
		saveEnabled = !hasCard();
		if (ICS_COMPATIBLE)
			invalidateOptionsMenu();
		setTitle(card.getTitle());
		boolean even = false;
        LinearLayout theLayout = (LinearLayout) findViewById(R.id.card_details);
        theLayout.removeAllViews();
		for (Entry<String,String> entry : card.getData().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			Log.d("Card Data",key+":"+value);
			if (!key.matches("\\W+")) {
				final TextView tv = new TextView(this);
				if (even)
					tv.setBackgroundResource(R.color.light);
				Spannable s = generateSpannable(key+": "+value);
				tv.setText(s, BufferType.SPANNABLE);
				tv.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv.setTypeface(tf);
				tv.setTextSize(20);
				theLayout.addView(tv);
				
				even = !even;
			}
		}
		setProgressBarIndeterminateVisibility(false);
		allowSave = true;
	}
	
	private Spannable generateSpannable(String text) {
		Spannable s;
		List<Integer> imgSpans = null, fontSpans = null;
		
		if (text.contains(Constants.GOLD_ICON_START))
			imgSpans = generateSpans(text, 
					new String[] {Constants.GOLD_ICON_START, 
									Constants.GOLD_ICON_END},
					new String[] {Constants.L5R_FONT_START,
									Constants.L5R_FONT_END});
		
		if (text.contains(Constants.L5R_FONT_START))
			fontSpans = generateSpans(text,
					new String[] {Constants.L5R_FONT_START,
							Constants.L5R_FONT_END},
					new String[] {Constants.GOLD_ICON_START, 
							Constants.GOLD_ICON_END});
		
		for (String indr : Constants.SPAN_INDICATORS)
			text = text.replaceAll(indr, "");
		
		s = new SpannableString(text);
		if (imgSpans != null && !imgSpans.isEmpty())
			addImageSpans(s, imgSpans, text);
		if (fontSpans != null && !fontSpans.isEmpty())
			addFontSpans(s, fontSpans, text);
		
		return s;
	}
	
	private List<Integer> generateSpans(String text, 
			String[] targets, String[] replacements) {
		List<Integer> spans = new ArrayList<Integer>();
		for (String s : replacements)
			text = text.replaceAll(s, "");
		while (text.contains(targets[0])) {
			int spanStart = text.indexOf(targets[0]);
			text = text.replaceFirst(targets[0], "");
			int spanEnd = text.indexOf(targets[1]);
			text = text.replaceFirst(targets[1], "");
			spans.add(spanStart);
			spans.add(spanEnd);
		}
		return spans;
	}
	
	private void addImageSpans(Spannable s, 
			List<Integer> imgSpans, String text) {
		for (int i = 0; i < imgSpans.size(); i+=2) {
			try {
				int idxStart = imgSpans.get(i), idxEnd = imgSpans.get(i+1);
				String imgId = text.substring(idxStart, idxEnd);
				InputStream is = getAssets().open("gold_icons/"+imgId+".png");
				Bitmap b = Bitmap.createScaledBitmap(
						BitmapFactory.decodeStream(is), 
						GOLD_ICON_SIZE, GOLD_ICON_SIZE, false);
				ImageSpan span = 
						new ImageSpan(this, b, ImageSpan.ALIGN_BOTTOM);
				s.setSpan(span, idxStart, idxEnd, 
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (IOException ioe) {
				Log.e("ASSET", "Asset not found for image span", ioe);
			}
		}
	}
	
	private void addFontSpans(Spannable s,
			List<Integer> fontSpans, String text) {
		Typeface l5rTf = Typeface.createFromAsset(this.getAssets(),
				"fonts/l5r_textsymbols.ttf");
		for (int i = 0; i < fontSpans.size(); i+=2) {
			int idxStart = fontSpans.get(i), idxEnd = fontSpans.get(i+1);
			s.setSpan(new CustomTypefaceSpan("serif", l5rTf), idxStart, 
					idxEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}

    private boolean viewCard(int offset) {
        pos += offset;
        if (cards != null && !cards.isEmpty() && pos < cards.size() && pos >= 0) {
            setProgressBarIndeterminateVisibility(true);
            card = cards.get(pos);
            if (card.getData().isEmpty())
                new ConsultTheOracleTask().execute(card);
            else
                populateView();
            return true;
        } else {
            Toaster toaster = new Toaster(this);
            toaster.toast("No more cards that direction");
            return false;
        }
    }

    @Override
    public void onClick(View v) {}

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > Constants.SWIPE_MAX_OFF_PATH)
                    return false;
                if (e1.getX() - e2.getX() > Constants.SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY)
                    return viewCard(1);
                if (e2.getX() - e1.getX() > Constants.SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY)
                    return viewCard(-1);
            } catch (Exception e) {}
            return false;
        }
    }

    private class ConsultTheOracleTask extends AsyncTask<Card,String,Card> {
		
		protected Card doInBackground(Card... card) {
			CardQuery cardQuery = new CardQuery(card[0]);
			Card theCard = null;
			try {
				cardQuery.execute();
				cardQuery.parseResponse();
				theCard = card[0];
			} catch (IllegalStateException e) {
				Log.e(Constants.APP_NAME,"IllegalStateException",e);
			} catch (ClientProtocolException e) {
				Log.e(Constants.APP_NAME,"ClientProtocalException",e);
			} catch (IOException e) {
				Log.e(Constants.APP_NAME,"IOException",e);
			}
			return theCard;
		}
		
		protected void onPostExecute(Card theCard) {
			card = theCard;
            if (card != null)
			    populateView();
		}
	}
}

package org.frojoe.l5rSearch.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;

import org.frojoe.l5rSearch.BitmapCache;
import org.frojoe.l5rSearch.Card;
import org.frojoe.l5rSearch.R;
import org.frojoe.l5rSearch.oracle.CardQuery;
import org.frojoe.l5rSearch.util.Constants;
import org.frojoe.l5rSearch.util.Toaster;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@SuppressWarnings("ResourceType")
public class CardImageActivity extends Activity implements View.OnClickListener {
	
    ArrayList<Card> cards;
    Card card;
    String imgUrl;
    int pos;
    ImageView iv;
    boolean urlLaunch;

	boolean icsCompat = android.os.Build.VERSION.SDK_INT >= 
				android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;

    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.card_image);
        iv = (ImageView) findViewById(R.id.card_image);
		cards = getIntent().getExtras().getParcelableArrayList(Constants.INTENT_CARDS);
        card = getIntent().getExtras().getParcelable(Constants.INTENT_CARD);
        pos = getIntent().getExtras().getInt(Constants.INTENT_POS);
        imgUrl = getIntent().getStringExtra(Constants.INTENT_IMGURL);
        urlLaunch = getIntent().getBooleanExtra(Constants.INTENT_URLLAUNCH, false);
        String cardTitle = getIntent().getStringExtra(Constants.INTENT_CARDTITLE);
        prepLayout(cardTitle);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        iv.setOnTouchListener(gestureListener);
	}

    private void prepLayout(String cardTitle) {
        setTitle(cardTitle);
        if (icsCompat)
            addHomeButton();
        Bitmap image = null;
        if (((BitmapCache)getApplication()).cacheInit())
            image = ((BitmapCache)getApplication())
                    .getBitmapFromMemCache(imgUrl);
        else
            ((BitmapCache)getApplication()).createCache(this);
        if (image == null) {
            Log.d("Debug","downloading image");
            setProgressBarIndeterminateVisibility(true);
            new DownloadCardImageTask().execute(imgUrl);
        } else
            displayImage(image);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
            case R.id.card_menu_details:
                loadDetails();
                return true;
			default:
				return true;
		}
	}

	@SuppressLint("NewApi")
	private void addHomeButton() {
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.card_menu_image, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        return onCreateOptionsMenu(menu);
    }

	private void displayImage(Bitmap image) {
        iv.setImageBitmap(image);
		iv.setVisibility(View.VISIBLE);
	}

    private boolean viewCardImage(int offset) {
        pos += offset;
        try {
            if (cards != null && !cards.isEmpty() && pos < cards.size() && pos >= 0) {
                card = cards.get(pos);
                if (card.getData().isEmpty())
                    new ConsultTheOracleTask().execute(card);
                else {
                    imgUrl = card.getImgUrl();
                    prepLayout(card.getTitle());
                }
            } else {
                Toaster toaster = new Toaster(this);
                toaster.toast("No more cards that direction");
            }
        } catch (IllegalStateException e) {
            Log.e(Constants.APP_NAME,"IllegalStateException",e);
        }
        return true;
    }

    private void loadDetails() {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putParcelableArrayListExtra(Constants.INTENT_CARDS, cards);
        intent.putExtra(Constants.INTENT_POS, pos);
        if (card != null) {
            intent.putExtra(Constants.INTENT_CARD, card);
        } else if (cards != null) {
            intent.putExtra(Constants.INTENT_CARD, cards.get(pos));
        }
        startActivity(intent);
        finish();
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
            boolean success = false;
            try {
                if (Math.abs(e1.getY() - e2.getY()) <= Constants.SWIPE_MAX_OFF_PATH + 300) {
                    if (e1.getX() - e2.getX() > Constants.SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY)
                        success = viewCardImage(1);
                    if (e2.getX() - e1.getX() > Constants.SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY)
                        success = viewCardImage(-1);
                }
            } catch (Exception e) {
                Log.e(Constants.APP_NAME, "Exception after gesture", e);
            }
            return success;
        }
    }

	private class DownloadCardImageTask extends AsyncTask<String,String,Bitmap> {
		
		protected Bitmap doInBackground(String... imgUrl) {
			return loadBitmap(Constants.BASE_URL + imgUrl[0]);
		}
		
		protected void onPostExecute(Bitmap image) {
			setProgressBarIndeterminateVisibility(false);
			if (image != null && 
				image.getRowBytes()*image.getHeight() > Constants.MIN_BYTES) {
				((BitmapCache)getApplication()).
					addBitmapToMemoryCache(imgUrl, image);
				displayImage(image);
			} else {
				TextView noImageView = 
						(TextView) findViewById(R.id.no_image_view);
				noImageView.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public Bitmap loadBitmap(String url) {
		try {
            Bitmap bitmap = BitmapFactory.decodeStream(
                    (InputStream) new URL(url).getContent());
			  return bitmap;
			} catch (MalformedURLException mue) {
                Log.e(Constants.APP_NAME, "MalformedURLException", mue);
			} catch (IOException ioe) {
			    Log.e(Constants.APP_NAME, "IOException", ioe);
			} catch (OutOfMemoryError oom) {
                //clear cache and try again
                Log.i(Constants.APP_NAME, "clearing bitmap cache");
                ((BitmapCache)getApplication()).recycle();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(
                            (InputStream) new URL(url).getContent());
                    return bitmap;
                } catch (MalformedURLException mue) {
                    Log.e(Constants.APP_NAME, "MalformedURLException", mue);
                } catch (IOException ioe) {
                    Log.e(Constants.APP_NAME, "IOException", ioe);
                } catch (OutOfMemoryError oom2) {
                    Log.e(Constants.APP_NAME, "OutOfMemoryError", oom2);
                }
            }
		return null;
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
            imgUrl = theCard.getImgUrl();
            prepLayout(theCard.getTitle());
        }
    }
}

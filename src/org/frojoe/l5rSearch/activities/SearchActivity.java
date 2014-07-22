package org.frojoe.l5rSearch.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.frojoe.l5rSearch.Card;
import org.frojoe.l5rSearch.Query;
import org.frojoe.l5rSearch.R;
import org.frojoe.l5rSearch.oracle.Search;
import org.frojoe.l5rSearch.storage.CardManager;
import org.frojoe.l5rSearch.storage.CardStorageHelper;
import org.frojoe.l5rSearch.util.Constants;
import org.frojoe.l5rSearch.util.Toaster;
import org.frojoe.l5rSearch.widget.RangeSeekBar;
import org.frojoe.l5rSearch.widget.RangeSeekBar.OnRangeSeekBarChangeListener;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

@SuppressWarnings("ResourceType")
public class SearchActivity extends Activity {
	
	Toaster toaster;
	ConsultTheOracleTask searchTask;
	final static String ADV_SEARCH_VISIBLE = "ADV_SEARCH_VISIBLE";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.search);
        initEditTexts();
        initRangeSeekBars();
        initTooltips();
        toaster = new Toaster(this);
        if (savedInstanceState != null && 
        		savedInstanceState.getBoolean(ADV_SEARCH_VISIBLE)) {
        	RelativeLayout advSearch = (RelativeLayout) 
            		findViewById(R.id.adv_search_layout);
        	advSearch.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	RelativeLayout advSearch = (RelativeLayout) 
    			findViewById(R.id.adv_search_layout);
    	outState.putBoolean(ADV_SEARCH_VISIBLE,	
    			advSearch.getVisibility() == View.VISIBLE);
    }
    
    private void initEditTexts() {
    	EditText[] basicEditTexts = 
    			getEditTexts(Constants.BASIC_SEARCH_EDIT_TEXTS);
    	EditText[] advEditTexts = getEditTexts(Constants.ADV_SEARCH_EDIT_TEXTS);
        for (EditText et : basicEditTexts)
        	setDefaultActionForEditText(et);
        for (EditText et : advEditTexts)
        	setDefaultActionForEditText(et);
    }
    
    private void setDefaultActionForEditText(EditText editText) {
    	editText.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, 
					int actionId, KeyEvent event) {
				if (event != null) {
					ImageButton searchButton = 
							(ImageButton) findViewById(R.id.search_button);
					searchButton.performClick();
					return false;
				}
				return false;
			}
		});
    }
    
    private void initTooltips() {
    	
    	for (Entry<Integer, Integer> entry:
    			Constants.TEXT_VIEW_TOOLTIPS_MAP.entrySet()) {
    		
    		final Toaster tooltipToaster = new Toaster(this);
    		
    		TextView tv = (TextView) findViewById(entry.getKey());
    		final String tooltip = getString(entry.getValue());
    		tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					tooltipToaster.toast(tooltip);
				}
			});
    	}
    }
    
    private void initRangeSeekBars() {
    	for (Entry<Integer, Integer[]> entry : 
    			Constants.SEEKBAR_GROUP_MAP.entrySet()) {
    		
    		RangeSeekBar<Integer> rsb = (RangeSeekBar) findViewById(entry.getKey());
    		final CheckBox cb = (CheckBox) 
    				findViewById(entry.getValue()[Constants.CB_IDX]);
    		final TextView min = (TextView)
    				findViewById(entry.getValue()[Constants.MIN_IDX]);
    		final TextView max = (TextView)
    				findViewById(entry.getValue()[Constants.MAX_IDX]);
    		
    		rsb.setOnRangeSeekBarChangeListener(
    				new OnRangeSeekBarChangeListener<Integer>() {
    				@Override
    				public void onRangeSeekBarValuesChanged(
    						RangeSeekBar<?> bar, Integer minValue,
    						Integer maxValue) {
    					
    					min.setText(minValue.toString());
    					max.setText(maxValue.toString());
    					cb.setChecked(true);
    				}
    			});
    	}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.layout.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.search_menu_mycards:
    			getSavedCards();
				return true;
    		case R.id.search_menu_reset:
    			resetFields();
    			return true;
    		case R.id.search_menu_adv_search:
    			toggleAdvSearchFields();
    			return true;
    		default:
    			return true;
    	}
    }
    
    private void getSavedCards() {
    	CardStorageHelper helper = 
    			new CardStorageHelper(this);
    	CardManager cm = new CardManager(helper, CardManager.READ);
    	try {
			ArrayList<Card> savedCards = cm.getCards();
			if (!savedCards.isEmpty())
				populateListView(savedCards);
			else
				toaster.toast("No saved cards exist");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	cm.close();
    }
    
    private void resetFields() {
    	EditText mainSearch = (EditText) findViewById(R.id.card_search);
    	mainSearch.requestFocus();
    	for (Integer key : Constants.VIEW_PARAM_MAP.keySet()) {
    		View view = findViewById(key);
    		if (view instanceof EditText)
    			((EditText)view).setText("");
    		else if (view instanceof Spinner)
    			((Spinner)view).setSelection(0);
    		else if (view instanceof CheckBox)
    			((CheckBox)view).setChecked(false);
    	}
    	for (Integer key : Constants.VIEW_MULTI_PARAM_MAP.keySet()) {
    		RangeSeekBar<Integer> rsb = (RangeSeekBar<Integer>) findViewById(key);
    		rsb.setSelectedMinValue(rsb.getAbsoluteMinValue());
    		rsb.setSelectedMaxValue(rsb.getAbsoluteMaxValue());
    		int cbId = Constants.SEEKBAR_GROUP_MAP
    				.get(rsb.getId())[Constants.CB_IDX];
    		CheckBox cb = (CheckBox) findViewById(cbId);
    		cb.setChecked(false);
    	}
    }
    
    private void toggleAdvSearchFields() {
    	RelativeLayout advSearchFields = 
    			(RelativeLayout) findViewById(R.id.adv_search_layout);
    	advSearchFields.setVisibility(
    			advSearchFields.getVisibility() == View.GONE
    			? View.VISIBLE
    			: View.GONE );
    }
    
    public void search(View view) {
    	setProgressBarIndeterminateVisibility(true);
    	toggleSearchButton();
    	searchTask = new ConsultTheOracleTask();
    	searchTask.execute(getQueryParams());
    }
    
    public void cancelSearch(View view) {
    	searchTask.cancel(true);
    	toggleSearchButton();
    	setProgressBarIndeterminateVisibility(false);
    }
    
    private BasicNameValuePair[] getQueryParams() {
    	
    	Query query = new Query();
    	query.processEditTexts(getEditTexts(Constants.BASIC_SEARCH_EDIT_TEXTS));
    	query.processSpinners(getSpinners(Constants.BASIC_SEARCH_SPINNERS));
    	
    	RelativeLayout advSearchFields = 
    			(RelativeLayout) findViewById(R.id.adv_search_layout);
    	if (advSearchFields != null 
    			&& advSearchFields.getVisibility() == View.VISIBLE) {
    		
    		Set<Entry<Integer, Integer[]>> integerSeekBarSet = 
    				Constants.SEEKBAR_GROUP_MAP.entrySet();
    		View[][] integerSeekBarGroups = 
    				new View[integerSeekBarSet.size()][];
    		
    		int i = 0;
    		for (Entry<Integer, Integer[]> entry : integerSeekBarSet) { 
    			//	each seek bar gets grouped with their checkbox
    			View isb = findViewById(entry.getKey());
    			View cb = findViewById(entry.getValue()[Constants.CB_IDX]);
    			integerSeekBarGroups[i] = new View[]{isb,cb};
    			i++;
    		}
    			
    		query.processIntegerSeekBarGroups(integerSeekBarGroups);
        	query.processEditTexts(
        			getEditTexts(Constants.ADV_SEARCH_EDIT_TEXTS));
    		query.processSpinners(getSpinners(Constants.ADV_SEARCH_SPINNERS));

            query.processCheckboxes(getCheckboxes(Constants.ADV_SEARCH_CBS));
    	}
    	
    	return query.getParamsAsArray();
    }
    
    private void toggleSearchButton() {
    	ImageButton search = (ImageButton) findViewById(R.id.search_button);
    	ImageButton cancel = (ImageButton) findViewById(R.id.cancel_button);
    	if (search.getVisibility() == View.VISIBLE) {
    		search.setVisibility(View.GONE);
    		cancel.setVisibility(View.VISIBLE);
    	} else {
    		search.setVisibility(View.VISIBLE);
    		cancel.setVisibility(View.GONE);
    	}
    }
    
    private EditText[] getEditTexts(int[] ids) {
    	EditText[] editTexts = new EditText[ids.length];
    	for (int i = 0; i < ids.length; i++)
    		editTexts[i] = (EditText) findViewById(ids[i]);
    	return editTexts;
    }
    
    private Spinner[] getSpinners(int[] ids) {
    	Spinner[] spinners = new Spinner[ids.length];
    	for (int i = 0; i < ids.length; i++)
    		spinners[i] = (Spinner) findViewById(ids[i]);
    	return spinners;
    }

    private CheckBox[] getCheckboxes(int[] ids) {
        CheckBox[] checkBoxes = new CheckBox[ids.length];
        for (int i = 0; i < ids.length; i++)
            checkBoxes[i] = (CheckBox) findViewById(ids[i]);
        return checkBoxes;
    }

    private void populateListView(ArrayList<Card> cards) {
    	Intent intent =  new Intent(this, ResultsActivity.class);
		intent.putParcelableArrayListExtra(Constants.INTENT_CARDS, cards);
		startActivity(intent);
    }
    
    private void populateCardView(Card card) {
    	Intent intent = new Intent(this, CardActivity.class);
		intent.putExtra(Constants.INTENT_CARD, card);
		startActivity(intent);
    }
    
    private class ConsultTheOracleTask 
    	extends AsyncTask<BasicNameValuePair,Integer,ArrayList<Card>> {

		protected ArrayList<Card> doInBackground(BasicNameValuePair... params) {
			List<NameValuePair> searchParams = new ArrayList<NameValuePair>();
			for (BasicNameValuePair param : params) {
				if (param != null)
					searchParams.add(param);
			}
			Search cardSearch = new Search(searchParams);
	    	ArrayList<Card> cards = new ArrayList<Card>(0);
	    	try {
				List<Card> results = cardSearch.sendQuery();
				cards.addAll(results);
				Log.d("Matches",""+cardSearch.getMatches());
				while (cards.size() < cardSearch.getMatches()) {
					cardSearch.incPage();
					List<Card> moreResults = cardSearch.sendQuery();
					cards.addAll(moreResults);
				}
	    	} catch (IllegalStateException e) {
	    		Log.e(Constants.APP_NAME,"IllegalStateException",e);
			} catch (ClientProtocolException e) {
				Log.e(Constants.APP_NAME,"ClientProtocalException",e);
			} catch (IOException e) {
				Log.e(Constants.APP_NAME,"IOException",e);
			}
			return cards;
		}
		
		protected void onPostExecute(ArrayList<Card> results) {
			if (!isCancelled()) {
				switch (results.size()) {
					case 0:
						toaster.toast("No cards found");
						break;
					case 1:
						Card uniqueCard = results.get(0);
						populateCardView(uniqueCard);
						break;
					default:
						populateListView(results);
				}
				setProgressBarIndeterminateVisibility(false);
				toggleSearchButton();
			}
		}
    	
    }
}
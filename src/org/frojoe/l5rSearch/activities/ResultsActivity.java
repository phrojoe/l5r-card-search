package org.frojoe.l5rSearch.activities;

import java.util.ArrayList;
import java.util.List;

import org.frojoe.l5rSearch.Card;
import org.frojoe.l5rSearch.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.frojoe.l5rSearch.util.Constants;
import org.frojoe.l5rSearch.util.Toaster;

public class ResultsActivity extends Activity {

	ArrayList<Card> cards;
	final static boolean ICS_COMPATIBLE = android.os.Build.VERSION.SDK_INT >= 
			android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		if (ICS_COMPATIBLE)
			addHomeButton();
		final ListView lv = (ListView) findViewById(R.id.results_list);
		    lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
		    	public void onItemClick(AdapterView<?> adapter, View view, 
						int pos, long lng) {
					Card card = cards.get(pos);
					Intent intent = 
							new Intent(view.getContext(), CardActivity.class);
					intent.putExtra(Constants.INTENT_CARD, card);
                    intent.putParcelableArrayListExtra(Constants.INTENT_CARDS, cards);
                    intent.putExtra(Constants.INTENT_POS, pos);
				startActivity(intent);
			}
		});
		cards = getIntent().getExtras().getParcelableArrayList(Constants.INTENT_CARDS);
		if (cards != null)
            populateResults(cards);
        else {
            Toaster toaster = new Toaster(this);
            toaster.toast("Results list is empty");
            onBackPressed();
        }
	}
	
	private void populateResults(ArrayList<Card> cards) {
		List<String> values = new ArrayList<String>();
    	for (Card card : cards)
			values.add(card.getTitle());
		ListView lv = (ListView) findViewById(R.id.results_list);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(
    			ResultsActivity.this,
    			android.R.layout.simple_list_item_1,
    			values);
    	lv.setAdapter(adapter);
    	lv.setVisibility(View.VISIBLE);
    	showMatchCount(cards.size());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return true;
		}
	}
	
    private void showMatchCount(int matches) {
    	TextView matchText = (TextView) findViewById(R.id.match_text);
    	matchText.setText(""+matches+" cards found");
    	matchText.setVisibility(View.VISIBLE);
    }
	
	@SuppressLint("NewApi")
	private void addHomeButton() {
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
}

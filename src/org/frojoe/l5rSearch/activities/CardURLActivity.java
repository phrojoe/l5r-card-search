package org.frojoe.l5rSearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.frojoe.l5rSearch.Card;
import org.frojoe.l5rSearch.util.Constants;

public class CardURLActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Card card = getCardFromURLIntent();
        if (card != null)
            sendToCardView(card);
        else
            sendToSearchView();
    }

    private Card getCardFromURLIntent() {
        Card card = null;
        String link = getIntent().getDataString();
        if (link != null && link.matches(".*cardid=\\d+.*")) {
            card = new Card();
            card.setLink(link.substring(link.indexOf("cardid")-1));
        }
        return card;
    }

    private void sendToCardView(Card card) {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra(Constants.INTENT_CARD, card);
        intent.putExtra(Constants.INTENT_URLLAUNCH, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendToSearchView() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}

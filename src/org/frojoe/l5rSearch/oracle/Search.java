package org.frojoe.l5rSearch.oracle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.frojoe.l5rSearch.Card;
import org.frojoe.l5rSearch.util.Constants;
import org.frojoe.l5rSearch.util.StringCleaner;

public class Search {

	private static final Pattern
            CARD_LINK_PATTERN = Pattern.compile("<a\\s+class=\"cardlookup\"\\s+href=\"(.*?)\"\\s*>"),
	        CARD_TITLE_PATTERN = Pattern.compile("<span\\s+class=\"l5rfont\"\\s*>(.*?)</span>"),
            CARD_LINE_PATTERN = Pattern.compile("<tr\\s+class=\"cardline\"\\s*>"),
	        SCRIPT_LINK_PATTERN = Pattern.compile("location.hash='(.*?)'"),
            MATCHES_PATTERN = Pattern.compile("<span\\s+class=\"listingmatches\">(\\d+).*?</span>"),
            DATA_PATTERN = Pattern.compile("<div class=\"shadowdatacurrent\"[\\s\\w=\"]+>(.+?)</div>"),
            INLINE_IMG_PATTERN = Pattern.compile("<img\\s+class=\"inlinebutton\"\\s+src=\".+g_([\\d\\w]+)\\.png\"\\s+/>"),
            SINGLE_CARD_TITLE_PATTERN = Pattern.compile("<tr\\s+class=\"greenhead\">(.*?)</tr>");

    private static final String
            FONT_SPAN = "<span class=\"l5rsym\">",
            CLOSE_SPAN = "</span>";

	private HttpClient client = new DefaultHttpClient();
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private Card card;
	
	private int page = 1;
	private int matches = 0;
	
	private BasicNameValuePair pageNumber = getPageNumber();

    public Search(Card card) {
        this.card = card;
        Matcher m = Card.CARDID_PATTERN.matcher(card.getLink());
        if (m.find())
		    params.add(new BasicNameValuePair("cardid",m.group(1)));
    }

    public Search(List<NameValuePair> params) {
		this.params = params;
		params.add(pageNumber);
	}

    public List<Card> sendQuery() throws IOException {
        String doc = sendRequest(Constants.DO_SEARCH_URL);
        return parseDoc(doc);
    }

    public Card updateCard() throws IOException {
        String doc = sendRequest(Constants.DO_CARD_URL);
        parseCardDoc(doc);
        return card.clone();
    }

    public void incPage() {
        this.page++;
        params.remove(pageNumber);
        pageNumber = getPageNumber();
        params.add(pageNumber);
    }

    public int getMatches() {
        return matches;
    }

	private String sendRequest(String uri) throws IOException {
		HttpPost post = new HttpPost(uri);
        post.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = client.execute(post);
        return parseResponse(response);
	}
	
	private String parseResponse(HttpResponse response) throws IllegalStateException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder("");
		while ((line = br.readLine()) != null)
			sb.append(line);
		return sb.toString();
	}
	
	private List<Card> parseDoc(String doc) {
		List<Card> cards = new ArrayList<Card>();
		String[] cardLines = CARD_LINE_PATTERN.split(doc); 
		for (int i = 1; i < cardLines.length; i++) {
			String cardLine = cardLines[i];
			cards.add(parseCardLine(cardLine));
		}
		if (cards.isEmpty()) {
			Matcher scriptMatcher = SCRIPT_LINK_PATTERN.matcher(doc);
			if (scriptMatcher.find()) {
				Card foundCard = new Card();
				String match = scriptMatcher.group(1);
				if (match.contains("cardid")) {
					foundCard.setLink(match);
					cards.add(foundCard);
				}
			}
		} else {
			//get matches from HTML so we know to increment pages
			Matcher matchCountMatcher = MATCHES_PATTERN.matcher(doc);
			if (matchCountMatcher.find())
				matches = Integer.parseInt(matchCountMatcher.group(1));
			else
				matches = cards.size();
		}
		return cards;
	}

    private void parseCardDoc(String doc) {
        Matcher m = DATA_PATTERN.matcher(doc);
        while (m.find()) {
            String name = m.group(1);
            if (m.find()) {
                name = StringCleaner.clean(name);
                if (name.length() > 0) {
                    String value = m.group(1);
                    if (value.contains(FONT_SPAN))
                        value = cleanFontSpan(value);
                    Matcher imgMatcher = INLINE_IMG_PATTERN.matcher(value);
                    List<String> matches = new ArrayList<String>();
                    while (imgMatcher.find())
                        matches.add(imgMatcher.group(1));
                    if (matches.size() > 0)
                        value = cleanImgSpan(value, matches);
                    value = StringCleaner.clean(value);
                    card.put(name,value);

                    Matcher titleMatcher = SINGLE_CARD_TITLE_PATTERN.matcher(doc);
                    if (titleMatcher.find())
                        card.setTitle(StringCleaner.clean(titleMatcher.group(1)));
                    else
                        card.setTitle("unknown");
                }
            }
        }
        int startIdx = doc.indexOf("src=\"showimage")+5;
        int closeIdx = doc.indexOf("\"",startIdx);
        card.setImgUrl(doc.substring(startIdx, closeIdx));
    }

	private Card parseCardLine(String cardLine) {
		Card card = new Card();
		Matcher linkMatcher = CARD_LINK_PATTERN.matcher(cardLine);
		if (linkMatcher.find())
			card.setLink(linkMatcher.group(1));
		Matcher titleMatcher = CARD_TITLE_PATTERN.matcher(cardLine);
		if (titleMatcher.find())
			card.setTitle(StringCleaner.clean(titleMatcher.group(1)));
		return card;
	}

    private static String cleanFontSpan(String value) {
        String[] valueArr = value.split(FONT_SPAN);
        String newValue = "";
        for (int i = 0; i < valueArr.length; i++) {
            if (i + 1 < valueArr.length)
                newValue += valueArr[i]
                        .replaceFirst(CLOSE_SPAN, Constants.L5R_FONT_END)
                        + Constants.L5R_FONT_START;
            else
                newValue += valueArr[i]
                        .replaceFirst(CLOSE_SPAN, Constants.L5R_FONT_END);
        }
        return newValue;
    }

    private static String cleanImgSpan(String value, List<String> matches) {
        String[] valueArr = value.split(INLINE_IMG_PATTERN.pattern());
        String newValue = valueArr[0];
        for (int i = 1; i < valueArr.length; i++)
            newValue += Constants.GOLD_ICON_START + matches.get(i-1)
                    + Constants.GOLD_ICON_END + valueArr[i];
        return newValue;
    }

	private BasicNameValuePair getPageNumber() {
        return new BasicNameValuePair(Constants.PAGE_NUMBER, ""+page);
	}
}

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

	private static final Pattern CARD_LINK_PATTERN = 
			Pattern.compile("<a\\s+class=\"cardlookup\"\\s+href=\"(.*?)\"\\s*>");
	
	private static final Pattern CARD_TITLE_PATTERN =
			Pattern.compile("<span\\s+class=\"l5rfont\"\\s*>(.*?)</span>");
	
	private static final Pattern CARD_LINE_PATTERN = 
			Pattern.compile("<tr\\s+class=\"cardline\"\\s*>");
	
	private static final Pattern SCRIPT_LINK_PATTERN = 
			Pattern.compile("location.hash='(.*?)'");
	
	private static final Pattern MATCHES_PATTERN = 
			Pattern.compile("<span\\s+class=\"listingmatches\">(\\d+).*?</span>");
	
	private String doc;
	private HttpClient client = new DefaultHttpClient();
	private HttpPost post = new HttpPost(Constants.DO_SEARCH_URL);
	private HttpResponse response;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	
	private int page = 1;
	private int matches = 0;
	
	private BasicNameValuePair pageNumber = 
			new BasicNameValuePair(Constants.PAGE_NUMBER, getPageStr());
	
	public Search(List<NameValuePair> params) {
		this.params = params;
		params.add(pageNumber);
	}
	
	public void incPage() {
		this.page++;
		params.remove(pageNumber);
		pageNumber = 
			new BasicNameValuePair(Constants.PAGE_NUMBER, getPageStr());
		params.add(pageNumber);
	}
	
	public void execute() throws ClientProtocolException, IOException {
		post.setEntity(new UrlEncodedFormEntity(params));
		response = client.execute(post);
	}
	
	public List<Card> parseResponse() 
			throws IllegalStateException, IOException {
		readHTML();
		return parseDoc();
	}
	
	private void readHTML() throws IllegalStateException, IOException {
		BufferedReader br = 
				new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder("");
		while ((line = br.readLine())!=null)
			sb.append(line);
		doc = sb.toString();
		parseDoc();
	}
	
	private List<Card> parseDoc() {
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
	
//	private Card parseScriptSource(String scriptSource) {
//		Card card = null;
//		if (scriptSource.contains("$.post")) {
//			int urlLocBeg = scriptSource.indexOf("location.hash='")+15;
//			int urlLocEnd = scriptSource.indexOf("'",urlLocBeg);
//			card = new Card();
//			card.setLink(scriptSource.substring(urlLocBeg,urlLocEnd));
//		}
//		return card;
//	}
	
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
	
//	private int parseMatchLine(Element matchLine) {
//		String value = matchLine.text();
//		String m = value.substring(0, value.indexOf(" "));
//		return Integer.parseInt(m);
//	}

	
	private String getPageStr() {
		return ""+page;
	}

	public int getMatches() {
		return matches;
	}
}

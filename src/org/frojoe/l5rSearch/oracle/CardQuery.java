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

public class CardQuery {

	private static final Pattern DATA_PATTERN = 
			Pattern.compile("<div class=\"shadowdatacurrent\"[\\s\\w=\"]+>(.+?)</div>");
	
	private static final Pattern INLINE_IMG_PATTERN = 
			Pattern.compile("<img\\s+class=\"inlinebutton\"\\s+src=\".+g_([\\d\\w]+)\\.png\"\\s+/>");
	
	private static final Pattern CARD_TITLE_PATTERN = 
			Pattern.compile("<tr\\s+class=\"greenhead\">(.*?)</tr>");
	
	private static final String FONT_SPAN = "<span class=\"l5rsym\">";
	private static final String CLOSE_SPAN = "</span>";
	
	private HttpClient client = new DefaultHttpClient();
	private HttpPost post = new HttpPost(Constants.DO_CARD_URL);
	private HttpResponse response;
	private String doc;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private Card card;
	
	public CardQuery(Card card) {
		this.card = card;
        Matcher m = Card.CARDID_PATTERN.matcher(card.getLink());
        if (m.find())
		    params.add(new BasicNameValuePair("cardid",m.group(1)));
	}
	
	public void execute() throws ClientProtocolException, IOException {
		post.setEntity(new UrlEncodedFormEntity(params));
		response = client.execute(post);
	}
	
	public void parseResponse() throws IllegalStateException, IOException {
		readHTML();
	}
	
	private void readHTML() throws IOException {
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
	
	private void parseDoc() {
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
					card.setTitle(getCardTitle());
				}
			}
		}
		card.setImgUrl(getImgURL());
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
	
	private String getImgURL() {
		int startIdx = doc.indexOf("src=\"showimage")+5;
		int closeIdx = doc.indexOf("\"",startIdx);
		return doc.substring(startIdx, closeIdx);
	}
	
	private String getCardTitle() {
		Matcher titleMatcher = CARD_TITLE_PATTERN.matcher(doc);
		if (titleMatcher.find())
			return StringCleaner.clean(titleMatcher.group(1));
		else
			return "unknown";
	}
	
	public Card getCard() {
		return card;
	}
}

package org.frojoe.l5rSearch.util;

import java.util.regex.Pattern;

import android.text.Html;

public class StringCleaner {

	private static final Pattern TAG_PATTERN = Pattern.compile("<[^<>]+>");
	
	public static String clean(String html) {
		//remove tags first to ensure there are spaces between data points
		html = TAG_PATTERN.matcher(html).replaceAll(" ");
		html = Html.fromHtml(html).toString().trim();
		html = html.replaceAll("\\s\\s+"," ");
		return html;
	}
	
}

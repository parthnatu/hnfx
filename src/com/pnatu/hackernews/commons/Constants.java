package com.pnatu.hackernews.commons;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class Constants {
	public static String HN_TOP_STORIES_URL = "https://hacker-news.firebaseio.com/v0/topstories.json";
	public static String HN_GET_ITEM_URL = "https://hacker-news.firebaseio.com/v0/item/%d.json";
	public static final Font ITALIC_FONT = Font.font("Serif", FontPosture.ITALIC, Font.getDefault().getSize());
	public static final Font BOLD_FONT = Font.font("Serif", FontPosture.REGULAR, Font.getDefault().getSize());

}

package com.pnatu.hackernews.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pnatu.hackernews.commons.Constants;
import com.pnatu.hackernews.commons.HNComment;
import com.pnatu.hackernews.commons.HNStory;
import com.pnatu.hackernews.commons.HTTPHelper;

public class HackerNewsAPI {

	public static List<Integer> getTopStories() throws IOException {
		List<Integer> topStories = null;
		JSONArray jsonTopStories = null;
		String topStoriesJsonResponse = "";

		topStoriesJsonResponse = HTTPHelper.makeRequest(Constants.HN_TOP_STORIES_URL, "get");
		jsonTopStories = new JSONArray(topStoriesJsonResponse);
		topStories = new ArrayList<>();
		for (Object storyId : jsonTopStories) {
			topStories.add(Integer.parseInt(storyId.toString()));
		}

		return topStories;
	}

	public static HNStory getStory(int storyId) throws IOException {
		String storyJsonResponse = "";
		String author;
		int id;
		List<Integer> kids;
		int score;
		long time;
		String title;
		String url;

		storyJsonResponse = HTTPHelper.makeRequest(String.format(Constants.HN_GET_ITEM_URL, storyId), "get");
		JSONObject jsonObjectStory = new JSONObject(storyJsonResponse);
		if (jsonObjectStory.getString("type").equals("story")) {
			author = jsonObjectStory.getString("by");
			id = jsonObjectStory.getInt("id");

			kids = new ArrayList<>();
			if (jsonObjectStory.has("kids")) {
				JSONArray jsonKidsList = jsonObjectStory.getJSONArray("kids");
				for (Object kidsId : jsonKidsList) {
					kids.add(Integer.parseInt(kidsId.toString()));
				}
			} else
				kids = null;

			score = jsonObjectStory.getInt("score");
			time = jsonObjectStory.getLong("time");
			title = jsonObjectStory.getString("title");
			if (jsonObjectStory.has("url")) {
				url = jsonObjectStory.getString("url");
			} else
				url = null;
			return new HNStory(author, id, kids, score, time, title, url);
		}
		return null;

	}

	public static HNComment getComment(int commentId) throws IOException {
		String author;
		String text;
		int time;
		String commentJsonResponse;
		HNComment singleComment = null;
		commentJsonResponse = HTTPHelper.makeRequest(String.format(Constants.HN_GET_ITEM_URL, commentId), "get");
		JSONObject jsonObjectComment = new JSONObject(commentJsonResponse);

		if (jsonObjectComment.getString("type").equals("comment")) {
			author = jsonObjectComment.has("by") ? jsonObjectComment.getString("by") : null;
			text = jsonObjectComment.has("text") ? jsonObjectComment.getString("text") : null;
			time = jsonObjectComment.getInt("time");
			singleComment = new HNComment(author, text, time);
			if (jsonObjectComment.has("kids")) {
				JSONArray jsonKidsList = jsonObjectComment.getJSONArray("kids");
				for (Object kidsId : jsonKidsList) {
					singleComment.addKid(getComment(Integer.parseInt(kidsId.toString())));
				}
			}
		}
		return singleComment;

	}
}

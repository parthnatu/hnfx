package com.pnatu.hackernews.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.pnatu.hackernews.commons.Constants;
import com.pnatu.hackernews.commons.HTTPHelper;

public class HackerNewsAPI {

	public static List<String> getTopStories() throws IOException {
		String topStoriesJsonResponse = HTTPHelper.makeRequest(Constants.HN_TOP_STORIES_URL, "get");
		topStoriesJsonResponse = topStoriesJsonResponse.substring(1, topStoriesJsonResponse.length() - 1);
		return Arrays.asList(topStoriesJsonResponse.split(","));
	}
}

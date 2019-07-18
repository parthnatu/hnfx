package com.pnatu.hackernews.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPHelper {
	public static String makeRequest(String requestURL, String method) throws IOException {
		URL url = new URL(requestURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		if (method.equalsIgnoreCase("get")) {
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		return content.toString();
	}
}

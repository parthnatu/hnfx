package com.pnatu.hackernews.commons;

import java.util.List;

public class HNStory {
	private String author;
	private int id;
	private List<Integer> kids;
	private int score;
	private long time;
	private String title;
	private String url;
	
	public HNStory(String author, int id, List<Integer> kids, int score, long time, String title, String url)
	{
		this.author = author;
		this.id = id;
		this.kids = kids;
		this.score = score;
		this.time = time;
		this.title = title;
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public int getId() {
		return id;
	}

	public List<Integer> getKids() {
		return kids;
	}

	public int getScore() {
		return score;
	}

	public long getTime() {
		return time;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}
}

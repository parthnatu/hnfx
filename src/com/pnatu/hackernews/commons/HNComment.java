package com.pnatu.hackernews.commons;

import java.util.ArrayList;
import java.util.List;

public class HNComment {
	private String author;
	private List<HNComment> kids;
	private String text;
	private int time;

	public HNComment(String author, String text, int time) {
		this.author = author;
		this.text = text;
		this.time = time;
		this.kids = new ArrayList<>();
	}

	public String getAuthor() {
		return author;
	}

	public List<HNComment> getKids() {
		return kids;
	}

	public void addKid(HNComment kid) {
		this.kids.add(kid);
	}

	public int getTime() {
		return time;
	}

	public String getText() {
		return text;
	}

}
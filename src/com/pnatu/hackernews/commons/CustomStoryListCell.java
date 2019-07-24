package com.pnatu.hackernews.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CustomStoryListCell extends ListCell<HNStory> {

	private Text lbl_Title;
	private Text lbl_Score;
	private Text lbl_Time;
	private VBox content;

	public CustomStoryListCell() {
		super();
		lbl_Score = new Text();
		lbl_Time = new Text();
		lbl_Title = new Text();
		content = new VBox(lbl_Title, lbl_Time, lbl_Score);
	}

	@Override
	protected void updateItem(HNStory item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setGraphic(null);
		} else {
			lbl_Title.setText(item.getTitle() + "\t\t\t" + item.getAuthor());
			lbl_Title.setStyle("-fx-font-weight: bold");
			lbl_Score.setText(String.valueOf(item.getScore()));
			lbl_Score.setStyle("-fx-stroke: gray");
			Date date = new Date(item.getTime() * 1000L);
			Calendar now = Calendar.getInstance();
			TimeZone timeZone = now.getTimeZone();
			SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
			sdf.setTimeZone(timeZone);
			lbl_Time.setText(sdf.format(date));
			setGraphic(content);
		}
	}
}

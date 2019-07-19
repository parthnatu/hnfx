package com.pnatu.hackernews.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pnatu.hackernews.api.HackerNewsAPI;
import com.pnatu.hackernews.commons.HNStory;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class TopStoriesScreenController implements Initializable {
	@FXML
	private ListView<HNStory> lv_topStories;

	@FXML
	private WebView wv_hnStory;
	private static ObservableList<HNStory> listTopStories;

	private static class AddToStoryListTask extends Task<Void> {
		private final int id;

		public AddToStoryListTask(int id) {
			this.id = id;
		}

		@Override
		protected Void call() throws Exception {
			HNStory story;
			System.out.println("Story id : " + this.id);
			story = HackerNewsAPI.getStory(this.id);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					listTopStories.add(story);
				}
			});

			return null;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Integer> topStoryIds;
		HNStory firstStory;
		WebEngine wvEngine;
		try {
			topStoryIds = HackerNewsAPI.getTopStories();
			int firstStoryId = topStoryIds.get(0);
			List<HNStory> initStoryTitle = new ArrayList<>();
			firstStory = HackerNewsAPI.getStory(firstStoryId);
			initStoryTitle.add(firstStory);
			wvEngine = wv_hnStory.getEngine();
			wvEngine.load(firstStory.getUrl());
			topStoryIds.remove(0);
			listTopStories = FXCollections.observableArrayList(initStoryTitle);
			lv_topStories.setItems(listTopStories);
			lv_topStories.setCellFactory(param -> new ListCell<HNStory>() {
				@Override
				protected void updateItem(HNStory item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null || item.getTitle() == null) {
						setText(null);
					} else {
						setText(item.getTitle());
					}
				}
			});
			for (int storyId : topStoryIds) {
				new Thread(new AddToStoryListTask(storyId)).start();
				lv_topStories.refresh();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML public void handleMouseClick(MouseEvent arg0) {
		WebEngine wvEngine;
		wvEngine = wv_hnStory.getEngine();
		wvEngine.load(lv_topStories.getSelectionModel().getSelectedItem().getUrl());
	}
}

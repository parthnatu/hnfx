package com.pnatu.hackernews.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pnatu.hackernews.api.HackerNewsAPI;
import com.pnatu.hackernews.commons.CustomStoryListCell;
import com.pnatu.hackernews.commons.HNComment;
import com.pnatu.hackernews.commons.HNStory;
import com.pnatu.hackernews.commons.WebViewFitContent;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

public class TopStoriesScreenController implements Initializable {
	@FXML
	private ListView<HNStory> lv_topStories;
	@FXML
	private SplitPane mainSplitPane;
	@FXML
	private WebView wv_hnStory;
	@FXML
	ProgressBar prgWv;
	@FXML
	private TreeView<WebViewFitContent> tv_CommentTree;
	@FXML
	private TabPane tbp_Content;
	private static ObservableList<HNStory> listTopStories;
	private static double tbp_ContentWidth = 0.0;
	private static int currentTopStoryProgress = 0;
	private static int totalTopStory = 0;

	private static class HNLoadApiTask extends Task<Void> {
		private final int id;

		public HNLoadApiTask(int id) {
			this.id = id;
		}

		@Override
		protected Void call() throws Exception {
			HNStory story;
			story = HackerNewsAPI.getStory(this.id);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					listTopStories.add(story);
					currentTopStoryProgress++;
					updateProgress(currentTopStoryProgress, totalTopStory);
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
		tbp_ContentWidth = tbp_Content.getWidth();
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
			lv_topStories.setCellFactory(new Callback<ListView<HNStory>, ListCell<HNStory>>() {

				@Override
				public ListCell<HNStory> call(ListView<HNStory> arg0) {
					// TODO Auto-generated method stub
					return new CustomStoryListCell();
				}
			});
			totalTopStory = topStoryIds.size();
			for (int storyId : topStoryIds) {
				HNLoadApiTask addToListTask = new HNLoadApiTask(storyId);
				addToListTask.setOnSucceeded(e -> {
					lv_topStories.refresh();
				});
				new Thread(addToListTask).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		lv_topStories.getSelectionModel().select(0);
		tbp_Content.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab arg2) {
				if (arg2.getText().equals("Comments")
						&& lv_topStories.getSelectionModel().getSelectedItem().getKids() != null) {
					TreeItem commentRootItem = new TreeItem("Comments");
					tv_CommentTree.setRoot(commentRootItem);
					tv_CommentTree.setShowRoot(false);
					for (int commentId : lv_topStories.getSelectionModel().getSelectedItem().getKids()) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								try {
									HNComment comment = HackerNewsAPI.getComment(commentId);
									TreeItem<WebView> commentTreeItem = getCommentTree(comment, true);
									commentRootItem.getChildren().add(commentTreeItem);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						tv_CommentTree.refresh();
					}
				}
			}
		});
		prgWv.progressProperty().bind(wv_hnStory.getEngine().getLoadWorker().progressProperty());
		prgWv.prefWidthProperty().bind(wv_hnStory.widthProperty());

		mainSplitPane.setDividerPositions(0.25f, 0.75f);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static TreeItem getCommentTree(HNComment rootComment, boolean topLevel) {
		TreeItem<WebViewFitContent> rootCommentItem = null;
		if (topLevel)
			rootCommentItem = new TreeItem<WebViewFitContent>(
					new WebViewFitContent(rootComment.getText(), " #f7f7f7", tbp_ContentWidth));
		else
			rootCommentItem = new TreeItem<WebViewFitContent>(
					new WebViewFitContent(rootComment.getText(), "", tbp_ContentWidth));
		for (HNComment comment : rootComment.getKids()) {
			if (comment.getKids().size() > 0)
				rootCommentItem.getChildren().add(getCommentTree(comment, false));
			else
				continue;
		}
		return rootCommentItem;

	}

	@FXML
	public void handleStoryMouseClick(MouseEvent arg0) {
		wv_hnStory.getEngine().load("about:blank");
		tbp_Content.getSelectionModel().select(0);
		WebEngine wvEngine;
		wvEngine = wv_hnStory.getEngine();
		wvEngine.load(lv_topStories.getSelectionModel().getSelectedItem().getUrl());
	}

}

package com.pnatu.hackernews.gui;

import java.io.IOException;

import com.pnatu.hackernews.api.HackerNewsAPI;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TopStoriesScreen extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("test_list_top_stories.fxml"));

			Scene scene = new Scene(root, 500, 500);

			primaryStage.setTitle("FXML Welcome");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void testHNAPI() throws IOException {
		Task task = new Task<Void>() {
			@Override
			public Void call() throws IOException {
				System.out.println(HackerNewsAPI.getTopStories());
				return null;
			}
		};
		new Thread(task).start();
	}
}

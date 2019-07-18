package com.pnatu.hackernews.gui;

import java.io.IOException;

import com.pnatu.hackernews.api.HackerNewsAPI;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Button btn_test = new Button();
			btn_test.setText("Test");
			btn_test.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					try {
						testHNAPI();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			BorderPane root = new BorderPane(btn_test);
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("HackerNews");
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
		    @Override public Void call() throws IOException {
		        System.out.println(HackerNewsAPI.getTopStories());
		        return null;
		    }
		};
		new Thread(task).start();
	}
}

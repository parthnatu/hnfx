package com.pnatu.hackernews.commons;

import java.util.Set;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;

public final class WebViewFitContent extends Region {

	final WebView webview = new WebView();
	final WebEngine webEngine = webview.getEngine();

	public WebViewFitContent(String content, String backgroundColor, double prefWidth) {
		webview.setPrefHeight(5);
		webview.setPrefWidth(prefWidth - 20);
		widthProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				Double width = (Double) newValue;
				webview.setPrefWidth(width);
				adjustHeight();
			}
		});

		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {

				if (newState == Worker.State.SUCCEEDED) {
					adjustHeight();
				}

			}
		});

		webview.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
			public void onChanged(Change<? extends Node> change) {
				Set<Node> scrolls = webview.lookupAll(".scroll-bar");
				for (Node scroll : scrolls) {
					scroll.setVisible(false);
				}
			}
		});

		setContent(content, backgroundColor);
		getChildren().add(webview);
	}

	public void setContent(final String content, String backgroundColor) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				webEngine.loadContent(getHtml(content, backgroundColor));
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						adjustHeight();
					}
				});
			}
		});
	}

	@Override
	protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		layoutInArea(webview, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
	}

	private void adjustHeight() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					Object result = webEngine.executeScript("document.getElementById('mydiv').offsetHeight");
					if (result instanceof Integer) {
						Integer i = (Integer) result;
						double height = new Double(i);
						height = height + 20;
						webview.setPrefHeight(height);
					}
				} catch (JSException e) {
				}
			}
		});
	}

	private String getHtml(String content, String color) {
		return "<html><style>body{overflow-x: hidden;overflow-y: hidden; background-color:" + color + ";}</style><body>"
				+ "<div id=\"mydiv\">" + content + "</div><hr>" + "</body></html>";
	}

}
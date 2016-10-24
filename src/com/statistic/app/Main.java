package com.statistic.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	//glowny kontener aplikacji
	private Stage rootStage;
	//panel okna
	private BorderPane rootPane;

	@Override
	public void start(Stage stage) throws Exception {
		this.rootStage = stage;
		this.rootStage.setTitle("Estymacja");
		initWindowRoot();
		initDataView();
	}
	
	/**
	 * Inicializacja g³ównego okna aplikacji
	 */
	public void initWindowRoot() {
		try {
			//ladowanie pliku
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/WindowRoot.fxml"));
			rootPane = (BorderPane) loader.load();
			//ustawienie sceny
			Scene scene = new Scene(rootPane);
			rootStage.setScene(scene);
			rootStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Inicializacja okna z danymi
	 */
	public void initDataView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/DataView.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			//ustawienie panelu
			rootPane.setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getter dla stage
	 * @return stage
	 */
	public Stage getStage() {
		return rootStage;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}

package com.statistic.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.*;
import com.statistic.app.model.Data;
import com.statistic.app.view.DataViewController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	//glowny kontener aplikacji
	private Stage rootStage;
	//panel okna
	private BorderPane rootPane;
	
	private ObservableList<Data> group = FXCollections.observableArrayList();
	private ObservableList<Data> sample = FXCollections.observableArrayList();

	public Main() {
		Path path = Paths.get(URI.create("file:///C:/workspace/Miasta.csv"));
		List<List<String>> value = readRecords(path);
		for (List<String> x : value) {
			group.add(new Data(x.get(0),
					Integer.valueOf(x.get(1)),
					Double.valueOf(x.get(2))));
		}
		
		logger.info("Wczytano " + group.size());
		
		Path p = Paths.get(URI.create("file:///C:/workspace/sample.csv"));
		List<List<String>> v = readRecords(p);
		for (List<String> x : v) {
			sample.add(new Data(x.get(0),
					Integer.valueOf(x.get(1)),
					Double.valueOf(x.get(2))));
		}
		
		logger.info("Wczytano " + sample.size());
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.rootStage = stage;
		this.rootStage.setTitle("Estymacja");
		initWindowRoot();
		initDataView();
	}
	
	/**
	 * Inicializacja g��wnego okna aplikacji
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
			
			DataViewController controller = loader.getController();
			controller.setReference(this);
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
	
	public ObservableList<Data> getData() {
		return group;
	}
	
	public ObservableList<Data> getSample() {
		return sample;
	}
 	
	
	List<List<String>> readRecords(Path p) {
	        try (BufferedReader reader = Files.newBufferedReader(p)) {
	            return reader.lines()
	            		.skip(1)
	                    .map(line -> Arrays.asList(line.split(";")))
	                    .collect(Collectors.toList());
	        } catch (IOException e) {
	            throw new UncheckedIOException(e);
	        }
	    }  
	 
	public static void main(String[] args) {
		launch(args);
	}

}

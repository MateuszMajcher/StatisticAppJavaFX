package com.statistic.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.util.stream.*;
import com.statistic.app.model.Data;
import com.statistic.app.model.DataWrapper;
import com.statistic.app.util.WindowUtil;
import com.statistic.app.view.DataEditController;
import com.statistic.app.view.DataViewController;
import com.statistic.app.view.WindowRootController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
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
		debug();
		
	}
	
	private void debug() {
		Path path = Paths.get(URI.create("file:///C:/Users/Mateusz/workspace/EstymacjaApp/dane.csv"));
		List<List<String>> value = readRecords(path);
		for (List<String> x : value) {
			group.add(new Data(x.get(0),
					Integer.valueOf(x.get(2)),
					Double.valueOf(x.get(1))));
		}
		
		logger.info("Wczytano " + group.size());
		
		Path p = Paths.get(URI.create("file:///C:/workspace/sample.csv"));
		List<List<String>> v = readRecords(p);
		for (List<String> x : v) {
			sample.add(new Data(x.get(0),
					Integer.valueOf(x.get(2)),
					Double.valueOf(x.get(1))));
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
			
			//ustawienie kontrolera
			WindowRootController controller = loader.getController();
			controller.setMain(this);
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
 	
	/**
	 * Wyswietlanie okna dodawania i edycji danych
	 * @param data edytowany obiekt
	 * @return czy zatwierdzono
	 */
	public boolean showEditDataDialog(Data data) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/DataEdit.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			//utworzenie stage dla okna
			Stage editStage = new Stage();
			editStage.setTitle("Edycja");
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.initOwner(rootStage);
			Scene scene = new Scene(pane);
			editStage.setScene(scene);
			
			//ustawienie kontrolera
			DataEditController controller = loader.getController();
			controller.setStage(editStage);
			controller.setData(data);
			
			editStage.showAndWait();
			
			return controller.isOkClick();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	} 
	
	

	/**
	 * Ladowanie dabych z pliku
	 * @param file
	 */
	public void loadDataFromFile(File file, ObservableList<Data> data) {
		try {
			JAXBContext ctx = JAXBContext.newInstance(DataWrapper.class);
			Unmarshaller m = ctx.createUnmarshaller();
			
			DataWrapper wrapper = (DataWrapper) m.unmarshal(file);
			data.clear();
			data.addAll(wrapper.getData());
	
		} catch (Exception e) {
			WindowUtil.showAlert(AlertType.ERROR,
					null, 
					"Error", 
					"Nie mozna odczytac danych", 
					"NIe mozna odzczytac danych z "  + file.getPath());
		}
	}
	
	/**
	 * Zapis danych do pliku
	 * @param file
	 */
	public void saveDataToFile(File file, ObservableList<Data> data) {
		try {
			JAXBContext ctx = JAXBContext.newInstance(DataWrapper.class);
			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			DataWrapper w = new DataWrapper();
			w.setData(data);
			
			m.marshal(w, file);
		} catch (Exception e) {
			e.printStackTrace();
			WindowUtil.showAlert(AlertType.ERROR,
					null, 
					"Error", 
					"Nie mozna zapisac danych", 
					"NIe mozna zapisac danych w "  + file.getPath());
		}
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
	 
	/**
	 * Zwraca ostatnio otwarty plik
	 * @return ostatnio otwarty plik
	 */
	public File getDataFilePath(String value) {
		Preferences p = Preferences.userNodeForPackage(Main.class);
		String Path = p.get(value, null);
		if (Path != null) {
			return new File(Path);
		} else {
			return null;
		}
	}
	
	/**
	 * Ustawienie sciezki otwartego pliku w rejestrze systemowym
	 * @param file plik dla ktorego ma zostac ustawiona sciezka
	 */
	public void setDataFilePath(File file, String value) {
		Preferences p = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			p.put(value, file.getPath());
			rootStage.setTitle("Estymacja " + file.getPath());
		} else {
			p.remove(value);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

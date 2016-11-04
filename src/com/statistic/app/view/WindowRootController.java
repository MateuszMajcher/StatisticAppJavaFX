package com.statistic.app.view;

import java.io.File;
import java.util.List;

import com.statistic.app.Main;
import com.statistic.app.model.Data;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class WindowRootController {

	private Main main;

	/**
	 * Ustawienie referencji do maina
	 * 
	 * @param main
	 */
	public void setMain(Main main) {
		this.main = main;
	}

	@FXML
	private void handleNew() {
		main.getData().clear();
		main.getSample().clear();
		main.setDataFilePath(null, "data");
		main.setDataFilePath(null, "sample");
	}

	
	/**
	 * Szablon otwarcia pliku
	 * @param data lista do ktorej zostana wczyatane dane
	 */
	private void handleOpen(ObservableList<Data> data, String value) {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// okno wyboru
		File file = fileChooser.showOpenDialog(main.getStage());

		if (file != null) {
			main.loadDataFromFile(file, data);
			main.setDataFilePath(file, value);
		}
	}
	
	@FXML
	private void handleOpenGroup() {
		handleOpen(main.getData(), "data");
	}
	
	@FXML
	private void handleOpenSample() {
		handleOpen(main.getSample(), "sample");
	}
	
	@FXML
	private void handleSaveAsGroup() {
		handleSaveAs(main.getData());
	}
	
	@FXML
	private void handleSaveAsSample() {
		handleSaveAs(main.getSample());
	}

	/**
	 * Zapis plików
	 * @param data lista ktora zostanie zapisana
	 */
	@FXML
	private void handleSave() {
		File file = main.getDataFilePath("data");
		
		if (file != null) {
			System.out.println("zapis");
			main.saveDataToFile(file, main.getData());
		} else {
			handleSaveAs(main.getData());
		}
		file = main.getDataFilePath("sample");
		
		if (file != null) {
			System.out.println("zapisa");
			main.saveDataToFile(file, main.getSample());
		} else {
			handleSaveAs(main.getSample());
		}
		
		
	}

	
	/**
	 * Szablon zapisu pliku
	 * @param data lista ktora ma byc zapisana
	 */
	private void handleSaveAs(ObservableList<Data> data) {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(main.getStage());

		if (file != null) {
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			main.saveDataToFile(file, data);
		}
	}

	/**
	 * O apliakcji
	 */
	@FXML
	private void handleAbout() {
	
	}

	/**
	 * Zamkniecie aplikacji
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}

}

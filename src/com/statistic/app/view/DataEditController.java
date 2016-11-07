package com.statistic.app.view;

import com.statistic.app.model.Data;
import com.statistic.app.util.WindowUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DataEditController {
	
	@FXML
	private TextField cityNameField;
	@FXML
	private TextField populationField;
	@FXML
	private TextField priceField;
	
	private Stage editStage;
	private Data data;
	private boolean isClick = false;
	
	@FXML
	private void initilaize() {}
	
	/**
	 * Ustawienie stage dla którego ma byc wywolane okno
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.editStage = stage;
	}
	
	/**Czy pole tekstowe dla zmiennej objasnianej ma byc aktywne
	 * @param val true - aktywne
	 */
	public void setEstimate(boolean isEstimate) {
		priceField.setDisable(isEstimate);
	}
	
	/**
	 * Utawienie danych do edycji
	 * @param data
	 */
	public void setData(Data data) {
		this.data = data;
		cityNameField.setText(data.getCityName());
		populationField.setText(data.getPopulation().toString());
		priceField.setText(data.getPrice().toString());
	}
	
	
	public boolean isOkClick() {
		return isClick;
	}
	
	@FXML
	private void handleOk() {
		if (validInput()) {
			data.setCityName(cityNameField.getText());
			data.setPopulation(Integer.parseInt(populationField.getText()));
			data.setPrice(Double.parseDouble(priceField.getText()));
			isClick = true;
			editStage.close();
		}
	}
	
	@FXML
	private void handleClose() {
		editStage.close();
	} 
	
	/**
	 * Walidacja danych
	 * @return true jesli dane poprawne
	 */
	private boolean validInput() {
		String error = "";
		if (cityNameField.getText() == null || cityNameField.getText().length() == 0) {
			error += "Podaj nazwe miasta\n";
		}
		if (populationField.getText() == null || populationField.getText().length() == 0) {
			error += "Podaj populacje\n";
		} else if(!WindowUtil.isInt(populationField.getText())) {
			error += "Podaj liczbe dla populacji\n";
		}
		if (priceField.getText() == null || priceField.getText().length() == 0) {
			error += "Podaj cenê\n";
		} else if(!WindowUtil.isDouble(priceField.getText())) {
			error += "Podaj liczbe dla ceny\n";
		}
		
		if (error.length() == 0) {
			return true;
		} else {
			WindowUtil.showAlert(AlertType.ERROR,
					editStage,
					"B³êdne dane",
					"B³êdne dane",
					error);
			
			return false;
		}
	}
	
}

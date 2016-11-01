package com.statistic.app.util;

import javax.print.attribute.standard.RequestingUserName;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class WindowUtil {
	
	
	/**
	 * Generowanie okien
	 * 
	 * @param t Typ okna
	 * @param owner Dla jakiego kontanera ma byc wywolane okno
	 * @param title tytul okna
	 * @param header temat okna
	 * @param content zawartosc okna
	 */
	public static void showAlert(AlertType t,Stage owner, String title, String header, String content) {
		Alert alert = new Alert(t);
		alert.initOwner(owner);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static boolean isDouble(String number) {
		try {
			Double.parseDouble(number);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isInt(String number) {
		try {
			Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}

package com.statistic.app.util;


import java.math.BigDecimal;
import java.math.RoundingMode;

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
	
	/**
	 * Sprawdza czy podany string mozna parsowac na double
	 * @param number ciag tekstowy
	 * @return true jesli mozna parsowac
	 */
	public static boolean isDouble(String number) {
		try {
			Double.parseDouble(number);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Sprawdza czy podany string mozna parsowac na int
	 * @param number ciag tekstowy
	 * @return true jesli mozna parsowac
	 */
	public static boolean isInt(String number) {
		try {
			Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Konwertuje string na double i ustawia precyzje
	 * @param val string
	 * @param p wartosc precyzji
	 * @return wartosc double
	 */
	public static Double setDoublePrecision(String val, int p) {
		return setDoublePrecision(Double.parseDouble(val), p);
	}
	
	/**
	 * Ustawia precyzje dla double
	 * @param val wartosc double
	 * @param p wartosc precyzji
	 * @return wartosc double
	 */
	public static Double setDoublePrecision(Double val, int p) {
		return BigDecimal
				.valueOf(val)
				.setScale(p, RoundingMode.HALF_UP)
				.doubleValue();
	}
}

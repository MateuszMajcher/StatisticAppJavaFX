package com.statistic.app.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Data {
	
	private final StringProperty cityName;
	private final IntegerProperty population;
	private final DoubleProperty price;
	
	public Data() {
		this(null, null, null);
	}
	
	public Data(String city, Integer population, Double price) {
		this.cityName = new SimpleStringProperty(city);
		this.population = new SimpleIntegerProperty(population);
		this.price = new SimpleDoubleProperty(price);
	}
	
	public String getCityName() {
			return cityName.get();
	}
	
	public void setCityName(String cityName) {
		this.cityName.set(cityName);
	}
	
	public StringProperty getCityNameProperty() {
		return cityName;
	}
	
	public Integer getPopulation() {
		return population.get();
	}
	
	public void setPopulation(Integer population) {
		this.population.set(population);
	}
	
	public IntegerProperty getPopulationProperty() {
		return population;
	}
	
	public Double getPrice() {
		return price.get();
	}
	
	public void setPrice(Double population) {
		this.price.set(population);
	}
	
	public DoubleProperty getPriceProperty() {
		return price;
	}
	
	

}

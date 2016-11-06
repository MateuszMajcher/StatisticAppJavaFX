package com.statistic.app.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.statistic.app.model.Data;
import com.statistic.app.util.Statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TestStatistic {

	ObservableList<Data> data;
	
	//dane
	//http://www.dreamincode.net/forums/topic/328935-simple-regression-library-part-2-linear-regression-model/
	double[] x = { 2, 3, 4, 5, 6, 8, 10, 11 };
	double[] y = { 21.05, 23.51, 24.23, 27.71, 30.86, 45.85, 52.12, 55.98 };

	@Before
	public void init() {

		System.out.println("Expected output from Excel: y = 9.4763 + 4.1939x");
		data = FXCollections.observableArrayList();
		data.add(new Data("c", 20154, 125.0));
		data.add(new Data("f", 5487, 582.5));
		data.add(new Data("g", 69584, 11524.0));
		data.add(new Data("r", 321884, 544.0));
		data.add(new Data("c", 215484, 52.0));
	}

	@Test
	public void testLinearRegression() {
		Double[] val = Statistics.LinearRegression(x, y);
		assertEquals(Double.valueOf(4.193873121869783), val[0]);
		assertEquals(Double.valueOf(9.476277128547583), val[1]);
	}
}

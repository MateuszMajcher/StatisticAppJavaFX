package com.statistic.app.test;

import org.junit.Before;

import com.statistic.app.model.Data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TestStatistic {
		
		ObservableList<Data> data;
	
		@Before
		public void init() {
			data = FXCollections.observableArrayList();
			data.add(new Data("c", 20154, 125.0));
			data.add(new Data("f", 5487, 582.5));
			data.add(new Data("g", 69584, 11524.0));
			data.add(new Data("r", 321884, 544.0));
			data.add(new Data("c", 215484, 52.0));
		}
}

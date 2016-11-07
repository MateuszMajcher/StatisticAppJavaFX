package com.statistic.app.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.statistic.app.model.Data;
import com.statistic.app.util.Statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TestStatistic {

	private static final double DELTA = 1e-15;
	ObservableList<Data> sample = FXCollections.observableArrayList();

	// dane
	// http://www.dreamincode.net/forums/topic/328935-simple-regression-library-part-2-linear-regression-model/

	List<List<String>> readRecords(Path p) {
		try (BufferedReader reader = Files.newBufferedReader(p)) {
			return reader.lines().skip(1).map(line -> Arrays.asList(line.split(";"))).collect(Collectors.toList());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Before
	public void init() {

		Path p = Paths.get(URI.create("file:///C:/Users/Mateusz/workspace/EstymacjaApp/dane.csv"));
		List<List<String>> v = readRecords(p);
		for (List<String> x : v) {
			sample.add(new Data(x.get(0), Integer.valueOf(x.get(2)), Double.valueOf(x.get(1))));
		}

	}

	@Test
	public void loadDataAndsizeTest() {
		assertEquals("should be 130 records", 130, sample.size());
	}

	@Test
	public void StatisticMaxTest() {
		OptionalDouble val = Statistics.maxValue(sample, Data::getPrice);
		assertEquals("should be 130 records", 8325.0, val.getAsDouble(), DELTA);
	}

	@Test
	public void testLinearRegression() {

		// assertEquals(Double.valueOf(4.193873121869783), val[0]);
		// assertEquals(Double.valueOf(9.476277128547583), val[1]);
	}
}

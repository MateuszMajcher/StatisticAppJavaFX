package com.statistic.app.view;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.fx.FXGraphics2D;

import com.statistic.app.Main;
import com.statistic.app.model.Data;
import com.statistic.app.util.Statistics;
import com.statistic.app.util.WindowUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DataViewController{
	private static final Logger logger = Logger.getLogger(DataViewController.class.getName());
	
	@FXML 
	private TabPane tabs;
	
	@FXML
	private TableView<Data> dataTable;
	
	@FXML
	private TableColumn<Data, String> cityColumn;
	
	@FXML
	private TableColumn<Data, Integer> populationColumn;
	
	@FXML
	private TableColumn<Data, Double> priceColumn;
	
	
	@FXML
	private TableView<Data> dataTableE;
	
	@FXML
	private TableColumn<Data, String> cityColumnE;
	
	@FXML
	private TableColumn<Data, Integer> populationColumnE;
	
	@FXML
	private TableColumn<Data, Double> priceColumnE;
	
	@FXML
	private Button showStat;
	
	//aktualna zakladka
	TableView<Data> tempTabs;
	
	//aktualna zakladka
	Integer idTabs = 0;
	
	
	@FXML
	private Label count; //srednia
	@FXML
	private Label max; //max
	@FXML
	private Label min; //min
	@FXML
	private Label average; //wartosc oczekiwana
	@FXML
	private Label median; //mediana
	@FXML
	private Label standardDeviation; //odchylenie stand.
	@FXML
	private Label correlation; //korelacja
	@FXML
	private Label rA;  //regresja alpha
	@FXML
	private Label rB;  //regresja beta
	@FXML
	private Label q1; //Q1
	@FXML
	private Label q3; //Q3
	@FXML
	private Label irq;  //IRQ
	@FXML
	private Label points; //Punkty oddalone
	
	
	//referencja do maina
	private Main main;
	
	//statystyka
	private Double[] regression;
	private Double q_1;
	private Double q_3;
	
	//Wykres
	JFreeChart chart;
	XYDataset dataset;
	
	
	
	public DataViewController() {}
	
	@FXML
	private void initialize() throws IOException {
		cityColumn.setCellValueFactory(t -> t.getValue().getCityNameProperty());
		populationColumn.setCellValueFactory(t -> t.getValue().getPopulationProperty().asObject());
		priceColumn.setCellValueFactory(t -> t.getValue().getPriceProperty().asObject());
		
		cityColumnE.setCellValueFactory(t -> t.getValue().getCityNameProperty());
		populationColumnE.setCellValueFactory(t -> t.getValue().getPopulationProperty().asObject());
		priceColumnE.setCellValueFactory(t -> t.getValue().getPriceProperty().asObject());
		
		//ustawienie referencje na tab 0
		setReferenceTab(0);
		
		//listener zmieniajacy refencje tab
		tabs.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				idTabs = newValue.intValue();
				setReferenceTab(idTabs);
			}
		});
		
		//wyswietlanie wykresu
		showStat.setOnAction(
		        new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		                final Stage stage = new Stage();
		                stage.initModality(Modality.APPLICATION_MODAL);
		                stage.initOwner(main.getStage());
		               
		                
						try {
							
							chart = createChart(dataset);
							drawRegressionLine();
			                ChartCanvas canvas = new ChartCanvas(chart);
			                StackPane stackPane = new StackPane(); 
			                stackPane.getChildren().add(canvas);  
			                // Bind canvas size to stack pane size. 
			                canvas.widthProperty().bind( stackPane.widthProperty()); 
			                canvas.heightProperty().bind( stackPane.heightProperty());  
			                stage.setScene(new Scene(stackPane)); 
			                stage.setTitle("FXGraphics2DDemo1.java"); 
			                stage.setWidth(700);
			                stage.setHeight(390);
			                stage.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		     
		            }
		         });
		
	}

	/**
	 * Ustawienie referencji do main
	 * Ustawienie item dla tabel
	 * Uaktualninie statystyk
	 * dodanie listenorow 
	 * @param main referncja do main
	 * @throws IOException 
	 */
	public void setReference(Main main) {
		this.main = main;
		dataTable.setItems(main.getData());
		dataTableE.setItems(main.getSample());
		updateStatistic();
		updateChart();
		main.getData().addListener(new ListChangeListener <Data> () {
		    @Override
		    public void onChanged(javafx.collections.ListChangeListener.Change <? extends Data> c) {
		        
		        while (c.next()) {
		            if (c.wasAdded()) {
		                logger.info("Added: " + c);
		             
		            } else if (c.wasUpdated()) {
		                logger.info("Update");
		            } else {
		                for (Data d: c.getRemoved()) {
		                    logger.info("remove: " + d);
		                }
		            }
		        }
		        updateStatistic();
		        updateChart();
		    }
		});
		
		main.getSample().addListener(new ListChangeListener <Data> () {
		    @Override
		    public void onChanged(javafx.collections.ListChangeListener.Change <? extends Data> c) {
		        
		        while (c.next()) {
		            if (c.wasAdded()) {
		                logger.info("Est Added: " + c);
		                Data d = c.getAddedSubList().get(0);
		                Double price = Statistics.Estimate(d.getPopulation(), regression[1],regression[0]);
		                c.getAddedSubList().get(0).setPrice(price);
		            } else if (c.wasUpdated()) {
		                logger.info("Est Update");
		            } else {
		                for (Data d: c.getRemoved()) {
		                    logger.info("Est remove: " + d);
		                }
		            }
		        }
		       
		    }
		});
		System.out.println("a"+Statistics.getPoints(main.getData(), q_1, q_3));
	}
	
	
	/**
	 * uaktualnienie statystyk
	 */
	public void updateStatistic() {
		
		//normalizacja
		Statistics.Normalize(main.getData());
		//Pobranie statystyk
		DoubleSummaryStatistics stat = Statistics.getStats(main.getData(), Data::getPrice);
		System.out.println(stat);
		//Liczebnosc
		count.setText(Long.toString(stat.getCount()));
		//max
		max.setText(Double.toString(stat.getMax()));
		//min
		min.setText(Double.toString(stat.getMin()));
		
		//wart oczekiwana
		average.setText(Double.toString(stat.getAverage()));
		//mediana
		median.setText(Double.toString(main.getData().size()!=0?
				Statistics.getMedian(main.getData(),Data::getPrice):0));
		//
		standardDeviation.setText(Double.toString(main.getData().size()!=0?
				Statistics.getStandardDeviation(main.getData(),Data::getPrice):0));
		//korelacja
		correlation.setText(Double.toString(main.getData().size()!=0?
				Statistics.getCorrelation(main.getData(), Data::getPrice, Data::getPopulation):0));
		
		//regresja
		regression = Statistics.getLinearRegression(main.getData(), Data::getPopulation, Data::getPrice);
		rA.setText("a="+ regression[0]);
		rB.setText("b="+ regression[1]);
		
		//kwartyle
		q_1 = Statistics.getQ1(main.getData(), Data::getPrice);
		q_3 = Statistics.getQ3(main.getData(), Data::getPrice);
		double ir_q = q_3 - q_1;
		q1.setText("Q1 = " + Double.toString(q_1));
		
		q3.setText("Q3 = " + Double.toString(q_3));
		irq.setText("IRQ = " + Double.toString(ir_q));
		
		//punkty odalone
		points.setText(Double.toString(
				Statistics.getPoints(main.getData(), q_1, q_3)));
		
	System.out.print("d"+Statistics.Estimate(1700000, regression[1],regression[0]));
		
	}
	
	/**
	 * uaktualnienie wykresu
	 */
	public void updateChart() {
		dataset = createDataset();
	}
	
	/**
	 * Usuwanie rekordu
	 */
	@FXML
	private void handleDelete() {
		int id = tempTabs.getSelectionModel().getSelectedIndex();
		if (id >= 0) {
			tempTabs.getItems().remove(id);
		} else {
			WindowUtil.showAlert(AlertType.WARNING,
					main.getStage(),
					"Brak zaznaczonego rekordu",
					"Brak zaznaczonego rekordu!",
					"Aby usun¹æ rekord nalezy go zaznaczyæ.");
		}
	}
	
	/**
	 * Ustawienie refencji do aktywnej zakladki
	 * @param id id aktywnej zakladki tabeli
	 */
	public void setReferenceTab(Integer id) {
		if (id == 0) {
			tempTabs = dataTable;
		} else {
			tempTabs = dataTableE;
		}
	}
	
	/**
	 * Dodanie nowego rekordu
	 */
	@FXML
	private void handleNew() {
		Data t = new Data();
		boolean click = main.showEditDataDialog(t);
		if (click) {
			if (idTabs == 0) 
				main.getData().add(t);
			else if (idTabs == 1)
				main.getSample().add(t);
		}
	}
		
	/**
	 * Edycja istniejacego rekordu
	 */
	@FXML
	private void handleEdit() {
		Data sel = tempTabs.getSelectionModel().getSelectedItem();
		if (sel != null) {
			boolean click = main.showEditDataDialog(sel);
		} else {
			WindowUtil.showAlert(AlertType.WARNING,
					main.getStage(),
					"Brak zaznaczenia", 
					"Brak zaznaczenia", 
					"Zaznacz rekord aby edytowaæ");
		}
	}
	
	/*Wykres*/

	 /**
	 * @author Mateusz
	 * Klasa adapter dla roz FXGraphics2D
	 */
		static class ChartCanvas extends Canvas { 
	        
	        JFreeChart chart;
	        
	        private FXGraphics2D g2;
	        
	        public ChartCanvas(JFreeChart chart) {
	            this.chart = chart;
	            this.g2 = new FXGraphics2D(getGraphicsContext2D());
	            // zmiana rozmiaru
	            widthProperty().addListener(e -> draw()); 
	            heightProperty().addListener(e -> draw()); 
	        }  
	        
	        private void draw() { 
	            double width = getWidth(); 
	            double height = getHeight();
	            getGraphicsContext2D().clearRect(0, 0, width, height);
	            this.chart.draw(this.g2, new Rectangle2D.Double(0, 0, width, 
	                    height));
	        } 
	        
	        @Override 
	        public boolean isResizable() { 
	            return true;
	        }  
	        
	        @Override 
	        public double prefWidth(double height) { return getWidth(); }  
	        
	        @Override 
	        public double prefHeight(double width) { return getHeight(); } 
	    } 
	
	 /**
	  	* Utworzenie danych do rysowania
	 * @return obiekt XYDateset
	 */
		public XYDataset createDataset() {
		 
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series = new XYSeries("Miasto");

			for (Data x : main.getData()) {
				series.add(x.getPopulation(),x.getPrice());
			}
			
			dataset.addSeries(series);

			return dataset;
		}

		/**
		 * Utworznie okna wykresu
		 * @param inputData dane do wyrysowania
		 * @return
		 * @throws IOException
		 */
		private JFreeChart createChart(XYDataset inputData) throws IOException {
			
			JFreeChart chart = ChartFactory.createScatterPlot(
					"ceny za metr kwadratowy (m2) mieszkania w miescie na podstawie liczby mieszkanców miasta", "Liczba mieszkañców", "Cena za metr2", inputData,
					PlotOrientation.VERTICAL, true, true, false);

			XYPlot plot = chart.getXYPlot();
			plot.getRenderer().setSeriesPaint(0, Color.blue);
			return chart;
		}

		/**
		 * Rysowanie lini regresji
		 */
		private void drawRegressionLine() {
			try {
			System.out.println(dataset.getSeriesCount());
			double regressionParameters[] = Regression.getOLSRegression(dataset,
					0);
			System.out.println(regressionParameters[0]);
		
			LineFunction2D linefunction2d = new LineFunction2D(
					regressionParameters[0], regressionParameters[1]);

		
			XYDataset dataset = DatasetUtilities.sampleFunction2D(linefunction2d,
					0D, 3000000, 100, "Regrasja linia");

			// Rysowanie lini
			XYPlot xyplot = chart.getXYPlot();
			xyplot.setDataset(1, dataset);
			XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(
					true, false);
			xylineandshaperenderer.setSeriesPaint(0, Color.YELLOW);
			xyplot.setRenderer(1, xylineandshaperenderer);
			} catch (IllegalArgumentException e) {
				WindowUtil.showAlert(AlertType.WARNING
						,null ,
						"Brak rekordów", 
						"Brak rekordów", 
						"Dodaj dane aby móc wyswietlic wykres");
			}
		}

		private void drawInputPoint(double x, double y) {
			// Create a new dataset with only one row
			XYSeriesCollection dataset = new XYSeriesCollection();
			String title = "Input area: " + x + ", Price: " + y;
			XYSeries series = new XYSeries(title);
			series.add(x, y);
			dataset.addSeries(series);

			XYPlot plot = (XYPlot) chart.getPlot();
			plot.setDataset(2, dataset);
			XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
			plot.setRenderer(2, renderer);
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

}

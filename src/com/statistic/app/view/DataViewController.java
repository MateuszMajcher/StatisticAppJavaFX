package com.statistic.app.view;

import java.util.DoubleSummaryStatistics;
import java.util.logging.Logger;

import com.statistic.app.Main;
import com.statistic.app.model.Data;
import com.statistic.app.util.Statistics;
import com.statistic.app.util.WindowUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DataViewController {
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
	
	/*estymacja*/
	@FXML
	private TableView<Data> dataTableE;
	
	@FXML
	private TableColumn<Data, String> cityColumnE;
	
	@FXML
	private TableColumn<Data, Integer> populationColumnE;
	
	@FXML
	private TableColumn<Data, Double> priceColumnE;
	
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
	
	
	//referencja do maina
	private Main main;
	
	public DataViewController() {}
	
	@FXML
	private void initialize() {
		cityColumn.setCellValueFactory(t -> t.getValue().getCityNameProperty());
		populationColumn.setCellValueFactory(t -> t.getValue().getPopulationProperty().asObject());
		priceColumn.setCellValueFactory(t -> t.getValue().getPriceProperty().asObject());
		
		cityColumnE.setCellValueFactory(t -> t.getValue().getCityNameProperty());
		populationColumnE.setCellValueFactory(t -> t.getValue().getPopulationProperty().asObject());
		priceColumnE.setCellValueFactory(t -> t.getValue().getPriceProperty().asObject());
		
		//ustawienie referencje na tab 0
		setReferenceTab(0);
		
		//akcja zmieniajaca refencje tab
		tabs.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				idTabs = newValue.intValue();
				setReferenceTab(idTabs);
			}
		});
	}

	/**
	 * Ustawienie referencji do main
	 * Ustawienie item dla tabel
	 * Uaktualninie statystyk
	 * dodanie listenorow 
	 * @param main referncja do main
	 */
	public void setReference(Main main) {
		this.main = main;
		dataTable.setItems(main.getData());
		dataTableE.setItems(main.getSample());
		updateStatistic();
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
		    }
		});
		System.out.println(Statistics.getVariance(main.getData(),Data::getPrice));
		System.out.println(Statistics.getMedian(main.getData(),Data::getPrice));
	}
	
	
	/**
	 * uaktualnienie statystyk
	 */
	public void updateStatistic() {
		//normalizacja
		//Statistics.Normalize(main.getData());
		//Pobranie statystyk
		DoubleSummaryStatistics stat = Statistics.getStats(main.getData(), Data::getPrice);
		System.out.println(stat);
		//Liczebnosc
		count.setText(Long.toString(stat.getCount()));
		//max
		max.setText(Double.toString(stat.getMax()));
		//min
		min.setText(Double.toString(stat.getMin()));
		
		
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
}

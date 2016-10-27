package com.statistic.app.view;

import java.util.logging.Logger;

import com.statistic.app.Main;
import com.statistic.app.model.Data;
import com.statistic.app.util.WindowUtil;

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
	
	
	
	
	@FXML
	private Label average; //srednia
	
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
		
	}

	public void setReference(Main main) {
		this.main = main;
		dataTable.setItems(main.getData());
		dataTableE.setItems(main.getSample());
		updateStatistic();
		main.getData().addListener(new ListChangeListener <Data> () {
		    @Override
		    public void onChanged(javafx.collections.ListChangeListener.Change <? extends Data> c) {
		        updateStatistic();
		        while (c.next()) {
		            if (c.wasAdded()) {
		                logger.info("Added");
		            } else if (c.wasUpdated()) {
		                logger.info("Update");
		            } else {
		                for (Data d: c.getRemoved()) {
		                    logger.info("remove: " + d);
		                }
		            }
		        }
		    }
		});
	}
	
	private TableView<Data> getData() {
		int id = tabs.getSelectionModel().getSelectedIndex();
		if (id == 0) return dataTable;
		return null;
	}
	
	/**
	 * uaktualnienie statystyk
	 */
	public void updateStatistic() {
		average.setText(Integer.toString(main.getData().size()));
	}
	
	/**
	 * Usuwanie rekordu
	 */
	@FXML
	private void handleDelete() {
		int id = dataTable.getSelectionModel().getSelectedIndex();
		if (id >= 0) {
			dataTable.getItems().remove(id);
		} else {
			WindowUtil.showAlert(AlertType.WARNING,
					main.getStage(),
					"Brak zaznaczonego rekordu",
					"Brak zaznaczonego rekordu!",
					"Aby usun¹æ rekord nalezy go zaznaczyæ.");
		}
	}
	
	
		
	
}

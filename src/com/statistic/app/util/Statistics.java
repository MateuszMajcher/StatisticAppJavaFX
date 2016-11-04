package com.statistic.app.util;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;

import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import com.statistic.app.model.Data;


public class Statistics {
	
    private Statistics() {
   
    }

    /**
     * Suma wszysktich elementow listy typu double
     * @param data lista dla ktorej ma byc policzona suma
     * @param field pole z obiektu typu T które ma byæ sumowane
     * @return suma
     */
    public static final <T> Double sumDouble(List<T> data, ToDoubleFunction<? super T> field) {
		return data.stream().mapToDouble(field).sum();
	}
    
    /**
     * @param data lista dla ktorej ma byc policzony max
     * @param field pole z obiektu typu T które ma byæ sumowane
     * @return
     */
    public static final <T> OptionalDouble maxValue(List<T> data, ToDoubleFunction<? super T> field) {
 		return data.stream().mapToDouble(field).max();
 	}
    
    /**
     * @param data
     * @param field pole z obiektu typu T które ma byæ sumowane
     * @return
     */
    public static final <T> OptionalDouble minValue(List<T> data, ToDoubleFunction<? super T> field) {
 		return data.stream().mapToDouble(field).min();
 	}
    
    /**
     * Wygenerowanie statystyk 
     * @param data lista dla ktorej maja zostac wygenerowane statystyki
     * @param field pole z obiektu typu T które ma byæ sumowane
     * @return zwraca obiekt DoubleSummaryStatistics
     */
    public static final <T> DoubleSummaryStatistics getStats(List<T> data, ToDoubleFunction<? super T> field) {
		return data
				.stream()
				.collect(Collectors.summarizingDouble(field));
    }
    
    /**
     * Normalizacja danych
     * @param data
     */
    public static void Normalize(List<Data> data) {
    	//Statystyka wszystich pelnych rekordow
    	DoubleSummaryStatistics statPrice = data
    			.stream()
    			.filter(x -> x.getPrice() > 0)
    			.collect(Collectors.summarizingDouble(x -> x.getPrice()));
    	
    	IntSummaryStatistics statPopulation = data
    			.stream()
    			.filter(x -> x.getPopulation() > 0) 
    			.collect(Collectors.summarizingInt(x -> x.getPopulation()));
    	
    	data.stream()
    			.filter(p -> p.getPrice() == 0)
    			.forEach(x -> x.setPrice(statPrice.getAverage()));
    	data.stream()
				.filter(p -> p.getPopulation() == 0)
				.forEach(x -> x.setPopulation((int)statPopulation.getAverage()));
    	
    	
    }
   
    
    /**
     * Zwraca wariancje
     * @param data
     * @param field pole z obiektu typu T które ma byæ sumowane
     * @return wariancja
     */
    public static final double getVariance(List<Data> data, ToDoubleFunction<? super Data> field) {
    	 List<Double> val = data.stream()
    			 .mapToDouble(field)
    			 .boxed()
                 .collect(Collectors.toList());
         return variance(val);
    } 
    
    
    /**
     * Obliczenie wariacji
     * @param list
     * @return
     */
    public static double variance(List<Double> list) {
    	   double sumDiffsSquared = 0.0;
    	   double avg = list.stream().mapToDouble(Double::new).average().getAsDouble();
    	   for (double value : list)
    	   {
    	       double diff = value - avg;
    	       diff *= diff;
    	       sumDiffsSquared += diff;
    	   }
    	   return sumDiffsSquared  / (list.size()-1);
    	}
    
    /**
     * Obliczenie mediany
     * @param m tablica wartosci
     * @return mediana
     */ 
    public static double median(double[] m) {
    	Arrays.sort(m);
        int middle = m.length/2; 
        if (m.length%2 == 1) {
            return m[middle];
        } else {
           return (m[middle-1] + m[middle]) / 2.0;
        }
    }
    
    /**
     * Zwraca mediane
     * @param data
     * @param field pole z obiektu typu Data
     * @return wartosc mediany
     */
    public static final double getMedian(List<Data> data, ToDoubleFunction<? super Data> field) {
    	double[] arr = data.stream().mapToDouble(field).toArray();
    		return median(arr);
    }
  
}
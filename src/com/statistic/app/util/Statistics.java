package com.statistic.app.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;

import java.util.function.ToDoubleFunction;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.statistic.app.model.Data;
import com.statistic.app.view.DataViewController;


public class Statistics {
	private static final Logger logger = Logger.getLogger(Statistics.class.getName());
	
    private Statistics() {
    }

    /**
     * Suma wszysktich elementow listy typu double
     * @param data lista dla ktorej ma byc policzona suma
     * @param field metoda referencyjna ktora zwraca wartosc, dl której ma byc policzona suma
     * @return suma suma elementow
     */
    public static final <T> Double sumDouble(List<T> data, ToDoubleFunction<? super T> field) {
		return data.stream().mapToDouble(field).sum();
	}
    
    /**
     * Zwaraca MAX
     * @param data lista dla ktorej ma byc policzony max
     * @param field metoda referencyjna ktora zwraca wartosc z obiektu typu T które ma byæ zwrocony MAX
     * @return
     */
    public static final <T> OptionalDouble maxValue(List<T> data, ToDoubleFunction<? super T> field) {
 		return data.stream().mapToDouble(field).max();
 	}
    
    /**
     * Zwraca MIN
     * @param data
     * @param field metoda referencyjna ktora zwraca wartosc z obiektu typu T które ma byæ zwrocony MIN
     * @return
     */
    public static final <T> OptionalDouble minValue(List<T> data, ToDoubleFunction<? super T> field) {
 		return data.stream().mapToDouble(field).min();
 	}
    
    /**
     * Wygenerowanie statystyk 
     * @param data lista dla ktorej maja zostac wygenerowane statystyki
     * @param field metoda referencyjna ktora zwraca wartosc
     * @return zwraca obiekt DoubleSummaryStatistics
     */
    public static final <T> DoubleSummaryStatistics getStats(List<T> data, ToDoubleFunction<? super T> field) {
		return data
				.stream()
				.collect(Collectors.summarizingDouble(field));
    }
    
    /**
     * Normalizacja danych
     * @param data Lista obiektów do znormalizowania
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
     * @param field metoda referencyjna ktora zwraca wartosc, dla policzenia wariacji
     * @return wariancja wartosc wariancji
     */
    public static final double getVariance(List<Data> data, ToDoubleFunction<? super Data> field) {
    	//pobranie zawartosc z pola filed do listy
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
    
    public static final double getStandardDeviation(List<Data> data, ToDoubleFunction<? super Data> field)
    {
        return Math.sqrt(getVariance(data, field));
    }
    
    /**
     * Zwraca wsp. Korelacji Pearsona
     * @param data lista dla ktorej ma byc policzony wsp korelacji
     * @param fieldX pole x
     * @param fieldY pole y
     * @return wartosc wsp
     */
    public static final double getCorrelation(List<Data> data, ToDoubleFunction<? super Data> fieldX, ToDoubleFunction<? super Data> fieldY)
    {
    	List<Double> arrX = data.stream()
   			 .mapToDouble(fieldX)
   			 .boxed()
                .collect(Collectors.toList());
    	List<Double> arrY = data.stream()
      			 .mapToDouble(fieldY)
      			 .boxed()
                   .collect(Collectors.toList());
    	double numerator = calcuteNumerator(arrX, arrY);  
        double denominator = calculateDenominator(arrX, arrY); 

        double wsp = numerator/denominator;
        logger.info("wsp korelacji: " + wsp);
        return wsp;
    }
  
     
    /**
     * @param xList
     * @param yList
     * @return
     */
    public static double calcuteNumerator(List<Double> xList, List<Double> yList){  
        double result =0.0;  
        double xAverage = 0.0;  
        double temp = 0.0;  
          
        int xSize = xList.size();  
        for(int x=0;x<xSize;x++){  
            temp += xList.get(x);  
        }  
        xAverage = temp/xSize;  
         
        double yAverage = 0.0;  
        temp = 0.0;  
        int ySize = yList.size();  
        for(int x=0;x<ySize;x++){  
            temp += yList.get(x);  
        }  
        yAverage = temp/ySize;  
   

        for(int x=0;x<xSize;x++){  
        	 result+=((Double)(xList.get(x))-xAverage)*((Double)(yList.get(x))-yAverage); 
        } 
        return result;  
    }  
    
    /**
     * @param xList
     * @param yList
     * @return
     */
    public static double calculateDenominator(List<Double> xList,List<Double> yList){  
        double standardDifference = 0.0;  
        int size = xList.size();  
        double xAverage = 0.0;  
        double yAverage = 0.0;  
        double xException = 0.0;  
        double yException = 0.0;  
        double temp = 0.0;  
        for(int i=0;i<size;i++){  
            temp += xList.get(i);  
        }  
        xAverage = temp/size;  
          
        for(int i=0;i<size;i++){  
            temp += yList.get(i);  
        }  
        yAverage = temp/size;  
          
        for(int i=0;i<size;i++){  
            xException += Math.pow(xList.get(i)-xAverage,2);  
            yException += Math.pow(yList.get(i)-yAverage, 2);  
        }   
        return standardDifference = Math.sqrt(xException*yException);  
    }  
    
    
    


}
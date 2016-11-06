package com.statistic.app.util;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Predicate;
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
     * @param field metoda referencyjna ktora zwraca wartosc, dl kt�rej ma byc policzona suma
     * @return suma suma elementow
     */
    public static final <T> Double sumDouble(List<T> data, ToDoubleFunction<? super T> field) {
		return data.stream().mapToDouble(field).sum();
	}
    
    /**
     * Zwaraca MAX
     * @param data lista dla ktorej ma byc policzony max
     * @param field metoda referencyjna ktora zwraca wartosc z obiektu typu T kt�re ma by� zwrocony MAX
     * @return
     */
    public static final <T> OptionalDouble maxValue(List<T> data, ToDoubleFunction<? super T> field) {
 		return data.stream().mapToDouble(field).max();
 	}
    
    /**
     * Zwraca MIN
     * @param data
     * @param field metoda referencyjna ktora zwraca wartosc z obiektu typu T kt�re ma by� zwrocony MIN
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
     * @param data Lista obiekt�w do znormalizowania
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
    
    
    /**
     * Oblicza a i b dla regresji liniowej
     * @param x 
     * @param y 
     * @return 
     * @throws java.lang.IllegalArgumentException jesli tablice nie sa tego samego rozmiaru
     */
    public static Double[] LinearRegression(double[] x, double[] y) {
    	 int N;
    	 double alpha;
    	 double beta;

        if (x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        N = x.length;

        // first pass
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < N; i++) sumx  += x[i];
        for (int i = 0; i < N; i++) sumx2 += x[i]*x[i];
        for (int i = 0; i < N; i++) sumy  += y[i];
        double xbar = sumx / N;
        double ybar = sumy / N;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < N; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        beta  = xybar / xxbar;
        alpha = ybar - beta * xbar;
        logger.info("regresja x: " + beta + ", alpha: " +alpha);
        
        return new Double[]{beta, alpha};
    
    }

    
    /**
     * Zwraca a i b dla regresji liniowej
     * @param data
     * @param fieldX
     * @param fieldY
     * @return
     */
    public static Double[] getLinearRegression(List<Data> data, ToDoubleFunction<? super Data> fieldX, ToDoubleFunction<? super Data> fieldY) {
    	double[] arrx = data.stream().mapToDouble(fieldX).toArray();
    	double[] arry = data.stream().mapToDouble(fieldY).toArray();
    	return LinearRegression(arrx, arry);
    }
    
    
    /************************/
    /** 
     * Calculates the median for a list of values (<code>Number</code> objects) 
     * that are assumed to be in ascending order. 
     *  
     * @param values  the values. 
     * @param copyAndSort  a flag that controls whether the list of values is 
     *                     copied and sorted. 
     *  
     * @return The median. 
     */ 
    public static double calculateMedian(List values, boolean copyAndSort) { 
         
        double result = Double.NaN; 
        if (values != null) { 
            if (copyAndSort) { 
                int itemCount = values.size(); 
                List copy = new ArrayList(itemCount); 
                for (int i = 0; i < itemCount; i++) { 
                    copy.add(i, values.get(i));    
                } 
                Collections.sort(copy); 
                values = copy; 
            } 
            int count = values.size(); 
            if (count > 0) { 
                if (count % 2 == 1) { 
                    if (count > 1) { 
                        Number value = (Number) values.get((count - 1) / 2); 
                        result = value.doubleValue(); 
                    } 
                    else { 
                        Number value = (Number) values.get(0); 
                        result = value.doubleValue(); 
                    } 
                } 
                else { 
                    Number value1 = (Number) values.get(count / 2 - 1); 
                    Number value2 = (Number) values.get(count / 2); 
                    result = (value1.doubleValue() + value2.doubleValue())  
                             / 2.0; 
                } 
            } 
        } 
        return result; 
    } 
    
    /** 
     * Calculates the median for a sublist within a list of values  
     * (<code>Number</code> objects). 
     *  
     * @param values  the values (in any order). 
     * @param start  the start index. 
     * @param end  the end index. 
     *  
     * @return The median. 
     */ 
    public static double calculateMedian(List values, int start, int end) { 
        return calculateMedian(values, start, end, true); 
    } 

    /** 
     * Calculates the median for a sublist within a list of values  
     * (<code>Number</code> objects).  The entire list will be sorted if the  
     * <code>ascending</code< argument is <code>false</code>. 
     *  
     * @param values  the values. 
     * @param start  the start index. 
     * @param end  the end index. 
     * @param copyAndSort  a flag that that controls whether the list of values  
     *                     is copied and sorted. 
     *  
     * @return The median. 
     */ 
    public static double calculateMedian(List values, int start, int end, 
                                         boolean copyAndSort) { 
         
        double result = Double.NaN; 
        if (copyAndSort) { 
            List working = new ArrayList(end - start + 1); 
            for (int i = start; i <= end; i++) { 
                working.add(values.get(i));   
            } 
            Collections.sort(working);  
            result = calculateMedian(working, false); 
        } 
        else { 
            int count = end - start + 1; 
            if (count > 0) { 
                if (count % 2 == 1) { 
                    if (count > 1) { 
                        Number value  
                            = (Number) values.get(start + (count - 1) / 2); 
                        result = value.doubleValue(); 
                    } 
                    else { 
                        Number value = (Number) values.get(start); 
                        result = value.doubleValue(); 
                    } 
                } 
                else { 
                    Number value1 = (Number) values.get(start + count / 2 - 1); 
                    Number value2 = (Number) values.get(start + count / 2); 
                    result  
                        = (value1.doubleValue() + value2.doubleValue()) / 2.0; 
                } 
            } 
        } 
        return result;     
         
    } 
    
    
    /**
     * Obliczenie 1 kwartylu.
     * @param values  Lista wartosci do oblicznia.
     * @return wartosc.
     */
    public static double computeQ1(List<Double> values) {
    	Collections.sort(values);
        double result = Double.NaN;
        int count = values.size();
        if (count > 0) {
            if (count % 2 == 1) {
                if (count > 1) {
                    result = calculateMedian(values, 0, count / 2);
                }
                else {
                    result = calculateMedian(values, 0, 0);
                }
            }
            else {
                result = calculateMedian(values, 0, count / 2 - 1);
            }
            
        }
        return result;
    }
    
    /**
     * Obliczenie 3 kwartylu.
     * @param values  Lista wartosci do oblicznia.
     * @return wartosc.
     */
    public static double computeQ3(List<Double> values) {
    	Collections.sort(values);
        double result = Double.NaN;
        int count = values.size();
        if (count > 0) {
            if (count % 2 == 1) {
                if (count > 1) {
                    result = calculateMedian(
                        values, count / 2, count - 1
                    );
                }
                else {
                    result = calculateMedian(values, 0, 0);
                }
            }
            else {
                result = calculateMedian(
                    values, count / 2, count - 1
                );
            }
            
        }
        return result;
    }
    
  
    public static double getQ1(List<Data> data, ToDoubleFunction<? super Data> field) {
    	//pobranie zawartosc z pola filed do listy
   	 List<Double> val = data.stream()
   			 .mapToDouble(field)
   			 .boxed()
                .collect(Collectors.toList());
    	return computeQ1(val);
    }
    
    public static double getQ3(List<Data> data, ToDoubleFunction<? super Data> field) {
    	//pobranie zawartosc z pola filed do listy
   	 List<Double> val = data.stream()
   			 .mapToDouble(field)
   			 .boxed()
                .collect(Collectors.toList());
    	return computeQ3(val);
    }
    
    /**
     * Predykat dla punktu oddalonego (ponizej Q1)
     * @param q1
     * @param q3
     * @return
     */
    public static Predicate<Data> isPointsBellow(double q1, double q3) {
    	double irq = q3 - q1;
		return p -> p.getPrice() < (q1 - (1.5 * irq));
	}
    
    /**
     * Predykat dla punktu oddalonego (powyzej Q3)
     * @param q1
     * @param q3
     * @return
     */
    public static Predicate<Data> isPointsAbove(double q1, double q3) {
    	double irq = q3 - q1;
		return p -> p.getPrice() > (q3 + (1.5 * irq));
	}
    
    /**
     * Zwraca ilosc punktow oddalonych
     * @param data
     * @param q1
     * @param q3
     * @return
     */
    public static int getPoints(List<Data> data, double q1, double q3) {
		return data.stream().filter(isPointsBellow(q1, q3)).collect(Collectors.<Data>toList()).size() + 
				data.stream().filter(isPointsAbove(q1, q3)).collect(Collectors.<Data>toList()).size();
    }
    

    public static double Estimate(double x, double a, double b) {
        return b*x + a;
    }


}
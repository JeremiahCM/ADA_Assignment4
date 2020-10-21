/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a4;

import java.util.Set;

/**
 *
 * @author lenovoX230
 */
public class BestConversionFinder<E> extends AdjacencyListGraph<E>{
    private static final int NZD = 0;    
    private static final int AUD = 1; //Australian Dollar
    private static final int MXN = 2; //Mexican Peso
    private static final int USD = 3; //US Dollar
    private static final int CAD = 4; //Canadian Dollar
    private static final int PHP = 5; //Philippine Pesos
    private static final int GBP = 6; //Great British Pound
    private static final int JPY = 7; //Japanese Yen
    private static final int MNT = 8; //Mongolian Dollar
    private static final int TOP = 9; //Tonga Pa'anga
    private static final int COP = 10; //Colombian Pesos


    //TODO: find currency exchange rates

    private double[][] exchangeRates;

   
    
    //TODO: accept n x n table of rates
    public BestConversionFinder(double[][] exchangeRates)
    {
        this.exchangeRates = exchangeRates;
    }
    public void createAdjacencyListGraph()
    {
        
    }
   
    public static void main(String[] args) 
    {
        
    }
    
}

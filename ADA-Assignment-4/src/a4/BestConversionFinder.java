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
    private static final int COP = 7; //Colombian Pesos
    private static final int JPY = 8; //Japanese Yen
    private static final int TOP = 9; //Tonga Pa'anga


    private double[][] rates;
    
    
    //TOP -> NZD [9][0] : 0.63111
    
    //TODO: accept n x n table of rates
    public BestConversionFinder(double[][] rates)
    {
        super(GraphType.DIRECTED);
        this.rates = rates;
        createAdjacencyListGraph();
    }
    
    public void createAdjacencyListGraph()
    {
        //exchange rates
        for(int i = 0; i < rates.length; i++)
        {
            this.addVertex(new AdjacencyListVertex(i));
            for(int k = 0; k < rates.length; k++)
            {
                
            }
        }
    }
   
    public static void main(String[] args) 
    {
        double[][] rates = new double[10][10];
        //assign rates here
        rates[NZD][NZD] = 1;
        rates[NZD][AUD] = 0.93481;
        rates[NZD][USD] = 0.66345;
        rates[NZD][PHP] = 32.1637;
        rates[NZD][GBP] = 0.54287;
        rates[NZD][JPY] = 69.57;
        rates[NZD][TOP] = 1.48820;
        
        rates[AUD][AUD] = 1;
        rates[AUD][USD] = 0.70962;
        rates[AUD][CAD] = 0.93076;
        rates[AUD][PHP] = 34.4024;
        rates[AUD][GBP] = 0.54287;
        rates[AUD][JPY] = 74.42;
        
        rates[MXN][MXN] = 1;
        rates[MXN][USD] = 0.04746;
        rates[MXN][PHP] = 2.30061;
        rates[MXN][COP] = 179.687;
        
        rates[USD][USD] = 1;
        rates[USD][NZD] = 1.50691;
        rates[USD][AUD] = 1.40890;
        rates[USD][MXN] = 21.0655;
        rates[USD][CAD] = 1.31162;
        rates[USD][PHP] = 48.4797;
        rates[USD][GBP] = 0.76501;
        rates[USD][COP] = 3786.46;
        rates[USD][JPY] = 104.86;
      
        rates[CAD][CAD] = 1;
        rates[CAD][NZD] = 1.14873;
        rates[CAD][AUD] = 1.70403;
        rates[CAD][GBP] = 0.58317;
 
        rates[PHP][PHP] = 1;
        rates[PHP][NZD] = 0.03102;
        rates[PHP][AUD] = 0.02900;
        rates[PHP][USD] = 0.02058;
        rates[PHP][JPY] = 2.16;
        
        rates[GBP][GBP] = 1;
        rates[GBP][NZD] = 1.96951;
        rates[GBP][AUD] = 1.30699;
        rates[GBP][USD] = 1.30699;
        rates[GBP][CAD] = 1.71428;
        
        rates[COP][COP] = 1;
        rates[COP][MXN] = 179.687;
        rates[COP][USD] = 0.00026;
       
        rates[JPY][JPY] = 1;
        rates[JPY][NZD] = 0.01437;
        rates[JPY][AUD] = 0.01343;
        rates[JPY][USD] = 0.00954;
        rates[JPY][PHP] = 0.46226;
        
        rates[TOP][TOP] = 1;
        rates[TOP][NZD] = 0.63111;
      
        System.out.println(rates[TOP][NZD]);
        System.out.println(rates[USD][CAD]);
        System.out.println(rates.length);
    
    }
    
}

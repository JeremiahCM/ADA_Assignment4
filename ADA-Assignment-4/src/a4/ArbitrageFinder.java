/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a4;

import java.util.HashMap;

/**
 *
 * @author bernie
 */
public class ArbitrageFinder {
      
    private double[][] rates;
    private HashMap<Integer,String> currencies; //for creating each Vertex (linked to the index of the initial n * n table
    BestConversionFinder bcf;
    public ArbitrageFinder(double[][] rates, HashMap<Integer,String> currencies)
    {
        this.rates = rates;
        this.currencies = currencies;
        bcf = new BestConversionFinder(rates,currencies);
    }
    
    public void findArbitrage()
    {
        //If there is no negative weight closed path detected by Bellman-Ford, then no arbitrage.
        if(bcf.bellmanFordPath(0, 0))
        {
            System.out.println("No arbitrage found. Use Best Conversion Finder instead");
        }
        else //if arbitrage is found.
        {
            FloydWarshall fw = new FloydWarshall(bcf.weights);
            System.out.println(bcf);
            System.out.println(fw);
        }
        
    }
    
    
    public static void main(String[] args) {
        //TEST CASE 1-----------------------------------
        HashMap<Integer, String> currs = new HashMap<>();
        currs.put(0, "NZD");
        currs.put(1, "TOP");
        currs.put(2, "AUD");
        currs.put(3, "PHP");
        double[][] rates = new double[currs.size()][currs.size()];
        
        rates[0][1] = 1.49356; // NZD->TOP
        rates[0][2] = 0.93481; // NZD->AUD
        rates[0][3] = 32.2959; //NZD->PHP
        
        rates[1][0] = 0.63111;// TOP->NZD
        rates[1][2] = 0.58826; //TOP->AUD
        
        rates[2][0] = 1.06690; //AUD->NZD
        rates[2][1] = 3.54; //AUD->TOP (change to 1.59501 for no negative weight closed path) 
        
        rates[3][0] = 0.03079; //PHP->NZD
        
        
        
       ArbitrageFinder af = new ArbitrageFinder(rates, currs);
       af.findArbitrage();
        //todo: run floydwarshall from a vertex back to itself, if dorpsto negative - u know theres a closed path with negative weight
    }
}

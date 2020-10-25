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
public class ArbritrageFinder {
    
    
    
    public static void main(String[] args) {
        //TODO: detect with bellman ford
        double[][] rates = new double[10][10];
        HashMap<Integer, String> currs = new HashMap<>();
        currs.put(0, "NZD");
        currs.put(1, "TOP");
        currs.put(2, "AUD");
        currs.put(3, "PHP");
        
        rates[0][1] = 1.49356; // NZD->TOP
        rates[0][2] = 0.93481; // NZD->AUD
        rates[0][3] = 32.2959; //NZD->PHP
        
        rates[1][0] = 0.63111;// TOP->NZD
        rates[1][2] = 0.58826; //TOP->AUD
        
        rates[2][0] = 1.06690; //AUD->NZD
        rates[2][1] = 3.54; //AUD->TOP 1.59501
        
        rates[3][0] = 0.03079; //PHP->NZD
        
        
        BestConversionFinder bcf2 = new BestConversionFinder(rates, currs);
        bcf2.findBestConversion(1, 2);
        
       // AllPairsFloydWarshall fw = new AllPairsFloydWarshall()
        //todo: run floydwarshall from a vertex back to itself, if dorpsto negative - u know theres a closed path with negative weight
    }
}

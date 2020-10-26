/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a4;

import static java.lang.Math.E;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author bernie
 */
public class ArbitrageFinder<E> {
     DecimalFormat df = new DecimalFormat("#.######");
    private double[][] rates;
    private HashMap<Integer,String> currencies; //for creating each Vertex (linked to the index of the initial n * n table
    BestConversionFinder bcf;
    FloydWarshall fw;
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
            fw = new FloydWarshall(bcf.weights, currencies);
            System.out.println(bcf);
            System.out.println(fw);
            printArbitrages();
        }
        
    }
    private void printArbitrages()
    {
        double[][][] d = fw.getDistances();
        double[][][] p = fw.getPTable();       
        
        
        //finds arbitrage path of vertex if possible.
        for(int currentV = 0; currentV < currencies.size(); currentV++)
        {   
            boolean[][] visited = new boolean[currencies.size()][currencies.size()];
            double total = 0;
            int k = 1;
            boolean arbitrageFound = false;
            while(k <= currencies.size() && !arbitrageFound)
            {
                if(d[k][currentV][currentV] < 0) //if theres a negative weight closed path.. (summed from least shortest path from a vertex to itself)
                {
                    arbitrageFound = true;
                    
                }
                else
                    k++;
            }

            if(arbitrageFound)
            {   
                int i = currentV;
                int next = -1;
                boolean pathFound = false;
                 System.out.println(currencies.get(i)+" Arbitrage Path is: ");
                while(!pathFound)
                {
                 
                    next =(int)p[k][i][i];
                    if(!visited[next][i])
                    {
                        visited[next][i] = true;
                        total+=bcf.weights[next][i];
                        System.out.print(currencies.get(i)+"-"+currencies.get(next)+", ");
                          i = next;
                    }
                    else if(i == (int)p[k][currentV][currentV] && i != currentV)
                    {
                        System.out.print(currencies.get(i)+"-"+currencies.get(currentV)+", ");
                        pathFound = true;
                    }
                    else
                    {
                        int c = 0;
                        int ix = 1;

                        while(ix <= k-1)
                        {
                            if(bcf.weights[ix][i] < bcf.weights[c][i] && !visited[ix][i])
                            {
                                c = ix;
                            }                            
                            ix++;
                            
                            if(ix == i)
                                ix++;
                        }
                        
                        next = c;
                        visited[next][i] = true;
                        total += bcf.weights[next][i];
                        System.out.print(currencies.get(i) + "-" + currencies.get(next)+", ");
                        i = next;

                    }
                    
                }
                System.out.println("\b\b");
                System.out.println("1 "+currencies.get(currentV)+" TO "+df.format(1/Math.pow(2, total))+" "+currencies.get(currentV));
                System.out.println();
            }
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
        rates[1][2] = 3.54; //TOP->AUD
        
        rates[2][0] = 1.06690; //AUD->NZD
        rates[2][1] = 1.59501; //AUD->TOP //(change to 1.59501 for no negative weight closed path) 
        
        rates[3][0] = 0.03079; //PHP->NZD
        
        
        
       ArbitrageFinder af = new ArbitrageFinder(rates, currs);
       af.findArbitrage();
        //todo: run floydwarshall from a vertex back to itself, if dorpsto negative - u know theres a closed path with negative weight
    }
}

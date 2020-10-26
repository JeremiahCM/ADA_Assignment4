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
 * @author Bernadette Cruz 17985971
 */
public class ArbitrageFinder<E> {
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
            int k = 1;
            boolean arbitrageFound = false;
            while(k <= currencies.size() && !arbitrageFound)
            {
             //   System.out.println("CURRENT IS "+d[k][currentV][currentV]);
                double rate= 1/Math.pow(2, d[k][currentV][currentV]);
                if(rate > 1) //if theres a negative weight closed path.. (summed from least shortest path from a vertex to itself)
                {
                    arbitrageFound = true;
                    
                }
                else
                    k++;
            }
            if(arbitrageFound)
            {   
                double total = 0;
                int i = currentV;
                int next = -1;
                boolean pathFound = false;
                 System.out.println(currencies.get(i)+" Arbitrage Path is: ");
                while(!pathFound)
                {
                 
                    next =(int)p[k][i][i];
                    if(!visited[next][i] && next!=i)
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
                            if(bcf.weights[ix][i] < bcf.weights[c][i] && !visited[ix][i] && ix!=i)
                            {
                                c = ix;
                            }                            
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
                           
                System.out.println("1 "+currencies.get(currentV)+" TO "+df.format(1/Math.pow(2, +d[k][currentV][currentV]))+" "+currencies.get(currentV));
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
        double[][] rates2 = new double[currs.size()][currs.size()];
        
        rates2[0][1] = 1.49356; // NZD->TOP
        rates2[0][2] = 0.93481; // NZD->AUD
        rates2[0][3] = 32.2959; //NZD->PHP
        
        rates2[1][0] = 0.63111;// TOP->NZD
        rates2[1][2] = 3.54; //TOP->AUD
        
        rates2[2][0] = 1.06690; //AUD->NZD
        rates2[2][1] = 1.59501; //AUD->TOP //(change to 1.59501 for no negative weight closed path) 
        
        rates2[3][0] = 0.03079; //PHP->NZD
        
        
        
       ArbitrageFinder af = new ArbitrageFinder(rates2, currs);
       af.findArbitrage();
       
       //TEST CASE 2--------------------------
        HashMap<Integer, String> currencies = new HashMap<>();
        currencies.put(NZD, "NZD");
        currencies.put(AUD, "AUD");
        currencies.put(MXN, "MXN");
        currencies.put(USD, "USD");
        currencies.put(CAD, "CAD");
        currencies.put(PHP, "PHP");
        currencies.put(GBP, "GBP");
        currencies.put(COP, "COP");
        currencies.put(JPY, "JPY");
        currencies.put(TOP, "TOP");
        double[][] rates = new double[10][10];

        rates[NZD][AUD] = 0.93481;
        rates[NZD][USD] = 0.66345;
        rates[NZD][PHP] = 32.1637;
        rates[NZD][GBP] = 0.54287;
        rates[NZD][JPY] = 69.57;
        rates[NZD][TOP] = 1.48820;
        

        rates[AUD][NZD] = 1.06690;
        rates[AUD][USD] = 0.70962;
        rates[AUD][CAD] = 0.93076;
        rates[AUD][PHP] = 34.4024;
        rates[AUD][GBP] = 0.54287;
        rates[AUD][JPY] = 74.42;
        

        rates[MXN][USD] = 0.04746;
        rates[MXN][PHP] = 2.30061;
        rates[MXN][COP] = 179.687;
        

        rates[USD][NZD] = 1.50691;
        rates[USD][AUD] = 1.40890;
        rates[USD][MXN] = 21.0655;
        rates[USD][CAD] = 1.31162;
        rates[USD][PHP] = 48.4797;
        rates[USD][GBP] = 0.76501;
        rates[USD][COP] = 3786.46;
        rates[USD][JPY] = 104.86;
      

        rates[CAD][NZD] = 1.14873;
        rates[CAD][AUD] = 1.70403;
        rates[CAD][GBP] = 0.58317;
 

        rates[PHP][NZD] = 0.03102;
        rates[PHP][AUD] = 0.02900;
        rates[PHP][USD] = 0.02058;
        rates[PHP][JPY] = 2.16;
        
    
        rates[GBP][NZD] = 1.96951;
        rates[GBP][AUD] = 1.30699;
        rates[GBP][USD] = 1.30699;
        rates[GBP][CAD] = 1.71428;
        
      
        rates[COP][MXN] = 0.00548;
        rates[COP][USD] = 0.00026;
       
     
        rates[JPY][NZD] = 0.01437;
        rates[JPY][AUD] = 0.01343;
        rates[JPY][USD] = 0.00954;
        rates[JPY][PHP] = 0.46226;
        
      
        rates[TOP][NZD] = 0.63111;
        ArbitrageFinder abf2 = new ArbitrageFinder(rates,currencies);
        abf2.findArbitrage();
        
    }
}

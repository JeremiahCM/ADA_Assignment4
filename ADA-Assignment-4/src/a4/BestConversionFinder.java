/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a4;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @author Bernadette Cruz 17985971
 * 
 */
public class BestConversionFinder<E> extends AdjacencyListGraph<String>{
    DecimalFormat df = new DecimalFormat("#.####");
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
    private ArrayList<String> currencies; //for creating each Vertex
    private HashMap<Integer, AdjacencyListVertex> currencyVertex; //to access each vertex using the enum values for the currencies defined above.
    
   
    //TODO: accept n x n table of rates
    public BestConversionFinder(double[][] rates, ArrayList<String> currencies)
    {
        super(GraphType.DIRECTED);
        this.rates = rates;
        this.currencies = currencies;
        currencyVertex = new HashMap<>();
        createAdjacencyListGraph();
    }
    
    private void createAdjacencyListGraph()
    {
        //create vertices
        for(int i = 0; i < currencies.size(); i++)
        {
            AdjacencyListVertex v =  super.addVertex(currencies.get(i));   
            currencyVertex.put(i, v);
        }
        
        //add edges of currencies (the weights) using the log(1/rate) as a weight.
        for(int i = 0; i < rates.length; i++)
        {
            
            for(int j = 0; j < rates.length; j++)
            {
                if(rates[i][j] != 0)
                {
                    double calculatedWeight = Math.log(1/rates[i][j]) / Math.log(2);
                    super.addEdge(currencyVertex.get(i), currencyVertex.get(j), calculatedWeight);
                    
                }
            }
        }       
        
    }
    
 
    //TODO:BELLMAN FORD ALGORITHM - Calculate's best rates between two currencies both ways.
    public boolean calculateBestRate(int curr1, int curr2)
    {
        if(!currencyVertex.containsKey(curr1) || !currencyVertex.containsKey(curr2))
        {
            throw new NoSuchElementException("Currency doesn't exist!");
        }
        
        //10 currencies = 9 iterations
        //set all vertices to have infinity distance
        for(int i = 0; i < currencies.size(); i++)
        {
            
            currencyVertex.get(i).setDistance(Double.POSITIVE_INFINITY);
            currencyVertex.get(i).leastEdge = null;
            
        }
        
        currencyVertex.get(curr1).setDistance(0);
      
        
        for(int i = 0; i < currencies.size()-1; i++)
        {
            for(Edge e : this.edgeSet())
            {
                //relax edge
                AdjacencyListEdge edgy = (AdjacencyListEdge)e;
                Vertex[] vertices = e.endVertices();
                AdjacencyListVertex u = (AdjacencyListVertex)vertices[0]; //start of edge
                AdjacencyListVertex v = (AdjacencyListVertex)vertices[1]; // end of edge
                
               
                if(!Double.isInfinite(u.getDistance()) && !u.getUserObject().equalsIgnoreCase(currencies.get(curr2)) && !v.getUserObject().equalsIgnoreCase(currencies.get(curr1)))
                {   System.out.println("distnace is "+df.format(u.getDistance()));
                    System.out.println("trtrtr is "+df.format(v.getDistance()));
                    System.out.println(u.getDistance()+ edgy.getWeight());
                    
                    if(u.getDistance() + edgy.getWeight() < v.getDistance())
                    {  System.out.println("INFINITE ----- "+i+"--> "+edgy);
                        v.setDistance(u.getDistance()+edgy.getWeight());
                        v.leastEdge = edgy;
                        
                        System.out.println("V's new distance is "+df.format(v.getDistance()));                     
                    }
               }
            }
        }
        
       //TESTERS:
        System.out.println(currencyVertex.get(NZD).leastEdge);
        System.out.println(currencyVertex.get(AUD).leastEdge);
        
        boolean matched = true;
        
//        //check if graph has negative weight closed path
//        for(Edge e : this.edgeSet())
//        {
//            AdjacencyListEdge edgy = (AdjacencyListEdge)e;
//            Vertex[] vertices = e.endVertices();
//            AdjacencyListVertex u = (AdjacencyListVertex)vertices[0]; //start of edge
//            AdjacencyListVertex v = (AdjacencyListVertex)vertices[1]; // end of edge
//           if(((u.distance) + edgy.getWeight()) < v.distance)               
//           {
//                matched = false;          
//           }
//        }
//               
         
        return matched;
        
    }
    

    public static void main(String[] args) 
    {
        //for printing outputs of currencies using their associated index.
        ArrayList<String> currencies = new ArrayList<>();
        currencies.add("NZD");
        currencies.add("AUD");
        currencies.add("MXN");
        currencies.add("USD");
        currencies.add("CAD");
        currencies.add("PHP");
        currencies.add("GBP");
        currencies.add("COP");
        currencies.add("JPY");
        currencies.add("TOP");
        
        
        double[][] rates = new double[10][10];
        //assign rates here
     //   rates[NZD][NZD] = 1;
        rates[NZD][AUD] = 0.93481;
        rates[NZD][USD] = 0.66345;
        rates[NZD][PHP] = 32.1637;
        rates[NZD][GBP] = 0.54287;
        rates[NZD][JPY] = 69.57;
        rates[NZD][TOP] = 1.48820;
        
     //   rates[AUD][AUD] = 1;
        rates[AUD][NZD] = 1.06690;
        rates[AUD][USD] = 0.70962;
        rates[AUD][CAD] = 0.93076;
        rates[AUD][PHP] = 34.4024;
        rates[AUD][GBP] = 0.54287;
        rates[AUD][JPY] = 74.42;
        
      //  rates[MXN][MXN] = 1;
        rates[MXN][USD] = 0.04746;
        rates[MXN][PHP] = 2.30061;
        rates[MXN][COP] = 179.687;
        
       // rates[USD][USD] = 1;
        rates[USD][NZD] = 1.50691;
        rates[USD][AUD] = 1.40890;
        rates[USD][MXN] = 21.0655;
        rates[USD][CAD] = 1.31162;
        rates[USD][PHP] = 48.4797;
        rates[USD][GBP] = 0.76501;
        rates[USD][COP] = 3786.46;
        rates[USD][JPY] = 104.86;
      
       // rates[CAD][CAD] = 1;
        rates[CAD][NZD] = 1.14873;
        rates[CAD][AUD] = 1.70403;
        rates[CAD][GBP] = 0.58317;
 
       // rates[PHP][PHP] = 1;
        rates[PHP][NZD] = 0.03102;
        rates[PHP][AUD] = 0.02900;
        rates[PHP][USD] = 0.02058;
        rates[PHP][JPY] = 2.16;
        
       // rates[GBP][GBP] = 1;
        rates[GBP][NZD] = 1.96951;
        rates[GBP][AUD] = 1.30699;
        rates[GBP][USD] = 1.30699;
        rates[GBP][CAD] = 1.71428;
        
       // rates[COP][COP] = 1;
        rates[COP][MXN] = 179.687;
        rates[COP][USD] = 0.00026;
       
       // rates[JPY][JPY] = 1;
        rates[JPY][NZD] = 0.01437;
        rates[JPY][AUD] = 0.01343;
        rates[JPY][USD] = 0.00954;
        rates[JPY][PHP] = 0.46226;
        
       // rates[TOP][TOP] = 1;
        rates[TOP][NZD] = 0.63111;
        
//      
//        System.out.println(rates[TOP][NZD]);
//        System.out.println(rates[USD][CAD]);
//       System.out.println(rates.length);
        
        BestConversionFinder bcf = new BestConversionFinder(rates, currencies);
        System.out.println(bcf.calculateBestRate(AUD, NZD));
        
        System.out.println(bcf);
       
    
    }
    
}

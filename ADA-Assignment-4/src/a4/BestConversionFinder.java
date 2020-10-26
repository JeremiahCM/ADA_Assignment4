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
    DecimalFormat df = new DecimalFormat("#.######");
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
    public double[][] weights; //for ArbitrageFinder and Floyd-Warshall algorithm
    private HashMap<Integer,String> currencies; //for creating each Vertex (linked to the index of the initial n * n table)
    ArrayList<AdjacencyListEdge> path1 = new ArrayList<>();// path of best conversion rate for currency 1
    ArrayList<AdjacencyListEdge> path2 = new ArrayList<>(); // path of best conversion rate for currency 2
    private HashMap<Integer, AdjacencyListVertex> currencyVertex; //to access each vertex using the enum values for the currencies defined above.
    
   
    //TODO: accept n x n table of rates
    public BestConversionFinder(double[][] rates, HashMap<Integer,String> currencies)
    {
        super(GraphType.DIRECTED);
        this.rates = rates;
        this.currencies = currencies;
        currencyVertex = new HashMap<>();
        convertToWeights();
        createAdjacencyListGraph();
    }
    
    private void convertToWeights()
    {
        weights = new double[rates.length][rates.length];

        for(int i = 0; i < rates.length; i++)
        {
            for(int j = 0; j < rates.length; j++)
            {
                 if(rates[i][j] != 0)
                {
                   weights[i][j] = Math.log(1/rates[i][j]) / Math.log(2);
                  
                }
                 else if(i == j)
                {
                    weights[i][j] = 0;
                   
                }
                 else
                 {                     
                     weights[i][j] = Double.POSITIVE_INFINITY;

                 }
            }
        }
    }
    private void createAdjacencyListGraph()
    {
        //create vertices
        for(int i = 0; i < currencies.size(); i++)
        {
            AdjacencyListVertex v =  super.addVertex(currencies.get(i));   
            v.setIndex(i);
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
    
    public void findBestConversion(int curr1, int curr2)
    {  
        path1 = new ArrayList<>();
        path2 = new ArrayList<>();
        if(bellmanFordPath(curr1, curr2))
        {
            bellmanFordPath(curr2, curr1);
            printPaths(curr1, curr2);
        }
        
        else
        {
            System.out.println("Negative Closed Path Found! Cannot find Best Conversion Rate");
        }
    
 
    }
    
    //Calculate's best rates between two currencies both ways by finding their shortest paths to and from each other.
    public boolean bellmanFordPath(int curr1, int curr2)
    {
        if(!currencyVertex.containsKey(curr1) || !currencyVertex.containsKey(curr2))
        {
            throw new NoSuchElementException("Currency is not in the graph!");
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
                
               
                if(!Double.isInfinite(u.getDistance()))
                {  
                    
                    if(u.getDistance() + edgy.getWeight() < v.getDistance())
                    { 
                        v.setDistance(u.getDistance()+edgy.getWeight());
                        v.leastEdge = edgy;
                                
                    }
               }
            }
        }
        
        
        boolean noClosedPath = true; //flag to indicate if graph has negative closed path
        
        //check if graph has negative weight closed path
        for(Edge e : this.edgeSet())
        {
            AdjacencyListEdge edgy = (AdjacencyListEdge)e;
            Vertex[] vertices = e.endVertices();
            AdjacencyListVertex u = (AdjacencyListVertex)vertices[0]; //start of edge
            AdjacencyListVertex v = (AdjacencyListVertex)vertices[1]; // end of edge
            
           if(((u.getDistance()) + edgy.getWeight()) < v.getDistance())               
           {
                noClosedPath = false;          
           }
        }
        
        
        if(noClosedPath) //get the shortest path for the currencies.
        {   
            if(path1.isEmpty())
            {
                path1 = getLeastPaths(curr2);
            }
            else
            {
                path2 = getLeastPaths(curr2);
            }
        }
                        
        return noClosedPath;
        
    }
    
    private ArrayList<AdjacencyListEdge> getLeastPaths(int startIndex)
    {
        ArrayList<AdjacencyListEdge> path = new ArrayList<>();
        int index = startIndex;
             
        boolean found = false;
        while (!found) 
        {
            AdjacencyListVertex v = currencyVertex.get(index);
            if (v.leastEdge == null) 
            {
                found = true;
            } 
            else 
            {
                Vertex[] leastVertices = v.leastEdge.endVertices();
                path.add(v.leastEdge);

                AdjacencyListVertex nextV = (AdjacencyListVertex) leastVertices[0];
                index = nextV.getIndex();
            }
        }
            
        return path;
    }
    
    private void printPaths(int curr1, int curr2)
    {   
        double totalWeight = 0;
        double originalRate = 0; //original exchange rate of the currencies before it was converted to the log(1/rate)e calculations.
       
        System.out.println("FROM "+currencyVertex.get(curr1)+" TO "+currencyVertex.get(curr2) +":");
        for(int i = path1.size()-1; i >= 0; i--)
        {
            System.out.print(path1.get(i)+",");
            totalWeight += path1.get(i).getWeight();
        }
        System.out.println("\b");
        originalRate = 1/Math.pow(2,  totalWeight); //convert back to 1/e^rate
        System.out.print("\n~~Total Rate is 1 "+currencyVertex.get(curr1)+" to "+df.format(originalRate)+" "+currencyVertex.get(curr2));
        System.out.print(" (Total Weight: "+df.format(totalWeight)+") \n");
             
        totalWeight = 0;       
        
        System.out.println("\nFROM "+currencyVertex.get(curr2)+" TO "+currencyVertex.get(curr1)+":");
        for(int i = path2.size()-1; i >= 0; i--)
        {
            System.out.print(path2.get(i)+",");
            totalWeight += path2.get(i).getWeight();
        }
        System.out.println("\b");
        originalRate = 1/Math.pow(2, totalWeight); //convert back to 1/e^rate
        System.out.print("\n~~Total Rate is 1 "+currencyVertex.get(curr2)+" to "+df.format(originalRate)+" "+currencyVertex.get(curr1));
        System.out.print(" (Total Weight: "+df.format(totalWeight)+")");
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public static void main(String[] args) 
    {
              
 //       double[][] rates = new double[10][10];
        //assign rates here

//        rates[NZD][AUD] = 0.93481;
//        rates[NZD][USD] = 0.66345;
//        rates[NZD][PHP] = 32.1637;
//        rates[NZD][GBP] = 0.54287;
//        rates[NZD][JPY] = 69.57;
//        rates[NZD][TOP] = 1.48820;
//        
//
//        rates[AUD][NZD] = 1.06690;
//        rates[AUD][USD] = 0.70962;
//        rates[AUD][CAD] = 0.93076;
//        rates[AUD][PHP] = 34.4024;
//        rates[AUD][GBP] = 0.54287;
//        rates[AUD][JPY] = 74.42;
//        
//
//        rates[MXN][USD] = 0.04746;
//        rates[MXN][PHP] = 2.30061;
//        rates[MXN][COP] = 179.687;
//        
//
//        rates[USD][NZD] = 1.50691;
//        rates[USD][AUD] = 1.40890;
//        rates[USD][MXN] = 21.0655;
//        rates[USD][CAD] = 1.31162;
//        rates[USD][PHP] = 48.4797;
//        rates[USD][GBP] = 0.76501;
//        rates[USD][COP] = 3786.46;
//        rates[USD][JPY] = 104.86;
//      
//
//        rates[CAD][NZD] = 1.14873;
//        rates[CAD][AUD] = 1.70403;
//        rates[CAD][GBP] = 0.58317;
// 
//
//        rates[PHP][NZD] = 0.03102;
//        rates[PHP][AUD] = 0.02900;
//        rates[PHP][USD] = 0.02058;
//        rates[PHP][JPY] = 2.16;
//        
//    
//        rates[GBP][NZD] = 1.96951;
//        rates[GBP][AUD] = 1.30699;
//        rates[GBP][USD] = 1.30699;
//        rates[GBP][CAD] = 1.71428;
//        
//      
//        rates[COP][MXN] = 179.687;
//        rates[COP][USD] = 0.00026;
//       
//     
//        rates[JPY][NZD] = 0.01437;
//        rates[JPY][AUD] = 0.01343;
//        rates[JPY][USD] = 0.00954;
//        rates[JPY][PHP] = 0.46226;
//        
//      
//        rates[TOP][NZD] = 0.63111;

      
        
        
        //TEST CASE 1---------------------------------------------------
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
        rates[2][1] = 1.59501; //AUD->TOP (make negative closed path by turning value to 3.34)
        
        rates[3][0] = 0.03079; //PHP->NZD
        
        
        BestConversionFinder bcf = new BestConversionFinder(rates, currs);
        System.out.println(bcf);
        bcf.findBestConversion(0, 3);
        //0.76129  better directly exchanging (TOP-AUD), as it gives 0.76547 weight (1 TOP to 0.58826 AUD)  
                                            
        bcf.findBestConversion(3, 1);
        
        //TEST CASE 2----------------
        
        HashMap<Integer, String> currs2 = new HashMap<>();
        currs2.put(0, "AUD");
        currs2.put(1, "EURO");
        currs2.put(2, "GBP");
        currs2.put(3, "NZD");
        currs2.put(4, "USD");
        double[][] rates2 = new double[currs2.size()][currs2.size()];
        rates2[0][1] = 0.61; //AUD->EURO
        rates2[0][3] = 1.08;  //AUD->NZD
        rates2[0][4] = 0.72; //AUD->USD
        
        
        rates2[1][0] = 1.63;// EURO->
        rates2[1][2] = 0.58826; //EURO->
        rates2[1][3] = 1.77; //EURO->NZD
        rates2[1][4] = 1.18; //EURO->USD
        
        rates2[2][4] = 1.291; //GBP->USD
        
       rates2[3][0] = 0.92; //NZD->UAD
       rates2[3][1] = 0.56; //NZD->EURO
       rates2[3][4] = 0.66; //NZD->USD
       
       rates2[4][0] = 1.38;//USD->AUD
       rates2[4][1] = 0.84;//USD->EURO
       rates2[4][2] = 0.77;//USD->GBP
       rates2[4][3] = 1.50;//USD->NZD
       
       BestConversionFinder bcf2 = new BestConversionFinder(rates2, currs2);
       System.out.println(bcf2);
       bcf2.findBestConversion(0, 2);
       bcf2.findBestConversion(3, 1);
    }   
    
}

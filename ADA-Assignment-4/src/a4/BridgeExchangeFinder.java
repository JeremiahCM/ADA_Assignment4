/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a4;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jeremiah Martinez 18027693
 * 
 */
public class BridgeExchangeFinder<E> extends DepthFirstSearch<String>{
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

    private int discoveredCounter;
    private double[][] rates;
    private ArrayList<String> currencies; //for creating each Vertex
    private ArrayList<Vertex<String>> vertexOrder;
    private HashMap<Integer, Vertex<String>> currencyVertex; //to access each vertex using the enum values for the currencies defined above.
    private HashMap<Vertex<String>, Integer> discoveredVertices;
    private HashMap<Vertex<String>, Integer> minimumVertices;
    private HashMap<Vertex<String>, Vertex<String>> parentVertices;
    private Set<Edge<String>> edgesTraversed;
    private Set<Edge<String>> bridges;
   
    public BridgeExchangeFinder(double[][] rates, ArrayList<String> currencies)
    {
        super(new AdjacencyListGraph());
        
        this.rates = rates;
        this.currencies = currencies;
        
        discoveredCounter = 0;
        vertexOrder = new ArrayList<>();
        currencyVertex = new HashMap<>();
        discoveredVertices = new HashMap<>();
        minimumVertices = new HashMap<>();
        parentVertices = new HashMap<>();
        edgesTraversed = new HashSet();
        bridges = new HashSet<>();
        
        createAdjacencyListGraph();
    }
    
    // creates adjacency list graph for the currencies
    private void createAdjacencyListGraph()
    {
        //create vertices
        for(int i = 0; i < currencies.size(); i++)
        {
            Vertex<String> v = graph.addVertex(currencies.get(i));
            vertexOrder.add(v);
            vertexColours.put(v, DepthFirstSearch.Colour.WHITE);
            currencyVertex.put(i, v);
        }
        
        // loop through rows of rates table
        for(int i = 0; i < rates.length; i++)
        {
            // loop through columns of rates table
            for(int j = i + 1; j < rates.length; j++)
            {
                // add edge to graph if rate is not zero
                if(rates[i][j] != 0)
                    graph.addEdge(currencyVertex.get(i), currencyVertex.get(j));
            }
        }
    }
    
    // finds the bridges in the graph
    public void findBridges()
    {
        // loop through each potential bridge (edges traversed)
        for (Edge<String> edge : edgesTraversed)
        {
            int discovered;
            int minimum;
            Vertex<String>[] edgeVertices = edge.endVertices();
            
            // find parent's discovered value and child's minimum value
            if (parentVertices.get(edgeVertices[0]) == edgeVertices[1])
            {
                discovered = discoveredVertices.get(edgeVertices[1]);
                minimum = minimumVertices.get(edgeVertices[0]);
            }
            else
            {
                discovered = discoveredVertices.get(edgeVertices[0]);
                minimum = minimumVertices.get(edgeVertices[1]);
            }
            
            // if discovered value less than minimum value, add bridge found to the set
            if (discovered < minimum)
                bridges.add(edge);
        }
    }

    // hook method definition for whenever a vertex has been discovered
    protected void vertexDiscovered(Vertex<String> vertex)
    {
        // add current vertex discovered to the hashmap
        discoveredVertices.put(vertex, discoveredCounter);
        discoveredCounter++;
    }

    // hook method definition for whenever a vertex has been finished
    protected void vertexFinished(Vertex<String> vertex)
    {
        // set current minimum value
        int minimum = discoveredVertices.get(vertex);
        
        // loop through adjacent vertices
        for (Vertex<String> adjacent : vertex.adjacentVertices())
        {
            // check adjacent vertex is not a parent
            if (adjacent != parentVertices.get(vertex))
            {
                // check if there is an adjacent minimum value and it is less than current minimum value
                if (minimumVertices.get(adjacent) != null && minimumVertices.get(adjacent) < minimum)
                    minimum = minimumVertices.get(adjacent);
                // otherwise check if adjacent discovered value less than current minimum value
                else if (discoveredVertices.get(adjacent) < minimum)
                    minimum = discoveredVertices.get(adjacent);
            }
        }
        
        minimumVertices.put(vertex, minimum);
    }

    // hook method definition for whenever a tree edge is traversed
    protected void edgeTraversed(Edge<String> edge)
    {
        // add traversed edge to the set
        edgesTraversed.add(edge);
        
        // get the vertices of the edge
        Vertex<String>[] edgeVertices = edge.endVertices();
        Integer discoveredValueOne = discoveredVertices.get(edgeVertices[0]);
        Integer discoveredValueTwo = discoveredVertices.get(edgeVertices[1]);
        
        // check for an unvisited vertex if the other has been visited
        // if true, visited vertex is the parent of the other vertex
        if (discoveredValueOne != null && discoveredValueTwo == null)
            parentVertices.put(edgeVertices[1], edgeVertices[0]);
        else if (discoveredValueOne == null && discoveredValueTwo != null)
            parentVertices.put(edgeVertices[0], edgeVertices[1]);
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

        BridgeExchangeFinder<String> bef = new BridgeExchangeFinder(rates, currencies);
        System.out.println(bef.graph);
        
        bef.search(bef.currencyVertex.get(0));
        System.out.println("Depth First Search Traversal:");
        
        int edgeCount = 1;
        for (Edge<String> edge: bef.edgesTraversed)
        {
            System.out.println(edgeCount + ": " + edge);
            
            edgeCount++;
        }
        System.out.println();
        
        bef.findBridges();
        System.out.println("Bridges found:");
        
        for (Edge<String> edge : bef.bridges)
        {
            System.out.println(edge);
        }
    }
}

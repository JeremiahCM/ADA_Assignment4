package a4;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
   A class that demonstrates the Floyd-Warshall algorithm for solving
   the all-pairs shortest paths problem in O(n^3)
   @author Andrew Ensor
*/

public class FloydWarshall
{
 
   DecimalFormat df = new DecimalFormat("#.#####"); //for rounding edge weights
   private static final double INFINITY = Double.POSITIVE_INFINITY;
   private static final double NO_VERTEX = -1;
   private int n; // number of vertices in the graph
   private double[][][] d; //d[k][i][i] is weight of path from v_i to v_j
   private double[][][] p; //p[k][i][i] is penultimate vertex in path
   private HashMap <Integer,String> currencies;
   
   public FloydWarshall(double[][] weights, HashMap <Integer,String> currencies)
   {  this.currencies = currencies;
       n = weights.length;
      d = new double[n+1][][];
      d[0] = weights;
      // create p[0]
      p = new double[n+1][][];
      p[0] = new double[n][n];
      for (int i=0; i<n; i++)
      {  for (int j=0; j<n; j++)
         {  
             if (weights[i][j]<INFINITY)
               p[0][i][j] = i; //assign penultimate vertex in path to be i, if there is an edge between i and j.
            else
               p[0][i][j] = NO_VERTEX; //otherwise no vertex between them.
         }
      }
      // build d[1],...,d[n] and p[1],...,p[n] dynamically
      for (int k=1; k<=n; k++)
      {  
        
         d[k] = new double[n][n];
         p[k] = new double[n][n];
         
         for (int i=0; i<n; i++)
         { 
            for (int j=0; j<n; j++)
            {  
                //PRINTOUTPUT
                //System.out.println("INDEX ["+currencies.get(i)+","+currencies.get(j)+"]");
                double s;
                //System.out.println("IF D["+currencies.get(i)+ ","+currencies.get(k-1)+"] IS NOT INFINITY AND "
                //        +"D["+currencies.get(k-1)+","+currencies.get(j)+"] IS NOT INFINITY..");
               if (d[k-1][i][k-1]!=INFINITY&&d[k-1][k-1][j]!=INFINITY)
               {
                  s = d[k-1][i][k-1] + d[k-1][k-1][j];
               }
               else
               {
                  s = INFINITY;
               }
               if (d[k-1][i][j] <= s)
               {  d[k][i][j] = d[k-1][i][j];
                  p[k][i][j] = p[k-1][i][j];
               }
               else
               {  d[k][i][j] = s;
                  p[k][i][j] = p[k-1][k-1][j];
               }
               
               //PRINTOUTPUT
              //  System.out.println("S IS : "+s);
               
            }
         }
      }
   }
   
   public double[][][] getPTable()
   {
       return p;
   }
   public double[][][] getDistances()
   {
       return d;
   }
   
   public void print()
   {
//       System.out.println(d[3][0][0]);
//       System.out.println(currencies.get((int)p[3][0][0]));
//       System.out.println(currencies.get((int)p[3][2][2]));
//       System.out.println(currencies.get((int)p[3][1][1]));
//       
//       System.out.println(d[3][1][1]);
       
//       System.out.println(d[4][3][3]);
//       System.out.println(currencies.get((int)p[4][3][3]));
//       System.out.println(currencies.get((int)p[4][0][0]));
//       System.out.println(currencies.get((int)p[4][2][2]));
//       System.out.println(currencies.get((int)p[4][1][1]));
//       System.out.println("----------------------");
//       //k = 2, TOP-AUD LAST EDGE BACKTRACKED. (PHP-NZD, NZD-AUD, AUD,TOP, TOP-AUD)
//       System.out.println(currencies.get((int)p[3][2][2])); //aussie
//       System.out.println(currencies.get((int)p[3][1][1])); //top
//       System.out.println(currencies.get((int)p[3][0][0])); //nzd
   }
  
   protected void getLastVertex(double v)
   {
       System.out.println("LAST VERTEX IS "+v);
   }
   // returns a string representation of matrix d[n] and p[n]
   public String toString()
   {  String output = "Shortest lengths\n";
   
      for (int i=0; i<n; i++)
      {  
          for (int j=0; j<n; j++)
         {  
             if (d[n][i][j] != INFINITY)
               output += ("\t" + d[n][i][j]);
            else
               output += "\tinfin";
         }
         output += "\n";
      }
      output += "Previous vertices on shortest paths\n"; //using the most vertices possible.
      for (int i=0; i<n; i++)
      {  for (int j=0; j<n; j++)
         {  
             if (p[n][i][j] != NO_VERTEX)
               output += ("\t" + df.format(p[n][i][j])); //print where k = 4;
            else
               output += "\tnull";
         }
         output += "\n";
      }
      return output;
   }

   public static void main(String[] args)
   {
//     int[][] weights = {
//         {0, 2, 15, INFINITY, INFINITY, INFINITY},
//         {INFINITY, 0, 9, 11, 5, INFINITY},
//         {INFINITY, -1, 0, 3, 6, INFINITY},
//         {INFINITY, INFINITY, INFINITY, 0, 5, 2},
//         {INFINITY, INFINITY, -2, INFINITY, 0, 7},
//         {INFINITY, INFINITY, INFINITY, 1, INFINITY, 0}};
//      FloydWarshall apfw = new FloydWarshall(weights);
//      System.out.println(apfw);   
 
        HashMap<Integer, String> currs = new HashMap<>();
        currs.put(0, "NZD");
        currs.put(1, "TOP");
        currs.put(2, "AUD");
        currs.put(3, "PHP");
        double[][] rates = new double[currs.size()][currs.size()];
        
        rates[0][0] = 0;
        rates[0][1] = -0.57876; // NZD->TOP
        rates[0][2] = 0.09725; // NZD->AUD
        rates[0][3] = -5.01328; //NZD->PHP
        
        rates[1][0] = 0.66404;// TOP->NZD
        rates[1][1] = 0;
        rates[1][2] = -1.82374936; //TOP->AUD
        rates[1][3] = INFINITY;
        
        rates[2][0] = -0.09342; //AUD->NZD
        rates[2][1] = -0.67357; //AUD->TOP 1.59501
        rates[2][2] = 0;
        rates[2][3] = INFINITY;
        
        rates[3][1] = INFINITY;
        rates[3][2] = INFINITY;
        rates[3][0] = 5.02139; //PHP->NZD
        rates[3][3] = 0;
        
        FloydWarshall fw = new FloydWarshall(rates, currs);
        System.out.println(fw.toString());
        fw.print();
   }
}

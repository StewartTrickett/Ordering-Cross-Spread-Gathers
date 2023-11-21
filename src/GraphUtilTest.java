/*
 *  Unit tests for GraphUtil.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.degrees;
import static CrossSpreadOrder.GraphUtil.isSpanningTree;
import static CrossSpreadOrder.GraphUtil.isTree;
import static CrossSpreadOrder.GraphUtil.leaves;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class GraphUtilTest
{
   void printTitle ()
     { printf ("%s: %s%n%n", className(), methodName(1)); }

   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void leavesDegreesTest ()
     {
       printTitle ();

       //  Build the test graph.

       int n = 8;
       Adjacent adj = new Adjacent (n);
       adj.add (1, 2);
       adj.add (3, 2);
       adj.add (4, 6);
       adj.add (5, 2);
       adj.add (6, 2);

       //  Test function "leaves".

       int leaves [] = leaves (adj, 0);
       assertTrue (leaves.length == 1);
       int answer [] = new int [] { 1, 3, 4, 5 };
       for (int i = 1; i <= 6; i++)
         {
           leaves = leaves (adj, i);
           assertTrue (leaves.length == answer.length);
           Arrays.sort (leaves);
           for (int j = 0; j < leaves.length; j++)
               assertTrue (leaves[j] == answer[j]);
         }
       leaves = leaves (adj, 7);
       assertTrue (leaves.length == 1);

       //  Test function "degrees".

       int degrees [] = degrees (adj, 0);
       for (int i = 0; i < n; i++)
           assertTrue (degrees[i] == 0);
       answer = new int [] { 0, 1, 4, 1, 1, 1, 2, 0 };
       for (int i = 1; i <= 6; i++)
         {
           degrees = degrees (adj, i);
           assertTrue (degrees[i] == answer[i]);
         }
       degrees = degrees (adj, 7);
       for (int i = 0; i < n; i++)
           assertTrue (degrees[i] == 0);
     }

   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void isSpanningTreeTest ()
     {
       printTitle ();

       int n = 7;
       Adjacent adj = new Adjacent (n);

       adj.add (1, 2);
       assertTrue (!isSpanningTree (adj));
       adj.add (3, 2);
       assertTrue (!isSpanningTree (adj));
       adj.add (4, 6);
       assertTrue (!isSpanningTree (adj));
       adj.add (5, 2);
       assertTrue (!isSpanningTree (adj));
       adj.add (6, 2);
       assertTrue (!isSpanningTree (adj));
       adj.add (0, 3);
       assertTrue (isSpanningTree (adj));
       adj.add (4, 3);
       assertTrue (!isSpanningTree (adj));
     }

   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void isTreeTest ()
     {
       printTitle ();

       int n = 20;
       Adjacent adj = new Adjacent (n);
       adj.add (1, 2);
       adj.add (3, 2);
       adj.add (1, 4);
       adj.add (6, 7);
       adj.add (6, 8);
       adj.add (7, 8);
       adj.add (12, 13);
       adj.add (13, 14);
       adj.add (14, 15);
       adj.add (15, 16);
       adj.add (13, 15);
       adj.add (17, 19);

       boolean answer [] =
         {
           true, true, true, true, true, true, false, false, false, true,
           true, true, false, false, false, false, false, true, true, true,
         };

       for (int i = 0; i < n; i++)
           assertTrue (isTree (adj, i) == answer[i]);
     }

   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void edgesTest ()
     {
       printTitle ();

       //  Define a path in an adjacency matrix.

       int n = 6;
       Adjacent adj1 = new Adjacent (n);
       adj1.add (0, 2);
       adj1.add (3, 2);
       adj1.add (1, 4);
       adj1.add (4, 3);
       adj1.add (1, 5);

       //  Build the path edges

       int [] edges = GraphUtil.edges (adj1);

       //  Build adjacency matrix from path edges

       Adjacent adj2 = new Adjacent (n);
       GraphUtil.addEdges (edges, adj2);

       //  Are the two adjancecy matrices the same?

       assertTrue (equals (adj1, adj2));
     }

   private boolean equals (Adjacent adj1, Adjacent adj2)
     {
       int n = adj1.numVertices ();
       if (n != adj2.numVertices())
           return false;
       for (int i = 0; i < n; i++)
         {
           Set <Integer> nb1 = adj1.neighbours (i);
           Set <Integer> nb2 = adj2.neighbours (i);
           if (! nb1.equals (nb2))
              return false;
         }
       return true;
     }
}

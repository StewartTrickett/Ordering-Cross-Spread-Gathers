/*
 *  Unit tests for Adjacent
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.addEdges;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class AdjacentTest
{
   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void general ()
     {
       printf ("%s: %s%n%n", className(), methodName());

       int n = 7;
       Adjacent adj = new Adjacent (n);

       //  Build the first graph and test.

       adj.add (1, 2);
       adj.add (3, 2);
       adj.add (4, 6);
       adj.add (5, 2);
       adj.add (6, 2);

       assertTrue (adj.totalDegree () == 5);
       assertTrue (adj.totalBranchingDegree () == 2);
       assertTrue (adj.degree (0) == 0);
       assertTrue (adj.degree (1) == 1);
       assertTrue (adj.degree (2) == 4);
       assertTrue (adj.degree (3) == 1);
       assertTrue (adj.degree (4) == 1);
       assertTrue (adj.degree (5) == 1);
       assertTrue (adj.degree (6) == 2);
       boolean links [] [] = new boolean [] []
         {
           { false, false, false, false, false, false, false },
           { false, false, true,  false, false, false, false },
           { false, true,  false, true , false, true,  true  },
           { false, false, true,  false, false, false, false },
           { false, false, false, false, false, false, true  },
           { false, false, true,  false, false, false, false },
           { false, false, true,  false, true,  false, false },
         };
       for (int i = 0; i < n; i++)
       for (int j = 0; j < n; j++)
           if (i != j)
               assert (adj.isNeighbor (i, j) == links[i][j]);

       //  Alter the graph and retest.

       adj.remove (2, 6);
       adj.remove (5, 2);
       adj.add (5, 6);
       adj.add (4, 2);
       adj.add (3, 0);

       assertTrue (adj.totalDegree () == 6);
       assertTrue (adj.totalBranchingDegree () == 1);
       assertTrue (adj.degree (0) == 1);
       assertTrue (adj.degree (1) == 1);
       assertTrue (adj.degree (2) == 3);
       assertTrue (adj.degree (3) == 2);
       assertTrue (adj.degree (4) == 2);
       assertTrue (adj.degree (5) == 1);
       assertTrue (adj.degree (6) == 2);
       links = new boolean [] []
         {
           { false, false, false, true,  false, false, false },
           { false, false, true,  false, false, false, false },
           { false, true,  false, true , true,  false, false },
           { true,  false, true,  false, false, false, false },
           { false, false, true,  false, false, false, true  },
           { false, false, false, false, false, false, true },
           { false, false, false, false, true,  true, false },
         };
       for (int i = 0; i < n; i++)
       for (int j = 0; j < n; j++)
           if (i != j)
               assert (adj.isNeighbour (i, j) == links[i][j]);
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Test how many leaves and branches are in minimum spanning tree of a
   //  square with randomly scattered points, as a function of the number of
   //  vertices.
   //
   //  Asymptotically it seems that 22% of vertices are leaves, 21% of the
   //  the vertices are brancing, and the total branching degree is 22% of the
   //  number of vertices (this must be the same as the first).

   @Test
   public void branching ()
     {
       printf ("%s: %s%n%n", className(), methodName());

       int maxN = 2000, numNTest = 300000;

       Random rand = new Random (102);

       for (int n = 1; n <= maxN; n += n)
         {
           int totalLeaves = 0, totalBranches = 0, totalBranchDegree = 0;
           Coord coords [] = new Coord [n];
           int numTests = numNTest / n;
           for (int j = 0; j < numTests; j ++)
             {
               for (int k = 0; k < n; k++)
                   coords[k] = new Coord (rand.nextFloat(), rand.nextFloat());
               Weights weights = new WeightsEuclid (coords);
               MinSpanTree mst = new MinSpanTreePrims ();
               int edges [] = mst.edges (n, weights);
               Adjacent adjacent = new Adjacent (n);
               addEdges (edges, adjacent);

               for (int k = 0; k < n; k++)
                   if (adjacent.degree(k) == 1)
                        totalLeaves ++;
                   else if (adjacent.degree(k) > 2)
                        totalBranches ++;
               totalBranchDegree += adjacent.totalBranchingDegree ();
              }
           float divisor = numTests * n;
           printf ("   %5d:   %5.3f   %5.3f   %5.3f%n",
                   n, totalLeaves / divisor,
                   totalBranches / divisor,
                   totalBranchDegree / divisor);
         }
       printf ("%n");
     }
}

/*
 *  Find an approximation to shortest Hamiltonian path (AKA the open-loop
 *  traveling salesman problem) of a complete symmetric graph using the
 *  "all pairs" variant of the branch-elimination algorithm of:
 *
 *      Franki, P., T. Nenonen, and Y. Mingchuan, 2021, "Converting MST to
 *      TSP Path by Branch Elimination", Applied Science, 11.
 *
 *  It works by calculating the minimum spanning tree of the graph, and then
 *  iteratively replacing edges connected to branching vertices with edges
 *  connected to leaves.
 *
 *  This is a heuristic method, meaning it does not always give the
 *  optimum SHP, but on average the total weight is less than 3% above the
 *  optimum.
 *
 *  Define the branching degree of an n-vertex tree as:
 *
 *       n
 *      ---
 *       >   max (0, 2 - degree(i))
 *      ---
 *      i=1
 *
 *  The branching degree measures how far a tree deviates from a simple path.
 *  This algorithm can be halted after the branching degree is reduced to a
 *  specified level (zero being the SHP). Thus it drives a tree towards a path.
 *
 *  It's computational complexity is roughly O(n^3).
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.addEdges;
import static CrossSpreadOrder.GraphUtil.isPathASpanningTree;
import static CrossSpreadOrder.GraphUtil.isSpanningTree;
import static CrossSpreadOrder.GraphUtil.leaves;
import static CrossSpreadOrder.GraphUtil.setEdges;

import java.util.Set;

public class ShortHamPathBE extends ShortHamPath
{
   MinSpanTree mst = new MinSpanTreePrims ();

   /////////////////////////////////////////////////////////////////////////////

   ShortHamPathBE (Weights weights)
     { super (weights); }

   /////////////////////////////////////////////////////////////////////////////

   @Override
   public int [] path (int n)
     {
       if (n <= 1)
          return new int [n];
       else if (n == 2)
          return new int [] { 0, 1 };

       return GraphUtil.path (edges (n, 0));
     }

   /////////////////////////////////////////////////////////////////////////////

   public int [] edges (int n)
     { return edges (n, 0); }

   public int [] edges (int n, int maxBranch)
     {
       assert n >= 0;
       if (n <= 1)
           return new int [0];
       else if (n == 2)
           return new int [] { 1 };

       //  Build a minimum spanning tree.

       int edges [] = mst.edges (n, weights);
       if (n == 3)
            return edges;

       //  Reduce the branching in the spanning tree so that the branching
       //  degree is no more than maxBranch.

       reduceBranching (edges, maxBranch);
       return edges;
     }

  /////////////////////////////////////////////////////////////////////////////

   public void reduceBranching (int edges [], int maxBranch)
     {
       int n = edges.length + 1;
       if (n <= 3)
           return;

       //  Build an adjacency structure and reduce branching.

       Adjacent adjacent = new Adjacent (n);
       addEdges (edges, adjacent);
       reduceBranching (adjacent, maxBranch);

       //  Set the edges to the new tree.

       setEdges (adjacent, edges);
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Reduce the branching degree of a spanning tree until it's at or
   //  below "maxBranch". Reducing it to zero turns the spanning tree into a
   //  path through all vertices.

   public void reduceBranching (Adjacent adjacent, int maxBranch)
     {
       assert isSpanningTree (adjacent);
       while (adjacent.totalBranchingDegree () > maxBranch)
           branchEliminate (adjacent);
       assert isSpanningTree (adjacent);
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Reduce the branching degree of a tree by one or two by swapping
   //  a branch edge for an edge connecting two leaves.

   public void branchEliminate (Adjacent adjacent)
     {
       int n = adjacent.numVertices();

       //  Find the edge of a branching vertex and two leaves that gives the
       //  minimum increase in weight when we delete the edge and join
       //  the two leaves.

       int bestA = -1, bestB = -1, bestI = -1, bestJ = -1;
       float bestWeight = Float.MAX_VALUE;

       for (int i = 0; i < n; i++)
         {
           if (adjacent.degree (i) <= 2)
               continue;
           final Set <Integer> neighbours = adjacent.neighbors (i);
           for (Integer j : neighbours)
             {
               //  Disconnect this edge so we have two trees A and B.

               float weightIJ = weights.value (i, j);
               adjacent.remove (i, j);

               //  Determine the leaves of each tree.

               int leavesA [] = leaves (adjacent, i);
               int leavesB [] = leaves (adjacent, j);

               //  Determine the edge swap which adds the least weight
               //  to the total tree.

               for (int a : leavesA)
               for (int b : leavesB)
                 {
                   float weightChange = weights.value (a, b) - weightIJ;
                   if (weightChange < bestWeight)
                     {
                       bestI = i;
                       bestJ = j;
                       bestA = a;
                       bestB = b;
                       bestWeight = weightChange;
                     }
                 }

               //  Reconnect this edge for the next iteration.

               adjacent.add (i, j);
             }
         }

       //  Sanity checking.

       assert bestI >= 0 && bestJ >= 0 && bestA >= 0 && bestB >= 0;
       assert bestWeight < Float.MAX_VALUE;
       assert adjacent.isNeighbor (bestI, bestJ);
       assert adjacent.degree (bestI) > 2 || adjacent.degree (bestJ) > 2;
       assert adjacent.degree (bestA) <= 1 || adjacent.degree (bestB) <= 1;

       //  Detach bestI from bestJ and attach bestA to bestB.

       adjacent.remove (bestI, bestJ);
       assert adjacent.degree (bestA) <= 1 && adjacent.degree (bestB) <= 1;
       adjacent.add (bestA, bestB);
     }

   /////////////////////////////////////////////////////////////////////////////

   @Override
   public int [] semiEnclosedPath (int n)
     {
       if (n <= 1)
          return new int [n];
       else if (n == 2)
          return new int [] { 0, 1 };

       //  Build a minimum spanning tree.

       int edges [] = mst.edges (n, weights);
       Adjacent adjacent = new Adjacent (n);
       addEdges (edges, adjacent);

       assert isSpanningTree (adjacent);

       while (adjacent.degree(0) != 1 || adjacent.totalBranchingDegree () > 0)
           branchEliminateSE (adjacent);

       assert isSpanningTree (adjacent);

       int path [] = GraphUtil.path (adjacent);
       if (path[n-1] == 0)
           for (int i = 0; i < n/2; i++)
             { int t = path[i]; path[i] = path[n-1-i]; path[n-1-i] = t; }
       assert path[0] == 0;
       assert isPathASpanningTree (path);
       return path;
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Reduce the branching degree of a tree by one or two by swapping
   //  a branch edge for an edge connecting two leaves.

   public void branchEliminateSE (Adjacent adjacent)
     {
       int n = adjacent.numVertices();

       //  Find the edge of a branching vertex and two leaves that gives the
       //  minimum increase in weight when we delete the edge and join
       //  the two leaves.

       int bestA = -1, bestB = -1, bestI = -1, bestJ = -1;
       float bestWeight = Float.MAX_VALUE;

       for (int i = 0; i < n; i++)
         {
           if ((i == 0 && adjacent.degree (0) == 1) ||
               (i != 0 && adjacent.degree (i) <= 2))
               continue;
           final Set <Integer> neighbours = adjacent.neighbors (i);
           for (Integer j : neighbours)
             {
               //  Disconnect this edge so we have two trees A and B.

               float weightIJ = weights.value (i, j);
               adjacent.remove (i, j);

               //  Determine the leaves of each tree.

               int leavesA [] = leaves (adjacent, i);
               int leavesB [] = leaves (adjacent, j);

               //  Determine the edge swap which adds the least weight
               //  to the total tree.

               int degree0 = adjacent.degree (0);
               for (int a : leavesA)
               for (int b : leavesB)
                 {
                   if (degree0 == 1 && (a == 0 || b == 0))
                        continue;
                   float weightChange = weights.value (a, b) - weightIJ;
                   if (weightChange < bestWeight)
                     {
                       bestI = i;
                       bestJ = j;
                       bestA = a;
                       bestB = b;
                       bestWeight = weightChange;
                     }
                 }

               //  Reconnect this edge for the next iteration.

               adjacent.add (i, j);
             }
         }

       //  Sanity checking.

       assert bestI >= 0 && bestJ >= 0 && bestA >= 0 && bestB >= 0;
       assert bestWeight < Float.MAX_VALUE;
       assert adjacent.isNeighbor (bestI, bestJ);
       assert adjacent.degree (bestA) <= 1 || adjacent.degree (bestB) <= 1;

       //  Detach bestI from bestJ and attach bestA to bestB.

       adjacent.remove (bestI, bestJ);
       assert adjacent.degree (bestA) <= 1 && adjacent.degree (bestB) <= 1;
       adjacent.add (bestA, bestB);
     }
}

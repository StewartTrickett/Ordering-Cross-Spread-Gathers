/*
 *  Static utility functions for graph manipulation.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.Print.printf;

import java.util.Set;

public class GraphUtil
{
   /////////////////////////////////////////////////////////////////////////////
   //  Print an adjacency structure.

   static public void print (Adjacent adjacent, String margin)
     {
       int n = adjacent.numVertices ();
       for (int i = 0; i < n; i++)
         {
           Set <Integer> neighbors = adjacent.neighbors (i);
           printf ("%s%5d: ", margin, i);
           printf ("%5d %5d   ",
                   adjacent.degree(i), adjacent.branchingDegree (i));
           for (int j  : neighbors)
               printf ("%5d", j);
           printf ("%n");
         }
       printf ("%n%sTotal & total branching degree:  %5d %5d%n",
               margin, adjacent.totalDegree(), adjacent.totalBranchingDegree());
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Return an (unsorted) list of all vertices which are connected to
   //  the given parent and have degree 1.

   static public int [] leaves (Adjacent adjacent, int parent)
     {
       int n = adjacent.numVertices ();
       assert parent >= 0 && parent < n;

       if (adjacent.degree (parent) == 0)
           return new int [] { parent };

       //  Determine degree of every vertex connected to "parent".

       int degrees [] = degrees (adjacent, parent);

       //  Collect the leaves into an (unsorted) array.

       int count = 0;
       for (int degree : degrees)
           if (degree == 1)
               count ++;
       int leaves [] = new int [count];
       count = 0;
       for (int i = 0; i < n; i++)
           if (degrees[i] == 1)
               leaves[count++] = i;
       assert count == leaves.length;

       return leaves;
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Determine the degree of every vertex connected to "vertex".

   static public int [] degrees (Adjacent adjacent, int vertex)
     {
       int degrees [] = new int [adjacent.numVertices ()];
       degrees (adjacent, vertex, degrees);
       return degrees;
     }

   static private void degrees (Adjacent adjacent, int vertex, int degrees [])
     {
       degrees[vertex] = adjacent.degree (vertex);
       Set <Integer> neighbors = adjacent.neighbors (vertex);
       for (int i  : neighbors)
           if (degrees[i] == 0)
               degrees (adjacent, i, degrees);
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Return true if the adjacency structure holds a spanning tree

   static public boolean isSpanningTree (Adjacent adjacent)
     {
       int n = adjacent.numVertices ();
       if (n <= 1)
           return true;

       //  Ensure it's a tree.

       boolean isVisited [] = new boolean [n];
       if (! isTree (adjacent, isVisited, 0, -1))
           return false;

       //  Ensure all vertices have been visited.

       for (boolean visited : isVisited)
           if (! visited)
              return false;
       return true;
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Return true if the adjacency structure beginning at vertex "start" is a tree

   static public boolean isTree (Adjacent adjacent, int start)
     {
       int n = adjacent.numVertices ();
       assert start >= 0 && start < n;
       if (n <= 1)
           return true;
       boolean isVisited [] = new boolean [n];
       return isTree (adjacent, isVisited, start, -1);
     }

   static private boolean isTree
          (Adjacent adjacent, boolean isVisited [], int vertex, int grandParent)
     {
       assert vertex >= 0 && vertex < adjacent.numVertices ();
       assert isVisited.length >= adjacent.numVertices();

       if (isVisited[vertex])      // Is there a loop?
           return false;
       isVisited[vertex] = true;

       Set <Integer> neighbors = adjacent.neighbors (vertex);
       for (int i : neighbors)
           if (i != grandParent && ! isTree (adjacent, isVisited, i, vertex))
               return false;
       return true;
     }

   /////////////////////////////////////////////////////////////////////////////

   static public int [] edges (Adjacent adjacent)
     {
       int n = adjacent.numVertices ();
       if (n <= 1)
           return new int [n];
       int edges [] = new int [n-1];
       setEdges (adjacent, edges);
       return edges;
     }

   static public void addEdges (int edges [], Adjacent adjacent)
     {
       assert edges.length <= adjacent.numVertices ();
       int m = edges.length;
       for (int i = 0; i < m; i++)
           if (edges[i] >= 0)
               adjacent.add (i, edges[i]);
     }

   static public int [] setEdges (Adjacent adjacent, int edges [])
      {
       // assert isSpanningTree (adjacent);

       int n = adjacent.numVertices ();
       for (int i = 0; i < n-1; i++)
           edges[i] = -1;
       Set <Integer> neighbors = adjacent.neighbors (n-1);
       for (int i : neighbors)
           setEdges (adjacent, edges, n-1, i);

       for (int i = 0; i < n-1; i++)
           assert edges[i] != -1;

       return edges;
     }

   static private void setEdges (Adjacent adjacent, int edges [],
                                 int from, int to)
     {
       assert edges[to] == -1;
       assert from >= 0 && from < adjacent.numVertices ();
       assert to >= 0 && to < adjacent.numVertices ();
       assert to != from;

       edges[to] = from;
       Set <Integer> neighbors = adjacent.neighbors (to);
       for (int i : neighbors)
           if (i != from)
               setEdges (adjacent, edges, to, i);
     }

   static public boolean areEdgesASpanningTree (int edges [])
     {
       int n = edges.length + 1;
       Adjacent adjacent = new Adjacent (n);
       addEdges (edges, adjacent);
       return isSpanningTree (adjacent);
     }

   /////////////////////////////////////////////////////////////////////////////

   static public boolean isPath (int path [])
     {
       int n = path.length;
       if (n == 0)
           return true;

       boolean isVisited [] = new boolean [n];
       for (int i : path)
         {
           if (i < 0 || i >= n)
              return false;
           isVisited[i] = true;
         }
       for (boolean visited : isVisited)
           if (! visited)
              return false;
       return true;
     }

   static public int [] path (int edges [])
     {
       int n = edges.length + 1;
       Adjacent adjacent = new Adjacent (n);
       addEdges (edges, adjacent);
       return path (adjacent);
     }

   static public int [] path (Adjacent adjacent)
     {
       int n = adjacent.numVertices ();

       //  Find a leaf (degree = 1)

       int init = 0;
       for (; init < n; init++)
           if (adjacent.degree(init) == 1)
               break;
       assert init < n;

       //  Build a path from the adjacency.

       int path [] = new int [n];
       path[0] = init;
       for (int i = 1; i < n; i++)
         {
           Set < Integer > neighbors = adjacent.neighbors (path[i-1]);
           for (int j : neighbors)
               if (i == 1 || j != path[i-2])
                    path[i] = j;
         }

       return path;
     }

   static public boolean isPathASpanningTree (int path [])
     {
       int n = path.length;
       boolean isVisited [] = new boolean [n];
       for (int vertex : path)
           if (vertex >= 0 && vertex < n)
               isVisited[vertex] = true;
       for (boolean visited : isVisited)
           if (! visited)
               return false;
       return true;
     }

   static public float pathWeight (Weights weights, int path [])
     {
       float weight = 0;
       int n = path.length;
       for (int i = 1; i < n; i++)
           weight += weights.value (path[i-1], path[i]);
       return weight;
     }

   /////////////////////////////////////////////////////////////////////////////

   static public float weight (Weights weights, Adjacent adjacent)
     {
       float weight = 0;
       int n = adjacent.numVertices ();
       for (int i = 0; i < n; i++)
         {
           Set <Integer> neighbors = adjacent.neighbors (i);
           for (Integer j : neighbors)
               if (i < j)
                   weight += weights.value (i, j);
         }
       return weight;
     }

   /////////////////////////////////////////////////////////////////////////////

   static public float edgesWeight (Weights weights, int edges [])
     {
       float weight = 0;
       int m = edges.length;
       for (int i = 0; i < m; i++)
           weight += weights.value (i, edges[i]);
       return weight;
     }
}

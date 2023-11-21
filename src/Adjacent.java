/*
 *  Keep track of what vertices are adjacent to what other vertices in a graph.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import java.util.HashSet;
import java.util.Set;

public class Adjacent
{
   int n, totalDegree = 0, branchingDegree = 0;
   Object neigbours [];    // Cannot directly store generics in an array
                           // so we store Objects and then cast.

   /////////////////////////////////////////////////////////////////////////////

   public Adjacent (int n)
     {
       assert n >= 0;
       this.n = n;
       neigbours = new Object [n];
       for (int i = 0; i < n; i++)
           neigbours[i] = new HashSet <Integer> ();
     }

   /////////////////////////////////////////////////////////////////////////////

   public int numVertices ()
     { return n; }

   /////////////////////////////////////////////////////////////////////////////

   public void add (int i, int j)
     {
       assert i >= 0 && i < n;
       assert j >= 0 && j < n;
       assert i != j;
       assert ! isNeighbor (i, j);

       getNeighbors(i).add (j);
       getNeighbors(j).add (i);

       totalDegree ++;
       if (degree (i) > 2)
           branchingDegree ++;
       if (degree (j) > 2)
           branchingDegree ++;

       assert totalDegree >= 0 && branchingDegree >= 0;
     }

   /////////////////////////////////////////////////////////////////////////////

   public void remove (int i, int j)
     {
       assert i >= 0 && i < n;
       assert j >= 0 && j < n;
       assert i != j;
       assert isNeighbor (i, j);

       getNeighbors(i).remove (j);
       getNeighbors(j).remove (i);

       totalDegree --;
       if (degree (i) >= 2)
           branchingDegree --;
       if (degree (j) >= 2)
           branchingDegree --;

       assert totalDegree >= 0 && branchingDegree >= 0;
     }

   /////////////////////////////////////////////////////////////////////////////

   public boolean isNeighbour (int i, int j)
     { return isNeighbor (i, j); }

   public boolean isNeighbor (int i, int j)
     {
       assert i >= 0 && i < n;
       assert j >= 0 && j < n;
       assert i != j;
       return getNeighbors(i).contains (j);
     }

   /////////////////////////////////////////////////////////////////////////////

   public Set <Integer> neighbors (int i)
     {
       assert i >= 0 && i < n;
       return new HashSet <> (getNeighbors (i));
     }

   public Set <Integer> neighbours (int i)
     { return neighbors (i); }

   /////////////////////////////////////////////////////////////////////////////

   public int degree (int i)
     {
       assert i >= 0 && i < n;
       return getNeighbors(i).size ();
     }

   /////////////////////////////////////////////////////////////////////////////

   public int totalDegree ()
     { return totalDegree; }

   /////////////////////////////////////////////////////////////////////////////

   public int branchingDegree (int i)
     {
       assert i >= 0 && i < n;
       return Math.max (0, degree(i) - 2);
     }

   /////////////////////////////////////////////////////////////////////////////

   public int totalBranchingDegree ()
     { return branchingDegree; }

   /////////////////////////////////////////////////////////////////////////////

   @SuppressWarnings ("unchecked")
   private Set <Integer> getNeighbors (int i)
     {
       assert i >= 0 && i < n;
       return (Set <Integer>) (neigbours[i]);
     }
}

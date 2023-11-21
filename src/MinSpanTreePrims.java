/*
 *  Derive the minimum spanning tree (MST) of a complete graph.
 *  This implements an O(V**2) version of Prim's algorithm, where V is the
 *  number of vertices.
 *
 *  For a dense graph with arbitrary edge weights, this is the lowest
 *  complexity possible. We have to examine every edge, which number
 *  (V-1)(V-2)/2. For sparse graphs or complete Euclidean graphs, however,
 *  faster methods are available.
 *
 *  We don't care about the actual weights between vertices, only whether
 *  one edge weight is greater than another. Thus for Euclidean graphs we can
 *  use distances squared and avoid the time-consuming sqrt.
 *
 *      Prim, R. C.. 1957, "Shortest connection networks And some
 *      generalizations", Bell System Technical Journal, 36 (6),
 *      1389–1401.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static java.lang.Math.min;

import java.util.Arrays;

public class MinSpanTreePrims extends MinSpanTree
{
   /////////////////////////////////////////////////////////////////////////////

   @Override
   public int [] edges (int n, Weights weights)
     {
       assert n >= 0;
       if (n <= 1)
           return new int [0];
       else if (n == 2)
           return new int [] { 1 };


       //  inMST               Is this vertex currently in the MST?
       //  nearestVertex       Which MST vertex is nearest this vertex?
       //  nearestWeight       What is the weight between this vertex
       //                      and the nearest MST vertex?

       boolean inMST [] = new boolean [n-1];
       int nearestVertexInMST [] = new int [n-1];
       float nearestWeight [] = new float [n-1];
       Arrays.fill (nearestWeight, Float.MAX_VALUE);

       //  Insert the n-1'st vertex first.

       int nearestVertexToMST = n - 1;

       //  Insert the rest of the vertices in the MST.
       //  Each iteration, the uninserted vertex nearest the tree is inserted.

       for (int i = 0; i < n-1; i++)
         {
           int insertVertex = nearestVertexToMST;
           nearestVertexToMST = -1;
           float minWeight = Float.MAX_VALUE;
           for (int j = 0; j < n-1; j++)
               if (! inMST[j])
                  {
                    float thisWeight = weights.value (j, insertVertex);
                    if (nearestWeight[j] > thisWeight)
                      {
                        nearestVertexInMST[j] = insertVertex;
                        nearestWeight[j] = thisWeight;
                      }
                    if (nearestWeight[j] < minWeight)
                      {
                        nearestVertexToMST = j;
                        minWeight = nearestWeight[j];
                      }
                  }
           assert nearestVertexToMST >= 0;
           inMST[nearestVertexToMST] = true;
         }

       return nearestVertexInMST;
     }

   /////////////////////////////////////////////////////////////////////////////
   //  Calculate the total weight of the MST without keeping track of the
   //  actual path. We override this as it's faster than calculating the MST
   //  edges and then summing their weights.

   @Override
   public float weight (int n, Weights weights)
     {
       assert n >= 0;
       if (n <= 1)
           return 0;
       else if (n == 2)
           return weights.value (0, 1);

       //  inMST               Is this vertex currently in the MST?
       //  nearestWeight       What is the weight between this vertex
       //                      and the nearest MST vertex?

       boolean inMST [] = new boolean [n-1];
       float nearestWeight [] = new float [n-1];
       Arrays.fill (nearestWeight, Float.MAX_VALUE);

       //  Insert the n-1'st vertex first.

       int nearestVertexToMST = n - 1;

       //  Insert the rest of the vertices in the MST.
       //  Each iteration, the uninserted vertex nearest the tree is inserted.

       float totalWeight = 0;
       for (int i = 0; i < n-1; i++)
         {
           int insertVertex = nearestVertexToMST;
           nearestVertexToMST = -1;
           float minWeight = Float.MAX_VALUE;
           for (int j = 0; j < n-1; j++)
               if (! inMST[j])
                  {
                    nearestWeight[j] = min (nearestWeight[j],
                                            weights.value (j, insertVertex));
                    if (nearestWeight[j] < minWeight)
                      {
                        nearestVertexToMST = j;
                        minWeight = nearestWeight[j];
                      }
                  }
           assert nearestVertexToMST >= 0;
           inMST[nearestVertexToMST] = true;
           totalWeight += minWeight;
         }

       return totalWeight;
     }
}

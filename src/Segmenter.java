/*
 *  Decompose a graph into segments, meaning lists of sequential adjacent
 *  vertices.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.Print.printf;

public class Segmenter
{
  int numSegments;
  Segment [] segments;
  float medianMult = 2F;
  boolean print = false;

  //////////////////////////////////////////////////////////////////////////////

  void setMedianMult (float medianMult)
    {
      assert medianMult >= 0;
      this.medianMult = medianMult;
    }

  //////////////////////////////////////////////////////////////////////////////

  Segment [] segments (int edges [], Weights weights)
    {
      if (print)
          printf ("Segment: segments%n%n   Num nodes = %d%n",
                  edges.length+1);
      assert edges.length > 0;

      //  Handle two values separately.

      if (edges.length == 1)
          return new Segment []
            { new SegmentSeries (new int [] { 0, 1 }) };

      int n = edges.length + 1;
      numSegments = 0;
      segments = new Segment [n];

      //  Find the degree of every vertex in the tree

      int degrees [] = new int [n];
      for (int i = 0; i < n-1; i++)
        {
          degrees[i] ++;
          degrees[edges[i]] ++;
        }

      //  Determine a distance limit, which is a multiple of the median
      //  distance between every two nodes.

      int length = edges.length;
      float adjWeights [] = new float [length-1];
      for (int i = 0; i < length-1; i++)
          adjWeights[i] = weights.value (i, edges[i]);
      float median = Sort.median (adjWeights, length-1);
      if (print)
          printf ("   Median = %f%n", median);
      float limit = medianMult * median;

      //  Build the connect structure, which lists every vertex that every
      //  vertex is connected to in the minimum spanning tree.
      //  Don't connect anything which is beyond the limit.

      int connect [] [] = new int [n] [];
      for (int i = 0; i < n; i++)
        {
          connect[i] = new int [degrees[i]];
          degrees[i] = 0;
        }
      for (int i = 0; i < n-1; i++)
          if (limit == 0 || weights.value (i, edges[i]) <= limit)
            {
              connect [i] [degrees[i]++] = edges[i];
              connect [edges[i]] [degrees[edges[i]]++] = i;
            }
          else if (print)
              printf ("   %d %d exceeds limit%n", i, edges[i]);

      //  Dump the connect structure.

      if (print)
        {
          printf ("   Minimum Spanning Tree Edges%n");
          for (int i = 0; i < n-1; i++)
              printf ("     %4d %4d%n", i+1, edges[i]);
          printf ("%n%n");
          printf ("   Minimum Spanning Tree Connections%n");
          for (int i = 0; i < n; i++)
            {
              printf ("   %4d: (%4d)", i, degrees[i]);
              for (int j = 0; j < connect[i].length; j++)
                  printf (" %4d", connect[i][j]);
              printf ("%n");
            }
          printf ("%n");
        }

      // Build the segments.

      int start = 0;
      boolean visited [] = new boolean [n];
      for (start = 0; start < n; start++)
        {
          if (! visited[start] && degrees[start] <= 1)
              buildRecurse (start, degrees, connect, visited);
        }

      //  Shorten the output segment array.

      Segment newSegments [] = new Segment [numSegments];
      for (int i = 0; i < numSegments; i++)
          newSegments[i] = segments[i];
      return newSegments;
    }

  //////////////////////////////////////////////////////////////////////////////

  private void buildRecurse (int start, int degrees[], int connect [] [],
                             boolean isVisited [])
    {
      if (print)
          printf ("   Start = %5d", start);

      int degree = degrees[start];
      isVisited[start] = true;

      if (degree == 0 || (degree == 1 && isVisited [connect[start][0]]))
        {
          if (print)
              printf ("   A %3d   Length %4d%n", numSegments+1, 1);
          segments[numSegments++] = new SegmentSeries (start);
        }
      else if (degree <= 2)
        {
          int series [] = new int [connect.length];
          series[0] = start;
          int n = 1;
          while (true)
            {
              if (degree == 1 || ! isVisited [connect [start][0]])
                  start = connect [start] [0];
              else
                  start = connect [start] [1];
              degree = degrees[start];
              if (degree == 1)
                {
                  isVisited[start] = true;
                  series[n++] = start;
                  if (print)
                      printf ("   B %3d   Length %4d%n", numSegments+1, n);
                  segments[numSegments++] = new SegmentSeries (series, n);
                  return;
                }
              else if (degree == 2)
                {
                  isVisited[start] = true;
                  series[n++] = start;
                }
              else
                {
                  assert degree >= 3;
                  if (print)
                      printf ("   C %3d   Length %4d%n", numSegments+1, n);
                  segments[numSegments++] = new SegmentSeries (series, n);
                  buildRecurse (start, degrees, connect, isVisited);
                  return;
                }
            }
        }
      else    // degree >= 3
        {
          if (print)
              printf ("   D %3d   Length %4d%n", numSegments+1, 1);
          segments[numSegments++] = new SegmentSeries (start);
          for (int i = 0; i < degree; i++)
              if (! isVisited [connect [start] [i]])
                  buildRecurse (connect [start] [i], degrees, connect,
                                isVisited);
        }

      if (print)
         printf ("%n");
    }
}

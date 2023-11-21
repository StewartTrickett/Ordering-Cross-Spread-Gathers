/*
 *  Find the shortest path through a list of segments. This is a Shortest
 *  Path problem. We convert the problem to a Shortest Hamiltonian Path problem
 *  with vertices rather than segments follows:
 *
 *     The m vertices of the graph are the terminals of the segments.
 *     Set the weight between two terminals of the same segment to zero.
 *     Set the weights between all other pairs of vertices to e + m D, where
 *     e is the physical distance between the two sources that the vertices
 *     represent and D is the greatest physical distance between any two
 *     sources in the line.
 *
 *  Thanks to the addition of m D to most edges, one can prove that the two
 *  terminals of each segment will be adjacent to each other in the final path.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.pathWeight;
import static CrossSpreadOrder.Print.printf;

////////////////////////////////////////////////////////////////////////////////

public class SegmentShortHamPath
{
  private boolean print = false;

  //////////////////////////////////////////////////////////////////////////////

  public void setPrint (boolean print)
    { this.print = print; }

  //////////////////////////////////////////////////////////////////////////////

  public Segment [] path (Segment segments [], Weights weights)
    {
      int numSegs = segments.length;
      if (print)
          printf ("Shortest Hamiltonian Path Problem%n%n" +
                  "   Number of segments = %d%n%n", numSegs);
      if (numSegs == 1)
          return segments;

      //  Determine the total number of vertices (segment terminals).

      int numTerms = 0;
      for (int i = 0; i < numSegs; i++)
          if (segments[i].length() == 1)
              numTerms += 1;
          else
              numTerms += 2;
      if (print)
          printf ("Number of segments, vertices = %d %d%n", numSegs, numTerms);

      //  Determine the weights between all of the vertices.
      //  The weight between the start and end of a segment is 0.
      //  The weight between all others is weight(i,j) + m D,
      //  where m is the number of vertices and D is the greatest weight
      //  between any vertices.

      float matrix [] [] = new float [numTerms] [numTerms];
      int terminals [] = new int [numTerms];
      boolean isStart [] = new boolean [numTerms];
      int count = 0;
      for (int i = 0; i < numSegs; i++)
          if (segments[i].length() == 1)
              terminals[count++] = segments[i].start();
          else
            {
              isStart[count] = true;
              terminals[count++] = segments[i].start();
              terminals[count++] = segments[i].end();
            }
      float D = 0;
      for (int i = 0; i < numTerms-1; i++)
      for (int j = i+1; j < numTerms; j++)
          D = Math.max (D, weights.value (terminals[i], terminals[j]));
      float md = 1.001F * numTerms * D;
      if (print)
          printf ("D = %.1f  mD = %.1f%n", D, md);
      for (int i = 0; i < numTerms-1; i++)
      for (int j = i+1; j < numTerms; j++)
          if (! (i+1 == j && isStart[i]))
              matrix[i][j] = matrix[j][i] =
                  weights.value (terminals[i], terminals[j]) + md;
      Weights newWeight = new WeightsMatrix (matrix);

      //  Find the shortest Hamiltonian path.

      ShortHamPathBAB shp = new ShortHamPathBAB (newWeight);
      shp.setPrint (print);
      float lowerLimit = 0.9999F * md * (numSegs-1);
      float upperLimit = 1.0001F * md * numSegs;
      shp.setUpperLimit (upperLimit);
      int path [] = shp.path (numTerms);
      float pathWeight = pathWeight (newWeight, path);
      if (print)
          printf ("LowerLimit, path weight, UpperLimit = %.0f %.0f %.0f%n",
                  lowerLimit, pathWeight, upperLimit);
      assert lowerLimit <= pathWeight && pathWeight < upperLimit;

      //  Put the segments in their final order.

      Segment orderedSegments [] = new Segment [numSegs];
      count = 0;
      for (int i = 0; i < numTerms; i++)
        {
          for (int j = 0; j < numSegs; j++)
              if (segments[j].start() == terminals[path[i]])
                {
                  orderedSegments[count] = segments[j];
                  if (orderedSegments[count].length() > 1)
                      i++;
                }
              else if (segments[j].end() == terminals[path[i]])
                {
                  orderedSegments[count] = segments[j];
                  orderedSegments[count].reverse ();
                  if (orderedSegments[count].length() > 1)
                      i++;
                }
          count ++;
        }
      assert count == numSegs;
      return orderedSegments;
    }
}

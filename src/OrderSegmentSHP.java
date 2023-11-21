/**
 *  Order the locations within a shot or receiver line in a physically sensible
 *  way.
 *
 *  Given an array of location coordinates, method "path" returns an array
 *  of length n = coords.length where the ordering of the coordinates is:
 *
 *     coords[path[0]] coords[path[1]] ... coords[path[n-1]]
 *
 *  This uses the "Segmented SHP" algorithm, which is probably the best among
 *  the four ordering algorithms. It working by finding an heuristic
 *  approximation to the Shortest Hamiltonian Path as follows:
 *
 *     1. Find the minimum-spanning tree of the locations.
 *     2. Split the MST into unconnected paths called segments.
 *     3. Find the Shortest Hamiltonian Path connecting the segments.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

///////////////////////////////////////////////////////////////////////////////

public class OrderSegmentSHP extends OrderLocns
{
   MinSpanTree mst = new MinSpanTreePrims ();
   Segmenter segmenter = new Segmenter ();
   SegmentShortHamPath shpSeg = new SegmentShortHamPath ();

   private int MaxBranching = 30, numSegments = 0;

  //////////////////////////////////////////////////////////////////////////////
  //  This is parameter "c". 2.5 is a good value.

  void setMedianMult (float medianMult)
    { segmenter.setMedianMult (medianMult); }

  //////////////////////////////////////////////////////////////////////////////
  //  Do not allow the number of segments to get too large, as finding the
  //  Shortest Hamilton Path between them is NP hard.

  void setMaxBranching (int maxBranching)
    {
      assert maxBranching >= 0;
      this.MaxBranching = maxBranching;
    }

  /////////////////////////////////////////////////////////////////////////////

  @Override
  public int [] path (Coord coords [])
    {
      Weights weights = new WeightsEuclid (coords);
      return path (coords.length, weights);
    }

  /////////////////////////////////////////////////////////////////////////////

  public int [] path (int n, Weights weights)
    {
      if (n <= 2)
        {
          int path [] = new int [n];
          for (int i = 0; i < n; i++)
              path[i] = i;
          return path;
        }

      //  Calculate minimum spanning tree (MST)

      int edges [] = mst.edges (n, weights);

      //  Reduce the branching degree of the MST if it's too large
      //  using the branch-elimination algorithm.

      ShortHamPathBE shpBE = new ShortHamPathBE (weights);
      shpBE.reduceBranching (edges, MaxBranching);

      //  Segment the MST.

      Segment segments [] = segmenter.segments (edges, weights);
      numSegments = segments.length;

      //  Order the segments to minimize weight between terminals.

      shpSeg.setPrint (print);
      Segment orderedSegments [] = shpSeg.path (segments, weights);

      //  Convert the ordered segments into a full vertex path.

      int path [] = new int [n];
      int start = 0;
      for (Segment orderedSegment : orderedSegments)
        {
          orderedSegment.load (path, start);
          start += orderedSegment.length ();
        }
      assert start == n;
      return path;
    }

  /////////////////////////////////////////////////////////////////////////////
  //  Called after "path" is called. This is for diagnostic purposes only.

  int numSegments ()
    {
      assert numSegments >= 1;
      return numSegments;
    }
}

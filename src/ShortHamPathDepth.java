/*
 *  Find the shortest Hamiltonian path through a list of vertices.
 *
 *  This is a naive depth-search algorithm used for testing.
 *  Don't use it for n > 10. It's too slow with O(n!) complexity.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.isPath;
import static CrossSpreadOrder.GraphUtil.pathWeight;
import static CrossSpreadOrder.Print.printf;

////////////////////////////////////////////////////////////////////////////////

public class ShortHamPathDepth extends ShortHamPath
  {
    int bestPath [];
    float bestWeight;
    long numCheckBest, numSetBest;

    ////////////////////////////////////////////////////////////////////////////

    ShortHamPathDepth (Weights weights)
      { super (weights); }

    ////////////////////////////////////////////////////////////////////////////

    @Override
    public int [] path (int n)
      {
        assert n >= 0;
        if (n <= 1)
            return new int [n];
        if (print)
           printf ("Shortest Hamiltonian Path Problem%n%n" +
                   "   Number of vertices = %d%n%n", n);

        //  Solve the system.

        bestWeight = Float.MAX_VALUE;
        numCheckBest = numSetBest = 0;
        int path [] = new int [n];
        for (int i = 0; i < n; i++)
            path[i] = i;
        for (int i = 0; i < n; i++)
          {
            swap (path, 0, i);
            depthSearch (path, 0F, 1, n);
          }

        //  Do some sanity checking and place segments with the optimum
        //  ordering before returning.

        assertCorrect ();
        return bestPath;
      }

    ////////////////////////////////////////////////////////////////////////////

    @Override
    public int [] semiEnclosedPath (int n)
      {
        assert n >= 0;
        if (n <= 1)
            return new int [n];
        if (print)
           printf ("Shortest Hamiltonian Path Problem%n%n" +
                   "   Number of vertices = %d%n%n", n);

        //  Solve the system.

        bestWeight = Float.MAX_VALUE;
        numCheckBest = numSetBest = 0;
        int path [] = new int [n];
        for (int i = 0; i < n; i++)
            path[i] = i;
        depthSearch (path, 0F, 1, n);

        //  Do some sanity checking and place segments with the optimum
        //  ordering before returning.

        assertCorrect ();
        assert path[0] == 0;
        return bestPath;
      }

    ////////////////////////////////////////////////////////////////////////////

    @Override
    public int [] enclosedPath (int n)
      {
        assert n >= 0;
        if (n <= 1)
            return new int [n];
        if (print)
           printf ("Shortest Hamiltonian Path Problem%n%n" +
                   "   Number of vertices = %d%n%n", n);

        //  Solve the system.

        bestWeight = Float.MAX_VALUE;
        numCheckBest = numSetBest = 0;
        int path [] = new int [n];
        for (int i = 0; i < n; i++)
            path[i] = i;
        depthSearch (path, 0F, 1, n-1);

        //  Do some sanity checking and place segments with the optimum
        //  ordering before returning.

        assertCorrect ();
        assert path[0] == 0 && path[n-1] == n-1;
        return bestPath;
      }

    ////////////////////////////////////////////////////////////////////////////

    protected void depthSearch (int path [], float weightSoFar,
                                int start, int end)
      {
        assert start >= 0;
        assert start < end;
        assert end <= path.length;

        //  Mild pruning.

        if (weightSoFar >= bestWeight)
           return;

        //  Bottom of the depth search?

        if (start == end-1)
          {
            int n = path.length;
            for (int i = start; i < n; i++)
                weightSoFar += weights.value (path[i-1], path[i]);
            checkBest (weightSoFar, path);
            return;
          }

        //  Continue the depth search to the level below

        int path1 [] = path.clone ();
        for (int i = start; i < end; i++)
          {
            swap (path1, start, i);
            float weight = weights.value (path1[start-1], path1[start]);
            depthSearch (path1, weightSoFar + weight, start + 1, end);
          }
      }

    ////////////////////////////////////////////////////////////////////////

    protected final void swap (int path [], int i, int j)
      {
        int temp = path[i];
        path[i] = path[j];
        path[j] = temp;
      }

    ////////////////////////////////////////////////////////////////////////
    //  Check if the current solution is the best so far.

    protected void checkBest (int path [])
      { checkBest (GraphUtil.pathWeight (weights, path), path); }

    protected void checkBest (float totalWeight, int path [])
      {
        numCheckBest ++;
        if (totalWeight >= bestWeight)
            return;
        numSetBest ++;
        bestWeight = totalWeight;
        int n = path.length;
        if (numSetBest == 1)
            bestPath = new int [n];
        for (int i = 0; i < n; i++)
            bestPath[i] = path[i];

        if (print)
            printf ("      Best so far with weight %.4g%n", bestWeight);
      }

    ///////////////////////////////////////////////////////////////////////////
    //  Sanity checking on the final solution. This is intended to find bugs.

    void assertCorrect ()
      {
        //  Ensure the best solution has been updated at least once.

        assert numCheckBest > 0 : "Best path was never set. " +
                                  "The upper limit may have been too low.";

        //  Ensure bestVertices is a true permutation.

        assert isPath (bestPath);

        //  Ensure best weight matches the actual best vertices.

        float pathWeight = pathWeight (weights, bestPath);
        assert Math.abs (pathWeight - bestWeight) <= bestWeight / 100;
     }
}

/*
 *  Find the shortest Hamiltonian path through a list of vertices.
 *  This is a branch-and-bound algorithm with extensive pruning.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.Print.printf;

////////////////////////////////////////////////////////////////////////////////

public class ShortHamPathBAB extends ShortHamPathDepth
  {
    long numPrunes [][], numCalls [];
    float upperLimit = Float.MAX_VALUE;
    Timer timer = new Timer ();
    int paths [] [];
    VerifySHP verifySHPs [];

    ////////////////////////////////////////////////////////////////////////////

    public ShortHamPathBAB (Weights weights)
      {
        super (weights);
        verifySHPs = new VerifySHP []
          {
            new VerifySHPReverse (weights),
            new VerifySHPDivide (weights),
            new VerifySHPMove (weights),
          };
      }

    ////////////////////////////////////////////////////////////////////////////

    void setUpperLimit (float upperLimit)
      {
        assert upperLimit >= 0;
        this.upperLimit = upperLimit;
      }

    ////////////////////////////////////////////////////////////////////////////

    @Override
    public int [] path (int n)
      {
        assert n >= 0;
        if (print)
            printf ("Shortest Hamiltonian Path Problem%n%n" +
                    "   Number of vertices = %d%n%n", n);
        if (n <= 1)
            return new int [n];

        paths = new int [n] [n];

        // The best path so far.

        bestPath = new int [n];
        bestWeight = upperLimit;

        //  Diagnostics

        numPrunes = new long [7][n];
        numCalls = new long [n];
        timer = new Timer ();

        //  Solve the problem using a fast heurestic method.
        //  This gives us a reasonable upper limit.

        ShortHamPath approx = new ShortHamPathBE (weights);
        int path [] = approx.path (n);
        for (int i = 0; i < n /2 ; i++)
            swap (path, i, n-i-1);
        checkBest (path);

        //  Perform the depth-first search for solutions.
        //  i == n-1 need not be considered as this problem is symmetrical.

        for (int i = 0; i < n-1; i++)
          {
            swap (path, 0, i);
            depthSearch (path, 0, n, 0F);
          }

        //  Print a report (if requested) and do some sanity checking.

        if (print)
            printReport (path);
        assertCorrect ();

        return bestPath;
      }

    ////////////////////////////////////////////////////////////////////////////

    public void depthSearch (final int path [], int level, int n,
                             float weightSoFar)
      {
        //  Diagnostics

        if (print && level == 0)
          {
            printf ("   Top indices = ");
            for (int i = 0; i <= level; i ++)
                printf ("%3d", path[i]);
            printf ("%n");
          }
        numCalls[level] ++;

        //  Bottom of the depth search?

        if (level == n-2)
          {
            checkBest (weightSoFar + weights.value (path[level], path[level+1]),
                       path);
            return;
          }

        //  Prune if the path down to this point is not part of an SHP.
        //  This is a fast and valuable way to prune.

        for (int i = 0; i < verifySHPs.length; i++)
            if (verifySHPs[i].reject (path, level))
              {
                numPrunes[i][level]++;
                numPrunes[6][level]++;
                return;
              }

        //  Prune if the remaining vertices will cause the entire weight
        //  to exceed the best weight so far.

        if (bestWeight <= weightSoFar + remainingLowerBound (path, level, n))
          {
            numPrunes[5][level]++;
            numPrunes[6][level]++;
            return;
          }

        //  Continue the depth search to the next level

        int parent = path [level];
        copy (path, paths[level]);
        int pathT [] = paths [level];
        for (int i = level + 1; i < n; i++)
          {
            swap (pathT, i, level + 1);
            depthSearch (pathT, level + 1, n,
                         weightSoFar + weights.value (path[i], parent));
          }
      }

   ////////////////////////////////////////////////////////////////////////////
   //  Lower bound of the remaining vertices from level to the end (n-1).

   private float remainingLowerBound (int path [], int level, int n)
     {
       assert level >= 0 && level < n;
       int remaining = n - level - 1;
       if (remaining == 0)
           return 0;
       else if (remaining == 1)
           return weights.value (path[level], path[level+1]);

       int newPath [] = new int [remaining+1];
       for (int i = 0; i < remaining+1; i++)
           newPath[i] = path[i+level];
       LowerBound bound = new LowerBoundMST (weights);
       return bound.semiEnclosedSHP (newPath);
     }

    ////////////////////////////////////////////////////////////////////////

    static void copy (int in [], int out [])
      {
        int n = in.length;
        assert n == out.length;
        for (int i = 0; i < n; i++)
            out[i] = in[i];
      }

    ////////////////////////////////////////////////////////////////////////

    private void printReport (int path [])
      {
        printf ("%n   Optimum path has a weight of %.4g.%n", bestWeight);
        printf ("%n   Execution time was %.3f s%n", timer.elapsed());
        int n = path.length;
        printf ("   Number of optimum checks was %d.%n", numCheckBest);
        printf ("   Optimum path was replaced %d times.%n%n", numSetBest);

        int m = numPrunes.length;
        printf ("   Level  ");
        for (int j = 0; j < m; j++)
            printf ("  Prune %1d", j+1);
        printf ("       Calls%n");
        long total [] = new long [m], totalCalls = 0;
        for (int i = 0; i < n; i++)
          {
            printf ("     %3d  ", i);
            for (int j = 0; j < m; j++)
              {
                if (numPrunes[j][i] == 0)
                    printf ("         ");
                else
                    printf (" %8d", 1000 * numPrunes[j][i] / numCalls[i]);
                total[j] += numPrunes[j][i];
              }
            printf (" %11d%n", numCalls[i]);
            totalCalls += numCalls[i];
          }

        total[m-1] /= 1000;
        printf ("   Total  ");
        for (int j = 0; j < numPrunes.length; j++)
            printf (" %8d", total[j]);
        printf (" %11d%n", totalCalls);
      }
}

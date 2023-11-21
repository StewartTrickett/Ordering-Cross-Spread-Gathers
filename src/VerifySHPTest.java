/**
 *  Unit test for Shortest Hamiltonian Path.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.pathWeight;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class VerifySHPTest
{
  private boolean print = true;

  /////////////////////////////////////////////////////////////////////////////
  //  Do these SHP verify tests detect when two vertices have been swapped
  //  within an SHP?.

  @Test
  public void swap ()
    {
      printf ("%s: %s%n%n", className(), methodName());
      Random rand = new Random (221);

      for (int n = 0; n <= 12; n++)
      for (int test = 0; test < 10; test++)
        {
          //  Build the weights and verifiers.

          Coord coords [] = new Coord [n];
          for (int i = 0; i < n; i++)
              coords[i] = new Coord (rand.nextFloat(), rand.nextFloat());
          Weights weights = new WeightsEuclid (coords);
          VerifySHP verifySHPs [] = new VerifySHP []
            {
              new VerifySHPDivide  (weights),
              new VerifySHPMove    (weights),
              new VerifySHPReverse (weights),
            };

          //  Build the SHP

          ShortHamPath shp = new ShortHamPathDepth (weights);
          int path [] = shp.path (n);

          //  Make sure the verifiers don't reject it.

          for (VerifySHP verify : verifySHPs)
          for (int posn = 0; posn < n; posn++)
              assertTrue (! verify.reject (path, posn));

          //  Swap two positions and ensure at least one verifier rejects it.

          for (int i = 0; i < n-2; i++)
          for (int j = i+1; j < n-1; j++)
            {
              if (print && j == 1)
                  printf ("%n");
              swap (path, i, j);
              if (print)
                  printf ("   n, i, j:   %2d %2d %2d   ", n, i, j);
              boolean rejectTotal = false;
              for (VerifySHP verify : verifySHPs)
                {
                  boolean reject = false;
                  for (int posn = 0; posn < n; posn++)
                      if (verify.reject (path, posn))
                          reject = true;
                  if (print)
                    {
                      if (reject)
                          printf ("*");
                      else
                          printf (".");
                    }
                  rejectTotal |= reject;
                }
              if (print)
                {
                  if (rejectTotal)
                      printf (" *");
                  else
                      printf (" .");
                }
              assertTrue (rejectTotal);
              swap (path, i, j);                 // Undo the swap
              if (print) printf ("%n");
            }
        }
      printf ("%n");
    }

  /////////////////////////////////////////////////////////////////////////////
  //  Do these SHP-verify tests detect when a good heurestic solution is not
  //  exactly the SHP?

  @Test
  public void heuristic ()
    {
      printf ("%s: %s%n%n", className(), methodName());
      Random rand = new Random (221);

      int numInexact = 0, numDetected = 0, numVerifies = 3;
      int verifyCount [] = new int [numVerifies];

      for (int n = 5; n <= 25; n++)
      for (int test = 1; test <= 10; test++)
        {
          //  Build the weights and verifys.

          Coord coords [] = new Coord [n];
          for (int i = 0; i < n; i++)
              coords[i] = new Coord (rand.nextFloat(), rand.nextFloat());
          Weights weights = new WeightsEuclid (coords);
          VerifySHP verifySHPs [] = new VerifySHP []
            {
              new VerifySHPDivide  (weights),
              new VerifySHPMove    (weights),
              new VerifySHPReverse (weights),
            };
          assert numVerifies == verifySHPs.length;

          //  Build the exact and heurestic paths and get their weights.

          ShortHamPath shp = new ShortHamPathBAB (weights);
          int pathExact [] = shp.path (n);
          float weightExact = pathWeight (weights, pathExact);

          shp = new ShortHamPathBE (weights);
          int pathHeuristic [] = shp.path (n);
          float weightHeuristic = pathWeight (weights, pathHeuristic);

          //  Make sure the verifiers don't reject the exact SHP.

          for (VerifySHP verify : verifySHPs)
          for (int posn = 0; posn < n; posn++)
              assertTrue (! verify.reject (pathExact, posn));

          if (weightHeuristic < 1.0001F * weightExact)
              continue;

          numInexact ++;
          printf ("   %3d %3d:   %5.2f %5.2f    %2.0f%%    ",
                  n, test, weightExact, weightHeuristic,
                  100 * (weightHeuristic - weightExact) / weightExact);

          // Do the tests reject the heurestic SHP?

          boolean rejectTotal = false;
          int ithVerify = 0;
          for (VerifySHP verify : verifySHPs)
            {
              boolean reject = false;
              for (int posn = 0; posn < n; posn++)
                  if (verify.reject (pathHeuristic, posn))
                      reject = true;
              if (print)
                {
                  if (reject)
                      printf ("*");
                  else
                      printf (".");
                }
              rejectTotal |= reject;
              if (reject)
                  verifyCount[ithVerify] ++;
              ithVerify ++;
            }
          if (rejectTotal)
              numDetected ++;
          if (rejectTotal)
            { if (print) printf (" *%n"); }
          else
            { if (print) printf (" .%n"); }
        }

      printf ("%n   NumInexact, NumDetermined, Ratio = %4d %4d   %3.0f%%   ",
              numInexact, numDetected, 100F * numDetected / numInexact);
      for (int i = 0; i < numVerifies; i++)
          printf (" %2.0f%%", 100F * verifyCount[i] / numInexact);
      printf ("%n");
    }

  //////////////////////////////////////////////////////////////////////////////

  private void swap (int path [], int i, int j)
    { int t = path[i]; path[i] = path[j]; path[j] = t; }
}

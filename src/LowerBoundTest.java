/**
 *  Unit test for LowerBoundMST
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

public class LowerBoundTest
{
  boolean code [];
  Random rand = new Random ();

  /////////////////////////////////////////////////////////////////////////////
  //  Test the Branch & Bound algorithm against the Naive algorithm.

  @Test
  public void compareBounds ()
    {
       printf ("%s: %s%n%n", className(), methodName());

      int weightType = 1, numTests = 10000;
      float elongate = 1;

      float sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0;
      Random rand = new Random (221);

      for (int test = 1; test <= numTests; test++)
        {
          int n = rand.nextInt (21);

          //  Build weights

         Coord coords [] = new Coord [n];
         for (int i = 0; i < n; i++)
              coords[i] = new Coord (elongate * rand.nextFloat(),
                                        rand.nextFloat());

          //  Choose from all sorts of weightings.
          //  Only the first is metric.

          Weights weights;
          if (weightType == 1)
              weights = new WeightsEuclid (coords);
          else if (weightType == 2)
              weights = new WeightsEuclidSq (coords);
          else if (weightType == 3)
            {
              weights = new WeightsEuclid (coords);
              float matrix [] [] = new float [n] [n];
              for (int i = 0; i < n-1; i++)
              for (int j = i+1; j < n; j++)
                  matrix[i][j] = matrix[j][i] =
                      (float) Math.pow (weights.value(i,j), 3);
              weights = new WeightsMatrix (matrix);
            }
          else    // Completely random weights between vertices.
            {
              float matrix [] [] = new float [n] [n];
              for (int i = 0; i < n-1; i++)
              for (int j = i+1; j < n; j++)
                  matrix[i][j] = matrix[j][i] = rand.nextFloat ();
              weights = new WeightsMatrix (matrix);
            }

          //  Shortest Hamiltonian path.

          ShortHamPathBAB shp = new ShortHamPathBAB (weights);
          int path [] = shp.path (n);
          float shpWeight = pathWeight (weights, path);

          //  Determine estimated bounds

          LowerBound bounds = new LowerBoundMST (weights);
          float lbWeight   = bounds.shp (n);
          float lbWeightSE = bounds.semiEnclosedSHP (path);
          float lbWeightE  = bounds.enclosedSHP (path);

          if (n <= 1)
            {
              assert shpWeight == 0 && lbWeight == 0 &&
                     lbWeightSE == 0 && lbWeightE == 0;
              shpWeight = lbWeight = lbWeightSE = lbWeightE = 1;
            }

          //  Diagnostics

          if (test % (numTests / 100) == 0)
              printf ("%8d:  %3d    %4.0f %4.0f %4.0f%n",
                      test, n,
                      100 * lbWeight   / shpWeight,
                      100 * lbWeightSE / shpWeight,
                      100 * lbWeightE  / shpWeight);
         if (n > 1)
            {
              sum1 += n;
              sum2 += n * lbWeight   / shpWeight;
              sum3 += n * lbWeightSE / shpWeight;
              sum4 += n * lbWeightE  / shpWeight;
            }

          //  Assert correctness.

          float tol = 1.00001F;
          assertTrue (lbWeight   <= tol * shpWeight);
          assertTrue (lbWeightSE <= tol * shpWeight);
          assertTrue (lbWeightE  <= tol * shpWeight);
          assertTrue (lbWeight   <= tol * lbWeightSE);
          assertTrue (lbWeightSE <= tol * lbWeightE);
          if (n == 2)
              assert lbWeight   == shpWeight &&
                     lbWeightSE == shpWeight &&
                     lbWeightE  == shpWeight;
        }
      printf ("%n   Average underestimates:   %.2f %.2f %.2f%n%n",
              100 * sum2 / sum1, 100 * sum3 / sum1, 100 * sum4 / sum1);
    }
}

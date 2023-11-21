/**
 *  Unit test for Weights and its childrem.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class WeightsTest
{
  boolean code [];
  Random rand = new Random ();

  /////////////////////////////////////////////////////////////////////////////
  //  Test WeightsEuclid and WeightsEuclidSq

  @Test
  public void euclid ()
    {
      printf ("%s: %s%n%n", className(), methodName());

      Random rand = new Random ();
      float tol = 1E-5F;

      int numTests = 10000;
      for (int test = 0; test < numTests; test++)
        {
          int n = rand.nextInt (100);

          Coord coords [] = new Coord [n];
          for (int i = 0; i < n; i++)
              coords[i] = new Coord (rand.nextFloat(), rand.nextFloat());

          Weights weight1 = new WeightsEuclid (coords);
          Weights weight2 = new WeightsEuclidSq (coords);

          assert weight1.isSymmetric ();
          assert weight1.isTriangleInequality ();
          assert weight2.isSymmetric ();
          assert ! weight2.isTriangleInequality ();

          for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
            {
              float dist = coords[i].distance (coords[j]);
              assertEquals (weight1.value (i, j), dist, tol);
              assertEquals (weight2.value (i, j), dist * dist, tol);
            }
        }
    }

  /////////////////////////////////////////////////////////////////////////////
  //  Test WeightsMatrix

  @Test
  public void matrix ()
    {
      printf ("%s: %s%n%n", className(), methodName());

      Random rand = new Random ();

      int numTests = 10000;
      for (int test = 0; test < numTests; test++)
        {
          int n = rand.nextInt (100);

          Coord coords [] = new Coord [n];
          for (int i = 0; i < n; i++)
              coords[i] = new Coord (rand.nextFloat(), rand.nextFloat());

          Weights weight1 = new WeightsEuclid (coords),
                     weight2 = new WeightsMatrix (n, weight1);

          assert weight1.isSymmetric ();
          assert weight1.isTriangleInequality ();
          assert weight2.isSymmetric ();
          assert weight2.isTriangleInequality ();

          for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
              assertTrue (weight1.value (i, j)  == weight2.value(i,j));
        }
    }

  /////////////////////////////////////////////////////////////////////////////
  //  Test WeightsShift

  @Test
  public void shift ()
    {
      printf ("%s: %s%n%n", className(), methodName());

      Random rand = new Random ();

      int numTests = 1000;
      for (int test = 0; test < numTests; test++)
        {
          int n = rand.nextInt (100);
          int shift = n - rand.nextInt (2 * n + 1);
          if (test % (numTests/10) == 10)
             printf ("   %4d %4d%n", n, shift);

          Coord coords [] = new Coord [n];
          for (int i = 0; i < n; i++)
              coords[i] = new Coord (rand.nextFloat(), rand.nextFloat());

          Weights weight1 = new WeightsEuclid (coords),
                     weight2 = new WeightsShift (weight1, shift);

          assert weight1.isSymmetric ();
          assert weight1.isTriangleInequality ();
          assert weight2.isSymmetric ();
          assert weight2.isTriangleInequality ();

          for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
            {
              int is = i + shift, js = j + shift;
              if (is >= 0 && is < n && js >= 0 && js < n)
                  assertTrue (weight1.value (is, js) == weight2.value (i, j));
            }
        }
      printf ("%n");
    }

  /////////////////////////////////////////////////////////////////////////////
  //  Test WeightsRenumber

  @Test
  public void renumber ()
    {
      printf ("%s: %s%n%n", className(), methodName());

      Random rand = new Random ();

      int numTests = 1000;
      for (int test = 0; test < numTests; test++)
        {
          int n = rand.nextInt (101);
          int from [] = shuffle (rand, n);
          int to [] = shuffle (rand, n);
          if (test % (numTests/10) == 10 && n > 2)
            {
              printf ("   %3d %3d %3d", from[0], from[1], from[2]);
              printf ("   %3d %3d %3d%n", to[0], to[1], to[2]);
            }

          Coord coords [] = new Coord [n];
          for (int i = 0; i < n; i++)
              coords[i] = new Coord (rand.nextFloat(), rand.nextFloat());

          //  Test using "from" only.

          Weights weight1 = new WeightsEuclid (coords),
                     weight2 = new WeightsRenumber (weight1, from);
          assert weight1.isSymmetric ();
          assert weight1.isTriangleInequality ();
          assert weight2.isSymmetric ();
          assert weight2.isTriangleInequality ();
          for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
            {
              int fi = from[i], fj = from[j];
              assertTrue (weight1.value (i, j) == weight2.value (fi, fj));
            }

          //  Test using to and from.

          weight2 = new WeightsRenumber (weight1, from, to);
          for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
            {
              int fi = from[i], fj = from[j], ti = to[i], tj = to[j];
              assertTrue (weight1.value (ti, tj) == weight2.value (fi, fj));
            }
         }
      printf ("%n");
    }

  /////////////////////////////////////////////////////////////////////////////

  private int [] shuffle (Random rand, int n)
    {
      assert n >= 0;
      int order [] = new int [n];
      for (int i = 0; i < n; i++)
          order[i] = i;
      for (int i = n-1; i >= 1; i--)
        {
          int j = rand.nextInt (i+1);
          int t = order[i]; order[i] = order[j]; order[j] = t;
        }
      return order;
    }
}

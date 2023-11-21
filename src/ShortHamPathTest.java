/**
 *  Unit test for Shortest Hamiltonian Path.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.isPathASpanningTree;
import static CrossSpreadOrder.GraphUtil.pathWeight;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.Print.printff;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class ShortHamPathTest
{
  boolean code [];
  Random rand = new Random ();

  void printTitle ()
    { printf ("%s: %s%n%n", className (), methodName (1)); }

  /////////////////////////////////////////////////////////////////////////////
  //  Test exact algorithms against each other

  @Test
  public void compareExact ()
    {
      printTitle ();
      Random rand = new Random (221);
      int numTests = 1000, incr = numTests / 10;

      for (int test = 1; test <= numTests; test++)
        {
          boolean print = test % incr == 0;

          int n = 1 + rand.nextInt (11);
          if (print)
              printf ("   Test %6d:   n = %2d%n", test, n);

          //  Build weights

          Weights weights = scatterWeights (rand, n);

          //  Build exact methods

          ShortHamPath shps [] = new ShortHamPath []
            {
              new ShortHamPathBAB (weights),
              new ShortHamPathDepth (weights),
              new ShortHamPathDynamic (weights),
            };

          //  Compare exact methods to each other.

          int lastPath [] = null;
          for (ShortHamPath shp : shps)
            {
              int path [] = shp.path (n);
              assertTrue (isPathASpanningTree (path));
              if (lastPath != null && lastPath[0] != path[0])
                  reverse (path);
              if (print)
                {
                  printf ("      ");
                  for (int i = 0; i < n; i++)
                      printf ("%d ", path[i]);
                  printf ("%n");
                }

              //  Compare with last path.

              if (lastPath != null)
                {
                  float weight1 = pathWeight (weights, lastPath);
                  float weight2 = pathWeight (weights, path);
                  float tol = 1.00001F;
                  assertTrue (weight1 <= tol * weight2);
                  assertTrue (weight2 <= tol * weight1);
                }
              lastPath = path;
            }

          if (print)
              printf ("%n");
        }
    }

  /////////////////////////////////////////////////////////////////////////////

  @Test
  public void scatteredEnclosed ()
    {
      printTitle ();
      Random rand = new Random (612);

      for (int test = 0; test < 1000; test++)
        {
          int n = 1 + rand.nextInt (10);
          printf ("Test: %5d  n = %3d   ", test+1, n);

          //  Build weights

          Weights weights = scatterWeights (rand, n);

          //  Shortest Hamiltonian path.

          ShortHamPathDepth shp = new ShortHamPathDepth (weights);
          int start = rand.nextInt (n);
          int end = start + 1 + rand.nextInt (n-start);
          int vertices [] = new int [n];
          for (int i = 0; i < n; i++)
              vertices[i] = i;
          int path [] = shp.semiEnclosedPath (vertices, start, end);
          for (int element : path)
              printf ("%3d", element);
          printf ("%n");
        }
    }

  /////////////////////////////////////////////////////////////////////////////
  //  Test a branch and bound with n = 50.
  //  This takes a few a minutes.

  @Test
  public void branchNBound ()
    {
      printTitle ();
      Random rand = new Random (612);
      int n = 50;

      //  Build weights.

      Weights weights = scatterWeights (rand, n);

      //  Shortest Hamiltonian path.

      ShortHamPath shp = new ShortHamPathBAB (weights);
      shp.setPrint (true);

      int path [] = shp.path (n);
      printf ("%n");

      assertEquals (pathWeight (weights, path), 5.314F, .01F);

      printf ("%n");
   }

  /////////////////////////////////////////////////////////////////////////////
  //  Test branch-and-bound when the vertices are laid out more ore less
  //  along a line. It can handle much larger problems than random scattering.

  @Test
  public void linear ()
    {
      printTitle ();
      Random rand = new Random (972);
      float sigma = .5F;

      for (int test = 0; test < 100; test++)
        {
          int n = 1 + rand.nextInt (100);
          printf ("Test = %5d  n = %3d%n", test+1, n);
          Coord coords [] = new Coord [n];
          for (int i = 0; i < n; i++)
              coords[i] = new Coord (i + sigma * (float) rand.nextGaussian(),
                                        i + sigma * (float) rand.nextGaussian());
          Weights weights = new WeightsEuclid (coords);
          float upperLimit = 0;
          for (int i = 0; i < n-1; i++)
              upperLimit += weights.value (i, i+1);
          weights = new WeightsMatrix (n, weights);
          ShortHamPathBAB shp = new ShortHamPathBAB (weights);
          shp.setUpperLimit (1.001F * upperLimit);
          shp.path (n);
        }
      printf ("%n");
   }

  /////////////////////////////////////////////////////////////////////////////

  @Test
  public void timing ()
    {
      printTitle ();
      Random rand = new Random (972);

      int n = 30;
      for (int test = 0; test < 100; test++)
        {
          Weights weights = scatterWeights (rand, n);
          ShortHamPath shp = new ShortHamPathBAB (weights);
          shp = new ShortHamPathBAB (weights);
          shp.path (n);
        }
    }

  /////////////////////////////////////////////////////////////////////////////

  @Test
  public void timing1 ()
    {
      printTitle ();
      Random rand = new Random (972);

      int type = 3;

      Timer timer = new Timer ();
      for (int n = 0; n <= 20; n++)
        {
          for (int test = 0; test < 1; test++)
            {
              Weights weights = scatterWeights (rand, n);
              ShortHamPath shp = null;
              switch (type)
                {
                  case 1:   shp = new ShortHamPathDepth (weights);
                            break;
                  case 2:   shp = new ShortHamPathDynamic (weights);
                            break;
                  case 3:   shp = new ShortHamPathBAB (weights);
                            break;
                  default:  assert false;
                }
              shp.path (n);
            }
          printf ("   %2d: %7.2f%n", n, timer.cpu());
          if (timer.cpuTotal() > 60)
              break;
        }
      printf ("%n");
    }

  /////////////////////////////////////////////////////////////////////////////

  @Test
  public void dynamic ()
    {
      printTitle ();
      Random rand = new Random (972);
      Timer timer = new Timer ();

      for (int n = 1; n <= 24; n++)
        {
          printf ("   %2d: ", n);
          Weights weights = scatterWeights (rand, n);
          ShortHamPath shp = new ShortHamPathDynamic (weights);
          timer.restart ();
          printff ("  %5.0f   %5.2f s%n",
                   100 * pathWeight (weights, shp.path (n)),
                   timer.cpu());
          shp = new ShortHamPathBAB (weights);
          printff ("         %5.0f   %5.2f s%n%n",
                   100 * pathWeight (weights, shp.path (n)),
                   timer.cpu());
        }
      printf ("%n");
    }


  //////////////////////////////////////////////////////////////////////////////
  //  Test problem reduction.
  //  How much do MSTs reduce the size of the SHP problem?

  @Test
  public void reduce ()
    {
      printTitle ();

      int n = 25, numTests = 100;

      float maxRatio = 0, avgRatio1 = 0, avgRatio2 = 0;
      int numDiff = 0, numSegments = 0;
      Random rand = new Random (221);

      for (int test = 1; test <= numTests; test++)
        {
          //  Build weights

          Coord coords [] = new Coord [n];
          for (int i = 0; i < n; i++)
              coords[i] = new Coord (rand.nextFloat(), rand.nextFloat());
          Weights weights = new WeightsEuclid (coords);

          //  Solve the full SHP problem and get its total length.

          ShortHamPathBAB shpFull = new ShortHamPathBAB (weights);
          float length1 = pathWeight (weights, shpFull.path (n));

          //  Solve the segmented SHP problem and get its total length.

          OrderSegmentSHP shpSegment = new OrderSegmentSHP ();
          shpSegment.setMedianMult (10000);
          float length2 = pathWeight (weights, shpSegment.path (coords));
          int ns = shpSegment.numSegments ();
          assert ns <= n;
          numSegments += ns;

          //  Print the results.

          float ratio = length2 / length1 - 1;
          avgRatio1 += ratio;
          if (Math.abs (ratio) > .00001)
            {
              printf ("   %5d:   %.2f %.2f", test, length1, length2);
              printf ("   %2.0f%n", 100 * ratio);
              maxRatio = Math.max (ratio, maxRatio);
              avgRatio2 += ratio;
              numDiff ++;
            }
          assertTrue (length1 <= 1.00001 * length2);
        }

      printf ("%n   %4.1f%% %4.1f%% %4.1f%% %4d%% %4d%%%n%n",
              100 * avgRatio1 / numTests,
              100 * avgRatio2 / numDiff,
              100 * maxRatio,
              100 * numDiff / numTests,
              100 * (n - numSegments / numTests) / n);
   }

  //////////////////////////////////////////////////////////////////////////////
  //  Test problem reduction.
  //  How much do MSTs reduce the size of the SHP problem?

  @Test
  public void reduceMST ()
    {
      printTitle ();
      Segmenter segmenter = new Segmenter ();
      MinSpanTree mst = new MinSpanTreePrims ();

      int numTests = 1000;

      Random rand = new Random (221);

      int incr = 10;
      for (int n = 10; n <= 800; n += incr)
        {
          int numSegments = 0;
          for (int test = 1; test <= numTests; test++)
            {
              Weights weights = scatterWeights (rand, n);
              int edges [] = mst.edges (n, weights);
              Segment segments [] = segmenter.segments (edges, weights);
              numSegments += segments.length;
              if (n == 100)
                  incr = 100;
            }
          printf ("   %4d:  %4d%%%n",
                  n, 100 * (n - numSegments / numTests) / n);
        }
      printf ("%n");
   }

  //////////////////////////////////////////////////////////////////////////////

  private void reverse (int path [])
    {
      int n = path.length;
      for (int i = 0; i < n/2; i++)
        { int t = path[i]; path[i] = path[n-i-1]; path[n-i-1] = t; }
    }

  //////////////////////////////////////////////////////////////////////////////

  private Weights scatterWeights (Random rand, int n)
    {
      assert n >= 0;
      Coord coords [] = new Coord [n];
      for (int i = 0; i < n; i++)
          coords[i] = new Coord (rand.nextFloat(), rand.nextFloat());
      return new WeightsEuclid (coords);
    }
}

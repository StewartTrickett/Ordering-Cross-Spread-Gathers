/**
 *  Unit tests for the four ordering algorithms.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;

import java.util.Random;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class OrderLocnsTimingTest
{      
  //  The four ordering algorithms.
  
  OrderLocns orderings [] = new OrderLocns []
    {
      new OrderProjectOntoLine (),
      new OrderFullSHP (),
      new OrderBranchEliminate (),
      new OrderSegmentSHP (),
    };

  //////////////////////////////////////////////////////////////////////////////
  //  Time the four ordering algorithms on scattered points in a 10 x 1 rectangle.

  @Test
  public void timing ()
    {
      printf ("%s: %s%n%n", className (), methodName ());
      
      Random rand = new Random (6104);
      Timer timer = new Timer ();
      float elongate = 10;
      
      for (int n = 0; n <= 50; n++)
        {
          int numTests = Math.max (1, 100000 / Math.max (1, n * n * n));
          printf ("   %2d %6d:   ", n, numTests);
          
          //  Scatter coordinates across a 10 x 1 rectangle
          
          Coord coords [] [] = new Coord [numTests] [n];
          for (int test = 0; test < numTests; test++)
          for (int i = 0; i < n; i++)
              coords[test] [i] =
                  new Coord (elongate * rand.nextFloat(), rand.nextFloat());
          
          //  Run the four ordering algorithms.
          //  Print the run times in 1/100 of a second.
          
          for (OrderLocns order : orderings)
            {
              for (int test = 0; test < numTests; test++)
                  order.path (coords[test]);
              printf (" %4.0f", 100 * timer.cpu());
            }
          printf ("%n");
        }
      printf ("%n   Total CPU:  %5.1f s%n%n", timer.cpuTotal());
    }
}

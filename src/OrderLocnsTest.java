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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class OrderLocnsTest
{      
  //  The four ordering algorithms.
  
  OrderLocns orderings [] = new OrderLocns []
    {
      new OrderProjectOntoLine (),
      new OrderFullSHP (),
      new OrderBranchEliminate (),
      new OrderSegmentSHP (),
    };

  /////////////////////////////////////////////////////////////////////////////
  //  Test the four ordering methods.
  /////////////////////////////////////////////////////////////////////////////

  @Test
  public void compare ()
    { 
      printf ("%s: %s%n%n", className (), methodName( ));
     
      //  Build a 25-point graph

      int n = 25;
      Random rand = new Random (6104);
      Coord coords [] = new Coord [n];
      for (int i = 0; i < n; i++)
        {
          float x = 400 * rand.nextFloat ();
          float y = 100 * rand.nextFloat ();
          coords[i] = new Coord (x, y);
        }
      Weights weights = new WeightsEuclid (coords);
            
      //  Here's what the final weights should be.
      
      float orderingWeights [] = new float [] { 999, 809, 821, 817 };

      //  Test all four 
      
      int i = 0;
      for (OrderLocns ordering : orderings)
        {
          int path [] = ordering.path (coords);
          assertTrue (GraphUtil.isPathASpanningTree (path));
          float weight = GraphUtil.pathWeight (weights, path);
          printf ("   %-25s   %5.0f%n", 
                  ordering.getClass().getSimpleName(), weight);
          assertEquals (weight, orderingWeights[i++], 1);
        }
      printf ("%n");
    }
}

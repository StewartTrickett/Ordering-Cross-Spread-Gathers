/*
 *  Unit test for SpanTree.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

import static CrossSpreadOrder.GraphUtil.pathWeight;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;

import java.util.Random;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class ShortHamPathBETest
{
    /////////////////////////////////////////////////////////////////////////////

   @Test
   public void accuracy ()
     {
       printf ("%s: %s%n%n", className(), methodName());

       //  Test parameters

       float width = 1;
       int maxNodes = 30, numTests = 100;

       Random rand = new Random (523);
       Timer timer = new Timer ();
       int totalZero = 0, edges [];
       float totalDiff = 0, maxDiff = 0;

       for (int test = 1; test <= numTests; test++)
         {
           int n = 11 + rand.nextInt (maxNodes - 10);
           Coord coords [] = new Coord [n];
           for (int i = 0; i < n; i++)
               coords[i] = new Coord (width * rand.nextFloat (), rand.nextFloat());
           Weights weights = new WeightsEuclid (coords);

           ShortHamPathBE shp1 = new ShortHamPathBE (weights);

           edges = shp1.edges (n);
           float weight1 = 0;
           for (int i = 0; i < n-1; i++)
               weight1 += weights.value (i, edges[i]);

           ShortHamPathBAB shp2 = new ShortHamPathBAB (weights);
           float weight2 = pathWeight (weights, shp2.path (n));

           float diff = (weight1 - weight2) / weight2;
           if (diff < .0005)
               diff = 0;

           printf ("   %5d:   %3d  %4.1f%%%n", test, n, 100 * diff);

           totalDiff += diff;
           maxDiff = Math.max (maxDiff, diff);
           if (diff == 0)
               totalZero ++;
         }
       printf ("%n");
       printf ("   Avg difference:     %4.1f%%%n",   100 * totalDiff / numTests);
       printf ("   Max difference:     %4.1f%%%n",   100 * maxDiff);
       printf ("   Percentage exact:   %4.1f%%%n",   100F * totalZero / numTests);
       printf ("   Time:               %4.1f s%n%n", timer.cpu());
     }

   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void accuracySemiEnclosed ()
     {
       printf ("%s: %s%n%n", className(), methodName());

       //  Test parameters

       float width = 1;
       int maxNodes = 15, numTests = 100;

       Random rand = new Random (523);
       Timer timer = new Timer ();
       int totalZero = 0;
       float totalDiff = 0, maxDiff = 0;

       for (int test = 1; test <= numTests; test++)
         {
           int n = 11 + rand.nextInt (maxNodes - 10);
           Coord coords [] = new Coord [n];
           for (int i = 0; i < n; i++)
               coords[i] = new Coord (width * rand.nextFloat (), rand.nextFloat());
           Weights weights = new WeightsEuclid (coords);

           ShortHamPath shp1 = new ShortHamPathBE (weights);
           float weight1 = pathWeight (weights, shp1.semiEnclosedPath (n));
           ShortHamPath shp2 = new ShortHamPathBAB (weights);
           float weight2 = pathWeight (weights, shp2.semiEnclosedPath (n));

           float diff = (weight1 - weight2) / weight2;
           if (diff < .0005)
               diff = 0;

           printf ("   %5d:   %3d  %4.1f%%%n", test, n, 100 * diff);

           totalDiff += diff;
           maxDiff = Math.max (maxDiff, diff);
           if (diff == 0)
               totalZero ++;
         }
       printf ("%n");
       printf ("   Avg difference:     %4.1f%%%n",   100 * totalDiff / numTests);
       printf ("   Max difference:     %4.1f%%%n",   100 * maxDiff);
       printf ("   Percentage exact:   %4.1f%%%n",   100F * totalZero / numTests);
       printf ("   Time:               %4.1f s%n%n", timer.cpu());
     }

   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void timing ()
     {
       printf ("%s: %s%n%n", className(), methodName());

       Random rand = new Random (523);
       Timer timer = new Timer ();

       int numTests = 1024;
       for (int test = 1; test <= numTests; test += test)
         {
           int n = test;
           Coord coords [] = new Coord [n];
           for (int i = 0; i < n; i++)
               coords[i] = new Coord (rand.nextFloat (), rand.nextFloat());
           Weights weights = new WeightsEuclid (coords);

           ShortHamPath spanTree = new ShortHamPathBE (weights);

           spanTree.path (n);
           float cpu = (float) timer.cpu ();
           printf ("   %5d:  %7.2f s  %6.2f%n", n, cpu, Math.log (cpu) / Math.log(2));
         }
     }

   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void compareTiming ()
     {
       printf ("%s: %s%n%n", className(), methodName());

       Random rand = new Random (523);
       Timer timer = new Timer ();

       for (int n = 32; n <= 1024; n += n)
         {
           int numTests = Math.max ((int) (1e9 / n / n / n), 1);
           float cpu = 0;
           for (int test = 1; test <= numTests; test ++)
             {
               Coord coords [] = new Coord [n];
               for (int i = 0; i < n; i++)
                   coords[i] = new Coord (rand.nextFloat (), rand.nextFloat());
               Weights weights = new WeightsEuclid (coords);

               ShortHamPath shp  = new ShortHamPathBE (weights);

               timer.restart ();
               shp.path (n);
               cpu += timer.cpu ();
             }
           printf ("   %5d x %-6d    %6.0f  %3.0f%n",
                   n, numTests, cpu, 1E9 * cpu / numTests / n / n / n);
         }
       printf ("%n");
     }
}

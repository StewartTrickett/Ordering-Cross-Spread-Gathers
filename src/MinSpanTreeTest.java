/**
 *  Unit test for MinSpanTreePrims
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.addEdges;
import static CrossSpreadOrder.GraphUtil.areEdgesASpanningTree;
import static CrossSpreadOrder.GraphUtil.edgesWeight;
import static CrossSpreadOrder.GraphUtil.isSpanningTree;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class MinSpanTreeTest
{
   static String directory = "";

   /////////////////////////////////////////////////////////////////////////////

  @Test
  public void shortLength ()
    {
      printf ("%s: %s%n%n", className(), methodName());
      int n = 5;
      Coord coords [] = new Coord [n];
      coords[0] = new Coord (5, 7);
      coords[1] = new Coord (6, 2);
      coords[2] = new Coord (3, 5);
      coords[3] = new Coord (4, 4);
      coords[4] = new Coord (1, 5);
      Weights weights = new WeightsEuclid (coords);
      Weights weightsSq = new WeightsEuclidSq (coords);

      MinSpanTree mst = new MinSpanTreePrims ();

      int edges [] = mst.edges (n, weights);
      Adjacent adjacent = new Adjacent (n);
      addEdges (edges, adjacent);
      assertTrue (isSpanningTree (adjacent));
      assertEquals (edgesWeight (weights, edges), 9.1, .05);

      edges = mst.edges (n, weightsSq);
      adjacent = new Adjacent (n);
      addEdges (edges, adjacent);
      assertTrue (isSpanningTree (adjacent));
      assertEquals (edgesWeight (weights, edges), 9.1, .05);
    }

  /////////////////////////////////////////////////////////////////////////////
  //  Test plots for various lengths

  @Test
  public void variousLengths ()
    {
      printf ("%s: %s%n%n", className(), methodName());

      int n = 10000;
      Random rand = new Random (6104);
      Coord coords [] = new Coord [n];
      for (int i = 0; i < n; i++)
        {
          float x = 100 * rand.nextFloat ();
          float y = 100 * rand.nextFloat ();
          coords[i] = new Coord (x, y);
        }
      Weights weightEuclid = new WeightsEuclid (coords);
      int lengths [] = { 1, 2, 3, 4, 5, 7, 10, 20, 50, 200, 1000,
                        3000, 10000 };
      for (int length : lengths)
          run ("Prim's O(N**2)", new MinSpanTreePrims (), length,
               weightEuclid, weightEuclid);

      printf ("%n");
    }

  /////////////////////////////////////////////////////////////////////////////

  @Test
  public void weight ()
    {
      printf ("%s: %s%n%n", className(), methodName());
      Random rand = new Random (6104);
      MinSpanTree mst = new MinSpanTreePrims ();

      for (int i = 0; i < 10000; i++)
        {
          int n = rand.nextInt (101);
          Coord coords [] = new Coord [n];
          for (int j = 0; j < n; j++)
            {
              float x = 100 * rand.nextFloat ();
              float y = 100 * rand.nextFloat ();
              coords[j] = new Coord (x, y);
            }
          Weights weights = new WeightsEuclid (coords);

          //  Compare paths.

          int edges [] = mst.edges (n, weights);
          float weightA = edgesWeight (weights, edges);
          float weightB = mst.weight (n, weights);

          assertEquals (weightA, weightB, .01F);
        }
      printf ("%n");
    }

  /////////////////////////////////////////////////////////////////////////////

  @Test
  public void many ()
    {
      printf ("%s: %s%n%n", className(), methodName());
      Random rand = new Random (6104);
      MinSpanTree mst = new MinSpanTreePrims ();
      for (int i = 0; i < 100000; i++)
        {
          int n = rand.nextInt (101);
          Coord coords [] = new Coord [n];
          for (int j = 0; j < n; j++)
            {
              float x = 100 * rand.nextFloat ();
              float y = 100 * rand.nextFloat ();
              coords[j] = new Coord (x, y);
            }
          Weights weightEuclid = new WeightsEuclid (coords);
          mst.edges (n, weightEuclid);
       }
    }

  /////////////////////////////////////////////////////////////////////////////

  public void run (String label, MinSpanTree mst, int n, Weights weight1,
                   Weights weight2)
    {
      Timer timer = new Timer ();
      int edges [] = mst.edges (n, weight1);

      Adjacent adjacent = new Adjacent (n);
      addEdges (edges, adjacent);
      printf ("   %-15s  %5d %6.0f  %5.1f  %3.0f%%  %5b %5.1f%n",
              label,
              n,
              edgesWeight (weight2, edges),
              edgesWeight (weight2, edges) / Math.sqrt (n),
              100F * adjacent.totalBranchingDegree () / n,
              areEdgesASpanningTree (edges),
              timer.elapsed());

      assert areEdgesASpanningTree (edges);
    }

  /////////////////////////////////////////////////////////////////////////////

  @Test
  public void testPlot ()
    {
      printf ("MinSpanTree: Plot%n%n");

      runPlot (100, 3);
      runPlot (400, 3);
      runPlot (1600, 2);
      runPlot (6400, 1);
      runPlot (25600, 1);

      printf ("%n");
    }

  /////////////////////////////////////////////////////////////////////////////

  public void runPlot (int n, int thick)
    {
      String fileName = "mst" + n;

      Random rand = new Random (6104);
      Coord coords [] = new Coord [n];
      for (int i = 0; i < n; i++)
        {
          float x = 100 * rand.nextFloat ();
          float y = 100 * rand.nextFloat ();
          coords[i] = new Coord (x, y);
        }
      Weights weights = new WeightsEuclid (coords);

      MinSpanTree mst = new MinSpanTreePrims ();
      int edges [] = mst.edges (n, weights);
      plot (edges, coords, thick, fileName);
   }

  /////////////////////////////////////////////////////////////////////////////

  private void plot (int edges [], Coord coord [], int thick,
                     String fileName)
    {
      int mult = 5, dim = mult * 100 + 1;
      try
        {
          BufferedImage bi = new BufferedImage
                              (dim, dim, BufferedImage.TYPE_INT_ARGB);
          Graphics2D graphics = bi.createGraphics ();
          graphics.setPaint (Color.white);
          graphics.fill3DRect (0, 0, dim, dim, true);

          int n = edges.length + 1;
          graphics.setStroke (new BasicStroke (thick));
          graphics.setPaint (Color.red);
          for (int i = 0; i < n-1; i++)
            {
              int j = edges[i];
              int xi = Math.round (mult * coord[i].x());
              int yi = Math.round (mult * coord[i].y());
              int xj = Math.round (mult * coord[j].x());
              int yj = Math.round (mult * coord[j].y());
              graphics.drawLine (xi, yi, xj, yj);
           }
         String fullName = directory + fileName + ".gif";
         printf ("   %s%n", fullName);
         ImageIO.write (bi, "gif", new File (fullName));
      }
    catch (IOException ie)
      {
        ie.printStackTrace ();
        assertTrue (false);
      }
    }

}

/**
 *  Generate figure plots.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static java.lang.Math.round;
import static org.junit.Assert.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.junit.Test;

////////////////////////////////////////////////////////////////////////////////////////

public class PlotFiguresTest
{
   static int shotCoords [] =
    {
      1, 5, 1, 4, 1, 3, 1, 2, 2, 1, 2, 0, 3, 0, 4, 0, 5, 1, 6, 2,
      6, 3, 5, 4, 6, 5, 6, 6, 6, 7, 6, 8, 5, 9, 4, 10, 3, 11, 2, 12,
      2, 13, 8, 11, 8, 12, 8, 13, 7, 14, 6, 15, 5, 16, 5, 17, 6, 18, 5, 19,
      4, 20, 4, 21, 5, 22, 5, 23, 5, 24, 5, 25, 4, 26, 3, 27, 2, 27, 1, 27,
      0, 26, 0, 25, 14, 24, 13, 25, 12, 26, 11, 26, 10, 26, 9, 26, 8, 27, 7, 28,
      7, 29, 7, 30, 6, 31, 6, 32, 7, 33, 6, 34, 6, 35, 6, 36, 7, 37, 6, 38,
      6, 39, 9, 38, 9, 39, 9, 40, 10, 41, 9, 42, 9, 43, 4, 42, 5, 43, 4, 44,
      4, 45, 5, 46, 6, 47, 6, 48, 6, 49, 5, 50, 2, 49, 2, 50, 01, 51, 1, 52
    };

  static String directory = "";

  //////////////////////////////////////////////////////////////////////////////
  //  Plot a simple graph

  @Test
  public void FigureA ()
    {
      printf ("%s: %s%n%n", className(), methodName());

      assertTrue (directory != null);    //  You didn't set the directory!
      String fileName = methodName ();

      //  Build the shot coordinates.

      int graph1 [] =
         { 1, 2,   3, 1,   6, 2,   7, 5,   5, 6,   4, 4,   1, 5,   2, 7 };
      int n = graph1.length / 2;
      Coord coords [] = new Coord [n];
      for (int i = 0; i < n; i++)
          coords[i] = new Coord (graph1[2*i], graph1[2*i+1]);
      int edgesFrom [] = new int [] { 0, 1, 1, 2, 2, 3, 3, 4, 5, 6, 0 };
      int edgesTo []   = new int [] { 1, 2, 5, 4, 5, 4, 5, 4, 6, 7, 6 };

      //  Plot the lines

      float scale = 40, xShift = -.5F * scale, yShift = xShift;
      try
        {
          int dimx = (int) (scale * 7);
          int dimy = (int) (scale * 7);
          BufferedImage bi = new BufferedImage
                              (dimx, dimy, BufferedImage.TYPE_INT_ARGB);
          Graphics2D graphics = bi.createGraphics ();
          graphics.setPaint (Color.white);
          graphics.fill3DRect (0, 0, dimx+1, dimy+1, true);

          //  Draw the edges

          graphics.setStroke (new BasicStroke (1));
          graphics.setPaint (Color.black);
          for (int i = 0; i < edgesFrom.length; i++)
            {
              int p = edgesFrom[i];
              int q = edgesTo[i];
              int xp = round (xShift + scale * coords[p].x());
              int yp = round (yShift + scale * coords[p].y());
              int xq = round (xShift + scale * coords[q].x());
              int yq = round (yShift + scale * coords[q].y());
              graphics.drawLine (xp, yp, xq, yq);
            }

          int thick = 9;
          graphics.setStroke (new BasicStroke (thick));
          graphics.setPaint (Color.black);
          for (Coord coord : coords)
            {
              int x = round (xShift + scale * coord.x());
              int y = round (yShift + scale * coord.y());
              graphics.drawLine (x, y, x, y);
            }

          ImageIO.write (bi, "gif", new File (directory + fileName + ".gif"));
        }
    catch (IOException ie)
      { ie.printStackTrace (); }
    }

  //////////////////////////////////////////////////////////////////////////////
  //  Plot the MST and SHP of a simple connected graph

  @Test
  public void FigureB ()
    {
      printf ("%s: %s%n%n", className(), methodName());
      assertTrue (directory != null);    //  You didn't set the directory!

      //  Parameters

      float sigma = .25F, scale = 60;
      String fileName = methodName ();

      //  Build the shot coordinates.

      Random rand = new Random (6104);
      int graph [] =
         {
           3, 1,    3, 2,    4, 1,    5, 1,    4, 2,
           3, 3,    5, 2,    2, 4,    1, 3,    2, 2,
           4, 4,    3, 4,    4, 2,    2, 3,
           5, 3,    4, 3,
         };
      int n = graph.length / 2;
      Coord coords [] = new Coord [n];
      for (int i = 0; i < n; i++)
          coords[i] = new Coord (graph[2*i] +
                                    (float) (sigma * rand.nextGaussian()),
                                    graph[2*i+1] +
                                    (float) (sigma * rand.nextGaussian()));
      Weights weights = new WeightsEuclid (coords);

      //  Determine the MST

      MinSpanTree mst = new MinSpanTreePrims ();
      int mstEdges [] = mst.edges (n, weights);

      //  Determine the SHP

      ShortHamPath fullShp = new ShortHamPathBAB (weights);
      int shpPath [] = fullShp.path (n);

      int yShift = round (-3 * scale / 4);

      //  Plot the lines

      try
        {
          int dimx = (int) (scale * 11F);
          int dimy = (int) (scale * 3.5);
          BufferedImage bi = new BufferedImage
                              (dimx, dimy, BufferedImage.TYPE_INT_ARGB);
          Graphics2D graphics = bi.createGraphics ();
          graphics.setPaint (Color.white);
          graphics.fill3DRect (0, 0, dimx+1, dimy+1, true);

          //  Draw the SHP edges

          int xShift = round (5.4F * scale);
          graphics.setStroke (new BasicStroke (1));
          graphics.setPaint (Color.black);
          for (int i = 0; i < n-1; i++)
            {
              int p = shpPath[i];
              int q = shpPath[i+1];
              int xp = xShift + round (scale * coords[p].x());
              int yp = yShift + round (scale * coords[p].y());
              int xq = xShift + round (scale * coords[q].x());
              int yq = yShift + round (scale * coords[q].y());
              graphics.drawLine (xp, yp, xq, yq);
            }

          int thick = 9;
          graphics.setStroke (new BasicStroke (thick));
          graphics.setPaint (Color.black);
          for (Coord coord : coords)
            {
              int x = xShift + round (scale * coord.x());
              int y = yShift + round (scale * coord.y());
              graphics.drawLine (x, y, x, y);
            }

          //  Draw the MST edges

          xShift = round (-.75F * scale);
          graphics.setStroke (new BasicStroke (1));
          graphics.setPaint (Color.black);
          for (int i = 0; i < n-1; i++)
            {
              int p = i;
              int q = mstEdges[i];
              int xp = xShift + round (scale * coords[p].x());
              int yp = yShift + round (scale * coords[p].y());
              int xq = xShift + round (scale * coords[q].x());
              int yq = yShift + round (scale * coords[q].y());
              graphics.drawLine (xp, yp, xq, yq);
            }

          thick = 9;
          graphics.setStroke (new BasicStroke (thick));
          graphics.setPaint (Color.black);
          for (Coord coord : coords)
            {
              int x = xShift + round (scale * coord.x());
              int y = yShift + round (scale * coord.y());
              graphics.drawLine (x, y, x, y);
            }

          ImageIO.write (bi, "gif", new File (directory + fileName + ".gif"));
        }
    catch (IOException ie)
      { ie.printStackTrace (); }
    }

  //////////////////////////////////////////////////////////////////////////////
  //  Demonstrate how the Segmented-SHP ordering works.

  @Test
  public void FigureC ()
    {
      printf ("%s: %s%n%n", className(), methodName());

      assertTrue (directory != null);    //  You didn't set the directory!

      //  Parameters

      float sigma = .1F, x = 1, y = 1, scale = 25, yIncr = 18 * scale;
      String fileName = methodName ();

      //  Build the shot coordinates.

      Random rand = new Random (6104);
      int n = shotCoords.length / 2;
      Coord coords [] = new Coord [n];
      for (int i = 0; i < n; i++)
          coords[i] = new Coord
             (shotCoords[2*i+1] + x + sigma * (float) rand.nextGaussian(),
              shotCoords[2*i] + y + sigma * (float) rand.nextGaussian ());

      Weights weights = new WeightsEuclid (coords);

      //  Calculate minimum spanning tree (MST)

      MinSpanTree mst = new MinSpanTreePrims ();
      int edges [] = mst.edges (n, weights);

      //  Segment the MST

      float medianMult = 2.5F;
      Segmenter segmenter = new Segmenter ();
      segmenter.setMedianMult (medianMult);
      Segment segments [] = segmenter.segments (edges, weights);

      //  Solve the segmented SHP.

      SegmentShortHamPath shp = new SegmentShortHamPath ();
      segments = shp.path (segments, weights);

      //  Plot the coordinates

      try
        {
          int dimx = (int) (scale * 54.5);
          int dimy = (int) (4 * yIncr - 2 * scale);
          BufferedImage bi = new BufferedImage
                              (dimx, dimy, BufferedImage.TYPE_INT_ARGB);
          Graphics2D graphics = bi.createGraphics ();
          graphics.setPaint (Color.white);
          graphics.fill3DRect (0, 0, dimx+1, dimy+1, true);
          graphics.fill3DRect (0, 0, dimx+1, dimy+1, true);
          Font font = new Font ("SansSerif", Font.PLAIN, 40);
          graphics.setFont (font);
          graphics.setPaint (Color.black);

          graphics.drawString ("Source Locations", 30, .65F * yIncr);

          //  MST lines.

          graphics.drawString ("1. Minimum Spanning Tree", 30, 1.65F * yIncr);
          int thick = 1;
          graphics.setStroke (new BasicStroke (thick));
          graphics.setPaint (Color.black);
          for (int i = 0; i < n-1; i++)
            {
              int xi = round (scale * coords[i].x());
              int yi = round (scale * coords[i].y() + yIncr);
              int xj = round (scale * coords[edges[i]].x());
              int yj = round (scale * coords[edges[i]].y() + yIncr);
              graphics.drawLine (xi, yi, xj, yj);
            }

          //  Segments (twice)

          graphics.drawString ("2. Segment MST", 30, 2.65F * yIncr);
          for (int k = 2; k < 4; k++)
            for (Segment segment : segments)
              {
                for (int j = 0; j < segment.length()-1; j++)
                  {
                    int p = segment.index(j);
                    int q = segment.index(j+1);
                    int xp = round (scale * coords[p].x());
                    int yp = round (scale * coords[p].y() + k * yIncr);
                    int xq = round (scale * coords[q].x());
                    int yq = round (scale * coords[q].y() + k * yIncr);
                    graphics.drawLine (xp, yp, xq, yq);
                  }
              }

          //  Draw dashed lines between segments

          graphics.drawString ("3. Connect Segments", 30, 3.65F * yIncr);
          thick = 3;
          Stroke dashed = new BasicStroke (thick, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
          graphics.setStroke (dashed);
          graphics.setPaint (Color.blue);
          for (int i = 0; i < segments.length-1; i++)
            {
              int p = segments[i].end();
              int q = segments[i+1].start();
              int xp = round (scale * coords[p].x());
              int yp = round (scale * coords[p].y() + 3 * yIncr);
              int xq = round (scale * coords[q].x());
              int yq = round (scale * coords[q].y() + 3 * yIncr);
              graphics.drawLine (xp, yp, xq, yq);
            }

          //  Draw vertices

          for (int j = 0; j < 4; j++)
            {
              graphics.setStroke (new BasicStroke (11));
              graphics.setPaint (Color.red);
              for (int i = 0; i < n; i++)
                {
                  int xi = round (scale * coords[i].x());
                  int yi = round (scale * coords[i].y() + j * yIncr);
                  graphics.drawLine (xi, yi, xi, yi);
                }
            }

          ImageIO.write (bi, "gif", new File (directory + fileName + ".gif"));
        }
      catch (IOException ie)
        { ie.printStackTrace (); }
    }

  //////////////////////////////////////////////////////////////////////////////
  //  Compare the different orderings on a 80-point synthetic test.
  //  The full-SHP ordering takes over an hour to compute.

  @Test
  public void FigureD ()
    {
      printf ("%s: %s%n%n", className(), methodName());

      assertTrue (directory != null);    //  You didn't set the directory!
      String fileName = methodName ();
      Timer timer = new Timer ();

      //  Parameters

      float sigma = .1F, x = 1, y = 1, scale = 25, yIncr = 18 * scale;

      //  Build the shot coordinates.

      Random rand = new Random (6104);
      int n = shotCoords.length / 2;
      Coord coords [] = new Coord [n];
      for (int i = 0; i < n; i++)
          coords[i] = new Coord
             (shotCoords[2*i+1] + x + sigma * (float) rand.nextGaussian(),
              shotCoords[2*i] + y + sigma * (float) rand.nextGaussian ());

      OrderLocns orderings [] =
        {
          new OrderProjectOntoLine (),
          new OrderFullSHP (),
          new OrderBranchEliminate (),
          new OrderSegmentSHP (),
        };
      String labels [] =
        {
          "Project Onto A Line",
          "Full SHP",
          "Branch Elimination",
          "Segmented SHP",
        };
      assert orderings.length == labels.length;
      int numGraphs = orderings.length + 1;

      try
        {
          int dimx = (int) (scale * 54) + 10;
          int dimy = (int) (numGraphs * yIncr - 50);
          BufferedImage bi = new BufferedImage
                              (dimx, dimy, BufferedImage.TYPE_INT_ARGB);
          Graphics2D graphics = bi.createGraphics ();
          graphics.setPaint (Color.white);
          graphics.fill3DRect (0, 0, dimx+1, dimy+1, true);
          Font font = new Font ("SanSerif", Font.PLAIN, 40);
          graphics.setFont (font);
          graphics.setPaint (Color.black);

          graphics.drawString ("Source Locations", 30, .65F * yIncr);

          //  Draw the paths in black.

          graphics.setPaint (Color.black);
          for (int ord = 0; ord < orderings.length; ord++)
            {
              timer.restart ();
              orderings[ord].setPrint (true);
              int path [] = orderings[ord].path (coords);
              printf ("   %-22s   %10.5f s%n", labels[ord], timer.cpu ());
              for (int k = 0; k < n-1; k++)
                {
                  int i = path[k];
                  int j = path[k+1];
                  int xi = round (scale * coords[i].x());
                  int yi = round (scale * coords[i].y() + (ord+1) * yIncr);
                  int xj = round (scale * coords[j].x());
                  int yj = round (scale * coords[j].y() + (ord+1) * yIncr);
                  graphics.drawLine (xi, yi, xj, yj);
                }
              graphics.drawString (labels[ord], 30, (ord + 1.65F) * yIncr);
            }

          //  Draw the vertices in red.

          graphics.setStroke (new BasicStroke (11));
          graphics.setPaint (Color.red);
          for (int j = 0; j < numGraphs; j++)
              for (int i = 0; i < n; i++)
                {
                  int xi = round (scale * coords[i].x());
                  int yi = round (scale * coords[i].y() + j * yIncr);
                  graphics.drawLine (xi, yi, xi, yi);
                }

          //  Generate the plot file.

          ImageIO.write (bi, "gif", new File (directory + fileName + ".gif"));

        }
      catch (IOException ie)
         { ie.printStackTrace (); }
    }
}

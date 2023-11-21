/*
 *  Unit tests for LineFit2D
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class LineFit2DTest
{
   int printLevel = 0;

   /////////////////////////////////////////////////////////////////////////////
   //  Test points with no scatter

   @Test
   public void exact ()
     {
        printf ("%s: %s%n%n", className(), methodName());

        double tol = 1E-5;
        LineFit2D lineFit = new LineFit2D ();

        //  All y points the same test. No fitting possible.

        float x0 [] = { 3, 3, 3, 3, 3 };
        float y0 [] = { 10, 10, 10, 10, 10 };
        for (int n = 2; n <= x0.length; n++)
          {
            lineFit.fit (x0, y0, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (lineFit.xAvg(), 3,  tol);
            assertEquals (lineFit.yAvg(), 10, tol);
            assertEquals (lineFit.xDir(), 0, tol);
            assertEquals (lineFit.yDir(), 0, tol);
          }
        printf ("%n");

        //  Sloping test

        float x [] = { 3, 4, 5, 7, 10 };
        float y [] = { 10, 13, 16, 22, 31 };
        for (int n = 2; n <= x.length; n++)
          {
            lineFit.fit (x, y, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (3 * lineFit.xDir(), lineFit.yDir(), tol);
          }
        printf ("%n");

        //  Vertical test

        x = new float [] { 3, 4, 5, 7, 10 };
        y = new float [] { 10, 10, 10, 10, 10 };
        for (int n = 2; n <= x.length; n++)
          {
            lineFit.fit (x, y, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (lineFit.yAvg(), 10, tol);
            assertEquals (abs (lineFit.xDir()), 1, tol);
            assertEquals (lineFit.yDir(), 0, tol);
          }
        printf ("%n");

        //  Horizontal test

        x = new float [] { 3, 3, 3, 3, 3 };
        y = new float [] { 10, 13, 16, 22, 31 };
        for (int n = 2; n <= x.length; n++)
          {
            lineFit.fit (x, y, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (lineFit.xAvg(), 3, tol);
            assertEquals (lineFit.xDir(), 0, tol);
            assertEquals (abs( lineFit.yDir()), 1, tol);
          }
        printf ("%n");

        //  45 Deg diagonal test

        x = new float [] { 3, 4, 5, 7, 10 };
        y = new float [] { -6, -5, -4, -2, 1 };
        for (int n = 2; n <= x.length; n++)
          {
            lineFit.fit (x, y, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (lineFit.xDir(), sqrt (.5), tol);
            assertEquals (lineFit.yDir(), sqrt (.5), tol);
          }
        printf ("%n");

        //  -45 deg diagonal test

        x = new float [] { 3, 4, 5, 7, 10 };
        y = new float [] { 6, 5, 4, 2, -1 };
        for (int n = 2; n <= x.length; n++)
          {
            lineFit.fit (x, y, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (lineFit.xDir(), sqrt (.5), tol);
            assertEquals (lineFit.yDir(), -sqrt (.5), tol);
          }
        printf ("%n");

        //  1 to 2 deg diagonal test

        x = new float [] { 3, 4, 5, 7, 10 };
        y = new float [] { 10, 12, 14, 18, 24 };
        for (int n = 2; n <= x.length; n++)
          {
            lineFit.fit (x, y, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (lineFit.xDir(), 1 / sqrt (5), .01F);
            assertEquals (lineFit.yDir(), 2 / sqrt (5), .01F);
          }
        printf ("%n");

        //  1 to -2 diagonal test

        x = new float [] { 3, 4, 5, 7, 10 };
        y = new float [] { -10, -12, -14, -18, -24 };
        for (int n = 2; n <= x.length; n++)
          {
            lineFit.fit (x, y, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (lineFit.xDir(), 1 / sqrt (5), .01F);
            assertEquals (lineFit.yDir(), -2 / sqrt (5), .01F);
          }
        printf ("%n");

        //  Failure test

        x = new float [] { 3, 3, 3, 3, 3 };
        y = new float [] { -10, -10, -10, -10, -10 };
        for (int n = 2; n <= x.length; n++)
          {
            lineFit.fit (x, y, n);
            printf ("   %2d:   %6.3f %6.3f   %6.3f %6.3f%n", n,
                    lineFit.xAvg(), lineFit.yAvg(),
                    lineFit.xDir(), lineFit.yDir());
            assertEquals (lineFit.xDir(), 0, 0);
            assertEquals (lineFit.yDir(), 0, 0);
          }
        printf ("%n");
   }

    /////////////////////////////////////////////////////////////////////////////
    //  Fitting with errors.

    @Test
    public void fit ()
      {
        LineFit2D lineFit = new LineFit2D ();
        Random rand = new Random (192);
        double tol = .01F;

        printf ("%s: %s%n%n", className(), methodName());

        int n = 100, numTests = 100;
        float x [] = new float [n];
        float y [] = new float [n];

        //  Slope test.

        for (int test = 1; test <= numTests; test++)
          {
            for (int i = 0; i < n; i++)
              {
                x[i] = 100 * rand.nextFloat ();
                y[i] = 10 + 3 * x[i] + (float) rand.nextGaussian ();
              }
            lineFit.fit (x, y, n);

            printf ("   %3d:   %7.3f %7.3f   %6.3f %6.3f%n", test,
                    lineFit.xAvg(), lineFit.yAvg (),
                    lineFit.xDir(), lineFit.yDir());

            assertEquals (lineFit.xAvg(), 50, 15);
            assertEquals (lineFit.yAvg(), 160, 40);
            assertEquals (3 * lineFit.xDir(), lineFit.yDir(), tol);
          }
        printf ("%n");

        //  Vertical test.

        for (int test = 1; test <= numTests; test++)
          {
            for (int i = 0; i < n; i++)
              {
                x[i] = rand.nextFloat ();
                y[i] = 100 * rand.nextFloat ();
              }
            lineFit.fit (x, y, n);

            printf ("   %3d:   %7.3f %7.3f   %6.3f %6.3f%n", test,
                    lineFit.xAvg(), lineFit.yAvg (),
                    lineFit.xDir(), lineFit.yDir());

            assertEquals (lineFit.xAvg(), .5, .2);
            assertEquals (lineFit.yAvg(), 50, 20);
            assertEquals (lineFit.xDir(), 0, .01F);
            assertEquals (Math.abs (lineFit.yDir()), 1, tol);
          }
        printf ("%n");

        //  Horizontal test.

        for (int test = 1; test <= numTests; test++)
          {
            for (int i = 0; i < n; i++)
              {
                x[i] = 100 * rand.nextFloat () - 20;
                y[i] = rand.nextFloat ();
              }
            lineFit.fit (x, y, n);

            printf ("   %3d:   %7.3f %7.3f   %6.3f %6.3f%n", test,
                    lineFit.xAvg(), lineFit.yAvg (),
                    lineFit.xDir(), lineFit.yDir());

            assertEquals (lineFit.xAvg(), 30, 15);
            assertEquals (lineFit.yAvg(), .5, .5F);
            assertEquals (Math.abs (lineFit.xDir()), 1, tol);
            assertEquals (lineFit.yDir(), 0, .01F);
          }
        printf ("%n");
     }

    /////////////////////////////////////////////////////////////////////////////
    //  Fitting with errors. The average and direction are selected randomly.

    @Test
    public void rand ()
      {
        LineFit2D lineFit = new LineFit2D ();
        Random rand = new Random (192);
        double avgTol = 1F, dirTol = .01F;

        printf ("%s: %s%n%n", className(), methodName());

        int n = 101, numTests = 100000, printIncr = numTests / 25;
        if (numTests <= 10000)
            printIncr = 1;
        else if (numTests > 1000000)
            printIncr = numTests / 100;
        boolean print = false;
        float x [] = new float [n];
        float y [] = new float [n];

        for (int test = 1; test <= numTests; test++)
          {
            //  Build the points with random average and direction,
            //  and some noise added.

            double xAvg = 100 * rand.nextGaussian ();
            double yAvg = 100 * rand.nextGaussian ();
            double xDir = 2 * rand.nextFloat () - 1;
            double yDir = sqrt (1 - xDir * xDir);
            if (rand.nextInt (2) == 0)
                 yDir = -yDir;
            for (int i = 0; i < n; i++)
              {
                x[i] = (float) (xAvg + 10 * (i-n/2) * xDir + rand.nextGaussian ());
                y[i] = (float) (yAvg + 10 * (i-n/2) * yDir + rand.nextGaussian ());
              }

            //  Fit the line.

            lineFit.fit (x, y);

            //  Match the polarity, as it is ambiguous.

            if (abs (xDir - lineFit.xDir()) > dirTol ||
                abs (yDir - lineFit.yDir()) > dirTol)
              {
                xDir = -xDir;
                yDir = -yDir;
              }

            //  Test that they're the same.

            if (print || test % printIncr == 0 || test == numTests)
                printf ("   %8d:   %6.1f %6.1f   %6.1f %6.1f   %6.3f %6.3f   %6.3f %6.3f%n", test,
                        xAvg, lineFit.xAvg(),
                        yAvg, lineFit.yAvg (),
                        xDir, lineFit.xDir (),
                        yDir, lineFit.yDir ());

            assertEquals (lineFit.xAvg(), xAvg, avgTol);
            assertEquals (lineFit.yAvg(), yAvg, avgTol);
            assertEquals (lineFit.xDir(), xDir, dirTol);
            assertEquals (lineFit.yDir(), yDir, dirTol);
          }
        printf ("%n");
     }
 }
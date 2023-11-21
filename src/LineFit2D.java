/*
 *  Fit a line to a cluster of points in two-dimensions in a least-squares sense.
 *  The resulting fitted line is (xAvg, yAvg) + z (xDir, yDir).
 *  This can handle vertical lines, unlike methods that solve the normal
 *  equations.
 *
 *  There are an infinite number of direction values that fit the same dataset.
 *  We make the direction unique as follows:
 *
 *     1. If a line can not be fitted then xDir = yDir = 0.
 *        Otherwise (xDir, yDir) has norm 1.
 *     2. We set the polarity of the direction so that xDir >= 0.
 *        If Xdir is zero then we set yDir >= 0.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class LineFit2D
{
  private float xAvg, yAvg, xDir, yDir;
  boolean isFit = false;

  /////////////////////////////////////////////////////////////////////////////

  public LineFit2D ()
    { }

  /////////////////////////////////////////////////////////////////////////////

  public void fit (final float x [], final float y [])
    {
      assert x.length == y.length;
      fit (x, y, x.length);
    }

  /////////////////////////////////////////////////////////////////////////////

  public void fit (final float x [], final float y [], int n)
    {
      assert n >= 2;
      assert x.length >= n;
      assert y.length >= n;

      //  Determine xAvg, yAvg.

      xAvg = yAvg = 0;
      for (int i = 0; i < n; i++)
        {
          xAvg += x[i];
          yAvg += y[i];
        }
      xAvg /= n;
      yAvg /= n;

      //  Determine xDir, yDir.

      double sum1 = 0, sum2 = 0, sum3 = 0;
      for (int i = 0; i < n; i++)
        {
          sum1 += square (x[i] - xAvg);
          sum2 += square (y[i] - yAvg);
          sum3 += (x[i] - xAvg) * (y[i] - yAvg);
        }
      if (sum1 != sum2 || sum3 != 0)
        {
          double theta = .5 * Math.atan2 (2 * sum3, sum1 - sum2);
          xDir = (float) Math.cos (theta);
          yDir = (float) Math.sin (theta);
          if (xDir < 0 || (xDir == 0 && yDir < 0))
            {
              xDir = -xDir;
              yDir = -yDir;
            }
        }
      else
          xDir = yDir = 0;

      isFit = true;
    }

  /////////////////////////////////////////////////////////////////////////////

  public float xDir ()
    {
      assert isFit;
      return xDir;
    }

  public float yDir ()
    {
      assert isFit;
      return yDir;
    }

  public float xAvg ()
    {
      assert isFit;
      return xAvg;
    }

  public float yAvg ()
    {
      assert isFit;
      return yAvg;
    }

  private static double square (double x)
    { return x * x; }
}

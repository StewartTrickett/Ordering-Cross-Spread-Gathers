/*
 *  Two-dimensional coordinates with Euclidean norm.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class Coord
  {
    private float x, y;

    public Coord ()
      { x = y = 0; }

    public Coord (float x, float y)
      { this.x = x; this.y = y; }

    public Coord (Coord coord)
      { this.x = coord.x; this.y = coord.y; }

    public float x ()
      { return x; }

    public float y ()
      { return y; }

    public void set (float x, float y)
      { this.x = x; this.y = y; }

    public boolean equals (Coord coord)
      { return x == coord.x && y == coord.y; }

    public float norm ()
      { return (float) Math.hypot (x, y); }

    public float distance (Coord coord)
      { return (float) Math.sqrt ((x - coord.x) * (x - coord.x) +
                                  (y - coord.y) * (y - coord.y)); }

    public float distanceHypot (Coord coord)   // Avoid overflow
      {
        float a = Math.abs (x - coord.x);
        float b = Math.abs (y - coord.y);
        if (a < b)                                //  Make sure a is biggest.
          { float t = a; a = b; b = t; }
        if (a == 0)
            return 0;
        float r = b / a;
        return a * (float) Math.sqrt (1 + r*r);
      }

    public float distanceSq (Coord coord)
      { return (x - coord.x) * (x - coord.x) +
               (y - coord.y) * (y - coord.y); }
 }

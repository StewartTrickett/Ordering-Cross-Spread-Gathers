/**
 *  Order the locations within a shot or receiver line in a physically sensible
 *  way.
 *
 *  Given an array of location coordinates, method "path" returns an array
 *  of length n = coords.length where the ordering of the coordinates is:
 *
 *     coords[path[0]] coords[path[1]] ... coords[path[n-1]]
 *
 *  This uses the Project-Onto-A-Line algorithm, which works by
 *
 *     1. Fitting a line through the coordinates in a least-squares manner.
 *     2. Orthogonally projecting the coordinates onto the line.
 *     3. Ordering the coordinates by their position on the line.
 *
 *  This is only a reasonable approach if the shots are laid out
 *  more-or-less linearly. Any "backtracking" results in nasty zig-zagging.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class OrderProjectOntoLine extends OrderLocns
{
   LineFit2D lineFit = new LineFit2D ();

   /////////////////////////////////////////////////////////////////////////////

   @Override
   public int [] path (Coord coords [])
     {
       int numPoints = coords.length;
       float x [] = new float [numPoints];
       float y [] = new float [numPoints];
       for (int i = 0; i < numPoints; i++)
         {
           x[i] = coords[i].x();
           y[i] = coords[i].y();
         }
       return path (x, y);
     }

   /////////////////////////////////////////////////////////////////////////////

   public int [] path (float x [], float y [])
     {
       assert x.length == y.length;
       int numPoints = x.length;
       if (numPoints <= 1)
           return new int [numPoints];

       //  Fit a line to the points.

       lineFit.fit (x, y);

       //  Project the locations onto the line orthogonally.

       float xAvg = lineFit.xAvg (), yAvg = lineFit.yAvg (),
             xDir = lineFit.xDir (), yDir = lineFit.yDir ();
       float projections [] = new float [numPoints];
       for (int i = 0; i < numPoints; i++)
           projections[i] = (x[i] - xAvg) * xDir + (y[i] - yAvg) * yDir;

       //  Sort the locations by their position on the line.

       Integer index [] = SortIndex.sort (projections);
       int [] indexInt = new int [numPoints];
       for (int i = 0; i < numPoints; i++)
           indexInt[i] = index[i];
       return indexInt;
     }
}

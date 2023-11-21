/*
 *  Weights based on coordinate distance squared.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class WeightsEuclidSq extends WeightsMatrix
  {
    public WeightsEuclidSq (Coord coords [])
      {
        super (new float [coords.length] [coords.length]);

        n = coords.length;
        for (int i = 0; i < n-1; i++)
        for (int j = i+1; j < n; j++)
            matrix[i][j] = matrix[j][i] = coords[i].distanceSq (coords[j]);

        setTriangleInequality (false);
        setSymmetric (true);
      }
  }

/*
 *  Weights based on coordinate distances.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class WeightsEuclid extends WeightsMatrix
  {

    ////////////////////////////////////////////////////////////////////////////

    public WeightsEuclid (Coord coords [])
      {
        super (new float [coords.length] [coords.length]);

        n = coords.length;
        for (int i = 0; i < n-1; i++)
        for (int j = i+1; j < n; j++)
            matrix[i][j] = matrix[j][i] = coords[i].distanceHypot (coords[j]);

        setTriangleInequality (true);
        setSymmetric (true);
      }
  }

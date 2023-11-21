/**
 *  Order the locations within a shot or receiver line in a physically sensible
 *  way.
 *
 *  Given an array of location coordinates, method "path" returns an array
 *  of length n = coords.length where the ordering of the coordinates is:
 *
 *     coords[path[0]] coords[path[1]] ... coords[path[n-1]]
 *
 *  This uses the Full-SHP algorithms, which calculates a Shortest Hamiltonian
 *  Path using a branch-and-bound algorithm. This algorithm is NP-Hard and
 *  should not be called with more than 40 or 50 locations.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

///////////////////////////////////////////////////////////////////////////////

public class OrderFullSHP extends OrderLocns
  {
    @Override
    public int [] path (Coord coords [])
      {
        int numPoints = coords.length;
        if (numPoints <= 1)
            return new int [numPoints];
        
        Weights weights = new WeightsEuclid (coords);
        ShortHamPath shp = new ShortHamPathBAB (weights);
        shp.setPrint (print);
        return shp.path (coords.length);
      }
  }

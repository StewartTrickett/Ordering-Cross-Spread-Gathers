/**
 *  Order the locations within a shot or receiver line in a physically sensible
 *  way.
 *
 *  Given an array of location coordinates, method "path" returns an array
 *  of length n = coords.length, where the ordering of the coordinates is:
 *
 *     coords[path[0]] coords[path[1]] ... coords[path[n-1]]
 *
 *  This uses a branch-elimination heuristic method to approximate the Shortest
 *  Hamiltonian Path. It runs fast and gives reasonable results, but is not
 *  quite as good as Segmented-SHP. This uses the "all pairs" variant from:
 *
 *      Fränti, P., T. Nenonen, and N. Yuan, 2021, Converting MST to TSP Path
 *      by Branch Elimination; Appl. Sci., 11.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

///////////////////////////////////////////////////////////////////////////////

public class OrderBranchEliminate extends OrderLocns
  {
    @Override
    public int [] path (Coord coords [])
      {
        Weights weights = new WeightsEuclid (coords);
        ShortHamPath shp = new ShortHamPathBE (weights);
        return shp.path (coords.length);
      }
  }

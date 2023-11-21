/*
 *  Comparitor for Coord.
 *  Doesn't matter how we order coordinates provided it is a proper ordering.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

import java.util.Comparator;

////////////////////////////////////////////////////////////////////////////////

public class CoordCompare implements Comparator <Coord>
  {
    @Override
    public int compare (Coord c1, Coord c2)
      {
        if (c1.x() > c2.x())
            return 1;
        else if (c1.x() < c2.x())
            return -1;
        else if (c1.y() > c2.y())
            return 1;
        else if (c1.y() < c2.y())
            return -1;
        else
            return 0;
      }
  }

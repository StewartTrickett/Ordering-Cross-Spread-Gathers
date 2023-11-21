/**
 *  Abstract base class to order the locations within a shot or receiver line
 *  in a physically sensible way.
 *
 *  Method "path" returns an array of length n = coords.length where the
 *  ordering of the coordinates is:
 *
 *     coords[path[0]] coords[path[1]] ... coords[path[n-1]]
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

abstract public class OrderLocns
{
  abstract public int [] path (Coord coords []);

  protected boolean print = false;
  void setPrint (boolean print)
    { this.print = print; }
}

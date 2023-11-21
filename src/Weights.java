/*
 *  Abstract base class for representing the weight between two vertices,
 *  as given by their indices.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public abstract class Weights
  {
    private boolean isTriangleInequality = false, isSymmetric = true;

    ////////////////////////////////////////////////////////////////////////////

    public void setTriangleInequality (boolean flag)
      { isTriangleInequality = flag; }

    public boolean isTriangleInequality ()
      { return isTriangleInequality; }

    ////////////////////////////////////////////////////////////////////////////

    public void setSymmetric (boolean symmetric)
      { isSymmetric = symmetric; }

    public boolean isSymmetric ()
      { return isSymmetric; }

    ////////////////////////////////////////////////////////////////////////////

    public abstract float value (int i, int j);
  }
/*
 *  Shift the index numbering of a weights object.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class WeightsShift extends Weights
  {
    private Weights weights;
    private int shift;

    ////////////////////////////////////////////////////////////////////////////

    public WeightsShift (Weights weights, int shift)
      {
        this.weights = weights;
        this.shift = shift;
        setTriangleInequality (weights.isTriangleInequality ());
      }

    ////////////////////////////////////////////////////////////////////////////

    @Override
    public float value (int i, int j)
      { return weights.value (i + shift, j + shift); }
  }
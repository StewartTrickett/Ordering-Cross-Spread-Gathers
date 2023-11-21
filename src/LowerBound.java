/**
 *  Abstract base class to estimate the lower bounds of a Shortest Hamiltonian
 *  path (SHP), a semi-enclosed SHP (the starting vertex is specified), and a
 *  fully enclosed SHP (the starting and ending vertices are specified.
 *
 *  This is useful for branch and bound algorithms, as the closer the bounds,
 *  the more pruning you can do, greatly speading up the algorithm.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

public abstract class LowerBound
{
  Weights weights;

  public LowerBound (Weights weights)
    { this.weights = weights; }

  public abstract float shp (int n);
  public abstract float shp (int vertices []);
  public abstract float semiEnclosedSHP (int n);
  public abstract float semiEnclosedSHP (int vertices []);
  public abstract float enclosedSHP (int n);
  public abstract float enclosedSHP (int vertices []);
}

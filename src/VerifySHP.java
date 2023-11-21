/**
 *  Abstract class to recognize when a partial Hamiltonian Path is not the
 *  shortest possible. This is used for pruning a branch-and-bound search for
 *  the Shortest Hamiltonian Path (SHP).
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public abstract class VerifySHP
{
  protected Weights weights;

  VerifySHP (Weights weights)
    {  this.weights = weights; }

  public abstract boolean reject (int path [], int posn);
}

/**
 *  Recognize when a partial Hamiltonian Path is not the shortest possible.
 *  This is used for pruning a branch-and-bound search for the Shortest
 *  Hamiltonian Path (SHP). It's assumed that path[0] ... path[level]
 *  are set.
 *
 *  This particular strategy is an order reversal test, which asks is:
 *
 *      A B ... C D
 *
 *  longer than
 *
 *      A C ... B D
 *
 *  where the values [B ... C] are reversed? If so then the sequence is not
 *  a semi-enclosed SHP.
 *
 *  As simple and fast as this test is, it's very good at finding paths that
 *  are not SHPs and thus can be pruned.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class VerifySHPReverse extends VerifySHP
{
  private float tol = 1.00005F;

  //////////////////////////////////////////////////////////////////////////////

  public VerifySHPReverse (Weights weights)
    {  super (weights); }

  //////////////////////////////////////////////////////////////////////////////

  @Override
  public boolean reject (int path [], int level)
    {
      assert level >= 0 && level < path.length;
      if (level < 2)
          return false;

      int c = path[level-1], d = path[level];
      float cd = weights.value (c, d),
            zd = weights.value (path[0], d);
      if (cd > tol * zd)
          return true;
      for (int i = 0; i < level-2; i++)
        {
          int a = path[i], b = path[i+1];
          float ab = weights.value (a, b),
                ac = weights.value (a, c),
                bd = weights.value (b, d);
          if (ab + cd > tol * (ac + bd))
              return true;
        }

      if (cd > tol * zd)
          return true;

      return false;
    }
}

/**
 *  Recognize when a partial Hamiltonian Path is not the shortest possible.
 *  This is used for pruning a branch-and-bound search for the Shortest
 *  Hamiltonian Path (SHP). It's assumed that path[0] ... path[level]
 *  are set.
 *
 *  This particular strategy asks if:
 *
 *      A ... B C ... D E
 *
 *  longer than
 *
 *      D ... C A ... B E  or
 *      C ... D A ... B E  or
 *      D ... C B ... A E  or
 *      C ... D B ... A E  ?
 *
 *  If so then the original sequence is not a semi-enclosed SHP.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class VerifySHPDivide extends VerifySHP
{
  private float tol = 1.00005F;

  //////////////////////////////////////////////////////////////////////////////

  public VerifySHPDivide (Weights weights)
    {  super (weights); }

  //////////////////////////////////////////////////////////////////////////////

  @Override
  public boolean reject (int path [], int level)
    {
      assert level >= 0 && level < path.length;
      if (level < 2)
          return false;

      int a = path[0];
      int d = path[level-1];
      int e = path[level];

      float de = weights.value (d, e),
            ae = weights.value (a, e),
            ad = weights.value (a, d);
      for (int i = 0; i < level-2; i++)
        {
          int b = path[i];
          int c = path[i+1];
          float be = weights.value (b, e),
                bc = weights.value (b, c),
                ac = weights.value (a, c),
                bd = weights.value (b, d),
                curr = (bc + de) / tol;
          if (curr > ac + be || curr > ad + be ||
              curr > bc + ae || curr > bd + ae) return true;
        }

      return false;
    }
}

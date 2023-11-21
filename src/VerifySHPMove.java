/**
 *  Recognize when a partial Hamiltonian Path is not the shortest possible.
 *  This is used for pruning a branch-and-bound search for the Shortest
 *  Hamiltonian Path (SHP). It's assumed that path[n] ... path[length-1]
 *  are set.
 *
 *  This particular strategy is a simple exchange  test.
 *  Is
 *      A B C ... G H
 *  longer than
 *      A C ... I B H?
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class VerifySHPMove extends VerifySHP
{
  private float tol = 1.00005F;

  public VerifySHPMove (Weights weights)
    {  super (weights); }

  @Override
  public boolean reject (int path [], int level)
    {
      assert level >= 0;
      assert level < path.length;

      if (level < 2)
          return false;

      int c = path[level-2], d = path[level-1], e = path[level];
      float de = weights.value (d, e),
            ce = weights.value (c, e),
            cd = weights.value (c, d),
            ad = weights.value (path[0], d);
      for (int i = 0; i < level-2; i++)
        {
          int a = path[i], b = path[i+1];
          float bd = weights.value (b, d),
                ab = weights.value (a, b);
          if (ab + cd + de > tol * (ad + bd + ce))
               return true;
          ad = bd;
        }

      //  Move to the very top?

      if (cd + de > tol * weights.value (path[0], d) + ce)
          return true;

      return false;
    }
}

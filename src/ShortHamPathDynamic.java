/*
 *  Find a shortest Hamiltonian path using dynamic programming algorithm.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

import static java.lang.Math.min;

import java.util.Arrays;

////////////////////////////////////////////////////////////////////////////////

public class ShortHamPathDynamic extends ShortHamPath
{
  //////////////////////////////////////////////////////////////////////////////

  public ShortHamPathDynamic (Weights weights)
    { super (weights); }

  //////////////////////////////////////////////////////////////////////////////

  @Override
  public int [] path (int n)
    {
      assert n >= 0 && n < 32;
      if (n <= 1)
          return new int [n];

      float weight [] [] = new float [n] [n];
      for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
          weight[i][j] = weights.value (i, j);

      //  Construct dp. This takes O(2**n n**2).

      int twoN = 1 << n;
      float dp [] [] = new float [twoN] [n];
      for (float [] d : dp)
          Arrays.fill (d, Float.MAX_VALUE);
      for (int i = 0; i < n; i++)
          dp[1<<i][i] = 0;
      for (int mask = 0; mask < twoN; mask++)
      for (int i = 0; i < n; i++)
        {
          int twoI = 1 << i;
          if ((mask & twoI) != 0)
            {
              float minDP = dp[mask][i];
              int maskTwoI = mask^twoI;
              float dpMaskTwoI [] = dp[maskTwoI];
              float weighti [] = weight[i];
              for (int j = 0; j < n; j++)
                  if ((mask & 1 << j) != 0)
                      minDP = min (minDP, dpMaskTwoI[j] + weighti[j]);
              dp[mask][i] = minDP;
            }
        }

      // Reconstruct path. This takes O(n**2)

      int cur = twoN - 1;
      int path [] = new int [n];
      int last = -1;
      for (int i = n - 1; i >= 0; i--)
        {
          int bj = -1;
          for (int j = 0; j < n; j++)
              if ((cur & 1 << j) != 0 &&
                  (bj == -1 ||
                   dp[cur][bj] + (last == -1 ? 0 : weights.value(bj,last)) > dp[cur][j] + (last == -1 ? 0 : weights.value (j,last))))
                  bj = j;
          path[i] = bj;
          cur ^= 1 << bj;
          last = bj;
        }

     return path;
   }
}

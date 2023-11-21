/*
 *  Weights based on a weights matrix.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class WeightsMatrix extends Weights
  {
    protected float matrix [] [];
    protected int n;

    ////////////////////////////////////////////////////////////////////////////

    public WeightsMatrix (float matrix [] [])
      {
        this.n = matrix.length;
        this.matrix = matrix;
      }

    ////////////////////////////////////////////////////////////////////////////

    public WeightsMatrix (int n, Weights weights)
      {
        assert n >= 0;
        this.n = n;
        matrix = new float [n] [n];

        if (weights.isSymmetric())
          {
            for (int i = 0; i < n-1; i++)
            for (int j = i+1; j < n; j++)
                matrix[i][j] = matrix[j][i] = weights.value (i, j);
          }
        else
          {
            for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
              {
                matrix[i][j] = weights.value (i, j);
                matrix[j][i] = weights.value (j, i);
                if (matrix[i][j] != matrix[j][i])
                    setSymmetric (false);
              }
          }

        setTriangleInequality (weights.isTriangleInequality ());
      }

     ///////////////////////////////////////////////////////////////////////////

    @Override
    public float value (int i, int j)
      {
        assert i >= 0 && i < n : "i = " + i;
        assert j >= 0 && j < n : "j = " + j;
        return matrix[i][j];
      }
  }

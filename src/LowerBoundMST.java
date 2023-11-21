/**
 *  Class to estimate the lower bounds of a Shortest Hamiltonian path (SHP),
 *  a semi-enclosed SHP (the starting vertex is specified), and a fully
 *  enclosed SHP (the starting and ending vertices are specified.
 *
 *  This uses minimum-spanning trees as a lower bound, which is always less
 *  than or equal to the weight of the SHP as the SHP is a spanning tree.
 *
 *  We do not make any assumption about the nature of the weights. As an
 *  example, we do not assume the triangle inequality holds.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static java.lang.Math.min;

////////////////////////////////////////////////////////////////////////////////

public class LowerBoundMST extends LowerBound
{
  MinSpanTree mst = new MinSpanTreePrims ();

  //////////////////////////////////////////////////////////////////////////////

  public LowerBoundMST (Weights weights)
    { super (weights); }

  //////////////////////////////////////////////////////////////////////////////

  @Override
  public float shp (int n)
    { return shp (n, weights); }

  @Override
  public float shp (int vertices [])
    { return shp (vertices.length, resequenceWeights (vertices)); }

  private float shp (int n, Weights weights)
    {
      assert n >= 0;
      return mst.weight (n, weights);
    }

  //////////////////////////////////////////////////////////////////////////////

  @Override
  public float semiEnclosedSHP (int n)
    { return semiEnclosedSHP (n, weights); }

  @Override
  public float semiEnclosedSHP (int vertices [])
    { return semiEnclosedSHP (vertices.length, resequenceWeights (vertices)); }

  private float semiEnclosedSHP (int n, Weights weights)
   {
      assert n >= 0;
      if (n <= 1)
          return 0;
      else if (n == 2)
          return weights.value (0, 1);

      //  Determine shortest distance between first vertex and remaining
      //  vertices.

      float firstWeight = Float.MAX_VALUE;
      for (int i = 1; i < n; i++)
          firstWeight = min (firstWeight, weights.value (0, i));
      assert firstWeight != Float.MAX_VALUE;

      //  Determine the weight of the MST of the remaining vertices.

      float mstWeight = mst.weight (n-1, new WeightsShift (weights, 1));

      return firstWeight + mstWeight;
    }

  //////////////////////////////////////////////////////////////////////////////

  @Override
  public float enclosedSHP (int n)
    { return enclosedSHP (n, weights); }

  @Override
  public float enclosedSHP (int vertices [])
    { return enclosedSHP (vertices.length, resequenceWeights (vertices)); }

  private float enclosedSHP (int n, Weights weights)
    {
      assert n >= 0;
      if (n <= 1)
          return 0;
      else if (n == 2)
          return weights.value (0, 1);

      //  Determine shortest distance between first vertex and interior
      //  vertices, and the last vertex and interior vertices.

      float firstWeight = Float.MAX_VALUE, lastWeight = Float.MAX_VALUE;
      for (int i = 1; i < n-1; i++)
        {
          firstWeight = min (firstWeight, weights.value (0, i));
          lastWeight = min (lastWeight, weights.value (n-1, i));
        }
      assert firstWeight != Float.MAX_VALUE;
      assert lastWeight != Float.MAX_VALUE;

      //  Determine the weight of the MST of the interior vertices.

      float mstWeight = mst.weight (n-2, new WeightsShift (weights, 1));

      return firstWeight + mstWeight + lastWeight;
    }

  //////////////////////////////////////////////////////////////////////////////
  //  Resequence weights

  private Weights resequenceWeights (int path [])
    {
      int n = path.length;
      assert n >= 0;

      float matrix [] [] = new float [n] [n];
      for (int i = 0; i < n-1; i++)
      for (int j = i+1; j < n; j++)
          matrix[i][j] = matrix[j][i] = weights.value (path[i], path[j]);

      return new WeightsMatrix (matrix);
    }
}

/**
 *  Abstract base class for deriving the minimum spanning tree (MST) of a
 *  complete graph.
 *
 *  Method "edges" returns an n-1 length integer array such that if
 *  array[i] = j then the MST has an edge between the i+1th and j+1th vertices.
 *
 *  Method "weight" returns the total MST weight. The derived classes may wish
 *  to override this, as it's often not the most efficient way to determine the
 *  total weight when you don't care about the edges.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.edgesWeight;

public abstract class MinSpanTree
{
  /////////////////////////////////////////////////////////////////////////////

  public abstract int [] edges (int n, Weights weights);

  /////////////////////////////////////////////////////////////////////////////

  public float weight (int n, Weights weights)
    { return edgesWeight (weights, edges (n, weights)); }
 }

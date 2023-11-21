/*
 *  Abstract base class for finding the shortest Hamiltonian path (or an
 *  approximation to it) through a list of vertices. Three different problems
 *  can be solved:
 *
 *     path               SHP
 *     semiEnclosedPath   SHP with the one terminal predefined
 *     enclosedPath       SHP with both terminals predefined
 *
 *  Typically the derived class will provide functions:
 *
 *     int [] path (int n)
 *     int [] semiEnclosedPath (int n)
 *     int [] enclosedPath (int n)
 *
 *  Not all methods need be overridden in the derived class, but this will
 *  crash if unimplemented method is called.
 *  
 *  All of the methods return an n-length array of indices defining the path,
 *  which will be a permutation of the values 0, ..., n-1.
 *
 *  In semiEnclosedPath, the predefined terminal is the first vertex, which
 *  will remain the first vertex in the output path. In short, path[0] = 0.
 *
 *  In enclosedPath, the predefined terminals are the first and last vertex,
 *  which will remain that way in the output path. In short, path[0] = 0 and
 *  path[n-1] = n-1.
 *
 *  All three methods assume that the vertex numbers go from 0 to n-1.
 *  If you want to provide specific vertex numbers then call the other
 *  provided methods, which will eventually call one of the three methods above.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.GraphUtil.isPathASpanningTree;

////////////////////////////////////////////////////////////////////////////////

public abstract class ShortHamPath
  {
    protected Weights weights;
    protected boolean print = false;
    protected enum PathType { Open, SemiEnclosed, Enclosed }

    ////////////////////////////////////////////////////////////////////////////

    protected ShortHamPath (Weights weights)
      { this.weights = weights; }

    public void setPrint (boolean print)
      { this.print = print; }

    ////////////////////////////////////////////////////////////////////////////

    public int [] path (int n)
      {
        assert false : "path method is unimplemented";
        return null;
      }

    public int [] path (int vertices [])
      { return execute (PathType.Open, vertices, 0, vertices.length); }

    public int [] path (int vertices [], int start, int end)
      { return execute (PathType.Open, vertices, start, end); }

    ////////////////////////////////////////////////////////////////////////////

    public int [] semiEnclosedPath (int n)
      {
        assert false : "semiEnclosedPath method is unimplemented";
        return null;
      }

    public int [] semiEnclosedPath (int vertices [])
      { return execute (PathType.SemiEnclosed, vertices, 0, vertices.length); }

    public int [] semiEnclosedPath (int vertices [], int start, int end)
      { return execute (PathType.SemiEnclosed, vertices, start, end); }

    ////////////////////////////////////////////////////////////////////////////

    public int [] enclosedPath (int n)
      {
        assert false : "enclosedPath method is unimplemented";
        return null;
      }

    public int [] enclosedPath (int vertices [])
      { return execute (PathType.Enclosed, vertices, 0, vertices.length); }

    public int [] enclosedPath (int vertices [], int start, int end)
      { return execute (PathType.Enclosed, vertices, start, end); }

    ////////////////////////////////////////////////////////////////////////////
    //  Determine a Hamiltonian path when a "vertices" argument is given.
    ///////////////////////////////////////////////////////////////////////////

    private int [] execute (PathType type, int vertices [], int start, int end)
      {
        assert start >= 0;
        assert end > start;
        assert vertices.length >= end;
        int n = end - start;

        Weights weightsSave = weights;
        weights = resequenceWeights (vertices, start, end);
        int path1 [] = null;
        switch (type)
          {
            case Open:          path1 = path (n);              break;
            case SemiEnclosed:  path1 = semiEnclosedPath (n);  break;
            case Enclosed:      path1 = enclosedPath (n);      break;
            default: assert false : "Unrecognized path type = " + type;
          }
        weights = weightsSave;

        //  Sanity checking.

        assert path1.length == n;
        assert isPathASpanningTree (path1);

        //  Form the final output

        int path [] = new int [n];
        for (int i = 0; i < n; i++)
            path[i] = vertices[start+path1[i]];
        return path;
     }

    ////////////////////////////////////////////////////////////////////////////
    //  Return a Weights object where the vertices are renumbered to
    //  from vertices[0] ... vertices[n-1] to 0 ... n-1, but with the
    //  same weights between the vertices.
    ///////////////////////////////////////////////////////////////////////////

    private Weights resequenceWeights (int vertices [], int start, int end)
      {
        assert start >= 0;
        assert end > start;
        assert vertices.length >= end;

        int n = end - start;
        float matrix [] [] = new float [n] [n];
        for (int i = 0; i < n-1; i++)
        for (int j = i+1; j < n; j++)
            matrix[i][j] = matrix[j][i] =
                weights.value (vertices[start+i], vertices[start+j]);
        Weights seqWeights = new WeightsMatrix (matrix);
        seqWeights.setTriangleInequality (weights.isTriangleInequality ());

        return seqWeights;
      }
}

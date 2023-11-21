/*
 *  Renumber the indice of a weights object.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import java.util.HashMap;
import java.util.Map;

////////////////////////////////////////////////////////////////////////////////

public class WeightsRenumber extends Weights
  {
    private Weights weights;
    private Map <Integer, Integer> map = new HashMap <> ();
    private Map <Integer, Integer> unmap = new HashMap <> ();

    ////////////////////////////////////////////////////////////////////////////

    public WeightsRenumber (Weights weights, int from [], int to [])
      { construct (weights, from, to); }

    ////////////////////////////////////////////////////////////////////////////

    public WeightsRenumber (Weights weights, int from [])
      {
        if (from.length > 0)
          {
            int to [] = new int [from.length];
            for (int i = 0; i < from.length; i++)
                to[i] = i;
            construct (weights, from, to);
          }
        setSymmetric (weights.isSymmetric());
        setTriangleInequality (weights.isTriangleInequality());
      }

    ////////////////////////////////////////////////////////////////////////////

    void construct (Weights weights, int from [], int to [])
      {
        assert from.length == to.length;
        this.weights = weights;
        setTriangleInequality (weights.isTriangleInequality ());
        for (int i = 0; i < from.length; i++)
          {
            if (! map.containsKey (from[i]))
                map.put (from[i], to[i]);
            else
                assert false : "Element in 'from' vector found twice = " +
                               from[i];
            if (! unmap.containsKey (to[i]))
                unmap.put (to[i], from[i]);
            else
                assert false : "Element in 'to' vector found twice = " +
                               to[i];
          }
        setSymmetric (weights.isSymmetric());
        setTriangleInequality (weights.isTriangleInequality());
      }

    ////////////////////////////////////////////////////////////////////////////

    @Override
    public float value (int i, int j)
      {
        Integer I = map.get (i);
        Integer J = map.get (j);
        assert I != null && J != null;
        return weights.value (I, J);
      }

    ////////////////////////////////////////////////////////////////////////////

    public void unnumber (int from [], int to [])
      {
        assert from.length == to.length;
        for (int i = 0; i < from.length; i++)
          {
            Integer I = unmap.get (from[i]);
            assert I != null;
            to[i] = I;
          }
     }
 }

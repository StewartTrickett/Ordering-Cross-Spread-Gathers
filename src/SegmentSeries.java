/*
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public class SegmentSeries extends Segment
  {
    int length, vertices [];
    boolean reverse = false;

    ///////////////////////////////////////////////////////////////////////////

    SegmentSeries (int vertices [])
      {
        this.length = vertices.length;
        assert this.length > 0;
        this.vertices = vertices.clone ();
      }

    ///////////////////////////////////////////////////////////////////////////

    SegmentSeries (int indices [], int length)
      {
        assert length > 0;
        this.length = length;
        this.vertices = new int [length];
        for (int i = 0; i < this.length; i++)
            this.vertices[i] = indices[i];
      }

    ///////////////////////////////////////////////////////////////////////////

    SegmentSeries (Segment seg1, Segment seg2)
      {
        int length1 = seg1.length ();
        int length2 = seg2.length ();
        length = length1 + length2;
        vertices = new int [length];
        for (int i = 0; i < length; i++)
            if (i < length1)
                vertices[i] = seg1.index (i);
            else
                vertices[i] = seg2.index (i-length1);
      }

    ///////////////////////////////////////////////////////////////////////////

    SegmentSeries (int vertex)
      {
        this.length = 1;
        this.vertices = new int [] { vertex };
      }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int start ()
      {
        if (! reverse)
            return vertices[0];
        else
            return vertices[length-1];
      }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int end ()
      {
        if (reverse)
            return vertices[0];
        else
            return vertices[length-1];
      }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void reverse ()
      { reverse = ! reverse; }

    ///////////////////////////////////////////////////////////////////////////

   @Override
    public int length ()
      { return length; }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int index (int i)
      {
        assert i >= 0 && i < length;
        if (! reverse)
            return vertices[i];
        else
            return vertices[length-i-1];
      }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void load (int vertices [], int start)
      {
        if (! reverse)
            for (int i = 0; i < length; i++)
                vertices[start+i] = this.vertices[i];
        else
            for (int i = 0; i < length; i++)
                vertices[start+i] = this.vertices[length-i-1];
      }
}

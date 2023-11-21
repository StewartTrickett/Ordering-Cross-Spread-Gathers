/*
 *  Pure abstract class for representing a "segment", which is a
 *  sequence of unique vertices making up part of a graph.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

////////////////////////////////////////////////////////////////////////////////

public abstract class Segment
  {
    public abstract int start ();
    public abstract int end ();
    public abstract void reverse ();
    public abstract int length ();
    public abstract void load (int indices [], int start);
    public abstract int index (int i);
 }

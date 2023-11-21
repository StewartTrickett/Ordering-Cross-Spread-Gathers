/**
 *  Index sorting on a floating point array, and related algorithms.
 */

package CrossSpreadOrder;

import java.util.Arrays;
import java.util.Comparator;

///////////////////////////////////////////////////////////////////////////////

public class SortIndex
{
   //  Private inner class.

   private static class FloatComparator implements Comparator <Integer>
     {
       private final float [] array;

       public FloatComparator (final float [] array)
         { this.array = array; }

   //    @Override
       @Override
      public int compare (Integer index1, Integer index2)
         {
           float f1 = array[index1], f2 = array[index2];
           if (f1 < f2)
               return -1;
           else if (f1 > f2)
               return 1;
           else if (index1 < index2)      // Stable sort
               return -1;
           else if (index1 > index2)
               return 1;
           else
               return 0;
         }
     }

   private static class IntComparator implements Comparator <Integer>
     {
       private final int [] array;

       public IntComparator (final int [] array)
         { this.array = array; }

   //    @Override
       @Override
      public int compare (Integer index1, Integer index2)
         {
           int f1 = array[index1], f2 = array[index2];
           if (f1 < f2)
               return -1;
           else if (f1 > f2)
               return 1;
           else if (index1 < index2)      // Stable sort
               return -1;
           else if (index1 > index2)
               return 1;
           else
               return 0;
         }
     }

   ///////////////////////////////////////////////////////////////////////////

   public static void sort (final float array [], Integer index [], int n)
     {
       assert n > 0;
       Arrays.sort (index, 0, n, new FloatComparator (array));
     }

   public static void sort (final int array [], Integer index [], int n)
     {
       assert n > 0;
       Arrays.sort (index, 0, n, new IntComparator (array));
     }

   ///////////////////////////////////////////////////////////////////////////

   public static Integer [] sort (final float array [], int n)
     {
        assert n > 0;
        Integer index [] = new Integer [n];
        for (int i = 0; i < n; i++)
            index[i] = i;
        sort (array, index, n);
        return index;
     }

   public static Integer [] sort (final int array [], int n)
     {
        assert n > 0;
        Integer index [] = new Integer [n];
        for (int i = 0; i < n; i++)
            index[i] = i;
        sort (array, index, n);
        return index;
     }

   ///////////////////////////////////////////////////////////////////////////

   public static void sort (final float array [], Integer index [])
     {  Arrays.sort (index, new FloatComparator (array));  }

   public static void sort (final int array [], Integer index [])
     {  Arrays.sort (index, new IntComparator (array));  }

   ///////////////////////////////////////////////////////////////////////////

   public static Integer [] sort (final float array [])
     {
        assert array.length > 0;
        int n = array.length;
        Integer index [] = new Integer [n];
        for (int i = 0; i < n; i++)
            index[i] = i;
        sort (array, index);
        return index;
     }

   public static Integer [] sort (final int array [])
     {
        assert array.length > 0;
        int n = array.length;
        Integer index [] = new Integer [n];
        for (int i = 0; i < n; i++)
            index[i] = i;
        sort (array, index);
        return index;
     }
}

/**
 *  Sorting utilities
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  LICENSERIGHTNOTICE
 */

package CrossSpreadOrder;

import java.util.Arrays;

public class Sort
  {
     ///////////////////////////////////////////////////////////////////////////

     public static float medianSafe (final float array [])
      { return median (array.clone (), array.length); }

     public static float medianSafe (final float array [], int n)
      { return median (array.clone (), n); }

     public static float median (float array [])
      { return median (array, array.length); }

     public static float median (float array [], int n)
      {
        //  Select the n / 2 element.

        assert n > 0 && n <= array.length;
        int posn = n / 2, left = 0, right = n-1, pivot, lastLeft = 0;
        do
          {
            pivot = partition (array, left, right);
            if (pivot < posn)
              {
                lastLeft = left;
                left = pivot + 1;
              }
            else if (pivot > posn)
                right = pivot - 1;
            assert left <= posn;
            assert right >= posn;
          }
        while (pivot != posn);

        //  If n is odd then return array[n/2].

        float value1 = array[posn];
        if (n%2 == 1)
            return value1;

        //  If n is even then return (array[n/2] + array[n/2-1]) / 2

        if (left == posn) left = lastLeft;
        float value2 = select (array, left, posn-1, posn-1);
        return (value1 + value2) / 2;
      }

     ///////////////////////////////////////////////////////////////////////////

     public static float select (float array [], int left, int right, int posn)
     {
        assert left >= 0;
        assert right >= left;
        assert posn >= left;
        assert posn <= right;

        int pivot;
        do
          {
            pivot = partition (array, left, right);
            if (pivot < posn)
                left = pivot + 1;
            else if (pivot > posn)
                right = pivot - 1;
            assert left <= posn;
            assert right >= posn;
          }
        while (pivot != posn);
        return array [pivot];
     }

     ///////////////////////////////////////////////////////////////////////////

     public static float select (float array [], int n, int posn)
     {  return select (array, 0, n-1, posn);  }

     ///////////////////////////////////////////////////////////////////////////

     public static int partition (float array [], int left, int right)
       {
         assert (left >= 0);
         assert (left <= right);

         //  Choose a pivot that is near the median value of the array.
         //  This speeds things up.

         int n = right - left + 1;
         int pivot;
         if (n < 3)
             pivot = left;
         else if (n < 20)
             pivot = median3 (array, left+n/4, left+n/2, left+3*n/4);
         else
           {
             int n6 = n / 6;
             pivot = median5 (array, left+n6, left+2*n6, left+3*n6,
                              left+4*n6, left+5*n6);
           }

         return partition (array, left, right, pivot);
       }

     ///////////////////////////////////////////////////////////////////////////
     //  Define pivot value := array[pivot].
     //  Sort an array into the following order.
     //      All values <= pivot value value.
     //      An element containing the pivot value
     //      All values > pivot value
     ///////////////////////////////////////////////////////////////////////////

     public static int partition (float array [], int left, int right, int pivot)
       {
         assert (left >= 0);
         assert (left <= right);
         assert (pivot >= left);
         assert (pivot <= right);
         float pivotValue = array[pivot];

         // Move pivot element to the beginning.

         swap (array, left, pivot);
         int origLeft = left;
         int origRight = right;
         left ++;

         //  Partition. At the end of each iteration, all values from
         //  the beginning to "left" are <= "pivotValue", and all values from
         //  "right" to the end are > "pivotValue".

         do
           {
             while (array[right] >  pivotValue) right--;
             while (left < right && array[left] <= pivotValue) left++;
             if (left < right)
                 swap (array, left, right);
           }
         while (left < right);

         //  Move the pivot element stored at the beginning to the new pivot
         //  position. Return the index to this pivot position.

         pivot = right;
         swap (array, origLeft, pivot);
         assert origLeft <= pivot;
         assert origRight >= pivot;
         return pivot;
       }

     ///////////////////////////////////////////////////////////////////////////

     public static void swap (float array [], int i, int j)
       {
         float t = array[i];
         array[i] = array[j];
         array[j] = t;
       }

     ///////////////////////////////////////////////////////////////////////////

     public static int median3 (float array [], int i, int j, int k)
       {
         float a = array[i], b = array[j], c = array[k];
         int index = 0;
         if (a > b) index += 1;
         if (b > c) index += 2;
         if (a > c) index = 3 - index;
         assert index >= 0 && index <= 2;
         int situation [] = { j, i, k };
         return situation[index];
       }

     ///////////////////////////////////////////////////////////////////////////

     public static int median5 (float array [], int i, int j, int k, int l, int m)
       {
         float array5 [] = { array[i], array[j], array[k], array[l], array[m] };
         Arrays.sort (array5);
         float median = array5[2];
         if (array[i] == median)
            return i;
         else if (array[j] == median)
            return j;
         else if (array[k] == median)
            return k;
         else if (array[l] == median)
            return l;
         assert array[m] == median;
         return m;
      }
  }

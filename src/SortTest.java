
/**
 *  Unit test for Sort methods.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  LICENSERIGHTNOTICE
 */

package CrossSpreadOrder;

import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class SortTest
  {

    int printLevel = 0;

///////////////////////////////////////////////////////////////////////////////

@Test
    public void median3 ()
      {
        printf ("%s: %s%n%n", className(), methodName());
        Random rand = new Random (950);

        for (int n = 1; n <= 10000; n++)
        for (int p = 0; p < 10000 / n; p++)
          {
            float array [] = new float [n];
            for (int i = 0; i < n; i++)
                array[i] = rand.nextInt (25);

            int i = rand.nextInt (n);
            int j = rand.nextInt (n);
            int k = rand.nextInt (n);
            int index = Sort.median3 (array, i, j, k);

            float array3 [] = new float [3];
            array3[0] = array[i];
            array3[1] = array[j];
            array3[2] = array[k];
            Arrays.sort (array3);

            assertTrue (array[index] == array3[1]);
          }
      }
///////////////////////////////////////////////////////////////////////////////

@Test
    public void median5 ()
      {
        printf ("%s: %s%n%n", className(), methodName());
        Random rand = new Random (572);

        for (int n = 1; n <= 10000; n++)
        for (int p = 0; p < 10000 / n; p++)
          {
            float array [] = new float [n];
            for (int i = 0; i < n; i++)
                array[i] = rand.nextInt (25);

            int i = rand.nextInt (n);
            int j = rand.nextInt (n);
            int k = rand.nextInt (n);
            int l = rand.nextInt (n);
            int m = rand.nextInt (n);
            int index = Sort.median5 (array, i, j, k, l, m);

            float array5 [] = new float [5];
            array5[0] = array[i];
            array5[1] = array[j];
            array5[2] = array[k];
            array5[3] = array[l];
            array5[4] = array[m];
            Arrays.sort (array5);

            assertTrue (array[index] == array5[2]);
          }
      }

///////////////////////////////////////////////////////////////////////////////

@Test
    public void partitionFloat ()
      {
        printf ("%s: %s%n%n", className(), methodName());
        Random rand = new Random (444);
        for (int n = 1; n <= 100; n++)
        for (int p = 0; p < 100 / n; p++)
          {
            float array [] = new float [n];
            for (int i = 0; i < n; i++)
                array[i] = rand.nextInt (100);
            float sortedArray [] = array.clone ();
            Arrays.sort (sortedArray);

            for (int left = 0; left < n; left++)
            for (int right = left; right < n; right++)
              {
                float array1 [] = array.clone ();
                int pivot = Sort.partition (array1, left, right);

                // Check that the results are correct.

                for (int i = left; i <= pivot; i++)
                    assertTrue (array1[i] <= array1[pivot]);
                for (int i = pivot+1; i <= right; i++)
                    assertTrue (array1[pivot] < array1[i]);
                float sortedArray1 [] = array1.clone ();
                Arrays.sort (sortedArray1);
                for (int i = 0; i < n; i++)
                    assertTrue (sortedArray[i] == sortedArray1[i]);
              }
          }
      }

///////////////////////////////////////////////////////////////////////////////

@Test
    public void partitionFloat1 ()
      {
        printf ("%s: %s%n%n", className(), methodName());
        Random rand = new Random (444);
        for (int test = 0; test < 1000; test++)
        for (int n = 1; n <= 100; n++)
          {
            float array [] = new float [n];
            for (int i = 0; i < n; i++)
                array[i] = rand.nextInt (100);
            int left = rand.nextInt (n);
            int pivot = left + rand.nextInt (n-left);
            int right = pivot + rand.nextInt (n-pivot);
            assert left >= 0 && pivot >= left && right >= pivot && right < n;
            Sort.partition (array, left, right, pivot);
          }
      }

///////////////////////////////////////////////////////////////////////////////

@Test
    public void selectFloat ()
      {
        printf ("%s: %s%n%n", className(), methodName());
        Random rand = new Random (371);
        for (int n = 1; n <= 100; n++)
        for (int p = 0; p < 10000 / n; p++)
          {
            float array [] = new float [n];
            for (int i = 0; i < n; i++)
                array[i] = rand.nextInt (100);
            float sortedArray [] = array.clone ();
            Arrays.sort (sortedArray);
            for (int posn = 0; posn < n; posn++)
              {
                float array1 [] = array.clone ();
                float select = Sort.select (array1, n, posn);
                assertTrue (select == sortedArray[posn]);
              }
          }
      }

///////////////////////////////////////////////////////////////////////////////

@Test
    public void medianFloat ()
      {
        printf ("%s: %s%n%n", className(), methodName());
        Random rand = new Random (371);
        for (int n = 1; n <= 100; n++)
        for (int p = 0; p < 100000 / n; p++)
          {
            float array [] = new float [n];
            for (int i = 0; i < n; i++)
                array[i] = rand.nextInt (100);

            float median1 = Sort.median (array, n);
            float median2 = Sort.median (array);     // no length arg
            float median3 = slowMedian (array);

            assertEquals (median1, median2, .0001F);
            assertEquals (median1, median3, .0001F);
          }

        //  Test the safe version that does not reorder the array

        for (int n = 1; n <= 100; n++)
        for (int p = 0; p < 100000 / n; p++)
          {
            float array [] = new float [n];
            for (int i = 0; i < n; i++)
                array[i] = rand.nextInt (100);
            float origArray [] = array.clone ();

            float median1 = Sort.medianSafe (array);
            float median2 = slowMedian (array);

            assertEquals (median1, median2, .0001F);
            for (int i = 0; i < n; i++)
                assertTrue (array[i] == origArray[i]);
          }
     }

///////////////////////////////////////////////////////////////////////////////

    private float slowMedian (float array [])
      {
        int n = array.length;
        float sortedArray [] = array.clone ();
        Arrays.sort (sortedArray);
        if (n % 2 == 1)
            return sortedArray [n/2];
        else
            return (sortedArray [n/2-1] + sortedArray[n/2]) / 2;
      }
}

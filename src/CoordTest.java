/*
 *  Unit tests for Coord
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import static CrossSpreadOrder.Print.printf;
import static CrossSpreadOrder.SystemCall.className;
import static CrossSpreadOrder.SystemCall.methodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

///////////////////////////////////////////////////////////////////////////////

public class CoordTest
{
   static float tol = 1E-5F;

   /////////////////////////////////////////////////////////////////////////////

   @Test
   public void test1 ()
     {
       printf ("%s: %s%n%n", className(), methodName());

       Coord c1 = new Coord (3, 4);
       Coord c2 = new Coord (3, 4);
       Coord c3 = new Coord (3, 6);
       Coord c4 = new Coord (0, 4);

       assertTrue (c1.x() == 3);
       assertTrue (c1.y() == 4);
       assertTrue (c1.equals (c2));
       assertTrue (c2.equals (c1));
       assertTrue (! c1.equals (c3));
       assertTrue (! c1.equals (c3));
       assertEquals (c1.norm (), 5, tol);
       assertEquals (c1.distance (c3), 2, tol);
       assertEquals (c1.distance (c4), 3, tol);
       assertEquals (c1.distanceHypot (c3), 2, tol);
       assertEquals (c1.distanceHypot (c4), 3, tol);
       assertEquals (c1.distanceSq (c3), 4, tol);
       assertEquals (c1.distanceSq (c4), 9, tol);
    }
}

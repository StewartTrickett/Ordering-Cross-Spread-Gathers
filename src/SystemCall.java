/**
 *  Linux system utilities
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class SystemCall
{
  //////////////////////////////////////////////////////////////////////////////
  //  Return clock time and cpu time in seconds.

  public static double time ()
    { return 1E-3 * System.currentTimeMillis (); }

  public static double cpuTime ()
    {
      ThreadMXBean bean = ManagementFactory.getThreadMXBean ();
      return 1E-9 * bean.getCurrentThreadCpuTime ();
    }

  //////////////////////////////////////////////////////////////////////////////
  //  Return name of method and class.

  public static String methodName ()
    { return methodName (1); }

  public static String methodName (int level)
    {
      assert level >= 0;
      return new Exception().getStackTrace()[level+1].getMethodName();
    }

  public static String className ()
    {
      String fullName = new Exception().getStackTrace()[1].getClassName();
      int length = fullName.length();
      int lastDot = fullName.lastIndexOf ('.');
      return fullName.substring (lastDot+1, length);
    }
}
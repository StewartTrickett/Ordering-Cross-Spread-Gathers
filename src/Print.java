/**
 *  Some static printing utilities.
 *
 *  COPYRIGHTNOITCE
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;
import java.io.PrintStream;

public class Print
{
   ///////////////////////////////////////////////////////////////////////////

   public static PrintStream printf (String format, Object... args)
     { return print (stringf (format, args)); }

   ///////////////////////////////////////////////////////////////////////////

   public static String stringf (String format, Object... args)
     { return String.format (format, args); }

   ///////////////////////////////////////////////////////////////////////////

   public static PrintStream printff (String format, Object... args)
     {
       PrintStream p = printf (format, args);
       flush ();
       return p;
     }

   ///////////////////////////////////////////////////////////////////////////

   public static PrintStream print (String str)
     {
       System.out.print (str);
       return System.out;
     }

   ///////////////////////////////////////////////////////////////////////////

   public static void flush ()
     { System.out.flush (); }
 }

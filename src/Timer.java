/**
 *  Time an interval of computation.
 *
 *  Copyright (c) 2023 Stewart Trickett
 *  Licensed under the MIT License (see LICENSE.md file)
 */

package CrossSpreadOrder;

public class Timer
{
   private double startCPUTime, lastCPUTime, startElapsedTime, lastElapsedTime;

   public Timer ()
     {
       startCPUTime = lastCPUTime = SystemCall.cpuTime ();
       startElapsedTime = lastElapsedTime = SystemCall.time();
     }

   public void restart ()
     {
       lastCPUTime = SystemCall.cpuTime ();
       lastElapsedTime = SystemCall.time ();
     }

   public double cpu ()
     {
       double currTime = SystemCall.cpuTime ();
       double cpuTime = currTime - lastCPUTime;
       lastCPUTime = currTime;
       return cpuTime;
     }

   public double elapsed ()
     {
       double currTime = SystemCall.time ();
       double elapsedTime = currTime - lastElapsedTime;
       lastElapsedTime = currTime;
       return elapsedTime;
     }

   public double cpuTotal ()
     { return SystemCall.cpuTime () - startCPUTime; }

   public double elapsedTotal ()
     { return SystemCall.time () - startElapsedTime; }
}

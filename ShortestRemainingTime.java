import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Shortest remaining time algorithm
 *
 */
public class ShortestRemainingTime
{
    private static final float TIME_SLICE = 1;
    
    /**
     * Runs the algo with new random processes
     *
     * @param processes
     * @param target
     * @return
     */
    public static String processor(Collection<SimulatedProcess> processes, float target)
    {
        ReadyQueue readyQueue = new ReadyQueue();
        LinkedList<SimulatedProcess> newQueue = new LinkedList<SimulatedProcess>();
        newQueue.addAll(processes);
        newQueue.sort(ScheduleHelper.arrivalComp);
        
        float currTime = 0;
        ArrayList<String> times = new ArrayList<String>();
        boolean hasStarved = false;
        while(!readyQueue.isEmpty() || !newQueue.isEmpty())
        {
            processSwitch(readyQueue, currTime);
            hasStarved = checkStarve(processes, readyQueue, newQueue, currTime, target, hasStarved);
            if(!readyQueue.isEmpty() || !newQueue.isEmpty())
            {
                times.add(runTimeSlice(readyQueue, newQueue, currTime));
                currTime += TIME_SLICE;
            }
        }
        String output = ScheduleHelper.formatOutput(processes, times, currTime); 
        return output;
    }
    
    /**
     * Runs the current process for x amount of time.
     * Makes the remaining processes wait if they aren't the current
     *
     * @param readyQueue
     * @param wait
     * @param currTime
     * @return
     */
    private static String runTimeSlice(ReadyQueue readyQueue, LinkedList<SimulatedProcess> newQueue, float currTime)
    {
        String n = "wait";
        if(!readyQueue.isEmpty())
        {
            readyQueue.runCurrentProc(TIME_SLICE, currTime);
            n = readyQueue.getCurrentProc().getProcName();
        }
        
        for(SimulatedProcess proc: newQueue)
        {
            proc.waitForArrival(TIME_SLICE);
            //If the newly arrived process has less remaining runtime, it can replace the current one
            if(proc.getArrivalTime() <= 0 && !readyQueue.isEmpty() 
                    && proc.getRunTime() < readyQueue.getCurrentProc().getRunTime()) 
                readyQueue.addAsCurrentProc(proc);
            else if(proc.getArrivalTime() <= 0)
                readyQueue.add(proc);
        }
        newQueue.removeIf((proc) -> proc.getArrivalTime() <= 0);
        
        return n;
    }
    
    /**
     * Checks if the queues are empty and less than the runTime time given is less than or equal to 0
     * Depending on which it will remove the current process and set the current processes time to the Quanta Slice
     * otherwise if it is less that the allowed time it updates the current postition and still sets the time.
     * The difference lies in whether it needs to be removed or not.
     *
     * @param rrQueue
     * @param currTime
     */
    private static void processSwitch(ReadyQueue rrQueue, float currTime)
    {
        if(!rrQueue.isEmpty() && rrQueue.getCurrentProc().getRunTime() <= 0)
        {
            rrQueue.sort(ScheduleHelper.runComp);
            rrQueue.removeCurrentProc(currTime);
        }
        
    }
    
    /**
     * checks if a process is starving or not
     * @param processes - process
     * @param readyQueue - queue of ready process
     * @param newQueue	- queue of processes waiting
     * @param currTime - current time
     * @param target - the when the proceess starves
     * @param hasStarved - has a process starved yet?
     * @return true if starve, false if not
     */
    private static boolean checkStarve(Collection<SimulatedProcess> processes, ReadyQueue readyQueue, 
            LinkedList<SimulatedProcess> newQueue, float currTime, 
            float target, boolean hasStarved)
    {
        if(currTime >= target && !hasStarved)
        {
            readyQueue.evictStarved();
            newQueue.clear();
            processes.removeIf((proc) -> (proc.getStartTime() < 0));
            hasStarved = true;
        }
        return hasStarved;
    }
}

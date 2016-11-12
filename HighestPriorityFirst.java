
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Highest priority first non-preemptive
 *
 */
public class HighestPriorityFirst
{
    private static final float MAX_AGE = 5;
    private static final float TIME_SLICE = 1;
    private static final float MAX_ALLOTED_TIME = 1;
    
    /**
     * Runs the algo with new random processes
     *
     * @param processes
     * @param target
     * @return
     */
    public static String processor(Collection<SimulatedProcess> processes, float target, boolean isAging)
    {
        LinkedList<SimulatedProcess> newQueue = new LinkedList<SimulatedProcess>();
        newQueue.addAll(processes);
        newQueue.sort(ScheduleHelper.arrivalComp);
        PriorityQueue readyQueues = new PriorityQueue(SimulatedProcess.MAX_PRIORITY);
        
        float currTime = 0;
        ArrayList<String> times = new ArrayList<String>();
        boolean hasStarved = false;
        while(!readyQueues.isEmpty() || !newQueue.isEmpty())
        {
            
            processSwitch(readyQueues, currTime);
            hasStarved = checkStarve(processes, readyQueues, newQueue, currTime, target, hasStarved);
            if(!readyQueues.isEmpty() || !newQueue.isEmpty())
            {
                times.add(runTimeSlice(readyQueues, newQueue, currTime, isAging));
                currTime += TIME_SLICE;
            }
            //System.out.println(readyQueues.toString());
        }
        return ScheduleHelper.formatPriorityOutput(processes, times, currTime);
    }

    /**
     * Runs a process and waits for arriving processes for a defined timeslice
     * @param readyQueues the multi level queue of ready processes
     * @param newQueue the queue of arriving processes
     * @param currTime the current time thus far
     * @param isAging true if the priorities should be 
     * changed based on a process's age since last changing priority
     * @return the name of the process that ran, if any
     */
    private static String runTimeSlice(PriorityQueue readyQueues,
            LinkedList<SimulatedProcess> newQueue, float currTime, boolean isAging)
    {
        String n = "wait";
        if(!readyQueues.isEmpty())
        {
            readyQueues.runCurrentProc(TIME_SLICE, currTime);
            n = readyQueues.getCurrentProc().getProcName();
        }
        if(isAging)
        {
            readyQueues.ageProcesses(TIME_SLICE);
            readyQueues.changePriority(MAX_AGE);
        }

        for(SimulatedProcess proc: newQueue)
        {
            proc.waitForArrival(TIME_SLICE);
            if(proc.getArrivalTime() <= 0)
            {
                readyQueues.add(proc);
                proc.setAllotedTime(MAX_ALLOTED_TIME);
            }
        }
        newQueue.removeIf((proc) -> proc.getArrivalTime() <= 0);
        
        return n;
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
    private static boolean checkStarve(Collection<SimulatedProcess> processes,
            PriorityQueue readyQueues, LinkedList<SimulatedProcess> newQueue,
            float currTime, float target, boolean hasStarved)
    {
        if(currTime >= target && !hasStarved)
        {  
            readyQueues.evictStarved();
            newQueue.clear();
            processes.removeIf((proc) -> (proc.getStartTime() < 0));
        }
        return hasStarved;
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
    private static void processSwitch(PriorityQueue rrQueue, float currTime)
    {
        
        if(!rrQueue.isEmpty()  && rrQueue.getCurrentProc().getRunTime() <= 0)
        {
            rrQueue.removeCurrentProc(currTime);
        }
        else if(!rrQueue.isEmpty() && rrQueue.getCurrentProc().getAllotedTime() <= 0)
        {
            rrQueue.updateCurrentProc();
            rrQueue.getCurrentProc().setAllotedTime(MAX_ALLOTED_TIME);
        }
    }
}

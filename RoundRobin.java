import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
* Created by Alex Preston on 9/25/2016.
* This is the round robin algo. The way round robin works is everything is queued
* priorities do not matter. There is a time quanta or a time slice, that dictates how long
* a process will execute for. The process will processor for x amount of time, and in this simulation have its time decremented, then it will
* be ejected and put on the end of the queue. The queue will continue to execute until all processes are done. Continuing to remove processes
* and add them onto the end of the queue until the processes are done. Hence the name "Round Robin"
*/
public class RoundRobin {

    private static final float QUANTA = 1;
    private static final float MAX_TIME = 1;

    /**
     * Runs the algo with new random processes
     *
     * @param processes
     * @param target
     * @return
     */
    public static String processor(Collection<SimulatedProcess> processes, float target) {
        ReadyQueue queue = new ReadyQueue();
        LinkedList<SimulatedProcess> newQueue = new LinkedList<>();
        newQueue.addAll(processes);
        newQueue.sort(ScheduleHelper.arrivalComp);


        float currTime = 0; //Current Time
        ArrayList<String> times = new ArrayList<>();
        boolean isEmpty = false;

        //Bulk of the work where the RoundRobin happens
        while (!queue.isEmpty() || !newQueue.isEmpty()) {

            processSwitch(queue, currTime);
            isEmpty = delete(processes, queue, newQueue, currTime, target, isEmpty);
            if (!queue.isEmpty() || !newQueue.isEmpty()) {
                times.add(timeSlice(queue, newQueue, currTime));
                currTime += QUANTA;
            }
        }
        String outPut = ScheduleHelper.formatOutput(processes, times, currTime);
        return outPut;
    }


    /**
     * Checks to see if it isnt empty and if the target time for the process is <= to the actual runTime
     * if it is then it evicts any starved processes clears the new queue and removes the process if the startTime is less
     * than 0. After all of that it would return true. if now it will return whatever is passed.
     *
     * @param process
     * @param queue
     * @param newQueue
     * @param runTime
     * @param target
     * @param isEmpty
     * @return
     */
    public static boolean delete(Collection<SimulatedProcess> process, ReadyQueue queue, LinkedList<SimulatedProcess> newQueue, float runTime, float target, boolean isEmpty) {

        if (!isEmpty && target <= runTime) { //Checks to see it's empty and if the target time is <= the runtime associated with the process
            queue.evictStarved(); //If the process is starved get rid of it
            newQueue.clear();
            process.removeIf((proc) -> (proc.getStartTime()) < 0);
            isEmpty = true;
        }
        return isEmpty;
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
    public static void processSwitch(ReadyQueue rrQueue, float currTime) {
        if (!rrQueue.isEmpty() && rrQueue.getCurrentProc().getRunTime() <= 0) {
            rrQueue.removeCurrentProc(currTime);
            if (!rrQueue.isEmpty()) {
                rrQueue.getCurrentProc().setAllotedTime(QUANTA);
            }
        } else if (!rrQueue.isEmpty() && rrQueue.getCurrentProc().getAllotedTime() <= 0) {
            rrQueue.updateCurrentProc();
            rrQueue.getCurrentProc().setAllotedTime(QUANTA);
        }
    }

    /**
     * Runs the current process for x amount of time.
     * Makes the remaining processes wait if they aren't the current
     *
     * @param queue
     * @param wait
     * @param currTime
     * @return
     */
    public static String timeSlice(ReadyQueue queue, LinkedList<SimulatedProcess> wait, float currTime) {
        String n = "";
        if (!queue.isEmpty()) {
            queue.runCurrentProc(QUANTA, currTime);
            n = queue.getCurrentProc().getProcName();
        }

        for (SimulatedProcess process : wait) {
            process.waitForArrival(QUANTA);
            if (process.getArrivalTime() <= 0) {
                queue.addAsCurrentProc(process);
                process.setAllotedTime(MAX_TIME);
            }
        }
        wait.removeIf((proc) -> proc.getArrivalTime() <= 0);

        return n;
    }
}
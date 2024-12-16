import java.util.*;

// Class to represent a Process with its attributes
class Process {
    String name; // Name of the process
    int arrivalTime, burstTime, priority, quantum, remainingTime, executedTime1, starttime;
    double fcaiFactor = 0; // FCAI factor of the process

    // Constructor to initialize a new process with the given parameters
    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.remainingTime = burstTime; // Initially, remaining time equals burst time
        this.executedTime1 = 0; // Initially, no time has been executed
        this.starttime = 0; // Initially, process start time is 0
    }
}

public class code {
    // Current time in the system, starts from 0
    static int currentTime = 0;

    public static void main(String[] args) {
        // Create a list of processes to simulate
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 17, 4, 4));
        processes.add(new Process("P2", 3, 6, 9, 3));
        processes.add(new Process("P3", 4, 10, 3, 5));
        processes.add(new Process("P4", 29, 4, 8, 2));

        // Call the method to simulate the FCAI scheduling algorithm
        simulateFCAIScheduling(processes);
    }

    // Method to simulate FCAI Scheduling
    public static void simulateFCAIScheduling(List<Process> processes) {
        // PriorityQueue to sort by FCAI factor (ascending order)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingDouble(p -> p.fcaiFactor));
        List<Process> completedProcesses = new ArrayList<>();

        // Calculate V1 and V2 based on the arrival time and burst time of all processes
        int lastArrivalTime = processes.stream().mapToInt(p -> p.arrivalTime).max().orElse(1);
        int maxBurstTime = processes.stream().mapToInt(p -> p.burstTime).max().orElse(1);
        double V1 = lastArrivalTime / 10.0; // V1 is based on the maximum arrival time
        double V2 = maxBurstTime / 10.0; // V2 is based on the maximum burst time

        // Calculate the initial FCAI factor for each process
        for (Process p : processes) {
            p.fcaiFactor = calculateFCAIFactor(p, V1, V2);
        }

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime)); // p1p2p3p4
        //processes = sortReadyQueueByArrivalTime(processes);

        // Process currentProcess = null;
        // Start the scheduling simulation
        Process p = null;
        Process currentProcess = null;
        Process newCurrProcess=null;
        while (!processes.isEmpty() || !readyQueue.isEmpty()) { // || !readyQueue.isEmpty()
            // Move all processes ose arrival time has passed into the ready queue
            if(!readyQueue.isEmpty()) readyQueue = sortReadyQueueByArrivalTime(readyQueue);
            //if(currentProcess==null){
            Iterator<Process> it = processes.iterator();
            while (it.hasNext()) {
                p = it.next();
                if (p.arrivalTime <= currentTime) {
                    readyQueue.offer(p); // Add to the ready queue if process has arrived //p1p2\
                    System.out.println(p.name);
                    it.remove(); // Remove it from the processes list
                    readyQueue = sortReadyQueueByArrivalTime(readyQueue);

                }
                readyQueue = sortReadyQueueByArrivalTime(readyQueue);
            }
        
            // readyQueue = sortReadyQueueByArrivalTime(readyQueue);
            

            // readyQueue.sort(Comparator.comparingInt(p -> p.arrivalTime));
           // readyQueue = sortReadyQueueByArrivalTime(readyQueue);
            
            //currentProcess=getLeastArrivalTimeProcess(readyQueue);
            readyQueue = sortReadyQueueByArrivalTime(readyQueue);
            
            // for (Process m : readyQueue) {
            //    // readyQueue = sortReadyQueueByArrivalTime(readyQueue);
            //     System.out.println(m.name);
            //     System.out.println(m.arrivalTime);
            //    // readyQueue = sortReadyQueueByArrivalTime(readyQueue);
            // }

// int  minarrival=100;
//             for (Process v : readyQueue) {
//                 if (v.arrivalTime < minarrival) {
//                     minarrival = v.arrivalTime;
//                     currentProcess = v;
//                 }
//             }

            currentProcess = readyQueue.poll(); // p1

            readyQueue.offer(currentProcess);
            readyQueue = sortReadyQueueByArrivalTime(readyQueue);
            // If the ready queue is empty, move forward in time
            // if (readyQueue.isEmpty()) {
            // currentTime++;
            // continue;
            // }

            // Poll the process with the lowest FCAI factor from the ready queue
            // if (currentProcess == null)

            // currentProcess.starttime = currentTime; // Set the start time for the process
            System.out.printf("Time %d: %s selected for execution (FCAI Factor: %.2f)\n",
                    currentTime, currentProcess.name, currentProcess.fcaiFactor);

            int initialQuantum = currentProcess.quantum; // Store the initial quantum

            // Calculate 40% of the quantum time for execution
            int executedTime = (int) Math.ceil(initialQuantum * 0.4);

            // Update current time and process attributes after execution
            currentTime += executedTime;
            // currentProcess.executedTime1 += executedTime;
            currentProcess.remainingTime -= executedTime;

            // Recalculate the FCAI factor for the current process

            currentProcess.fcaiFactor = calculateFCAIFactor(currentProcess, V1, V2);

            for (Process m : processes) {
                if (p.arrivalTime <= currentTime) {
                    readyQueue.offer(m); // Add to the ready queue if the process has arrived // Remove it from the
                                         // processes list
                    processes.remove(m);
                    //System.out.println("\nExecution ");
                }
            }
            readyQueue = sortReadyQueueByArrivalTime(readyQueue);

            // do{

            // }while(readyQueue.poll() == null);
            int flag = 0;
            if ((readyQueue.poll() == null && currentProcess != null)
                    || currentProcess.fcaiFactor <= getLeastFcaiFactorProcess(readyQueue)) {
                while (flag == 0||currentProcess.remainingTime!=0) {
                    // Adjust the current time when the readyQueue is empty
                    currentTime++;
                    executedTime++;
                    currentProcess.remainingTime--;

                   // System.out.printf("Time IS %d \n",
                   //         currentTime);

                    // Check if any process has arrived and add it to the readyQueue
                    // Check if any process has arrived and add it to the readyQueue
                    if (readyQueue.isEmpty()) {
                        for (Process l : processes) {
                            if (l.arrivalTime <= currentTime) {
                                flag = 1;
                                //System.out.println("\nExecution ");
                                break;
                            }
                        }
                    }
                    if (executedTime >= currentProcess.quantum) {
                       // System.out.printf("HI\n");
                        flag = 1;
                    }

                    

                    if (flag == 1) {
                        break;
                    }
                }

                currentProcess.quantum = Math.max(1, currentProcess.quantum + 2); // Increase quantum by 2
                calculateFCAIFactor(currentProcess, V1, V2);
                // currentProcess.arrivalTime = currentTime;
                // if (currentProcess.remainingTime > 0)
                readyQueue.offer(currentProcess); // Reinsert into the priority queue
                System.out.printf(
                        "%s moved to the ready queue with updated quantum %d and FCAI factor %.2f (RemainingTime: %d)\n",
                        currentProcess.name, currentProcess.quantum, currentProcess.fcaiFactor,
                        currentProcess.remainingTime);
                // readyQueue.offer(currentProcess);
                // currentProcess = readyQueue.poll();
                // if (executedTime <= 0)
                // break;
                if (currentProcess.remainingTime <= 0) {
                    System.out.printf("%s completed at time %d\n", currentProcess.name, currentTime);
                    completedProcesses.add(currentProcess);
                    readyQueue.remove(currentProcess);
                }

            } else {
                currentProcess.quantum = currentProcess.quantum + (currentProcess.quantum - initialQuantum); // Increase
                                                                                                             // quantum
                                                                                                             // by 2
                calculateFCAIFactor(currentProcess, V1, V2);
                readyQueue.offer(currentProcess); // Reinsert into the priority queue"
                readyQueue = sortReadyQueueByArrivalTime(readyQueue);

                System.out.printf("%s moved to the ready queue with ERROR quantum %d and FCAI factor %.2f\n",
                        currentProcess.name, currentProcess.quantum, currentProcess.fcaiFactor);
                // currentProcess = readyQueue.poll();
                if (currentProcess.remainingTime <= 0) {
                    System.out.printf("%s completed at time %d\n", currentProcess.name, currentTime);
                    completedProcesses.add(currentProcess);
                }
                // readyQueue.offer(currentProcess);
            }
            // readyQueue.offer(currentProcess);
        }

        // Print the results of the completed processes
        System.out.println("\nExecution Results:");
        for (Process c : completedProcesses) {
            System.out.println(c.name + " completed.");
        }
    }

    // Method to calculate the FCAI factor for a process based on its properties
    public static double calculateFCAIFactor(Process p, double V1, double V2) {
        return Math.ceil((10 - p.priority) + (p.arrivalTime / V1) + (p.remainingTime / V2)); // FCAI factor formula
    }

    // Function to sort the readyQueue by arrival time
    public static PriorityQueue<Process> sortReadyQueueByArrivalTime(PriorityQueue<Process> readyQueue) {
        // Check if the readyQueue is empty
        if (!readyQueue.isEmpty()) {
            // Convert PriorityQueue to a list for sorting
            List<Process> sortedList = new ArrayList<>(readyQueue);
    
            // Implement sorting using loops (Bubble Sort)
            for (int i = 0; i < sortedList.size() - 1; i++) {
                for (int j = 0; j < sortedList.size() - i - 1; j++) {
                    // Compare the arrival times of adjacent processes
                    if (sortedList.get(j).arrivalTime > sortedList.get(j + 1).arrivalTime) {
                        // Swap if the current process has a later arrival time
                        Process temp = sortedList.get(j);
                        sortedList.set(j, sortedList.get(j + 1));
                        sortedList.set(j + 1, temp);
                    }
                }
            }
    
            // Clear the original queue and add all elements back in sorted order
            readyQueue.clear();
            readyQueue.addAll(sortedList);
    
            return readyQueue;
        } else {
            // Return null if the readyQueue is empty
            return null;
        }
    }
    
    public static double getLeastFcaiFactorProcess(Queue<Process> readyQueue) {

        // Start with the first process as the one with the least FCAI factor
        Process leastFcaiProcess = null;
        double minFcai = Double.MAX_VALUE;
        // double flag=0;

        // Iterate through the queue to find the process with the smallest FCAI factor
        for (Process p : readyQueue) {
            if (p.fcaiFactor < minFcai) {
                minFcai = p.fcaiFactor;
                leastFcaiProcess = p;
            }
        }
        if (leastFcaiProcess != null) {
            Math.ceil(leastFcaiProcess.fcaiFactor);
            return leastFcaiProcess.fcaiFactor;
        } else
            return 100; // Return the process with the least FCAI factor
    }

    // Function to get the process with the least arrival time from the readyQueue
    public static Process getLeastArrivalTimeProcess(PriorityQueue<Process> readyQueue) {
        return readyQueue.stream()
                .min(Comparator.comparingInt(p -> p.arrivalTime))
                .orElse(null); // Return null if the queue is empty
    }

    public static Process findProcessWithLowerFcai(Queue<Process> readyQueue, double currentFcai, int currentTime) {
        return readyQueue.stream()
                .filter(p -> p.fcaiFactor < currentFcai && p.arrivalTime <= currentTime) // Filter processes with lower
                                                                                         // FCAI factor
                .min(Comparator.comparingDouble(p -> p.fcaiFactor)) // Find the process with the lowest FCAI factor
                .orElse(null); // Return null if no such process exists
    }

}

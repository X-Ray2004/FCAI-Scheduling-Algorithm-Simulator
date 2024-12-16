# FCAI Scheduling Algorithm Simulator

This repository contains the implementation of the **FCAI Scheduling Algorithm**, a custom CPU scheduling strategy that prioritizes tasks based on their **FCAI factor** (a formula combining priority, arrival time, and remaining burst time). The goal is to provide an efficient and adaptive CPU scheduling mechanism tailored for dynamic multitasking environments.

## Key Features

- **Custom Scheduling Algorithm**: The FCAI factor balances:
  - Process priority.
  - Arrival time normalization.
  - Remaining burst time.
- **Dynamic Quantum Adjustment**: Each process's quantum adjusts adaptively during execution to optimize CPU utilization.
- **Ready Queue Management**: A priority queue handles process selection dynamically based on the recalculated FCAI factor.
- **Support for Preemption**: Processes can be preempted when a newly arrived process has a lower FCAI factor.
- **Simulation Output**: Provides detailed logs of execution, including:
  - Process selection.
  - Quantum adjustments.
  - Completion times.

## FCAI Factor Formula

The **FCAI Factor** for a process is calculated as:

```
FCAI Factor = (10 - Priority) + (Arrival Time / V1) + (Remaining Burst Time / V2)
```

Where:
- **Priority**: Inverse priority (higher number = lower priority).
- **Arrival Time / V1**: Normalized by dividing by the maximum arrival time.
- **Remaining Burst Time / V2**: Normalized by dividing by the maximum burst time.

## How It Works

1. **Initialization**:
   - Input processes with attributes: `Name`, `Arrival Time`, `Burst Time`, `Priority`, and `Quantum`.
   - Calculate normalization constants `V1` and `V2` based on the maximum arrival time and burst time.

2. **Ready Queue Management**:
   - Processes are added to the ready queue as they arrive.
   - The queue is sorted by FCAI factor (lowest first).

3. **Execution**:
   - Select the process with the lowest FCAI factor.
   - Execute the process for 40% of its quantum or until preempted.
   - Recalculate FCAI factor after execution.

4. **Dynamic Quantum Adjustment**:
   - Increase quantum by 2 if the process is not preempted.
   - Reinstate the process in the ready queue if not completed.

5. **Completion**:
   - Once a process finishes, it's moved to the completed processes list with its total execution time logged.

## Input Example

```java
List<Process> processes = new ArrayList<>();
processes.add(new Process("P1", 0, 17, 4, 4));
processes.add(new Process("P2", 3, 6, 9, 3));
processes.add(new Process("P3", 4, 10, 3, 5));
processes.add(new Process("P4", 29, 4, 8, 2));
```

### Process Attributes:
- **Name**: Identifier of the process (e.g., P1, P2).
- **Arrival Time**: The time at which the process arrives in the ready queue.
- **Burst Time**: Total time required for execution.
- **Priority**: Priority of the process (lower values indicate higher priority).
- **Quantum**: Initial time slice allocated to the process.

## Output Example

The program logs detailed execution steps, such as:

```plaintext
Time 0: P1 selected for execution (FCAI Factor: 10.0)
P1 moved to the ready queue with updated quantum 6 and FCAI factor 12.0 (Remaining Time: 10)
Time 3: P2 selected for execution (FCAI Factor: 9.5)
P2 completed at time 6
...
Execution Results:
P1 completed.
P2 completed.
P3 completed.
P4 completed.
```

## Project Structure

```
├── src/
│   ├── Process.java     # Class representing a process
│   ├── FCAIScheduling.java  # Main simulation logic
├── README.md            # Project documentation
```

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/fcai-scheduling.git
   cd fcai-scheduling
   ```
2. Compile and run the code:
   ```bash
   javac code.java
   java code
   ```

## Future Enhancements

- **Support for Multi-Core Scheduling**: Extend the algorithm for multi-core processors.
- **GUI Simulation**: Add a graphical interface for better visualization of scheduling behavior.
- **Performance Metrics**: Incorporate metrics like average turnaround time, waiting time, and CPU utilization.

## Contributions

Contributions are welcome! Please fork the repository and submit a pull request with your changes. For major changes, open an issue first to discuss the proposed changes.

## License

This project is licensed under the MIT License. See `LICENSE` for more details.

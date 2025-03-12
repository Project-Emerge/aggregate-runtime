# Aggregate Computing Tool Demonstrator

This is a demonstrator used to coordinate a team of robots using aggregate computing (in particular [ScaFi](https://github.com/scafi/scafi)).

This demonstrator contains:
- The general abstractions needed to create the demonstrator, in the package: it/unibo/core
- One instantiation of the pipeline for the Researchers' Night Demo, in it/unibo/demo
- A demo using JavaFX to verify effective operation in simulation in advance.

In the following section are some images collected during the Researchers' Night.

<div style="display: flex; justify-content: space-between; width: 100%;">
  <img src="https://github.com/user-attachments/assets/cee46e68-bff8-491a-bb00-1a9b423e7c1a" width="49%" alt="setup">
  <img src="https://github.com/user-attachments/assets/991528f2-33d1-4599-a113-3a0090e179a3" width="49%" alt="step-1-recover">
</div>
<img src="https://github.com/user-attachments/assets/fd9ef5ad-2ec7-4d16-a367-72c044d2ce1f" width="100%" alt="step-3">

## Project structure

The project is structured as follows:

- `src`: Contains the source code of the project.
    - `main`: Contains the main source code.
        - `scala`: Contains the Scala code.
            - `it/unibo`: Base package for the project's Scala code.
                - `core`: Contains the general abstractions needed for the demonstrator.
                - `demo`: Contains an instantiation of the pipeline for the Researchers' Night Demo.
                - `mock`: Contains mock implementations for testing and simulation.
                - `checker`: Contains code for checking properties of the system.
                - `utils`: Contains utility classes.
        - `java`: Contains the Java code.
            - `it/unibo`: Base package for the project's Java code.
                - `artificial_vision_tracking`: Contains code related to artificial vision tracking.
                    - `aruco_markers`: Contains code for detecting Aruco markers.
        - `resources`: Contains resources such as configuration files.
            - `calibration`: Contains calibration files.
- `python`: Contains Python scripts.
    - `calibration.py`: Python script for camera calibration.
    - `get-frames.py`: Python script to extract frames.
    - `calibration_matrix.yaml`: YAML file containing calibration matrix.
    - `frame-*.jpg`: Image frames used for calibration.

## How to run the demonstrator

This section explains how to run the demonstrator in simulation mode.

### Requirements
- sbt (Scala Build Tool) >= 1.10.1
- Java >= 8

### Launch the demonstrator
The following commands simulate the scenarios demonstrated during the Researchers' Night:

```bash
# Simulate robots forming a line
sbt "runMain it.unibo.mock.LineFormationDemo"

# Simulate robots forming a circle
sbt "runMain it.unibo.mock.CircleFormationDemo"
```

Each simulation will:
1. Open a window showing robots moving in real-time
2. Display the formation process towards the desired shape (line or circle)
3. Simulate a network failure by disconnecting 4 robots
4. Demonstrate the self-healing capability as remaining robots recover the formation

### Screenshots
![base](https://github.com/user-attachments/assets/1e31d297-b490-4774-842e-99c12d16c2c2) ![after](https://github.com/user-attachments/assets/5d735927-38ad-4a56-9794-167bdddf079d)


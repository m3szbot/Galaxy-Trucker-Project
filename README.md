# Project Overview

A LAN-based multiplayer implementation of the strategy board game Galaxy Trucker, built in Java using a fat server-thin client architecture and the Model-View-Controller (MVC) design pattern.

This project was developed as part of a group project (4 members) between February 2025 – June 2025 for the Software Engineering course at the Politecnico di Milano.

The game supports both Text-based UI (TUI) and Graphical UI (GUI) using JavaFX, along with TCP/IP socket and RMI protocols for client-server communication. The game is unplayable in its uploaded state, as the image resources are under copyright and have been removed from the repo. TUI gameplay screenshots are provided in the screenshots folder.

## Tech Stack

- Language: Java

- IDE: IntelliJ IDEA

- Version Control: Git & GitHub

- Build & Dependency Management: Maven

- Testing: JUnit

- Design & Architecture: UML, MVC

- Protocols: TCP/IP & RMI

- UI Options: Terminal-based TUI & JavaFX GUI

 ## Features

- LAN Multiplayer Support – Play with friends over a local network.

- Dual Protocols – Choose between TCP/IP sockets or RMI for communication.

- Multiple Parallel Games – The server supports several games simultaneously.

- Centralized Game Logic – The server handles all game logic, sending only view updates to clients.

- Flexible Interfaces – Players can choose between TUI and GUI.

- In-Game Chat – Each game session includes its own chat for players.

---

 # Group members

 Carlo Maggi, Ludovico Meloni, Giacomo Merlo, Botond Meszaros.

 # Implemented functionalities

 Complete rule set + TUI + GUI + RMI + Socket + 2 AF

 The 2 AF (advanced functionalities) are:

 1) Test flight
 2) Multiple games

In addition to those AF, we have implmented 2 extra functionalities not indicated in the project specification:

1) The possibility to send a message to a specific player inside a game (private message).
2) The possibility to send a message to all the players inside a game (public message).

# Instructions to execute the jar

Before continuing with the instructions please make sure that you have a recent version of openJDK installed on your system. Versions 23.0.* upwards should be fine.

### Step 1) Download the zip file containing the jar files

Go to the github main branch. Go to the directory 'deliverables/final/jar' and download the zip file.

### Step 2) Unzip the zip file

Go to your system download directory and unzip the zip file that you just downloaded.

### Step 3) Execute the server

Open the unziped file. Go to the directory 'GalaxyTruckerServer/Files'. Open a terminal in the directory and execute the command 'java -jar Server.jar'.
The server should now be running.

### Step 4) Execute the client

Open the unziped file. Go to the directory 'GalaxyTruckerClient/Files. Open a terminal in the directory. Now, depending on your OS do the following:

Windows: execute the command '.\run-client-windows.bat'.

MacOS: execute the command 'chmod +x run-client-macos.sh' to make sure you have the permission to execute the file. Then execute the command './run-client-macos.sh'.

Linux: execute the command 'chmod +x run-client-linux.sh' to make sure you have the permission to execute the file. Then execute the command './run-client-linux.sh'.

The client should now be running.

You can now start playing. 

# Test coverage percentage

### Model

Class: 100% (36/36) -- Method: 91% (304/332) -- Line: 92% (1332/1440) -- Branch: 85% (750/875)

### Controller

Class: 78% (39/50) -- Method: 71% (156/219) -- Line: 45% (863/1907) -- Branch: 35% (294/832)




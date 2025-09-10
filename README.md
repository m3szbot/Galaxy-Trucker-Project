# Galaxy Trucker Multiplayer Game

## 🧾 Project Overview

A **LAN-based multiplayer** implementation of the strategy board game **Galaxy Trucker**, built in **Java** using a **fat server-thin client** architecture and the **Model-View-Controller (MVC)** design pattern.

This project was developed as part of a **group project** (4 members) between **February 2025 – June 2025** for the **Software Engineering course** at **Politecnico di Milano**.

The game supports both **Text-based UI (TUI)** and **Graphical UI (GUI)** using **JavaFX**, along with **TCP/IP socket** and **RMI** protocols for client-server communication.

> ⚠️ **Note:** The game is **unplayable in its uploaded state**, as the image resources are under copyright and have been removed from the repository.  
> TUI gameplay screenshots are available in the **`/screenshots`** folder.

---

## 🛠 Tech Stack

- **Language:** Java  
- **IDE:** IntelliJ IDEA  
- **Version Control:** Git & GitHub  
- **Build & Dependency Management:** Maven  
- **Testing:** JUnit  
- **Design & Architecture:** UML, MVC  
- **Protocols:** TCP/IP & RMI  
- **UI Options:** Terminal-based TUI & JavaFX GUI

---

## 🚀 Features

- **LAN Multiplayer Support** – Play with friends over a local network.  
- **Dual Protocols** – Choose between **TCP/IP sockets** or **RMI** for communication.  
- **Multiple Parallel Games** – The server supports several games simultaneously.  
- **Centralized Game Logic** – The server handles **all game rules** and sends only view updates to clients.  
- **Flexible Interfaces** – Players can choose between **TUI** and **GUI**.  
- **In-Game Chat** – Each game session includes its own dedicated chat.

---

## 👥 Group Members

- Carlo Maggi  
- Ludovico Meloni  
- Giacomo Merlo  
- Botond Meszaros  

---

## ✅ Implemented Functionalities

- **Complete Rule Set**  
- **TUI (Text-Based UI)**  
- **GUI (JavaFX)**  
- **RMI Support**  
- **Socket Support**  
- **2 Advanced Functionalities (AF):**  
  1. **Test Flight**  
  2. **Multiple Games**

Additionally, we implemented **two extra functionalities** beyond the project specifications:

1. Ability to **send private messages** to specific players within a game.  
2. Ability to **send public messages** to all players within a game.

---

## ▶️ Instructions to Execute the JAR

Before proceeding, ensure you have a **recent version of OpenJDK** installed.  
**Recommended:** JDK **23.0.\*** or later.

### **Step 1 — Download the ZIP File**

- Navigate to the **GitHub main branch**.  
- Go to the directory: `deliverables/final/jar`  
- Download the provided **ZIP** file.

### **Step 2 — Unzip the File**

- Locate the downloaded ZIP file in your **Downloads** folder.  
- Extract it to your preferred location.


### **Step 3 — Start the Server**

- Open the unzipped folder.  
- Navigate to: `GalaxyTruckerServer/Files`  
- Open a terminal in this directory and run:

```bash
java -jar Server.jar
```
The server should now be **running** and ready to accept client connections.

### **Step 4 — Start the Client**

- Open the unzipped folder.  
- Navigate to: `GalaxyTruckerClient/Files`  
- Open a terminal in this directory.

Depending on your operating system, run:

#### **Windows**
```bash
.\run-client-windows.bat

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






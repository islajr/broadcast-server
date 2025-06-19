# Broadcast Server

This is a simple TCP broadcast server that allows clients to connect to it and send messages that will be broadcasted to all connected clients.

It makes use of sockets from the [java.net](https://docs.oracle.com/javase/8/docs/api/java/net/package-summary.html) package and good old IO programming.

## Installation

### Pre-requisites:
- Java 8+

If you don't have java installed, follow [this](https://www.java.com/en/download/manual.jsp) link depending on the specs of your machine.

To use this application, follow the following steps:

#### **1. Clone this repository**
    
The command below downloads a local copy of this codebase to your local computer:

    git clone https://github.com/islajr/broadcast-server


#### **2. Navigate to the source folder**

While within the download directory, use `cd broadcast-server/src` to get to the directory containing the source code.

## Usage

### **Start a Broadcast Server**

To start a server, use the command:

    broadcast-server start -p <port>

This starts a server on the specified port, and it automatically starts to listen for connections.

### **Connect to the server as a client**

To connect to a server, use:

    broadcast-server connect -p <port>

This attempts to connect to a listening broadcast server on the specified port.

### **Disconnect a client from the server**

To disconnect a client from a server, send **"exit"** as a message from the intended client. This will effectively disconnect and shut down the client from operations.

### **Shut down the server**

To terminate the operation and shut down the server, disconnect all connected clients. 
This action will cause the server to prompt you for a disconnection decision.

When confronted with the message, although it is self-explanatory, press 'y' or 'yes' to shut down the server.


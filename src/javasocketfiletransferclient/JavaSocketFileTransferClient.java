/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasocketfiletransferclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author 30039802 Caspian Maclean
 *
 * Question 7 – JMC needs a way to transfer data files (mainly CSV) using
 * sockets in a client-server application.
 *
 * This is the client program.
 *
 * Socket connection code and user input-reading code based on provided example
 * "ClientServerDemo"
 */
public class JavaSocketFileTransferClient {

static Socket socket;
	static DataOutputStream outStream; // output stream to server
	static DataInputStream inStream; // input stream from server

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.println("Incorrect arguments used!");
			System.out.println("Usage: java -jar \"JavaSocketFileTransferClient.jar\" hostName port# download filename");
			System.out.println("or:    java -jar \"JavaSocketFileTransferClient.jar\" hostName port# upload filename");
			System.out.println("e.g.:  java -jar \"JavaSocketFileTransferClient.jar\" hostName port# upload filename");
			System.exit(1);
		}

		String host = args[0]; // get host
		int port = Integer.valueOf(args[1]).intValue(); // get port #
                String commandString = args[2];
                String filename = args[3];
                boolean commandUpload = false;
                
                if (commandString.equals("upload")) {
                    commandUpload = true;
                } else if (commandString.equals("download")) {
                    commandUpload = false;
                } else {
                        System.out.println("Unrecognised command '" + commandString + "'");
			System.out.println("Must be upload or download");
			System.exit(1);
                }
               
                
		try {
			// create socket for connection
			socket = new Socket(host, port);

			// get input/output streams
			inStream = new DataInputStream(socket.getInputStream());
			outStream = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println(e);
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}

		chat(); // perform chatting loop

		try { // close resources
			socket.close();
			inStream.close();
			outStream.close();
		} catch (IOException e) {
			// handle exception here
		}
	} // main

	public static void chat() {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			try {
				String lineInput = sc.nextLine();

				if (lineInput.length() > 0) { // if client types something
					outStream.writeBytes(lineInput); // send message to server
					outStream.write(13); // carriage return
					outStream.write(10); // line feed
					outStream.flush(); // flush the stream line

					if (lineInput.equalsIgnoreCase("quit")) {
						System.exit(0); // stop client chatting as well.
					}

					// print any message received from server
					int inByte;
					System.out.print("Server>>> ");
					while ((inByte = inStream.read()) != '\n') {
						System.out.write(inByte);
					}
					System.out.println();
				}
			} catch (IOException e) {
				// handle IO exception here
			}
		}
	}

}

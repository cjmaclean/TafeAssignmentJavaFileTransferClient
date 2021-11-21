/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasocketfiletransferclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        String fileName = args[3];
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

        try {
            outStream.writeBoolean(commandUpload);
            outStream.writeUTF(fileName);
        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(1);
        }

        if (commandUpload) {
            System.out.println("Trying '" + commandString + "'");
            try {
                FileInputStream fileInputStream = new FileInputStream(fileName);
                upload(fileInputStream);

            } catch (FileNotFoundException ex) {
                // Show message that the file isn't found
                System.out.println(ex);
                System.exit(1);
            }
        } else {
            System.out.println("not yet implemented '" + commandString + "'");
        }

        try { // close resources
            socket.close();
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            // handle exception here
        }
    } // main

    private static void upload(FileInputStream fileInputStream) {
        int count;
        byte[] buffer = new byte[1024];
        try {
            while ((count = fileInputStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, count);
            }
        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(1);
        }
    }

}


/*
 The MIT License

 Copyright (c) 2013 - 2013
 1. High Performance Computing Group, 
 School of Electrical Engineering and Computer Science (SEECS), 
 National University of Sciences and Technology (NUST)
 2. Khurram Shahzad, Mohsan Jameel, Aamir Shafi, Bryan Carpenter (2013 - 2013)
   

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be included
 in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
/*
 * File         : IOMessagesThread.java 
 * Author       : Khurram Shahzad, Mohsan Jameel, Aamir Shafi, Bryan Carpenter
 * Created      : Oct 28, 2013
 * Revision     : $
 * Updated      : Nov 05, 2013 
 */
package runtime.starter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

//test
import runtime.starter.MPJRun;

public class IOMessagesThread extends Thread {

    Socket clientSock;

    public IOMessagesThread(Socket clientSock) {
        this.clientSock = clientSock;
    }

    @Override
    public void run() {
        serverSocketInit();
    }

    private void serverSocketInit() {
        Scanner input = null;
        PrintWriter output = null;
        try {
            input = new Scanner(clientSock.getInputStream());
            output = new PrintWriter(clientSock.getOutputStream(), true);
            String message = input.nextLine();
            while (!(message.endsWith("EXIT"))) {
                //ArrayList<String> job = MPJRun.executedJob.put(device, className);
                if (message.startsWith("Dilmun")) {
                    // retrive device name and length of executed job
                    String[] parts = message.split(" , ");
                    String device = parts[1];
                    //String command = parts[2];
                    String job = parts[2];
                    String timeType = parts[3];
                    String time = parts[4];

                    // String placeHolder = parts[3];
                    String jobInfo[] = new String[4];
                    if (timeType.equals("startTime")) {
                        String processCounter = parts[5];
                        if (MPJRun.executedJob.get(device) == null) {
                            jobInfo[0] = job;
                            jobInfo[1] = time;
                            jobInfo[2] = "Running";
                            jobInfo[3] = processCounter;

                            MPJRun.executedJob.put(device, jobInfo);

                        }

                  // System.out.println("start: "+MPJRun.executedJob); 
                        //  System.out.println("start: job info"+jobInfo[0]+" "+jobInfo[1]+" "+jobInfo[2]);
                    } else if (timeType.equals("endTime")) {
                        jobInfo = MPJRun.executedJob.get(device);

                        //System.out.println("end: job info"+jobInfo[0]+" "+jobInfo[1]+" "+jobInfo[2]);
                        int counter = Integer.parseInt(jobInfo[3]);
                        counter--;
                        if (counter == 0) {
                            double length = Double.parseDouble(time) - Double.parseDouble(jobInfo[1]);
                            jobInfo[2] = String.format("%.4f", length);
                        }
                        jobInfo[3] = "" + counter;
                        MPJRun.executedJob.put(device, jobInfo);
                        System.out.println("end: job info" + jobInfo[0] + " " + jobInfo[1] + " " + jobInfo[2] + " " + jobInfo[3]);
                        // System.out.println("end: "+MPJRun.executedJob);
                    }
                 //  if (message.startsWith("job")) {
                    //MPJRun.executedJob.put(device, job);
                    //  }
                    //MPJRun.executedJob.put(device, length);
                } else if (!message.startsWith("@Ping#")) {
                    System.out.println(message);
                }
                message = input.nextLine();

            }
            if (message.endsWith("EXIT")) {
                //System.out.println(MPJRun.executedJob);
            }

        } catch (Exception cce) {
            cce.printStackTrace();
        } finally {
            try {
                clientSock.close();
                input.close();
                output.close();
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
    }

}

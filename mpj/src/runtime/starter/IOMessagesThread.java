
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
        double length = 0.0;
        String device = "";
        String job = "";
        String timeType = "";
        String time = "";
        String jobInfo[] = new String[4];
        try {
            input = new Scanner(clientSock.getInputStream());
            output = new PrintWriter(clientSock.getOutputStream(), true);
            String message = input.nextLine();
            
            while (!(message.endsWith("EXIT"))) {
         
                //ArrayList<String> job = MPJRun.executedJob.put(device, className);
                if (message.startsWith("Dilmun")) {
        
                    // retrive device name and length of executed job
                    String[] parts = message.split(" , ");
                    device = parts[1];
                    //String command = parts[2];
                    job = parts[2];
                    timeType = parts[3];
                    time = parts[4];
                    // String placeHolder = parts[3];
                    //String jobInfo[] = new String[4];
                    if (timeType.equals("startTime")) {
                        String processCounter = parts[5];
                        if (MPJRun.executedJob.get(device) == null) {
                            jobInfo[0] = job;
                            jobInfo[1] = time;
                            jobInfo[2] = "Running";
                            jobInfo[3] = processCounter;

                            MPJRun.executedJob.put(device, jobInfo);
                            writeToFile(device, jobInfo[0], jobInfo[2]);
                        }
                        // System.out.println("start: "+MPJRun.executedJob); 
                        //  System.out.println("start: job info"+jobInfo[0]+" "+jobInfo[1]+" "+jobInfo[2]);
                    } else if (timeType.equals("endTime")) {
                        jobInfo = MPJRun.executedJob.get(device);

                        //System.out.println("end: job info"+jobInfo[0]+" "+jobInfo[1]+" "+jobInfo[2]);
                        int counter = Integer.parseInt(jobInfo[3]);
                        counter--;
                        if (counter == 0) {
                            length = Double.parseDouble(time) - Double.parseDouble(jobInfo[1]);
                            jobInfo[2] = String.format("%.4f", length);
                                                    writeToFile(device, jobInfo[0], jobInfo[2]);

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

                    //   System.out.println(executedJob);
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

    public void writeToFile(String device, String name, String length) {

        // write the information of job in a text file
        File f = new File("test.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
                FileWriter writer = new FileWriter(f, true);
                writer.write("Device name: \n");
                writer.write(device + "\n");
                writer.write("Name of job: \n");
                writer.write(name + "\n");
                //writer.write(job + "\n");
                writer.write("Job status : \n");
                if (length == "Running") {
                    writer.write("Running \n");
                } else {
                    writer.write("Executed \nLength = \n");
                    writer.write(length + "\n");
                }
                //writer.write(length + "\n");
                writer.write("---------- \n");
                writer.close();

                //if file is already exist
            } else {
                FileWriter writer = new FileWriter(f, true);
                writer.write("Device name: \n");
                writer.write(device + "\n");
                writer.write("Name of job: \n");
                writer.write(name + "\n");
                //writer.write(job + "\n");
                writer.write("Job status : \n");
                if (length == "Running") {
                    writer.write("Running \n");
                } else {
                    writer.write("Executed \nLength = \n");
                    writer.write(length + "\n");
                }
                //writer.write(length + "\n");
                writer.write("---------- \n");
                writer.close();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //read the file and calculate the average
        /*double total = 0.0;
        double d;
        BufferedReader reader;
        int number_of_E_jobs = 0;
        try {
            reader = new BufferedReader(new FileReader("test.txt"));
            String line = reader.readLine();
            while (line != null) {
                try {
                    d = Double.valueOf(line);
                    total += d;
                    number_of_E_jobs = number_of_E_jobs + 1;

                } catch (NumberFormatException e) {
                }
                line = reader.readLine();
            }
            FileWriter writer2 = new FileWriter(f, true);
            writer2.write("AVG Length = " + total / number_of_E_jobs + "\n");
            if (length == "Running") {
                writer2.write("Name of running job now: " + name + "\n");
            }
            writer2.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        File inputFile = new File("test.txt");
        File tempFile = new File("myTempFile.txt");
        BufferedReader reader6 = null;
        try {
            reader6 = new BufferedReader(new FileReader(inputFile));
        } catch (FileNotFoundException e2) {
            // e2.printStackTrace();
        }
        BufferedWriter writer6 = null;
        try {
            writer6 = new BufferedWriter(new FileWriter(tempFile));
        } catch (IOException e1) {
            // e1.printStackTrace();
        }
        String currentLine;

        try {
            while ((currentLine = reader6.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.contains("AVG Length = ")) {
                    continue;
                } else if (trimmedLine.contains("Name of running job now: ")) {
                    continue;
                }
                try {
                    writer6.write(currentLine + "\n");
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
            writer6.write("AVG Length = " + total / number_of_E_jobs + "\n");
            if (length == "Running") {
                writer6.write("Name of running job now: " + name + "\n");
            }
            writer6.close();
            reader6.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }

        boolean rename = tempFile.renameTo(inputFile);*/

    }

}

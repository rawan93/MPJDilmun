package runtime.daemonmanager;
import java.io.IOException;
import java.lang.management.*;
import java.io.*;
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
 * File         : PMThread.java 
 * Author       : Khurram Shahzad, Mohsan Jameel, Aamir Shafi, Bryan Carpenter
 * Created      : January 30, 2013 6:00:57 PM 2013
 * Revision     : $
 * Updated      : $
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

//test 
import java.net.Socket;
import java.net.ServerSocket;


import runtime.common.MPJUtil;
import runtime.common.RTConstants;

public class BootThread extends DMThread {

  private String host = "";
  private String port = "";
  ProcessBuilder pb = null;
  private String msg="";
  


  public BootThread(String machineName, String daemonPort) {
    host = machineName;
    port = daemonPort;
  }

  public void run() {
    try {
      bootNetWorkMachines();
      //dataSocket();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void bootNetWorkMachines() throws IOException {
    long tid = Thread.currentThread().getId(); 

    if (validExecutionParams()) {
      try {
      InetAddress localAddress = InetAddress.getLocalHost();
      //String masterAddress = localAddress.getHostAddress();
      String masterAddress = localAddress.toString();
      String masterName = localAddress.getHostName();
      //System.out.println(masterName);
	String[] command = { "ssh", host, "java", "-cp",
	    MPJUtil.getJarPath("daemon") + ":.", "runtime.daemon.MPJDaemon",
	    port, masterName //masterAddress
	};

	ArrayList<String> consoleMessages = 
			DaemonUtil.runProcess(command, false);
	String pid = DaemonUtil.getMPJProcessID(host);

	if(MPJDaemonManager.DEBUG)
          System.out.println("BootThread.run: tid ="+tid+", pid ="+pid);
					   
	if (!pid.equals("") && Integer.parseInt(pid) > -1) {
	  System.out.println(MPJUtil.FormatMessage(host,
	      DMMessages.MPJDAEMON_STARTED + pid));
       dataSocket();

	  //test
	  writeMessage(true, pid);
	  //msg = MPJUtil.FormatMachineMessage(host,"MPJ Daemon is running", pid, port);
	  //DMThreadUtil.numberOfDaemons++;
	  
	  
     } else {
	  System.out.println(MPJUtil.FormatMessage(host,
	      DMMessages.MPJDAEMON_NOT_STARTED + pid));  
	  for (String message : consoleMessages) //leaving here for legacy 
	    System.out.println(message); // reasons .. this does not make sense
	}
      } catch (Exception ex) {
	ex.printStackTrace();
      }

    } 
  }

  private boolean validExecutionParams() {

    String pid = DaemonUtil.getMPJProcessID(host);
    
    if (!pid.equals("")) {
    System.out.println(MPJUtil.FormatMessage(host,
    DMMessages.MPJDAEMON_ALREADY_RUNNING + pid));
   
    
    
    writeMessage(true, pid);
      return false;
    }
    InetAddress address = null;
    try {

      address = InetAddress.getByName(host);
    }
    catch (UnknownHostException e) {
    writeMessage(false, "");
      e.printStackTrace();
      System.out.println(e.getMessage());
      return false;
    }
    if (MPJUtil.IsBusy(address, Integer.parseInt(port))) {
      System.out.println(MPJUtil.FormatMessage(host, DMMessages.BUSY_PORT));
      return false;
    }
  
  
        
      return true;
  }//end function
  
  
    
//Data socket 
 public void dataSocket(){
  ServerSocket myService = null;
    try {
       myService = new ServerSocket(Integer.parseInt(RTConstants.MPJ_RUN_SERVER_PORT));
        }
    catch (IOException e) {
      System.out.println(e);
    }
     Socket serviceSocket = null;
    try {
       serviceSocket = myService.accept();
        }
    catch (IOException e) {
       System.out.println(e);
    }
     DataInputStream input= null;
    try {
       input = new DataInputStream(serviceSocket.getInputStream());
       String line = input.readLine();
        while (!(line.endsWith("EXIT"))) {
        	if (line.startsWith("CPU")){
             	System.out.println("socket line: "+line); 
             	//String fileName = RTConstants.MPJ_Dilmun_DIR + "/CPUinfo.txt";
             	String fileName = RTConstants.MPJ_HOME_DIR + "/Dilmun_REST/CPUinfo.txt";
             	String[] parts = line.split(" , ");
             	try {
             		File f = new File (fileName);
             		if (!f.exists()) {
             			f.createNewFile();
             			FileWriter fileWriter =new FileWriter(f, true);
      					fileWriter.write(parts[1]+"\n");
						fileWriter.write(parts[2]+"\n");
						fileWriter.write(parts[3]+"\n");
						fileWriter.write(parts[4]+"\n");
						fileWriter.write(parts[5]+"\n");
						fileWriter.write(parts[6]+"\n");
      					fileWriter.close();
             		} else {
                		FileWriter fileWriter = new FileWriter(f, true);
                		fileWriter.write(parts[1]+"\n");
						fileWriter.write(parts[2]+"\n");
						fileWriter.write(parts[3]+"\n");
						fileWriter.write(parts[4]+"\n");
						fileWriter.write(parts[5]+"\n");
						fileWriter.write(parts[6]+"\n");
      					fileWriter.close();
                		
                	}
        			/*FileWriter fileWriter =new FileWriter(fileName);
      				BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
      				bufferedWriter.write(parts[1]+"\n");
					bufferedWriter.write(parts[2]+"\n");
					bufferedWriter.write(parts[3]+"\n");
					bufferedWriter.write(parts[4]+"\n");
					bufferedWriter.write(parts[5]+"\n");
      				bufferedWriter.close();*/
    			} catch(IOException ex) {
      				System.out.println("Error writing to file '"+ fileName + "'");
      		  } //end write in the text file
            }
            line = input.readLine();
       }
       
    }
    catch (IOException e) {
       System.out.println(e);
    }
    
    
     try {
       input.close();
       serviceSocket.close();
       myService.close();
    } 
    catch (IOException e) {
       System.out.println(e);
    }
    
  }
    
  
  public void writeMessage(boolean availability, String pid){
  	if (availability == true){
  		msg = MPJUtil.FormatMachineMessage(host,"MPJ Daemon is running", pid, port);
  		DMThreadUtil.numberOfDaemons++;

  	} 
    else if (availability == false){
  		msg = MPJUtil.FormatMachineMessage(host,DMMessages.MPJDAEMON_NOT_AVAILABLE, pid, "");
  		//DMThreadUtil.numberOfDaemons++;
  	}
  	DMThreadUtil.machineMsgList.add(msg);
  }//end writeMessage method 
}// end BootThread class 


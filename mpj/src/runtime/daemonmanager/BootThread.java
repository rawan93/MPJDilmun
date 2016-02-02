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

import runtime.common.MPJUtil;

public class BootThread extends DMThread {

  private String host = "";
  private String port = "";
  ProcessBuilder pb = null;
  private String msg="";
  /*Aisha*/
  private int numcpu=1;
  private double load=0;
  private double total=0;
  private double free=0;
  private double used=0;


  public BootThread(String machineName, String daemonPort) {
    host = machineName;
    port = daemonPort;
  
    

        

        
    

  }

  public void run() {
    try {
      bootNetWorkMachines();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void bootNetWorkMachines() throws IOException {

    long tid = Thread.currentThread().getId(); 

    if (validExecutionParams()) {
      try {
	String[] command = { "ssh", host, "java", "-cp",
	    MPJUtil.getJarPath("daemon") + ":.", "runtime.daemon.MPJDaemon",
	    port,
	};

	ArrayList<String> consoleMessages = 
			DaemonUtil.runProcess(command, false);
	String pid = DaemonUtil.getMPJProcessID(host);

	if(MPJDaemonManager.DEBUG)
          System.out.println("BootThread.run: tid ="+tid+", pid ="+pid);
					   
	if (!pid.equals("") && Integer.parseInt(pid) > -1) {
	  System.out.println(numcpu+MPJUtil.FormatMessage(host,
	      DMMessages.MPJDAEMON_STARTED + pid));
       

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
    //Aisha
    String num=DaemonUtil.getCPUnum(host);
    String tot=DaemonUtil.gettotalmem(host);
    if (!pid.equals("")) {
      System.out.println(MPJUtil.FormatMessage(host,
	  DMMessages.MPJDAEMON_ALREADY_RUNNING + pid));
    System.out.println("new number of CPUs"+num); 
    System.out.println("new total memory "+tot); 
    
    
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
   //Aisha write in the text file
    String fileName = "info.txt";

        try {
           
            FileWriter fileWriter =
                new FileWriter(fileName);

           
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

            bufferedWriter.write("new number of CPUs"+num);
            bufferedWriter.newLine();
            bufferedWriter.write("new total memory "+tot);
          

            /
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            
        }
        //end file
    
  }

  public void writeMessage(boolean availability, String pid){
  	if (availability == true){
  		msg = MPJUtil.FormatMachineMessage(host,"MPJ Daemon is running", pid, port);
  		DMThreadUtil.numberOfDaemons++;

  	} else if (availability == false){
  		msg = MPJUtil.FormatMachineMessage(host,DMMessages.MPJDAEMON_NOT_AVAILABLE, pid, "");
  		DMThreadUtil.numberOfDaemons++;
  	}
  	DMThreadUtil.machineMsgList.add(msg);
  }
}

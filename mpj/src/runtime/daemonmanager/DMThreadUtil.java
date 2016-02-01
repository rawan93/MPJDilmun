/*
 The MIT License

 Copyright (c) 2013 - 2014
   1. High Performance Computing Group, 
   School of Electrical Engineering and Computer Science (SEECS), 
   National University of Sciences and Technology (NUST)
   2. Khurram Shahzad, Mohsan Jameel, Aamir Shafi, Bryan Carpenter (2014)
   
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
 * File         : PMThreadUtil.java 
 * Author(s)    : Khurram Shahzad, Mohsan Jameel, Aamir Shafi, 
 *    		  Bryan Carpenter, Farrukh Khan
 * Created      : Jan 30, 2013
 * Revision     : $
 * Updated      : Aug 27, 2014
 */

package runtime.daemonmanager;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import runtime.daemonmanager.CLOptions;
import runtime.daemonmanager.DMThread;
import runtime.common.MPJUtil;

public class DMThreadUtil {

 // Dilmun code 
//status of runtime
public static boolean runtimeStatus = false;

// (Daemon)Process ID 
public static String ProcessID="";
//Daemon port number 
private static int daemonPort = 0;
 // The size of the machine list 
public static int sizeOfMachineList;


//status of Cluster (array list to save the status of each daemon in the cluster)
public static ArrayList<Integer> clusterStatus = new ArrayList<Integer>();

// status of cluster (Final value)

public static boolean clusterStatusValue = false;

 // Total number of daemons.
public static int numberOfDaemons;
//end of dilmun
//test
//array list of device name and id
public static ArrayList<String> machineMsgList;
//end test
    
  public static ExecutorService getThreadExecutor(int nThreads) {
    return Executors.newFixedThreadPool(nThreads);
  }//end getThreadExecutor method 

  public static void ExecuteThreads(ArrayList<Thread> threads, int nThreads) {

    ExecutorService tpes = getThreadExecutor(nThreads);

    for (Thread thread : threads) {
      tpes.execute(thread);
    } //end for loop 

    tpes.shutdown();

    try {

      tpes.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }//end try - catch block 
  }// end ExecuteThreads method 

  public static void ExecuteCommand(CLOptions options) {

    String type = options.getCmdType();
    ArrayList<Thread> threads = new ArrayList<Thread>();
    ArrayList<String> machinesList = new ArrayList<String>();
    //test
	machineMsgList = new ArrayList<String>();
	//end test

    if (options.getMachineList().size() > 0)
      machinesList = options.getMachineList();
    else
      machinesList = MPJUtil.readMachineFile(options.getMachineFilePath());

    if (machinesList != null && machinesList.size() > 0) {

      for (String host : machinesList) {

	DMThread thread = null;

	if (type.equals(DMConstants.BOOT)) { 
	  thread = new BootThread(host, options.getPort());
	} else if (type.equals(DMConstants.HALT)) {
	  thread = new HaltThread(host);
	} else if (type.equals(DMConstants.CLEAN)) {
	  thread = new CleanUpThread(host);
	} else if (type.equals(DMConstants.STATUS)) {
	  thread = new StatusThread(host);
	/*test*/
	} else if (type.equals(DMConstants.DATA)) {
	  thread = new DataThread(host);
	/*end test*/
	} else if (type.equals(DMConstants.INFO)) {
	  thread = new ProcessInfoThread(host);
	}
	if (thread != null) {	
	  threads.add(thread);
	}

      } //end for loop 

      ExecuteThreads(threads, options.getThreadCount());
      
      // set the number of machines in the machineList file 
     
       sizeOfMachineList=machinesList.size();
      
      /*test*/
       //check if the status is required and print
     if (type.equals(DMConstants.DATA)) {
     	//status of cluster
     	//revisit cluster status (doesnt check if cluster exists without runtime)
     	 if(runtimeStatus==true)
      	{
         clusterStatusValue=true;
      	}
      	System.out.println("Cluster status: "+clusterStatusValue);
      	//status of runtime
     	System.out.println("Runtime status: "+runtimeStatus);
     	//print the total number of Daemons
      	System.out.println("Total number of Daemons: "+numberOfDaemons+".");
      	System.out.println("Machines in cluster:");
     	for (int i=0; i< machineMsgList.size(); i++){
     		System.out.println(machineMsgList.get(i));
     	}
     }
     /*end of test */
    }//end  outer if 
  }// end ExecuteCommand method 
  
}// end DMThreadUtil class 

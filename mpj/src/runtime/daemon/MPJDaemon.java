/*
 The MIT License

 Copyright (c) 2005 - 2014
   1. Distributed Systems Group, University of Portsmouth (2014)
   2. Aamir Shafi (2005 - 2014)
   3. Bryan Carpenter (2005 - 2014)
   4. Mark Baker (2005 - 2014)

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
 * File         : MPJDaemon.java 
 * Author(s)    : Aamir Shafi, Bryan Carpenter, Khurram Shahzad, 
 *		  Mohsan Jameel, Farrukh Khan
 * Created      : Dec 12, 2004
 * Revision     : $Revision: 1.29 $
 * Updated      : Aug 27, 2014
 */

package runtime.daemon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.management.*;
import java.io.*;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggerRepository;

import runtime.common.MPJRuntimeException;
import runtime.common.MPJUtil;
import runtime.common.RTConstants;

import runtime.daemonmanager.DMConstants;
import runtime.daemonmanager.MPJHalt;

import com.sun.management.OperatingSystemMXBean; 


public class MPJDaemon {

  private int D_SER_PORT;
  private int portManagerPort;
  static final boolean DEBUG = true;
  private String logLevel = "OFF";
  static Logger logger = null;
  private static String mpjHomeDir = null;
  public volatile static ConcurrentHashMap<Socket, ProcessLauncher> servSockets;

  ConnectionManager connectionManager;
  PortManagerThread pManager;

  public MPJDaemon(String args[]) throws Exception {
    System.out.println("TESSST: address sent: "+args[1]);
    //collect CPU and Memory informations
    // InetAddress localaddr = InetAddress.getLocalHost();
     //String hostName = localaddr.getHostName();
    int Num_of_CPU= Runtime.getRuntime().availableProcessors();
    //System.out.println("You have :  "+Num_of_CPU+" CPUs ");

    OperatingSystemMXBean osMBean= (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    double load = osMBean.getSystemLoadAverage();
    //System.out.println("Load Average in one minute :  "+load);

    OperatingSystemMXBean mbean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        double usage;
        for(int z=0; z<10; z++) {
            usage = mbean.getSystemCpuLoad();
            if((usage<0.0 || usage>1.0) && usage != -1.0) 
            {
                throw new RuntimeException("getProcessCpuLoad() returns " + usage
                       +   " which is not in the [0.0,1.0] interval");
            }//end if
            else
              if (z!=0)
              //System.out.println("load the overall system "+ usage*100+" %"); 
           try {
                Thread.sleep(200);
            } //end try
            catch(InterruptedException e)
             {
                e.printStackTrace();
            }//end catch
        }//end for
  
    com.sun.management.OperatingSystemMXBean mxbean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    double total_mem = mxbean.getTotalPhysicalMemorySize()/(1024*1024*1024);
    double free_mem = mxbean.getFreePhysicalMemorySize()/(1024*1024*1024);
    double used_mem= total_mem - free_mem ;

    //System.out.println("Total Physical Memory in GB:" + total_mem);

    //System.out.println("Physical Memory Used in GB: " + used_mem);

    //System.out.println("Physical Memory Free in GB:" + free_mem);
	/*String cpuInfoMsg = "CPU"+ " , ";
    cpuInfoMsg = cpuInfoMsg + hostName + " , " +
    		 "You have :  "+Num_of_CPU+" CPUs" +  " , " +
            "Load Average in one minute :  "+load + " , "+
            "Total Physical Memory in GB:" + total_mem + " , "+
            "Physical Memory Used in GB: " + used_mem + " , "+
            "Physical Memory Free in GB:" + free_mem;*/
    //System.out.println(cpuInfoMsg);
	/*System.out.println("try 1!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
 	File f = new File("info.txt");
 	 try {
            if (!f.exists()) {
            System.out.println("try 2!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                f.createNewFile();
                FileWriter writer = new FileWriter(f, true);
                writer.write("TESSST: \n"+cpuInfoMsg);
                writer.close();

                //if file is already exist
            } else if (f.exists()) {
            System.out.println("try 3!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                FileWriter writer = new FileWriter(f, true);
                writer.write("TESSST: \n"+cpuInfoMsg);
                writer.close();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }*/
        
        
        
 	
   /* String fileName = "info.txt";
    //start write in the text file
    try 
    {
           
      FileWriter fileWriter =new FileWriter(fileName);
      BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
      bufferedWriter.write("TESSST: \n"+cpuInfoMsg);
      bufferedWriter.write("You have :  "+Num_of_CPU+" CPUs ");
      bufferedWriter.newLine();
      bufferedWriter.write("Load Average in one minute :  "+load);
      bufferedWriter.newLine();
      bufferedWriter.write("Total Physical Memory in GB:" + total_mem);
      bufferedWriter.newLine();
      bufferedWriter.write("Physical Memory Used in GB: " + used_mem);
      bufferedWriter.newLine();
      bufferedWriter.write("Physical Memory Free in GB:" + free_mem);
      bufferedWriter.close();
    }
    catch(IOException ex) 
    {
      System.out.println("Error writing to file '"+ fileName + "'");
            
    } //end write in the text file
        
	*/
    
    InetAddress localaddr = InetAddress.getLocalHost();
    String hostName = localaddr.getHostName();
    servSockets = new ConcurrentHashMap<Socket, ProcessLauncher>();
    Map<String, String> map = System.getenv();
	/*String cpuInfoMsg = "CPU"+ " , ";
    cpuInfoMsg = cpuInfoMsg + hostName + " , " +
    		 "You have :  "+Num_of_CPU+" CPUs" +  " , " +
            "Load Average in one minute :  "+load + " , "+
            "Total Physical Memory in GB:" + total_mem + " , "+
            "Physical Memory Used in GB: " + used_mem + " , "+
            "Physical Memory Free in GB:" + free_mem;*/
    try {
      mpjHomeDir = map.get("MPJ_HOME");
      RTConstants.MPJ_HOME_DIR = mpjHomeDir;
      if (mpjHomeDir == null) {
		throw new Exception("MPJ_HOME environment variable not set!!!");
      }
      //test
      /*String dataFile = mpjHomeDir+"/Dilmun_REST/info.txt";
      FileWriter fileWriter =new FileWriter(dataFile);
      BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
      bufferedWriter.write("TESSST Dilmun REST: \n"+cpuInfoMsg);
      bufferedWriter.close();*/
    }

    catch (Exception exc) {
      System.out.println("Error: " + exc.getMessage());
      exc.printStackTrace();
      return;
    }
	
	//String dilmunDir = map.get("MPJ_Dilmun");
	//System.out.println("Daemon dilmun path: "+dilmunDir );
    // Reading values from conf/mpjexpress.conf
    
    /*String fileName = mpjHomeDir+"/Dilmun_REST/info.txt";
    //start write in the text file
    try 
    {
           
      FileWriter fileWriter =new FileWriter(fileName);
      BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
      bufferedWriter.write("TESSST Dilmun REST: \n"+cpuInfoMsg);
      bufferedWriter.close();
    }
    catch(IOException ex) 
    {
      System.out.println("Error writing to file '"+ fileName + "'");
            
    } //end write in the text file
*/
    readValuesFromMPJExpressConf();
    createLogger(mpjHomeDir, hostName);
    
    
///check SOCKET!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	Socket MyClient = null;
    try {
          // MyClient = new Socket("Rawans-MacBook-Air.local",Integer.parseInt(RTConstants.MPJ_RUN_SERVER_PORT));
          MyClient = new Socket(args[1],Integer.parseInt(RTConstants.MPJ_RUN_SERVER_PORT));
          //byte[] masterAddr = args[1].getBytes();
          //InetAddress masterAdress = InetAddress.getByAddress(masterAddr);
          //MyClient = new Socket(masterAdress,Integer.parseInt(RTConstants.MPJ_RUN_SERVER_PORT));
    }
    catch (IOException e) {
        System.out.println(e);
    }


 	DataOutputStream output = null;
    try {
       output = new DataOutputStream(MyClient.getOutputStream());
    }
    catch (IOException e) {
       System.out.println(e);
    }
    
    if(output!=null){
    try{
    String cpuMsg = "CPU"+ " , " + hostName + " , " +
			 "You have :  "+Num_of_CPU+" CPUs" +  " , " +
            "Load Average in one minute :  "+load + " , "+
            "Total Physical Memory in GB:" + total_mem + " , "+
            "Physical Memory Used in GB: " + used_mem + " , "+
            "Physical Memory Free in GB:" + free_mem +"\n";
    
    
    output.writeBytes(cpuMsg);
            
    

    output.writeBytes("EXIT");

      }
     catch (IOException e) {
       System.out.println(e);
    }
    }
    
     try {
           output.close();
           MyClient.close();
    } 
    catch (IOException e) {
       System.out.println(e);
       
    }
    ////end sockettttt!!!!!!!!!!!!!!!!!!!!
    
    

    if (DEBUG && logger.isDebugEnabled()) {
      logger.debug("mpjHomeDir " + mpjHomeDir);
    }
    
//CHANGE LENGTH was 1
    if (args.length == 2) {
      if (DEBUG && logger.isDebugEnabled()) {
	logger.debug(" args[0] " + args[0]);
	logger.debug("setting daemon port to" + args[0]);
      }
      D_SER_PORT = new Integer(args[0]).intValue();
    } 

    else {
      throw new MPJRuntimeException("Usage: java MPJDaemon daemonServerPort");
    }

    if (DEBUG && logger.isDebugEnabled())
        logger.debug("Starting PortManager thread .. ");

    // Invoking port manager
      pManager = new PortManagerThread(portManagerPort);
      pManager.start();

    if (DEBUG && logger.isDebugEnabled())
        logger.debug("Starting ConnectionManager thread .. ");

    // Invoking connection manager thread 
    connectionManager = new ConnectionManager();
    connectionManager.start();

    if (DEBUG && logger.isDebugEnabled())
        logger.debug("serverSocketInit .. ");

    serverSocketInit();
  }





  private void createLogger(String homeDir, String hostName)
      throws MPJRuntimeException {

    if (logger == null) {
      DailyRollingFileAppender fileAppender = null;

      try {
	if (logLevel.toUpperCase().equals("DEBUG")) {
	  fileAppender = new DailyRollingFileAppender(new PatternLayout(
	      " %-5p %c %x - %m\n"), homeDir + "/logs/daemon-" + hostName
	      + ".log", "yyyy-MM-dd-a");

	  Logger rootLogger = Logger.getRootLogger();
	  rootLogger.addAppender(fileAppender);
	  LoggerRepository rep = rootLogger.getLoggerRepository();
	  rootLogger.setLevel((Level) Level.ALL);
	}
	logger = Logger.getLogger("mpjdaemon");
	logger.setLevel(Level.toLevel(logLevel.toUpperCase(), Level.OFF));
      }
      catch (Exception e) {
	throw new MPJRuntimeException(e);
      }
    }
  }

  private void serverSocketInit() {
    if (DEBUG && logger.isDebugEnabled()) {
      logger.debug("serverSocketInit called .. ");
    }

    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(D_SER_PORT);
      do {
        if (DEBUG && logger.isDebugEnabled()) {
          logger.debug("Accepting connection ..");
        }
	Socket servSock = null; 
        try {
           servSock = serverSocket.accept();
 	} catch(Exception eee) { 
	   eee.printStackTrace(); 
	} 
	
	//TESTTTT


	// Connection is accepted and the socket passed onto 
        // ProcessLauncher.java which takes care of the rest
	ProcessLauncher pLaunch = new ProcessLauncher(servSock);
	servSockets.put(servSock, pLaunch);
	pLaunch.start();
      } while (true);
    }
    catch (IOException ioEx) {
      if (DEBUG && logger.isDebugEnabled()) {
        logger.debug("Unable to attach to port!");
      }
      System.out.println("Unable to attach to port!");
      System.exit(1);
    } 
    catch (Exception e) { 
      e.printStackTrace();
    }

    if (!serverSocket.isClosed())
      try {
	serverSocket.close();
      }
      catch (IOException e) {
	e.printStackTrace();
      }
    if (pManager != null) {
      pManager.isRun = false;
    }
    if (connectionManager != null) {
      connectionManager.isRun = false;
    }
  }

  private void readValuesFromMPJExpressConf() {
    FileInputStream in = null;
    DataInputStream din = null;
    BufferedReader reader = null;
    String line = "";

    try {
      String path = mpjHomeDir + File.separator
	  + RTConstants.MPJEXPRESS_CONF_FILE;
      in = new FileInputStream(path);
      din = new DataInputStream(in);
      reader = new BufferedReader(new InputStreamReader(din));

      while ((line = reader.readLine()) != null) {
	if (line.startsWith(RTConstants.MPJ_PORTMANAGER_PORT_KEY)) {
	  portManagerPort = Integer.parseInt(MPJUtil.confValue(line));
	} else if (line.startsWith(RTConstants.MPJ_DAEMON_LOGLEVEL_KEY)) {
	  logLevel = MPJUtil.confValue(line);
	} else if (line.startsWith(RTConstants.MPJ_RUN_SERVER_PORT_KEY )) {
	  RTConstants.MPJ_RUN_SERVER_PORT =  MPJUtil.confValue(line);
	} 
      }
      in.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static void main(String args[]) {
    try {
      
      MPJDaemon dae = new MPJDaemon(args);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}

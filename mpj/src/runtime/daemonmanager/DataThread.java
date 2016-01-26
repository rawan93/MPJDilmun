
package runtime.daemonmanager;

import runtime.common.MPJUtil;
import runtime.daemonmanager.DMThreadUtil;
import runtime.common.RTConstants;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.Map;


public class DataThread extends DMThread {
  private String host = "localhost";
private static int daemonPort = 0;
static String mpjHomeDir = null;

  public DataThread(String machineName) {
    host = machineName;
  }

  public void run() {
    queryMPJExpressDeamons();
  }

  public void queryMPJExpressDeamons() {
    String pid = DaemonUtil.getMPJProcessID(host);
    String msg = "";
      if (pid != ""){
      getDaemonPort();
      msg = MPJUtil.FormatMachineMessage(host,"MPJ Daemon is running", pid, ""+daemonPort);
      //msg = msg + " with port number "+ daemonPort;
      DMThreadUtil.runtimeStatus = true;
      DMThreadUtil.numberOfDaemons++;
      }
    else {
      msg = MPJUtil.FormatMachineMessage(host, DMMessages.MPJDAEMON_NOT_AVAILABLE, "", "");
    }
    DMThreadUtil.machineMsgList.add(msg);
  }
	
	public static void getDaemonPort(){
  	Map<String, String> map = System.getenv();
    try{
      mpjHomeDir = map.get("MPJ_HOME");
      RTConstants.MPJ_HOME_DIR = mpjHomeDir;
      if (mpjHomeDir == null) {
        throw new Exception("[MPJRun.java]:MPJ_HOME environment found..");
      }
    }
    catch (Exception exc) {
      System.out.println("[MPJRun.java]:" + exc.getMessage());
      exc.printStackTrace();
      return;
    }
    FileInputStream in = null;
    DataInputStream din = null;
    BufferedReader reader = null;
    String line = "";
  	try {
	
      String path = mpjHomeDir + File.separator + RTConstants.MPJEXPRESS_CONF_FILE;
      in = new FileInputStream(path);
      din = new DataInputStream(in);
      reader = new BufferedReader(new InputStreamReader(din));

      while ((line = reader.readLine()) != null) {
        if (line.startsWith(RTConstants.MPJ_DAEMON_PORT_KEY)) {
          daemonPort = Integer.parseInt(MPJUtil.confValue(line));
        } 
      }

      in.close();

    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }
}


package runtime.daemonmanager;

import runtime.common.MPJUtil;
import runtime.daemonmanager.DMThreadUtil;

import java.util.ArrayList;

public class DataThread extends DMThread {
  private String host = "localhost";

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
      msg = MPJUtil.FormatMessage(host,DMMessages.MPJDAEMON_AVAILABLE + pid);
      DMThreadUtil.runtimeStatus = true;
      DMThreadUtil.numberOfDaemons++;
      }
    else {
      msg = MPJUtil.FormatMessage(host, DMMessages.MPJDAEMON_NOT_AVAILABLE);
    }
    DMThreadUtil.machineMsgList.add(msg);
  }
	
}

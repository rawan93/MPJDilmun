
package runtime.daemonmanager;

import runtime.daemonmanager.MPJData;

public class MPJData {

  public void getMPJExpressData(CLOptions options) {
    DMThreadUtil.ExecuteCommand(options);
  }
}

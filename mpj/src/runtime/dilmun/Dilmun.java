
package runtime.dilmun;

import java.io.IOException;


public class Dilmun {

  public static void main(String args[]){
      System.out.println("Dilmun");
      CPU_Info info = new CPU_Info();
      info.NumberOfCPUs();
      info.SytemMemory();
  }
}

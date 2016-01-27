package runtime.dilmun;
import java.io.IOException;
import java.lang.management.*;
import java.io.*;


public class CPU_Info {

  
      public void NumberOfCPUs(){
    
      int NUM= Runtime.getRuntime().availableProcessors();
      System.out.println("You have  "+NUM+" CPUs ");



      OperatingSystemMXBean osMBean
            = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        double load = osMBean.getSystemLoadAverage();

      

      System.out.println("System load "+load+" m ");

  }



      public void SytemMemory(){

        String userName = System.getProperty("user.name");

        long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        System.out.println("User Name="+userName);


        com.sun.management.OperatingSystemMXBean mxbean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println("Total Memory in MB: " + mxbean.getTotalPhysicalMemorySize()/(1024*1024));
        
        System.out.println("Free Memory in MB: " + mxbean.getFreePhysicalMemorySize()/(1024*1024));

        long total = mxbean.getTotalPhysicalMemorySize()/(1024*1024);
        long free = mxbean.getFreePhysicalMemorySize()/(1024*1024);
        long used= total - free ;

        System.out.println("Used Memory in MB: " + used);
   

  }

   

 

  
}

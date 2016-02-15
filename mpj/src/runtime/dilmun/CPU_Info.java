package runtime.dilmun;
import java.io.IOException;
import java.lang.management.*;
import java.io.*;
import com.sun.management.OperatingSystemMXBean;  


public class CPU_Info {

  
      public void NumberOfCPUs()

      {
    
      int NUM= Runtime.getRuntime().availableProcessors();

      System.out.println("You have :  "+NUM+" CPUs ");


      OperatingSystemMXBean osMBean= (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

      double load = osMBean.getSystemLoadAverage();


      System.out.println("Load Average in one minute :  "+load);

    
     }// end the function



      public void SytemMemory()

      {

       com.sun.management.OperatingSystemMXBean mxbean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        

       long total = mxbean.getTotalPhysicalMemorySize()/(1024*1024*1024);
       long free = mxbean.getFreePhysicalMemorySize()/(1024*1024*1024);
       long used= total - free ;
       

        System.out.println("Total Physical Memory in GB:" + total);

        System.out.println("Physical Memory Used in GB: " + used);

        System.out.println("Physical Memory Free in GB:" + free);




 }//end the function
  
}//end of the class

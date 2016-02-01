package runtime.dilmun;
import java.io.IOException;
import java.lang.management.*;
import java.io.*;


public class CPU_Info {

  
      public void NumberOfCPUs(){
    
      int NUM= Runtime.getRuntime().availableProcessors();

      System.out.println("You have :  "+NUM+" CPUs ");




      OperatingSystemMXBean osMBean
            = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        double load = osMBean.getSystemLoadAverage();



      System.out.println("Load Average in one minute :  "+load);

   /* com.sun.management.OperatingSystemMXBean operatingSystemMXBean = 
         (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
    long prevUpTime = runtimeMXBean.getUptime();
    long prevProcessCpuTime = operatingSystemMXBean.getProcessCpuTime();
    double cpuUsage;
    try 
    {
        Thread.sleep(500);
    } 
    catch (Exception ignored) { }

    operatingSystemMXBean  = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    long upTime = runtimeMXBean.getUptime();
    long processCpuTime = operatingSystemMXBean.getProcessCpuTime();
    long elapsedCpu = processCpuTime - prevProcessCpuTime;
    long elapsedTime = upTime - prevUpTime;

    cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 10000F * availableProcessors));
    System.out.println("Java CPU: " + cpuUsage);*/

   try {
    Process p = Runtime.getRuntime().exec("ls -l");

    System.out.println("CPU usage :  "+p);
       }

  catch(IOException e) {
         System.out.println("Completed!");
    }
    
  }



      public void SytemMemory()

      {

       /* long maxMemory= Runtime.getRuntime().maxMemory();

        System.out.println("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
      
        System.out.println("Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory());


        System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
=======
      

      System.out.println("System load "+load+" m ");

  }



      public void SytemMemory(){
>>>>>>> 380a54bb1980659fef0a6134ef1f0423d064174f

        String userName = System.getProperty("user.name");

        long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        System.out.println("User Name="+userName);
<<<<<<< HEAD
        System.out.println("RAM Size="+memorySize+" Bytes");*/


       com.sun.management.OperatingSystemMXBean mxbean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        

       double total = mxbean.getTotalPhysicalMemorySize()/(1024*1024*1024);
       double free = mxbean.getFreePhysicalMemorySize()/(1024*1024*1024);
       double used= total - free ;

        System.out.println("Total Physical Memory in GB:" + total);

        System.out.println("Physical Memory Used in GB: " + used);

        System.out.println("Physical Memory Free in GB:" + free);

        


       }



        
   



   

 

  
}

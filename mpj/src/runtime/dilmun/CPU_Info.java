package runtime.dilmun;
import java.io.IOException;
import java.lang.management.*;
import java.io.*;
import com.sun.management.OperatingSystemMXBean;  


public class CPU_Info {

  
      public void NumberOfCPUs(){
    
      int NUM= Runtime.getRuntime().availableProcessors();

      System.out.println("You have :  "+NUM+" CPUs ");




      OperatingSystemMXBean osMBean
            = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        double load = osMBean.getSystemLoadAverage();




      System.out.println("Load Average in one minute :  "+load);

       


   com.sun.management.OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
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

    
    long upTime = runtimeMXBean.getUptime();
    long processCpuTime = operatingSystemMXBean.getProcessCpuTime();
    long elapsedCpu = processCpuTime - prevProcessCpuTime;
    long elapsedTime = upTime - prevUpTime;

    cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 10000F * availableProcessors));
    System.out.println("Java CPU: " + cpuUsage);


   try {
    Process p = Runtime.getRuntime().exec("ls -l");

    System.out.println("CPU usage :  "+p);


       }

  catch(IOException e) {
         System.out.println("Completed!");
    }
    
  }// end the function



      public void SytemMemory()

      {

       


       com.sun.management.OperatingSystemMXBean mxbean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        

       long total = mxbean.getTotalPhysicalMemorySize()/(1024*1024*1024);
       long free = mxbean.getFreePhysicalMemorySize()/(1024*1024*1024);
       long used= total - free ;
       double d=mxbean.getSystemCpuLoad()*100;
       double f=mxbean.getProcessCpuLoad()*100;
   

        System.out.println("Total Physical Memory in GB:" + total);

        System.out.println("Physical Memory Used in GB: " + used);

        System.out.println("Physical Memory Free in GB:" + free);

        System.out.println("recent cpu usage for the whole system" + d);

         System.out.println("Java Virtual Machine process" + f);

        
      


         OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);  
        // What % CPU load this current JVM is taking, from 0.0-1.0  
        System.out.println("CPU load this current JVM "+osBean.getProcessCpuLoad()*100+" %");  
  
       // What % load the overall system is at, from 0.0-1.0  
       System.out.println("load the overall system "+osBean.getSystemCpuLoad()+" %");  


      /*  try{

          

          MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
         ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

       if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

       // usually takes a couple of seconds before we get real values
       if (value == -1.0)    
        System.out.println(Double.NaN);   
       // returns a percentage value with 1 decimal point precision
      double u= ((int)(value * 1000) / 10.0);
        System.out.println("CPU Usage " + u);

       }
       catch (Exception ignored) { }*/


    /*public void getProcessCpuLoad() throws Exception {

    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

    if (list.isEmpty())     return Double.NaN;

    Attribute att = (Attribute)list.get(0);
    Double value  = (Double)att.getValue();

    // usually takes a couple of seconds before we get real values
    if (value == -1.0)      return Double.NaN;
    // returns a percentage value with 1 decimal point precision
    return ((int)(value * 1000) / 10.0);
}*/



 }
  
}//end of the class

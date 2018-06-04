package testes;

import br.usp.icmc.lasdpc.BeQoS.classes.DatacenterConfiguration;
import br.usp.icmc.lasdpc.BeQoS.classes.Utilidades;
import br.usp.icmc.lasdpc.BeQoS.classes.VMConfiguration;
import org.workflowsim.examples.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.workflowsim.ClusterStorage;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowDatacenter;
import org.workflowsim.Job;
import org.workflowsim.WorkflowEngine;
import org.workflowsim.WorkflowPlanner;
import org.workflowsim.utils.ClusteringParameters;
import org.workflowsim.utils.OverheadParameters;
import org.workflowsim.utils.Parameters;
import org.workflowsim.utils.ReplicaCatalog;

public class MultiplosDatacentersTeste1 extends WorkflowSimBasicExample1 {

    protected static List<CondorVM> createVM(int userId, int vms, int vmIdBase) {

        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<CondorVM> list = new LinkedList<>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        CondorVM[] vm = new CondorVM[vms];
        for (int i = 0; i < vms; i++) {
            double ratio = 1.0;
            vm[i] = new CondorVM(vmIdBase + i, userId, mips * ratio, 
                    pesNumber, ram, bw, size, vmm, 
                    new CloudletSchedulerSpaceShared());
            list.add(vm[i]);
        }
        return list;
    }

    ////////////////////////// STATIC METHODS ///////////////////////
    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {

        //######################################################################
          DatacenterConfiguration confA1 = new DatacenterConfiguration();
            confA1.hostId = 1;
            confA1.mips = 10000;
            confA1.num_cores = 1;
            confA1.num_hosts = 30;
            confA1.ram = 8000; //Mb
            confA1.bw = 1000; //MBit/s
            confA1.storage = 500000; //Mb
            confA1.arch = "x64";
            confA1.os = "Linux";
            confA1.vmm = "Xen";
            confA1.time_zone = 10.0; //TimeZone onde está o Datacenter
            confA1.cost = 3.0; //USD
            confA1.costPerBw = 0.05; //USD
            confA1.costPerMem = 0.1; //USD
            confA1.costPerStorage = 0.1; //USD
            confA1.maxTransferRate = 15;

            //Configuração de VM para o Ambiente A1
            int vmNumA1 = 10;
            VMConfiguration vmConfA1 = new VMConfiguration();
            vmConfA1.idShift = 1;
            vmConfA1.bw = 1000;
            vmConfA1.pesNumber = 1;
            vmConfA1.mips = 10000;
            vmConfA1.vmm = "Xen";
            vmConfA1.size = 250000;  //Tamanho de Imagem em Mbytes
            vmConfA1.ram = 8000; //MBytes
        
        
            DatacenterConfiguration confCosmos = new DatacenterConfiguration();
            confCosmos.hostId = 1000;
            confCosmos.mips = 10000;
            confCosmos.num_cores = 1;
            confCosmos.num_hosts = 30;
            confCosmos.ram = 8000; //Mb
            confCosmos.bw = 10000; //Mb Total
            confCosmos.storage = 500000; //Mb
            confCosmos.arch = "x64";
            confCosmos.os = "Linux";
            confCosmos.vmm = "Xen";
            confCosmos.time_zone = 10.0;
            confCosmos.cost = 3.0; //USD
            confCosmos.costPerBw = 0.05; //USD
            confCosmos.costPerMem = 0.1; //USD
            confCosmos.costPerStorage = 0.1; //USD

            //*********
            //Configuração de VMs Cosmos
            int vmNumA2Cosmos = 10;
            VMConfiguration vmConfCosmos = new VMConfiguration();
            vmConfCosmos.idShift = 1000;
            vmConfCosmos.bw = 1000;
            vmConfCosmos.pesNumber = 1;
            vmConfCosmos.mips = 10000;
            vmConfCosmos.vmm = "Xen";
            vmConfCosmos.size = 250000;  //Tamanho de Imagem em Mbytes
            vmConfCosmos.ram = 4000; //MBytes
            
             int vmNumA3SoftLayer = 10;
            DatacenterConfiguration confSoftLayer = new DatacenterConfiguration();
            confSoftLayer.hostId = 4000;
            confSoftLayer.mips = 10000;
            confSoftLayer.num_cores = 1;
            confSoftLayer.num_hosts = 10;
            confSoftLayer.ram = 16000; //Mb
            confSoftLayer.bw = 10000; //MB Total
            confSoftLayer.storage = 500000; //Mb
            confSoftLayer.arch = "x64";
            confSoftLayer.os = "Linux";
            confSoftLayer.vmm = "Xen";
            confSoftLayer.time_zone = 10.0;
            confSoftLayer.cost = 3.0; //USD
            confSoftLayer.costPerBw = 0.05; //USD
            confSoftLayer.costPerMem = 0.10; //USD
            confSoftLayer.costPerStorage = 0.10; //USD

            //*********
            //Configuração de VMs SoftLayer
            VMConfiguration vmConfSoftLayer = new VMConfiguration();
            vmConfSoftLayer.idShift = 4000;
            vmConfSoftLayer.bw = 1000;
            vmConfSoftLayer.pesNumber = 1;
            vmConfSoftLayer.mips = 10000;
            vmConfSoftLayer.vmm = "Xen";
            vmConfSoftLayer.size = 250000;  //Tamanho de Imagem em Mbytes
            vmConfSoftLayer.ram = 4000; //MBytes
        
        //######################################################################
        
        
        
        
        try {
          
            int vmNum = 90;
         
            String daxPath = "" + System.getProperty("user.dir") + "/config/dax/Montage_1000.xml";
            File daxFile = new File(daxPath);
            if (!daxFile.exists()) {
                Log.printLine("Warning: Please replace daxPath with the physical path in your working environment!");
                return;
            }

         
            Parameters.SchedulingAlgorithm sch_method = Parameters.SchedulingAlgorithm.ROUNDROBIN;
            Parameters.PlanningAlgorithm pln_method = Parameters.PlanningAlgorithm.EDVARD1;
            ReplicaCatalog.FileSystem file_system = ReplicaCatalog.FileSystem.SHARED;          
            
            OverheadParameters op = new OverheadParameters(0, null, null, null, null, 0);
            ClusteringParameters.ClusteringMethod method =
                    ClusteringParameters.ClusteringMethod.NONE;
            ClusteringParameters cp = new ClusteringParameters(0, 0, method, null);

            Parameters.init(vmNum, daxPath, null,
                    null, op, cp, sch_method, pln_method,
                    null, 0);
            ReplicaCatalog.init(file_system);

            // before creating any entities.
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            WorkflowDatacenter datacenter4 = Utilidades.createCustomDatacenter("DC_A1",confA1);
            WorkflowDatacenter datacenter3 = Utilidades.createCustomDatacenter("DC_A2",confCosmos);
            WorkflowDatacenter datacenter5 = Utilidades.createCustomDatacenter("DC_A3",confSoftLayer);
//            WorkflowDatacenter datacenter0 = createDatacenter("Datacenter_0");
//            WorkflowDatacenter datacenter1 = createDatacenter("Datacenter_1");
//            WorkflowDatacenter datacenter2 = createDatacenter("Datacenter_2");
           
            
            WorkflowPlanner wfPlanner = new WorkflowPlanner("planner_0", 1);
            
            WorkflowEngine wfEngine = wfPlanner.getWorkflowEngine();
            
         //   List<CondorVM> vmlist0 = createVM(wfEngine.getSchedulerId(0), Parameters.getVmNum() / 3, 1);
//            List<CondorVM> vmlist1 = createVM(wfEngine.getSchedulerId(0), Parameters.getVmNum() / 3, 500);
//            List<CondorVM> vmlist2= createVM(wfEngine.getSchedulerId(0), Parameters.getVmNum() / 3, 9000);
            List<CondorVM> vmlist0 = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA1, vmConfA1);
            List<CondorVM> vmlist1 = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA2Cosmos, vmConfCosmos);
            List<CondorVM> vmlist2= Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA3SoftLayer, vmConfSoftLayer);

            
            wfEngine.submitVmList(vmlist0, 0);
            wfEngine.submitVmList(vmlist1, 0);
            wfEngine.submitVmList(vmlist2, 0);
            
//            wfEngine.bindSchedulerDatacenter(datacenter0.getId(), 0);
//            wfEngine.bindSchedulerDatacenter(datacenter1.getId(), 0);
//            wfEngine.bindSchedulerDatacenter(datacenter2.getId(), 0); 
            wfEngine.bindSchedulerDatacenter(datacenter3.getId(), 0); 
            wfEngine.bindSchedulerDatacenter(datacenter4.getId(), 0);
            wfEngine.bindSchedulerDatacenter(datacenter5.getId(), 0);
            
            CloudSim.startSimulation();
            List<Job> outputList0 = wfEngine.getJobsReceivedList();
            CloudSim.stopSimulation();
            printJobList(outputList0);
        } catch (Exception e) {
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }

    protected static WorkflowDatacenter createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.
        //
        // Here is the trick to use multiple data centers in one broker. Broker will first
        // allocate all vms to the first datacenter and if there is no enough resource then it will allocate 
        // the failed vms to the next available datacenter. The trick is make sure your datacenter is not 
        // very big so that the broker will distribute them. 
        // In a future work, vm scheduling algorithms should be done
        //
        for (int i = 1; i <= 3; i++) {
            List<Pe> peList1 = new ArrayList<>();
            int mips = 2000;
            // 3. Create PEs and add these into the list.
            //for a quad-core machine, a list of 4 PEs is required:
            peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
            peList1.add(new Pe(1, new PeProvisionerSimple(mips)));

            int hostId = 0;
            int ram = 2048; //host memory (MB)
            long storage = 1000000; //host storage
            int bw = 10000;
            hostList.add(
                    new Host(
                            hostId,
                            new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw),
                            storage,
                            peList1,
                            new VmSchedulerTimeShared(peList1)
                    )); // This is our first machine
            hostId++;
        }

        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.1;	// the cost of using storage in this resource
        double costPerBw = 0.1;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<>();	//we are not adding SAN devices by now
        WorkflowDatacenter datacenter = null;
        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        // 6. Finally, we need to create a cluster storage object.
        /**
         * The bandwidth between data centers.
         */
        double interBandwidth = 1.5e7;// the number comes from the futuregrid site, you can specify your bw
        /**
         * The bandwidth within a data center.
         */
        double intraBandwidth = interBandwidth * 2;
        try {
            ClusterStorage s1 = new ClusterStorage(name, 1e12);
            switch (name) {
                case "Datacenter_0":
                    /**
                     * The bandwidth from Datacenter_0 to Datacenter_1.
                     */
                    s1.setBandwidth("Datacenter_1", interBandwidth);
                    break;
                case "Datacenter_1":
                    /**
                     * The bandwidth from Datacenter_1 to Datacenter_0.
                     */
                    s1.setBandwidth("Datacenter_0", interBandwidth);
                    break;
                 case "Datacenter_2":
                    /**
                     * The bandwidth from Datacenter_1 to Datacenter_0.
                     */
                    s1.setBandwidth("Datacenter_0", interBandwidth);
                    break;    
                    
            }
            // The bandwidth within a data center
            s1.setBandwidth("local", intraBandwidth);
            // The bandwidth to the source site 
            s1.setBandwidth("source", interBandwidth);
            storageList.add(s1);
            datacenter = new WorkflowDatacenter(name,
                    characteristics, 
                    new VmAllocationPolicySimple(hostList), 
                    storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datacenter;
    }
}

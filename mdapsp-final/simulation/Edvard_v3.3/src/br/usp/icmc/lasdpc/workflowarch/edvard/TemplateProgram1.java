package br.usp.icmc.lasdpc.workflowarch.edvard;

import br.usp.icmc.lasdpc.BeQoS.classes.DatacenterConfiguration;
import br.usp.icmc.lasdpc.BeQoS.classes.Utilidades;
import br.usp.icmc.lasdpc.BeQoS.classes.VMConfiguration;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.workflowsim.CondorVM;
import org.workflowsim.Job;
import org.workflowsim.WorkflowDatacenter;
import org.workflowsim.WorkflowEngine;
import org.workflowsim.WorkflowPlanner;
import org.workflowsim.utils.ClusteringParameters;
import org.workflowsim.utils.OverheadParameters;
import org.workflowsim.utils.Parameters;
import org.workflowsim.utils.ReplicaCatalog;

/**
 *
 * @author Edvard Oliveira
 * @author Mario Pardo
 * @version 1.0
 *
 * Este programa servirá de base para validar todas as classes adicionais e
 * métodos implementados adicionalmente ao WorkFlowSim de modo que os demais
 * programas a serem construídos para o projeto experimental da Tese de
 * Doutorado de Edvard Oliveira sejam criados com base neste modelo de sequência
 * de comandos para a correta modelagem do cenário de simulação desejado para os
 * Workflows científicos de processamento de proteínas com base nos Datacenters
 * de Computação Científica do ICMC/USP de São Carlos.
 *
 */
public class TemplateProgram1 {

    public static void main(String[] args) {

        try {
            //***Configuração da Infraestrutura***

            //**********************************************************************
            //Ambiente A1 - 01 WorkStation
            //Configuração do Datacenter do WorkStation - Ambiente A1
            //Equipamento:
            //Dell Workstation
            //Processador Intel® Xeon® E3-1225 V5 (Quad Core, 3.3GHZ com turbo expansível para até 3.7GHZ, Cache de 8MB, HD GRAPHICS P530)
            //Number of computers    Avg. cores/computer    GFLOPS/core     GFLOPs/computer
            //      14                     3.86                3.19            12.29
            DatacenterConfiguration confA1 = new DatacenterConfiguration();
            confA1.hostId = 1;
            confA1.mips = 3200;
            confA1.num_cores = 4;
            confA1.num_hosts = 1;
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
            int vmNumA1 = 1;
            VMConfiguration vmConfA1 = new VMConfiguration();
            vmConfA1.idShift = 1;
            vmConfA1.bw = 1000;
            vmConfA1.pesNumber = 4;
            vmConfA1.mips = 3200;
            vmConfA1.vmm = "Xen";
            vmConfA1.size = 250000;  //Tamanho de Imagem em Mbytes
            vmConfA1.ram = 8000; //MBytes

            //**********************************************************************
            //Ambiente A2 - Cluster de Computadores LaSDPC
            //Aglomerado computacional: Cosmos, Andromeda, Halley
            //Estimados: 35 Hosts
            //Configuração do Datacenter do Cluster - Ambiente A2
            //Aglomerado Cosmos
            //Intel® Core™2 Quad Processor Q9400 (6M Cache, 2.66 GHz, 1333 MHz FSB)
            //Number of computers    Avg. cores/computer    GFLOPS/core     GFLOPs/computer
            //        3                    3.97                2.97             11.78
            DatacenterConfiguration confCosmos = new DatacenterConfiguration();
            confCosmos.hostId = 1000;
            confCosmos.mips = 4000;
            confCosmos.num_cores = 4;
            confCosmos.num_hosts = 10;
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

            //Configuração de VMs Cosmos
            int vmNumA2Cosmos = 10;
            VMConfiguration vmConfCosmos = new VMConfiguration();
            vmConfCosmos.idShift = 1000;
            vmConfCosmos.bw = 1000;
            vmConfCosmos.pesNumber = 4;
            vmConfCosmos.mips = 4000;
            vmConfCosmos.vmm = "Xen";
            vmConfCosmos.size = 250000;  //Tamanho de Imagem em Mbytes
            vmConfCosmos.ram = 4000; //MBytes

            //**********************************************************************
            //Aglomerado Andromeda
            //Equipamento:
            //AMD Processor Vishera 4.2 Ghz FX(tm)-8350 Eight-Core Processor
            //Number of computers    Avg. cores/computer    GFLOPS/core     GFLOPs/computer
            //      140                     7.96               3.31             26.33
            DatacenterConfiguration confAndromeda = new DatacenterConfiguration();
            confAndromeda.hostId = 2000;
            confAndromeda.mips = 3400;
            confAndromeda.num_cores = 4;
            confAndromeda.num_hosts = 10;
            confAndromeda.ram = 8000; //Mb
            confAndromeda.bw = 10000; //MB Total
            confAndromeda.storage = 500000; //Mb
            confAndromeda.arch = "x64";
            confAndromeda.os = "Linux";
            confAndromeda.vmm = "Xen";
            confAndromeda.time_zone = 10.0;
            confAndromeda.cost = 3.0; //USD
            confAndromeda.costPerBw = 0.05; //USD
            confAndromeda.costPerMem = 0.10; //USD
            confAndromeda.costPerStorage = 0.10; //USD

            //Configuração de VMs Andromeda
            int vmNumA2Andromeda = 10;
            VMConfiguration vmConfAndromeda = new VMConfiguration();
            vmConfAndromeda.idShift = 2000;
            vmConfAndromeda.bw = 1000;
            vmConfAndromeda.pesNumber = 4;
            vmConfAndromeda.mips = 3400;
            vmConfAndromeda.vmm = "Xen";
            vmConfAndromeda.size = 250000;  //Tamanho de Imagem em Mbytes
            vmConfAndromeda.ram = 4000; //MBytes

            //**********************************************************************
            //Aglomerado Halley
            //Equipamento:
            //Intel® Core™ I7 Processor – LGA -1150 – 4790 3.60GHZ DMI 5GT/S 8MB
            //Number of computers    Avg. cores/computer    GFLOPS/core     GFLOPs/computer
            //       1785                  7.93                4.27             33.86
            DatacenterConfiguration confHalley = new DatacenterConfiguration();
            confHalley.hostId = 3000;
            confHalley.mips = 4300;
            confHalley.num_cores = 4;
            confHalley.num_hosts = 15;
            confHalley.ram = 8000; //Mb
            confHalley.bw = 15000; //MB Total
            confHalley.storage = 500000; //Mb
            confHalley.arch = "x64";
            confHalley.os = "Linux";
            confHalley.vmm = "Xen";
            confHalley.time_zone = 10.0;
            confHalley.cost = 3.0; //USD
            confHalley.costPerBw = 0.05; //USD
            confHalley.costPerMem = 0.10; //USD
            confHalley.costPerStorage = 0.10; //USD

            //Configuração de VMs Halley
            int vmNumA2Halley = 15;
            VMConfiguration vmConfHalley = new VMConfiguration();
            vmConfHalley.idShift = 3000;
            vmConfHalley.bw = 1000;
            vmConfHalley.pesNumber = 4;
            vmConfHalley.mips = 4300;
            vmConfHalley.vmm = "Xen";
            vmConfHalley.size = 250000;  //Tamanho de Imagem em Mbytes
            vmConfHalley.ram = 4000; //MBytes

            //**********************************************************************
            //Ambiente A3 - Nuvem Privada na SoftLayer
            //DC Nuvem Privada na SoftLayer
            //Equipamento:
            //Quad Intel Xeon E7-4850 v2 (48 Cores, 2.30 GHz)
            //Number of computers    Avg. cores/computer       GFLOPS/core     GFLOPs/computer
            //        125                  8.00                  3.99              31.94
            DatacenterConfiguration confSoftLayer = new DatacenterConfiguration();
            confSoftLayer.hostId = 4000;
            confSoftLayer.mips = 4000;
            confSoftLayer.num_cores = 8;
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
            int vmNumA3SoftLayer = 10;
            VMConfiguration vmConfSoftLayer = new VMConfiguration();
            vmConfSoftLayer.idShift = 4000;
            vmConfSoftLayer.bw = 1000;
            vmConfSoftLayer.pesNumber = 8;
            vmConfSoftLayer.mips = 4000;
            vmConfSoftLayer.vmm = "Xen";
            vmConfSoftLayer.size = 250000;  //Tamanho de Imagem em Mbytes
            vmConfSoftLayer.ram = 4000; //MBytes

            //**********************************************************************
            // APLICAÇÃO - WORKFLOW - Pode ser representado por um List de daxPaths!!
            // OBS: Para uso de vários workflows, ver assinatura do método Parameteres.init();
//          List<String> daxPaths = new ArrayList<>();
//          daxPaths.add("/Users/weiweich/NetBeansProjects/WorkflowSim-1.0/config/dax/Montage_100.xml");
//          daxPaths.add("/Users/weiweich/NetBeansProjects/WorkflowSim-1.0/config/dax/Montage_25.xml");
//          daxPaths.add("/Users/weiweich/NetBeansProjects/WorkflowSim-1.0/config/dax/Montage_1000.xml");
            //**********************************************************************
//            String daxPath = "" + System.getProperty("user.dir")
//                    + "/config/dax/Montage_100.xml";
            String daxPath = "" + System.getProperty("user.dir")
                    + "/config/dax/Protein1VII_B.xml";
            File daxFile = new File(daxPath);
            if (!daxFile.exists()) {
                Log.printLine("Warning: Please replace daxPath with the physical path in your working environment!");
                return;
            }

            //**********************************************************************
            //Configuração de Geração Dinâmica de Workflow para o Simulador
            //Gerar valores dinâmicos para os parâmetros amin, pop e ger
            //Gerar o Workflow Dinâmico: lista de jobs e tasks em formato XML (DAX)
            //**********************************************************************
            //Parâmetros de Configuração do Ambiente de Simulação - WorkflowSim
            //Escalonamento e de Sistema de Arquivos
            Parameters.SchedulingAlgorithm sch_method = Parameters.SchedulingAlgorithm.EDVSCHED1;
            Parameters.PlanningAlgorithm pln_method = Parameters.PlanningAlgorithm.EDVARD1;
            ReplicaCatalog.FileSystem file_system = ReplicaCatalog.FileSystem.SHARED; //ou LOCAL

            //**********************************************************************
            //Outros Parâmetros de Inicialização
            //Overhead
            OverheadParameters op = new OverheadParameters(0, null, null, null, null, 0);

            //Clustering
            ClusteringParameters.ClusteringMethod method = ClusteringParameters.ClusteringMethod.NONE;
            ClusteringParameters cp = new ClusteringParameters(0, 0, method, null);

            //Inicializando os Parâmetros do WorkflowSim
            int vmNum = vmNumA1 + vmNumA2Andromeda + vmNumA2Cosmos + vmNumA2Halley + vmNumA3SoftLayer;
            Parameters.init(vmNum, daxPath, null,
                    null, op, cp, sch_method, pln_method,
                    null, 0);
            ReplicaCatalog.init(file_system);
            //**********************************************************************

            //Inicialização do CloudSim
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            //**********************************************************************
            //---> Criação dos Objetos do Cenário de Simulação <---
            //Criação do Datacenter - Ambiente A1
            WorkflowDatacenter dcA1 = Utilidades.createCustomDatacenter("DC_WorkStation", confA1);

            //Criação do Datacenter - Ambiente A2 - Cosmos
            WorkflowDatacenter dcA2Cosmos = Utilidades.createCustomDatacenter("DC_Cosmos", confCosmos);

            //Criação do Datacenter - Ambiente A2 - Andromeda
            WorkflowDatacenter dcA2Andromeda = Utilidades.createCustomDatacenter("DC_Andromeda", confAndromeda);

            //Criação do Datacenter - Ambiente A2 - Halley
            WorkflowDatacenter dcA2Halley = Utilidades.createCustomDatacenter("DC_Halley", confHalley);

            //Criação do Datacenter - Ambiente A3 - SoftLayer
            WorkflowDatacenter dcA3 = Utilidades.createCustomDatacenter("DC_SoftLayer", confSoftLayer);

            //Inicialização do WorkflowSim - definição de 5 escalonadores
            WorkflowPlanner wfPlanner = new WorkflowPlanner("planner_0", 1);
            WorkflowEngine wfEngine = wfPlanner.getWorkflowEngine();

            List<CondorVM> vmListA1 = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA1, vmConfA1);
            List<CondorVM> vmListA2Cosmos = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA2Cosmos, vmConfCosmos);
            List<CondorVM> vmListA2Andromeda = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA2Andromeda, vmConfAndromeda);
            List<CondorVM> vmListA2Halley = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA2Halley, vmConfHalley);
            List<CondorVM> vmListA3SoftLayer = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA3SoftLayer, vmConfSoftLayer);

            System.out.println("vmLst1 = " + vmListA1.size());
            System.out.println("vmLst2 = " + vmListA2Cosmos.size());
            System.out.println("vmLst3 = " + vmListA2Andromeda.size());
            System.out.println("vmLst4 = " + vmListA2Halley.size());
            System.out.println("vmLst5 = " + vmListA3SoftLayer.size());

            wfEngine.submitVmList(vmListA1, 0);
            wfEngine.submitVmList(vmListA2Cosmos, 0);
            wfEngine.submitVmList(vmListA2Andromeda, 0);
            wfEngine.submitVmList(vmListA2Halley, 0);
            wfEngine.submitVmList(vmListA3SoftLayer, 0);

            //Iniciar Simulação e coletar resultados de experimento
            wfEngine.bindSchedulerDatacenter(dcA1.getId(), 0);
            wfEngine.bindSchedulerDatacenter(dcA2Cosmos.getId(), 0);
            wfEngine.bindSchedulerDatacenter(dcA2Andromeda.getId(), 0);
            wfEngine.bindSchedulerDatacenter(dcA2Halley.getId(), 0);
            wfEngine.bindSchedulerDatacenter(dcA3.getId(), 0);

            //Define a Escala de Variação dos MI das Tarefas
            Parameters.setRuntimeScale(1.0);
            //***

            CloudSim.startSimulation();
            List<Job> outputList0 = wfEngine.getJobsReceivedList();
            CloudSim.stopSimulation();
            //CloudSim.terminateSimulation(300);
            Utilidades.printJobList(outputList0);

            //Shishido - impressão do grafo de Workflow executado
            //Utilidades.printWorkflow(outputList0);
        } catch (Exception ex) {
            Logger.getLogger(TemplateProgram1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

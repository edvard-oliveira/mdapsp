package experimentos;

import br.usp.icmc.lasdpc.BeQoS.classes.DatacenterConfiguration;
import br.usp.icmc.lasdpc.BeQoS.classes.MonitorRecursos;
import br.usp.icmc.lasdpc.BeQoS.classes.Utilidades;
import br.usp.icmc.lasdpc.BeQoS.classes.VMConfiguration;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.NetworkTopology;
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
 * @Programa: Este programa possui a mesma sequência de Configurações da classe
 * TemplateProgram2.java, contudo, foram acrescidos comandos para adicionar os
 * delays de rede do ambiente simulado.
 *
 */
public class Experimento20 {

    public static void main(String[] args) {
        Experimento20 obj = new Experimento20();
        String output = obj.executar();
    }

    public String executar() {

        Log.printLine("Experimento 20");
        //Parametrização do Experimento
        Parameters.SchedulingAlgorithm  f1 = Parameters.SchedulingAlgorithm.EDVSCHED2;
        String                          f2 = "Protein1VII_Final.xml";
        int                             f3 = Utilidades.SEM_NUVEM;

        //Registrando dados da Configuração do Experimento...
        Utilidades.escalonador = f1;
        Utilidades.nomeWorkflow = f2;
        Utilidades.selectInfra = f3;

        Log.printLine(f2);
        
        //Habilitando mensagens de saída do simulador.
        Log.disable();

        //Define a Escala de Variação dos MI das Tarefas
        //Solução: uso de uma tabela com 5 valores referentes ao multiplicador usado no método
        //setRunTimeScale. O intervalo (range) de valores é o que segue:
        // Valor Sorteado | Tamanho da Escala
        //              1 | 10
        //              2 | 25
        //              3 | 50
        //              4 | 100
        int numEscalas = 5;
        int escala = (int) (1 + Math.random() * numEscalas);
        switch (escala) {
            case 1:
                escala = 10;
                break;

            case 2:
                escala = 25;
                break;
            case 3:
                escala = 50;
                break;
            case 4:
                escala = 75;
                break;
            case 5:
                escala = 100;
                break;
        }
        Parameters.setRuntimeScale(escala);
        Log.printLine("Escala gerada = " + escala);
        //***

        //Variáveis de Controle booelanas - Uso: Ativação/Desativação de Datacenters - conforme plano experimental
        boolean a1 = true;
        boolean a2 = true;
        boolean a3 = true;
        boolean a4 = true;
        boolean a5 = true;

        //######################################################################
        //CONFIGJRAÇÕES DE EXPERIMENTO
        //F1 - Algoritmo
        Parameters.SchedulingAlgorithm escalonador = f1;

        Utilidades.useDynamicParameterValues(true); //Configuração para uso de valores dinâmicos nos algoritmos EDV1 e EDV2.
        if (escalonador == Parameters.SchedulingAlgorithm.EDVSCHED1) {
            Utilidades.generateParameters();
        }

        //F2 - Workflow (DAX File)  
        String daxName = f2;
        String daxPath = "" + System.getProperty("user.dir")
                + "/config/dax/" + daxName;

//        List<String> daxPaths = new ArrayList<>();
//        daxPaths.add(System.getProperty("user.dir")+"/config/dax/Montage_100.xml");
//        daxPaths.add(System.getProperty("user.dir")+"/config/dax/Montage_25.xml");
//        daxPaths.add(System.getProperty("user.dir")+"/config/dax/Montage_1000.xml");
//        daxPaths.add(System.getProperty("user.dir")+"/config/dax/Protein1AGY_Final.xml");
//        daxPaths.add(System.getProperty("user.dir")+"/config/dax/Protein1VII_Final.xml");
//        daxPaths.add(System.getProperty("user.dir")+"/config/dax/Protein1EOD_Final.xml");

//        File df;
//        for (String dp : daxPaths) {
//            df = new File(daxPath);
//            if (!df.exists()) {
//                Log.printLine("Warning: Please replace daxPath with the physical path in your working environment!");
//                return null;
//            }
//        }

        //F3 - Infraestrutura - Tabela de Configuração: foram adotados valores inteiros
        //para ajudar a definir quais Datacenters da infraestrutura serão considerados em cada experimento.
        //Os valores considerados (assumidos pela variável selectInfra) são os que seguem:
        //  Utilidades.SEM NUVEM - 0 (i.e., 4 Datacenters: A1, A2, A3 e A4)
        //  Utilidades.COM NUVEM - 1 (i.e., 5 Datacenters: A1, A2, A3, A4 e A5)
        //  Utilidades.APENAS CLUSTER - 2 (i.e., 3 Datacenters: A2, A3 e A4)
        Utilidades.selectInfra = f3;

        switch (Utilidades.selectInfra) {
            case Utilidades.SEM_NUVEM:
                a5 = false;
                Log.printLine("Experimento com infra: SEM_NUVEM");
                break;
            case Utilidades.COM_NUVEM:
                //não precisa nenhum comandos... todos os DCs já estão ativados!
                Log.printLine("Experimento com infra: COM_NUVEM");
                break;

            case Utilidades.APENAS_CLUSTER:
                Log.printLine("Experimento com infra: APENAS_CLUSTER");
                a1 = false;
                a5 = false;
                break;
        }

        if (escalonador == Parameters.SchedulingAlgorithm.EDVSCHED2) {
            Utilidades.deadlineCalculation(daxPath);
        }

        //######################################################################
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
            int vmNumA1 = 0;
            DatacenterConfiguration confA1 = null;
            VMConfiguration vmConfA1 = null;
            if (a1) {
                confA1 = new DatacenterConfiguration();
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
                vmNumA1 = 1;
                vmConfA1 = new VMConfiguration();
                vmConfA1.idShift = 1;
                vmConfA1.bw = 1000;
                vmConfA1.pesNumber = 4;
                vmConfA1.mips = 3200;
                vmConfA1.vmm = "Xen";
                vmConfA1.size = 250000;  //Tamanho de Imagem em Mbytes
                vmConfA1.ram = 8000; //MBytes
            }
            //**********************************************************************
            //Ambiente A2 - Cluster de Computadores LaSDPC
            //Aglomerado computacional: Cosmos, Andromeda, Halley
            //Estimados: 35 Hosts
            //Configuração do Datacenter do Cluster - Ambiente A2
            //Aglomerado Cosmos
            //Intel® Core™2 Quad Processor Q9400 (6M Cache, 2.66 GHz, 1333 MHz FSB)
            //Number of computers    Avg. cores/computer    GFLOPS/core     GFLOPs/computer
            //        3                    3.97                2.97             11.78
            int vmNumA2Cosmos = 0;
            DatacenterConfiguration confCosmos = null;
            VMConfiguration vmConfCosmos = null;
            if (a2) {
                confCosmos = new DatacenterConfiguration();
                confCosmos.hostId = 1000;
                confCosmos.mips = 2970;
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
                vmNumA2Cosmos = 10;
                vmConfCosmos = new VMConfiguration();
                vmConfCosmos.idShift = 1000;
                vmConfCosmos.bw = 1000;
                vmConfCosmos.pesNumber = 4;
                vmConfCosmos.mips = 2970;
                vmConfCosmos.vmm = "Xen";
                vmConfCosmos.size = 250000;  //Tamanho de Imagem em Mbytes
                vmConfCosmos.ram = 4000; //MBytes
            }
            //**********************************************************************
            //Aglomerado Andromeda
            //Equipamento:
            //AMD Processor Vishera 4.2 Ghz FX(tm)-8350 Eight-Core Processor
            //Number of computers    Avg. cores/computer    GFLOPS/core     GFLOPs/computer
            //      140                     7.96               3.31             26.33

            DatacenterConfiguration confAndromeda = null;
            VMConfiguration vmConfAndromeda = null;
            int vmNumA2Andromeda = 0;
            if (a3) {
                confAndromeda = new DatacenterConfiguration();
                confAndromeda.hostId = 2000;
                confAndromeda.mips = 3300;
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
                vmNumA2Andromeda = 10;
                vmConfAndromeda = new VMConfiguration();
                vmConfAndromeda.idShift = 2000;
                vmConfAndromeda.bw = 1000;
                vmConfAndromeda.pesNumber = 4;
                vmConfAndromeda.mips = 3300;
                vmConfAndromeda.vmm = "Xen";
                vmConfAndromeda.size = 250000;  //Tamanho de Imagem em Mbytes
                vmConfAndromeda.ram = 4000; //MBytes
            }
            //**********************************************************************
            //Aglomerado Halley
            //Equipamento:
            //Intel® Core™ I7 Processor – LGA -1150 – 4790 3.60GHZ DMI 5GT/S 8MB
            //Number of computers    Avg. cores/computer    GFLOPS/core     GFLOPs/computer
            //       1785                  7.93                4.27             33.86
            DatacenterConfiguration confHalley = null;
            VMConfiguration vmConfHalley = null;
            int vmNumA2Halley = 0;
            if (a4) {
                confHalley = new DatacenterConfiguration();
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
                vmNumA2Halley = 15;
                vmConfHalley = new VMConfiguration();
                vmConfHalley.idShift = 3000;
                vmConfHalley.bw = 1000;
                vmConfHalley.pesNumber = 4;
                vmConfHalley.mips = 4300;
                vmConfHalley.vmm = "Xen";
                vmConfHalley.size = 250000;  //Tamanho de Imagem em Mbytes
                vmConfHalley.ram = 4000; //MBytes
            }
            //**********************************************************************
            //Ambiente A3 - Nuvem Privada na SoftLayer
            //DC Nuvem Privada na SoftLayer
            //Equipamento:
            //Quad Intel Xeon E7-4850 v2 (48 Cores, 2.30 GHz)
            //Number of computers    Avg. cores/computer       GFLOPS/core     GFLOPs/computer
            //        125                  8.00                  3.99              31.94

            DatacenterConfiguration confSoftLayer = null;
            VMConfiguration vmConfSoftLayer = null;
            int vmNumA3SoftLayer = 0;
            if (a5) {
                confSoftLayer = new DatacenterConfiguration();
                confSoftLayer.hostId = 4000;
                confSoftLayer.mips = 8000;
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
                vmNumA3SoftLayer = 10;
                vmConfSoftLayer = new VMConfiguration();
                vmConfSoftLayer.idShift = 4000;
                vmConfSoftLayer.bw = 1000;
                vmConfSoftLayer.pesNumber = 8;
                vmConfSoftLayer.mips = 8000;
                vmConfSoftLayer.vmm = "Xen";
                vmConfSoftLayer.size = 250000;  //Tamanho de Imagem em Mbytes
                vmConfSoftLayer.ram = 4000; //MBytes
            }
            //**********************************************************************
            // APLICAÇÃO - WORKFLOW - Pode ser representado por um List de daxPaths!!
            // OBS: Para uso de vários workflows, ver assinatura do método Parameteres.init();
//          List<String> daxPaths = new ArrayList<>();
//          daxPaths.add("/Users/weiweich/NetBeansProjects/WorkflowSim-1.0/config/dax/Montage_100.xml");
//          daxPaths.add("/Users/weiweich/NetBeansProjects/WorkflowSim-1.0/config/dax/Montage_25.xml");
//          daxPaths.add("/Users/weiweich/NetBeansProjects/WorkflowSim-1.0/config/dax/Montage_1000.xml");
            //**********************************************************************
            File daxFile = new File(daxPath);
            if (!daxFile.exists()) {
                String err = "Warning: Please replace daxPath with the physical path in your working environment!";
                Log.printLine(err);
                return err;
            }

            //**********************************************************************
            //Configuração de Geração Dinâmica de Workflow para o Simulador
            //Gerar valores dinâmicos para os parâmetros amin, pop e ger
            //Gerar o Workflow Dinâmico: lista de jobs e tasks em formato XML (DAX)
            //**********************************************************************
            //Parâmetros de Configuração do Ambiente de Simulação - WorkflowSim
            //Escalonamento e de Sistema de Arquivos
            Parameters.SchedulingAlgorithm sch_method = escalonador;
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
            WorkflowDatacenter dcA1WorkStation = null;
            WorkflowDatacenter dcA2Cosmos = null;
            WorkflowDatacenter dcA3Andromeda = null;
            WorkflowDatacenter dcA4Halley = null;
            WorkflowDatacenter dcA5SoftLayer = null;
            if (a1) {
                dcA1WorkStation = Utilidades.createCustomDatacenter("DC_WorkStation", confA1);
            }
            //Criação do Datacenter - Ambiente A2 - Cosmos
            if (a2) {
                dcA2Cosmos = Utilidades.createCustomDatacenter("DC_Cosmos", confCosmos);
            }

            //Criação do Datacenter - Ambiente A2 - Andromeda
            if (a3) {
                dcA3Andromeda = Utilidades.createCustomDatacenter("DC_Andromeda", confAndromeda);
            }

            //Criação do Datacenter - Ambiente A2 - Halley
            if (a4) {
                dcA4Halley = Utilidades.createCustomDatacenter("DC_Halley", confHalley);
            }

            //Criação do Datacenter - Ambiente A3 - SoftLayer
            if (a5) {
                dcA5SoftLayer = Utilidades.createCustomDatacenter("DC_SoftLayer", confSoftLayer);
            }

            //Inicialização do WorkflowSim - definição de 5 escalonadores
            WorkflowPlanner wfPlanner = new WorkflowPlanner("planner_0", 1);
            WorkflowEngine wfEngine = wfPlanner.getWorkflowEngine();

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            List<CondorVM> vmListA1 = null;
            List<CondorVM> vmListA2Cosmos = null;
            List<CondorVM> vmListA2Andromeda = null;
            List<CondorVM> vmListA2Halley = null;
            List<CondorVM> vmListA3SoftLayer = null;
            //Criando as listas de VMs para cada Datacenter
            if (a1) {
                vmListA1 = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA1, vmConfA1);
            }
            if (a2) {
                vmListA2Cosmos = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA2Cosmos, vmConfCosmos);
            }
            if (a3) {
                vmListA2Andromeda = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA2Andromeda, vmConfAndromeda);
            }
            if (a4) {
                vmListA2Halley = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA2Halley, vmConfHalley);
            }
            if (a5) {
                vmListA3SoftLayer = Utilidades.createCustomVM(wfEngine.getSchedulerId(0), vmNumA3SoftLayer, vmConfSoftLayer);
            }

            //Imprimindo tamanho das listas de VMs
            if (a1) {
                Log.printLine("vmLst1 = " + vmListA1.size());
            }
            if (a2) {
                Log.printLine("vmLst2 = " + vmListA2Cosmos.size());
            }
            if (a3) {
                Log.printLine("vmLst3 = " + vmListA2Andromeda.size());
            }
            if (a4) {
                Log.printLine("vmLst4 = " + vmListA2Halley.size());
            }
            if (a5) {
                Log.printLine("vmLst5 = " + vmListA3SoftLayer.size());
            }

            //Enviando listas de VMs ao componente WorkFlowEngine
            if (a1) {
                wfEngine.submitVmList(vmListA1, 0);
            }
            if (a2) {
                wfEngine.submitVmList(vmListA2Cosmos, 0);
            }
            if (a3) {
                wfEngine.submitVmList(vmListA2Andromeda, 0);
            }
            if (a4) {
                wfEngine.submitVmList(vmListA2Halley, 0);
            }
            if (a5) {
                wfEngine.submitVmList(vmListA3SoftLayer, 0);
            }

            //Iniciar Simulação e coletar resultados de experimento
            if (a1) {
                wfEngine.bindSchedulerDatacenter(dcA1WorkStation.getId(), 0);
            }
            if (a2) {
                wfEngine.bindSchedulerDatacenter(dcA2Cosmos.getId(), 0);
            }
            if (a3) {
                wfEngine.bindSchedulerDatacenter(dcA3Andromeda.getId(), 0);
            }
            if (a4) {
                wfEngine.bindSchedulerDatacenter(dcA4Halley.getId(), 0);
            }
            if (a5) {
                wfEngine.bindSchedulerDatacenter(dcA5SoftLayer.getId(), 0);
            }

            
            //******************************************************************
            //Configuração de Delays de Rede
            //* * * Valores definidos para Latência * * * 
            //1. Do Broker (WorkFlowScheduler) para os DCs... 100 ms
            //2. Entre os Datacenters: 250 ms
            //3. Para Internet: 500 ms
            boolean delay = true;

            if (delay) {
                Log.printLine("---Network Delay ON...");
                //Latência do WorkFlowScheduler (Broker) para os DCs...
                if (a1) {
                    NetworkTopology.addLink(wfEngine.getSchedulerId(0), dcA1WorkStation.getId(), 1000, 0.10);
                }
                if (a2) {
                    NetworkTopology.addLink(wfEngine.getSchedulerId(0), dcA2Cosmos.getId(), 1000, 0.10);
                }
                if (a3) {
                    NetworkTopology.addLink(wfEngine.getSchedulerId(0), dcA3Andromeda.getId(), 1000, 0.10);
                }
                if (a4) {
                    NetworkTopology.addLink(wfEngine.getSchedulerId(0), dcA4Halley.getId(), 1000, 0.10);
                }
                if (a5) {
                    NetworkTopology.addLink(wfEngine.getSchedulerId(0), dcA5SoftLayer.getId(), 1000, 0.5);
                }
                //*****
                //Latência entre os DCs...
                if (a1 && a2) {
                    NetworkTopology.addLink(dcA1WorkStation.getId(), dcA2Cosmos.getId(), 1000, 0.25);
                }
                if (a1 && a3) {
                    NetworkTopology.addLink(dcA1WorkStation.getId(), dcA3Andromeda.getId(), 1000, 0.25);
                }
                if (a1 && a4) {
                    NetworkTopology.addLink(dcA1WorkStation.getId(), dcA4Halley.getId(), 1000, 0.25);
                }
                if (a1 && a5) {
                    NetworkTopology.addLink(dcA1WorkStation.getId(), dcA5SoftLayer.getId(), 1000, 0.5);
                }
                //***
                if (a2 && a3) {
                    NetworkTopology.addLink(dcA2Cosmos.getId(), dcA3Andromeda.getId(), 1000, 0.25);
                }
                if (a2 && a4) {
                    NetworkTopology.addLink(dcA2Cosmos.getId(), dcA4Halley.getId(), 1000, 0.25);
                }
                if (a2 && a5) {
                    NetworkTopology.addLink(dcA2Cosmos.getId(), dcA5SoftLayer.getId(), 1000, 0.5);
                }
                //***
                if (a3 && a4) {
                    NetworkTopology.addLink(dcA3Andromeda.getId(), dcA4Halley.getId(), 1000, 0.25);
                }
                if (a3 && a5) {
                    NetworkTopology.addLink(dcA3Andromeda.getId(), dcA5SoftLayer.getId(), 1000, 0.5);
                }
                //***
                if (a4 && a5) {
                    NetworkTopology.addLink(dcA4Halley.getId(), dcA5SoftLayer.getId(), 1000, 0.5);
                }
            }
            //******************************************************************
            
            Utilidades.preencherTabelaDatacenters();
            
            //Ativando Monitor de Recursos
            MonitorRecursos mr = new MonitorRecursos("Mon_Recursos", wfEngine.getScheduler(0));
            
            CloudSim.startSimulation();
            List<Job> outputList0 = wfEngine.getJobsReceivedList();
            CloudSim.stopSimulation();
            //CloudSim.terminateSimulation(300);

            //Gerando a saída para o Monitor e Arquivos (pasta relatorios)
            String retorno = Utilidades.printJobList(outputList0);
            //Log.printLine(retorno);
            Utilidades.registraSaidaCloudSimReport(retorno);
            //Shishido - impressão do grafo de Workflow executado
            //Utilidades.printWorkflow(outputList0);
            return "";
        } catch (Exception ex) {
            Logger.getLogger(Experimento20.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}

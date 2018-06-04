package br.usp.icmc.lasdpc.BeQoS.classes;

import br.usp.icmc.lasdpc.workflowarch.edvard.VMAllocationPolicyForMultiDatacenters;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.workflowsim.ClusterStorage;
import org.workflowsim.CondorVM;
import org.workflowsim.Job;
import org.workflowsim.Task;
import org.workflowsim.WorkflowDatacenter;
import org.workflowsim.utils.Parameters;

/**
 *
 * @author Mario Pardo
 * @author Edvard Oliveira
 */
public class Utilidades {

    public static String saidaMonRecursos;  //Variável de informações de saída da classe MonitorRecursos

    public static final int SEM_NUVEM = 0;
    public static final int COM_NUVEM = 1;
    public static final int APENAS_CLUSTER = 2;
    public static int selectInfra;
    public static Parameters.SchedulingAlgorithm escalonador;
    public static String nomeWorkflow;

    public static StringBuffer saidaConsumoRecursos = new StringBuffer();

    public static final int DEFAULT_MIPS_DC = (4 * 3200); //Valor dos MIPS do menor equipamento do cenário, i.e., VM da WorkStation. (A1)   
    public static boolean dynamicParameterValues;
    public static double overhead; //Overhead em valores de porcentagem (%)
    public static long deadline = 0; //Deadline do Workflow (em segundos)
    public static long totalWorkloadLength = 0; //valor do tamanho do Workflow em MI
    public static long workflowExecutionTimePreview;

    public static Map<Integer, String> tabelaDatacenters = new HashMap();
    public static Map<String, Long> tabelaJobs = new HashMap();

    public static StringBuffer saidaCloudSimReport = new StringBuffer();

    public static int amin = 0;  //Número de Aminoácidos
    public static int ger = 0;  //Número de Gerações
    public static int pop = 0;  //Tamanho da população

    public static List<CondorVM> createCustomVM(int userId, int numberOfVms, VMConfiguration conf) {

        //VM Parameters
        long size = conf.size;
        int ram = conf.ram; //vm memory (MB)
        int mips = conf.mips;
        long bw = conf.bw;
        int pesNumber = conf.pesNumber; //number of cpus
        String vmm = conf.vmm; //VMM name
        //Lista de Máquinas Virtuais
        LinkedList<CondorVM> list = new LinkedList<>();

        CondorVM[] vm = new CondorVM[numberOfVms];
        //Looping de Criação de VMs
        for (int i = 0; i < numberOfVms; i++) {
//            CondorVM vmTemp = new CondorVM(conf.idShift++, userId, mips,
//                    pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
//            list.add(vmTemp);
            vm[i] = new CondorVM(conf.idShift++, userId, mips,
                    pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm[i]);

        }
        return list;
    }

    public static WorkflowDatacenter createCustomDatacenter(String name, DatacenterConfiguration conf) {

        try {

            //Parâmetros de host enviados como argumentos de entrada.
            int hostId = conf.hostId;  //índice inicial para identificação dos Hosts
            int mips = conf.mips; //MIPS por core
            int num_cores = conf.num_cores; //Núcleos (cores físico) por Processador
            int num_hosts = conf.num_hosts; //Número de Máquinas
            int ram = conf.ram; //memória RAM dos Hosts
            long storage = conf.storage; // disco do Host
            int bw = conf.bw; //Bandwidth - banda de rede do Host
            String arch = conf.arch; // Arquitetura dos computadores
            String os = conf.os; // Sistema Operacional
            String vmm = conf.vmm; //Monitor de Máquina Virtual

            //Parâmetros de Modelo de Custo (pay-as-you-go)
            double time_zone = conf.time_zone; // tarifa de fuso horário do recurso alocado
            double cost = conf.cost; // custo de uso de Processamento de CPU
            double costPerMem = conf.costPerMem; // custo de uso de memória
            double costPerStorage = conf.costPerStorage; // custo de uso de Storage (disco)
            double costPerBw = conf.costPerBw; // custo de uso de Rede

            //Parâmetros de Storage
            int maxTransferRate = conf.maxTransferRate;

            // 1. Criando Lista de Hosts (Máquinas Físicas) do DataCenter
            List<Host> hostList = new ArrayList<>();

            // 2.Criando uma Lista de Elementos de Processamento - Entenda-se: CPUs ou Cores
            List<Pe> peList = new ArrayList<>();

            // 3. Criando uma lista de componentes de Storage (disco)
            LinkedList<Storage> storageList = new LinkedList<>();

            // 4. Criar os PEs e alimentar a lista
            peList.clear();
            for (int i = 0; i < num_cores; i++) {
                Pe temp = new Pe(i, new PeProvisionerSimple(mips));
                peList.add(temp);
            }

            // 5. Criar os Hosts com ID, PEs e adiciona-los na lista de Hosts
            //preciso de um for...
            for (int i = 0; i < num_hosts; i++) {
                //Instanciando e adicionando a máquina na lista de Hosts
                hostList.add(
                        new Host(
                                hostId++,
                                new RamProvisionerSimple(ram),
                                new BwProvisionerSimple(bw),
                                storage,
                                peList,
                                //new VmSchedulerTimeShared(peList)));
                                new VmSchedulerSpaceShared(peList)));
            }

            // 6. Criando o objeto de Características de DataCenter
            // Este inclui definições como: arquitetura, SO,
            // lista de máquinas (hosts), política de alocação,
            // gestão de fuso horários e custo (G$/Pe time unit)
            WorkflowDatacenter datacenter = null;

            DatacenterCharacteristics characteristics
                    = new DatacenterCharacteristics(
                            arch, os, vmm, hostList, time_zone,
                            cost, costPerMem, costPerStorage,
                            costPerBw);

            // 7. Criando a unidade de Storage do Host
            HarddriveStorage hd1 = new HarddriveStorage("hd1", 1e12);
            hd1.setMaxTransferRate(maxTransferRate);
            storageList.add(hd1);

            /**
             * The bandwidth between data centers.
             */
            double interBandwidth = 1.5e7;

            /**
             * The bandwidth within a data center.
             */
            double intraBandwidth = interBandwidth * 2;
            ClusterStorage s1 = new ClusterStorage(name, 1e12);
            s1.setBandwidth(name, interBandwidth);

            switch (name) {
                case "DC_WorkStation":
                    s1.setBandwidth("DC_Cosmos", interBandwidth);
//                    s1.setBandwidth("DC_Andromeda", interBandwidth);
//                    s1.setBandwidth("DC_Halley", interBandwidth);
//                    s1.setBandwidth("DC_SoftLayer", interBandwidth);
                    break;
                case "DC_Cosmos":
                    s1.setBandwidth("DC_WorkStation", interBandwidth);

//                    s1.setBandwidth("DC_Halley", interBandwidth);
//                    s1.setBandwidth("DC_SoftLayer", interBandwidth);
                    break;
                case "DC_Andromeda":
                    s1.setBandwidth("DC_Andromeda", interBandwidth);
                    break;
                case "DC_SoftLayer":
                    s1.setBandwidth("DC_SoftLayer", interBandwidth);
                    break;
                case "DC_Halley":
                    s1.setBandwidth("DC_Halley", interBandwidth);
                    break;

            }

            // The bandwidth within a data center
            s1.setBandwidth("local", intraBandwidth);
            // The bandwidth to the source site 
            s1.setBandwidth("source", interBandwidth);
            storageList.add(s1);
            // 8. Criando o Datacenter
            datacenter = new WorkflowDatacenter(name,
                    characteristics,
                    //new VmAllocationPolicySimple(hostList),
                    new VMAllocationPolicyForMultiDatacenters(hostList),
                    storageList, 0);

            //Alimentando a tabela de Datacenters
            //tabelaDatacenters.put(datacenter.getId(), datacenter.getName());
            return datacenter;
        } catch (ParameterException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String printJobList(List<Job> list) {
        String indent = "\t";
        String output;
        //Comandos para a limpeza de memória da JVM
        System.gc();
        System.runFinalization();

        output = ""; //Inicializando variável de saída de dados

        //Informações de Cabeçalho do Relatório de Saída (Output Report)
        output += "Job_ID" + indent
                + "Task_ID" + indent
                + "STATUS" + indent
                + "DataCenter_ID" + indent
                + "VM_ID" + indent
                + "Execution_Time" + indent
                + "Start_Time" + indent
                + "Finish_Time" + indent
                + "Transf_Time" + indent
                + "Depth" + indent
                + "Cloudlet_Length" + indent
                + "Initial_Length" + indent
                + "Extra_Length" + indent
                + "Network_Delay" + indent
                + "Job_Name\n";

        //Imprimindo em vídeo o cabeçalho do relatório de saída
        Log.printLine();
        Log.printLine("========== RESULTADOS DA EXECUÇÃO DAS TAREFAS (Jobs/Cloudlets) ==========");
        System.out.println(output);
        //***

        //Configurando objeto formatador de valores numéricos...
        DecimalFormat dft = new DecimalFormat("######.##");

        //* * * Looping de Impressão de dados de cada uma das tarefas executadas * * * 
        for (Job job : list) {
            String linha = "";  //Variável para armazenar uma linha da saída

            linha += job.getCloudletId() + indent;

            if (job.getClassType() == Parameters.ClassType.STAGE_IN.value) {
                //linha += "Stage-in" + indent;
                continue;
            } else {
                for (Task task : job.getTaskList()) {
                    linha += task.getCloudletId() + ",";
                }
                linha += indent;
            }

            if (job.getCloudletStatus() == Cloudlet.SUCCESS) {
                linha += "SUCCESS";
                linha += indent + job.getResourceId()
                        + indent + job.getVmId()
                        + indent + dft.format(job.getActualCPUTime())
                        + indent + dft.format(job.getExecStartTime())
                        + indent + dft.format(job.getFinishTime())
                        + indent + dft.format(job.getExtraTime())
                        + indent + job.getDepth()
                        + indent + job.getCloudletTotalLength()
                        + indent + (job.getInitialCloudletLength())
                        + indent + job.getExtraSize()
                        + indent + dft.format(job.getNetworkDelayTime() - job.getFinishTime())
                        + indent + job.getName();
            } else if (job.getCloudletStatus() == Cloudlet.FAILED) {
                linha += "FAILED";
                linha += job.getResourceId()
                        + indent + job.getVmId()
                        + indent + dft.format(job.getActualCPUTime())
                        + indent + dft.format(job.getExecStartTime())
                        + indent + dft.format(job.getFinishTime())
                        + indent + job.getDepth()
                        + indent + job.getCloudletTotalLength()
                        + indent + (job.getInitialCloudletLength())
                        + indent + job.getExtraSize()
                        + indent + dft.format(job.getNetworkDelayTime() - job.getFinishTime())
                        + indent + job.getName();
            }
            System.out.println(linha);
            output += linha + "\n";
        }
        Log.printLine("========== FIM DOS RESULTADOS ==========");
        return output;
    }

    public static void printWorkflow(List<?> taskList) {

//        Log.enable();
        Log.printLine("\n\n\n");

        Log.printLine("digraph {");
        for (Object t1 : taskList) {
            Task task = (Task) t1;

            for (Object t2 : task.getParentList()) {
                Task parent = (Task) t2;

                Log.printLine(parent.getCloudletId() + " -> " + task.getCloudletId() + ";");
                Log.printLine(parent.getCloudletId() + " [label=\"" + task.getCloudletId() + "\"]");
            }

            if (task.getParentList().size() == 0) {

                Log.printLine(task.getCloudletId() + " [label=\"" + task.getCloudletId() + "\"]");
            }
        }
        Log.printLine("}");
        Log.disable();
    }

    public static void gravaXLSSaidaCloudSimReport(String nomeArq2) {
        gravaXLSSaidaCloudSimReport();
        String nomeArq = "relatorios" + File.separator + "SaidaCloudSimReport.xls";
        File original = new File(nomeArq);
        if (original.exists()) {
            File novoNome = new File("relatorios" + File.separator + nomeArq2 + ".xls");
            original.renameTo(novoNome);
        }
    }

    public static void gravaXLSSaidaCloudSimReport() {
        BufferedWriter arquivo = null;
        String linha = "";
        String nomeArq = "relatorios" + File.separator + "SaidaCloudSimReport.xls";
        String cabecalho = "";
        String indent = "\t";
        try {
            File file = new File(nomeArq);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
            file = null;
            arquivo = new BufferedWriter(new FileWriter(nomeArq, true));
            linha = getSaidaCloudSimReport();
            arquivo.write(linha);
            arquivo.newLine();
            arquivo.close();

        } catch (IOException ex) {
            Logger.getLogger(
                    Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                arquivo.close();
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String getSaidaCloudSimReport() {
        return saidaCloudSimReport.toString();
    }

    public static void setSaidaCloudSimReport(StringBuffer saidaCloudSimReport) {
        Utilidades.saidaCloudSimReport = saidaCloudSimReport;
    }

    public static void clearSaidaCloudSimReport() {
        saidaCloudSimReport.delete(0, saidaCloudSimReport.length());
    }

    public static void registraSaidaCloudSimReport(String valor) {
        saidaCloudSimReport.append(valor).append("\n");
    }

    public static boolean isDynamicParameterValues() {
        return dynamicParameterValues;
    }

    public static void useDynamicParameterValues(boolean dynamicParameterValues) {
        Utilidades.dynamicParameterValues = dynamicParameterValues;
    }

    public static void generateParameters() {
        //Mínimo e Máximo para número de aminoácidos
        int minAmin = 20;
        int maxAmin = 200;

        //Mínimo e Máximo para número de populações
        int minPop = 20;
        int maxPop = 300;

        //Mínimo e Máximo para número de gerações
        int minGer = 15;
        int maxGer = 200;

        //Geração dinâmica dos valores para amin, pop e ger com base 
        //nos intervalos observados na estatística do CASP.
        amin = (int) (minAmin + Math.random() * maxAmin);
        pop = (int) (minPop + Math.random() * maxPop);
        ger = (int) (minGer + Math.random() * maxGer);

    }

    public static void deadlineCalculation(String daxFile) {

        double deadline_factor = 0.0;
        if (!daxFile.isEmpty()) {
            try {
                //1. Rotina para obtenção do valor do overhead do Workflow no arquivo DAX (valor CONSTANTE)
                SAXBuilder builder = new SAXBuilder();
                Document dom = builder.build(new File(daxFile));
                Element root = dom.getRootElement();
                List<Element> list = root.getChildren();
                for (Element node : list) {
                    switch (node.getName().toLowerCase()) {
                        case "parameters":
                            overhead = (long) Integer.parseInt(node.getAttributeValue("overhead"));
                            break;
                    }

                    if (overhead > 0) {
                        break;
                    }
                }
                //**************************************************************
                //2. Rotina para obtenção do tamanho do Workflow em MI (Milhões de Instruções)
                for (Element node : list) {
                    switch (node.getName().toLowerCase()) {
                        case "job":
                            long length = 0;
                            double runtime = 0.0;
                            if (node.getAttributeValue("runtime") != null) {
                                String nodeTime = node.getAttributeValue("runtime");
                                runtime = 1000 * Double.parseDouble(nodeTime);
//                                System.out.println("runtime = " + runtime);
                                if (runtime < 100) {
                                    runtime = 100;
                                }
                                length = (long) runtime;

                            } else {
                                length = 100;
                            }

                            //Ajuste da carga pela variável de ESCALA (RuntimeScale).
//                            System.out.println("length antes = " + length);
                            length *= Parameters.getRuntimeScale();
//                            System.out.println("length depois= " + length);
                            String id = node.getAttributeValue("id");
                            tabelaJobs.put(id, length);
                            totalWorkloadLength += length;
                            //System.out.println("Job número " + node.getAttributeValue("id") + " = " + length + " segundos");
                            break;
                    }
                }

                //**************************************************************
                //System.out.println("* * * Verificação de tags CHILD * * * ");
                long tempo = 0;
                for (Element node : list) {
                    switch (node.getName().toLowerCase()) {
                        case "child":
                            List<Element> pList = node.getChildren();
                            ArrayList<String> parents = new ArrayList();
                            String childName = node.getAttributeValue("ref");
//                            System.out.println("Child ID = " + childName);
//                            System.out.println("---> Lista de Jobs Pais "
//                                    + "(PARENTS) <---");
                            for (Element parent : pList) {
                                String parentName = parent.getAttributeValue("ref");
//                                System.out.println("Parent ID = " + parentName);
                                parents.add(parentName);
                            }
//                            System.out.println("****Verificação do componente "
//                                    + "Parent de maior tamanho****");
                            long maior = 0;
                            for (String s : parents) {
                                long atual = tabelaJobs.get(s);
                                if (atual >= maior) {
                                    maior = atual;
                                }
                            }
                            tempo += (maior / Utilidades.DEFAULT_MIPS_DC);
                        //System.out.println("######Tempo total de execução = " + tempo + " segundos #####");

                    }

                }
                //**************************************************************

                //**************************************************************
                //3. Cálculo do deadline total (dinâmico) do Workflow
//                System.out.println("Overhead do Workflow = " + overhead);
//                System.out.println("Tamanho Workflow Normal = " + totalWorkloadLength);
//                if (overhead > 0) {  //Adicionando os MIs do overhead
//                    totalWorkloadLength += ((totalWorkloadLength * overhead) / 100);
//                }
//                System.out.println("Total Workflow Length com Overhead = " + totalWorkloadLength);
                //Geração do Fator de Deadline dinâmico do Workflow
                deadline_factor = Math.random();
                if (deadline_factor > 0.25) {
                    deadline_factor -= 0.25;
                }
//                System.out.println("deadline factor gerado = " + deadline_factor);
                //***

                //Fórmula de Cálculo do Deadline Dinâmico
                //long tempoNormal = (long) (totalWorkloadLength*Parameters.getRuntimeScale()) / DEFAULT_MIPS_DC;
                //long tempoNormal = (long) totalWorkloadLength / DEFAULT_MIPS_DC;
                long tempoNormal = tempo;
                workflowExecutionTimePreview = tempoNormal;
                deadline = (long) (tempoNormal - (tempoNormal * deadline_factor));
                //System.out.println("workflow deadline time = " + deadline + " segundos.");
                //**************************************************************
            } catch (JDOMException | IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void gravaXLSConsumoRecursos(String nomeArq2) {
        
        nomeArq2 = "relatorios" + File.separator + nomeArq2 + ".xls";
        BufferedWriter arquivo = null;
        
        String cabecalho = "";
        String linha;
        final String indent = "\t";

        try {
            
            //Verificando se o arquivo já existe e criando, se necessário.
            File f = new File(nomeArq2);
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            } else {
                f.createNewFile();
            }
            f = null;
            
            //Abrindo o arquivo para inclusão de conteúdo.
            arquivo = new BufferedWriter(new FileWriter(nomeArq2, true));
            linha = getSaidaConsumoRecursos();
            arquivo.write(linha);
            arquivo.newLine();
            arquivo.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                arquivo.close();
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String getSaidaConsumoRecursos() {
        return saidaConsumoRecursos.toString();
    }

    public static void registraConsumoRecursos(String valor) {
        saidaConsumoRecursos.append(valor).append("\n");
    }

    public static void preencherTabelaDatacenters() {
        //System.out.println("$$$$$ Preenchendo a tabela de Datacenters $$$$$");
        List lstEntidades = CloudSim.getEntityList();
        for (Object se : lstEntidades) {
            if (se instanceof WorkflowDatacenter) {
                //System.out.println("É um datacenter!!");
                WorkflowDatacenter dc = (WorkflowDatacenter) se;
                Utilidades.tabelaDatacenters.put(dc.getId(), dc.getName());
            }
        }
    }

    public static void gravarConfiguracoesExperimento(String nomeArq) {
        System.out.println("****************************************************");
        System.out.println("^^^Configurações do Experimento executado^^^");
        BufferedWriter arquivo = null;
        StringBuffer conteudo = new StringBuffer();
        String s = "";
        nomeArq = "relatorios" + File.separator + nomeArq + ".xls";
        try {
            File file = new File(nomeArq);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
            //------------------------------------------------------------------
            //Ajustando o conteúdo do arquivo
            String indent = "\t";
            String algoritmo = "";

            if (null != Utilidades.escalonador) {
                switch (Utilidades.escalonador) {
                    case ROUNDROBIN:
                        algoritmo = "ROUND_ROBIN";
                        break;
                    case EDVSCHED1:
                        algoritmo = "SCHED1";
                        break;
                    case EDVSCHED2:
                        algoritmo = "SCHED2";
                        break;
                    default:
                        break;
                }
            }

            String infra = "";

            switch (selectInfra) {
                case Utilidades.APENAS_CLUSTER:
                    infra = "APENAS_CLUSTER";
                    break;
                case Utilidades.SEM_NUVEM:
                    infra = "SEM_NUVEM";
                    break;
                case Utilidades.COM_NUVEM:
                    infra = "COM_NUVEM";
                    break;
                default:
                    break;
            }

            s = "F1 - Algoritmo" + indent + algoritmo + "\n";
            conteudo.append(s);
            s = "F2 - Workflow" + indent + Utilidades.nomeWorkflow + "\n";
            conteudo.append(s);
            s = "F3 - Infraestrutura" + indent + infra + "\n\n";
            conteudo.append(s);

            if (algoritmo.equals("ROUND_ROBIN")) {
                //Nada a relatar...
            } else if (algoritmo.equals("SCHED1")) {
                s = "amin" + indent + Utilidades.amin + "\n";
                conteudo.append(s);
                s = "pop" + indent + Utilidades.pop + "\n";
                conteudo.append(s);
                s = "ger" + indent + Utilidades.ger + "\n";
                conteudo.append(s);
            } else if (algoritmo.equals("SCHED2")) {
                s = "deadline" + indent + Utilidades.deadline + "\n";
                conteudo.append(s);
                s = "executionTimePreview" + indent + Utilidades.workflowExecutionTimePreview + "\n";
                conteudo.append(s);
            }
            //------------------------------------------------------------------
            file = null;
            arquivo = new BufferedWriter(new FileWriter(nomeArq, true));
            //Inserindo o conteúdo no arquivo
            System.out.println(conteudo.toString());
            System.out.println("****************************************************");
            arquivo.write(conteudo.toString());
            arquivo.newLine();
            arquivo.close();
            conteudo.delete(0, conteudo.length());
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                arquivo.close();
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

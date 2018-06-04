package br.usp.icmc.lasdpc.workflowarch.edvard;

import br.usp.icmc.lasdpc.BeQoS.classes.Utilidades;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.workflowsim.CondorVM;
import org.workflowsim.FileItem;
import org.workflowsim.WorkflowDatacenter;
import org.workflowsim.WorkflowSimTags;
import org.workflowsim.scheduling.BaseSchedulingAlgorithm;
import org.workflowsim.utils.Parameters;

/**
 *
 * @author Edvard Oliveira
 * @author Mário Pardo
 *
 * Este algoritmo é referente à opção de escalonamento: EDVSCHED2.
 */
public class WorkloadLengthSchedulingAlgorithm extends BaseSchedulingAlgorithm {

    private String daxPath;
    private long totalWorkloadLength;
    private long deadline;
    private double overheadPerc; //Porcentagem de Overhead gerado no processamento (%)
    private HashMap<Integer, Long> mipsDCs;
    private int selectedDC;

    public WorkloadLengthSchedulingAlgorithm() {
        daxPath = Parameters.getDaxPath();
        totalWorkloadLength = 0;
        overheadPerc = 0.0;
        Log.printLine("\n\n.....Usando Escalonador SCHED2.....");
    }

    @Override
    public void run() throws Exception {

        if (Utilidades.isDynamicParameterValues()) {
            if (Utilidades.deadline != 0) {
                deadline = Utilidades.deadline;
                totalWorkloadLength = Utilidades.totalWorkloadLength;
                overheadPerc = Utilidades.overhead;
            }
        } else {
            readParameters();
        }
        calcDatacentersProcessingCapacity();
        pickDatacenterToWorkload();
        executeScheduling();

        Log.printLine("***********************************************");

    }

    private void readParameters() {

        totalWorkloadLength = 0;
        try {
            SAXBuilder builder = new SAXBuilder();
            Document dom = builder.build(new File(daxPath));
            Element root = dom.getRootElement();
            List<Element> list = root.getChildren();
            for (Element node : list) {
                switch (node.getName().toLowerCase()) {
                    case "job":
                        long length = 0;
                        double runtime;
                        if (node.getAttributeValue("runtime") != null) {
                            String nodeTime = node.getAttributeValue("runtime");
                            //runtime = 1000 * Double.parseDouble(nodeTime);
                            runtime = 1000 * Double.parseDouble(nodeTime);
                            if (runtime < 100) {
                                runtime = 100;
                            }
                            length = (long) runtime;

                        } else {
                            length = 100;
                        }

                        //multiple the scale, by default it is 1.0
                        length *= Parameters.getRuntimeScale();
                        totalWorkloadLength += length;
                        break;
                    case "parameters":
                        if (node.getAttributeValue("deadline") != null) {
                            deadline = Integer.parseInt(node.getAttributeValue("deadline"));
                            Log.printLine("==> Deadline do workload = " + deadline + " segundos.");
                        }
                        if (node.getAttributeValue("overhead") != null) {
                            overheadPerc = Double.parseDouble(node.getAttributeValue("overhead"));
                            Log.printLine("==> Overhead do workload = " + overheadPerc + " %.");
                        }

                        break;
                }
            }
            Log.printLine("Tamanho total do Workload = " + totalWorkloadLength + " MI");

            long totalLengthOverhead = (long) (totalWorkloadLength + ((totalWorkloadLength * overheadPerc) / 100));
            Log.printLine("Tamanho total com Overhead de " + overheadPerc + "% : " + (long) totalLengthOverhead);
            if (overheadPerc > 0) {
                totalWorkloadLength = totalLengthOverhead;
            }

        } catch (JDOMException | IOException ex) {
            Logger.getLogger(MultiDatacenterSchedulingAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void calcDatacentersProcessingCapacity() {
//        //Acesso ao mapa de Datacenters criados no ambiente de simulação
////        Map<Integer, String> dcs = Utilidades.tabelaDatacenters;
////        mipsDCs = new HashMap<Integer, Long>();
//        for (Map.Entry pair : Utilidades.tabelaDatacenters.entrySet()) {
//            WorkflowDatacenter dc = (WorkflowDatacenter) CloudSim.getEntity((int) pair.getKey());
//
//            long totalMipsDC = 0;
//            List<CondorVM> vmlst = dc.getVmList();
//            for (CondorVM v : vmlst) {
//                //totalMipsDC += (v.getMips() * v.getNumberOfPes());
//                totalMipsDC += (v.getMips());
//            }
//            mipsDCs.put((int) pair.getKey(), totalMipsDC);
//            Log.printLine("Total de MIPS do DC nome = " + dc.getName() + " e ID = " + pair.getKey() + " = " + totalMipsDC + " MIPS!");
//            totalMipsDC = 0;
//        }
//        Log.printLine("Total de DCs no cenário: " + mipsDCs.size());
//
//        //Colocar os DCs em ordem crescente de capacidade por MIPS...
//        Log.printLine("DCs ordenados por MIPS (ordem crescente)");
//        Map<Integer, Long> mpOrdered = sortByValues(mipsDCs);
//        for (Map.Entry pair : mpOrdered.entrySet()) {
//            Log.printLine(pair.getKey() + " : " + pair.getValue());
//        }
//        Log.printLine("***********************");
//        mipsDCs = (HashMap) mpOrdered;
//    }
    public void calcDatacentersProcessingCapacity() {
        //Acesso ao mapa de Datacenters criados no ambiente de simulação
        mipsDCs = new HashMap <Integer, Long>();
        List lstEntidades = CloudSim.getEntityList();
        for (Object se : lstEntidades) {
            WorkflowDatacenter dc = null;
            if (se instanceof WorkflowDatacenter) {
//                System.out.println("É um datacenter!!");
                dc = (WorkflowDatacenter) se;
                Utilidades.tabelaDatacenters.put(dc.getId(), dc.getName());
            

            long totalMipsDC = 0;
            List<CondorVM> vmlst = dc.getVmList();
            for (CondorVM v : vmlst) {
                //totalMipsDC += (v.getMips() * v.getNumberOfPes());
                totalMipsDC += (v.getMips());
            }
            mipsDCs.put((int) dc.getId(), totalMipsDC);
            Log.printLine("Total de MIPS do DC nome = " + dc.getName() + " e ID = " + dc.getId() + " = " + totalMipsDC + " MIPS!");
            totalMipsDC = 0;
            }
        }
        Log.printLine("Total de DCs no cenário: " + mipsDCs.size());

        //Colocar os DCs em ordem crescente de capacidade por MIPS...
        Log.printLine("DCs ordenados por MIPS (ordem crescente)");
        Map<Integer, Long> mpOrdered = sortByValues(mipsDCs);
        for (Map.Entry pair : mpOrdered.entrySet()) {
            Log.printLine(pair.getKey() + " : " + pair.getValue());
        }
        Log.printLine("***********************");
        mipsDCs = (HashMap) mpOrdered;
    }

    private HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public void pickDatacenterToWorkload() {

        long menor = deadline;
        Log.printLine("Deadline considerado no escalonador = " + menor);

        selectedDC = 0;
        for (Map.Entry pair : mipsDCs.entrySet()) {
            //WorkflowDatacenter dc = (WorkflowDatacenter) CloudSim.getEntity((int) pair.getKey());

            long execTime = (totalWorkloadLength / (long) pair.getValue());

            if (execTime <= menor) {
                menor = execTime;
                selectedDC = (int) pair.getKey();
                break;
            }
        }

        Log.printLine("Menor Tempo = " + menor);
        Log.printLine("DC escolhido = " + selectedDC);
    }

    public long calcFileExtraTransferTime(List<FileItem> requiredFiles, Cloudlet c) {
        return 0;
    }

    public List filterVmsBySelectedDatacenter() {
        Log.printLine("selectDC = " + selectedDC);
        if (selectedDC > 0) {
            List<CondorVM> newVmLst = new ArrayList();
            List vmlst = this.getVmList();
            int size = vmlst.size();
            for (int x = 0; x < size; x++) {
                CondorVM vm = (CondorVM) vmlst.get(x);
                if (vm.getHost().getDatacenter().getId() == selectedDC) {
                    newVmLst.add(vm);
                }
            }
            return newVmLst;
        } else {
            return this.getVmList(); //Caso contrário usa todas as VMs
        }
        //return null;
    }

    private final List<Boolean> hasChecked = new ArrayList<>();

    public void executeScheduling() {

        //Preparação...
        ArrayList<CondorVM> vmSet = (ArrayList) filterVmsBySelectedDatacenter();
        Collections.sort(getCloudletList(), new CloudletListComparator());
        Collections.sort(vmSet, new VmListComparator());
        //*****

        //Status do grupo de VMs (Standby ou Busy).
        for (Iterator it = getCloudletList().iterator(); it.hasNext();) {
            Cloudlet cloudlet = (Cloudlet) it.next();
            boolean stillHasVm = false;

            //Conta VMs Standby e Busy
//            int contStd = 0, contBusy = 0;
//            for (CondorVM v : vmSet) {
//                if (v.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
//                    contStd++;
//                } else {
//                    contBusy++;
//                }
//            }
//            System.out.println("Quant. VMs Standby = " + contStd);
//            System.out.println("Quant. VMs Busy = " + contBusy);
            //*****
            //Alocação para VMs IDLE com melhor MIPS possível(Standby com fila ZERO).
            CondorVM vmMaiorMIPS = null;
            double maiorMIPS = 0.0;
            for (Iterator itc = vmSet.iterator(); itc.hasNext();) {
                CondorVM vm = (CondorVM) itc.next();
                if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    double mipsAtual = vm.getMips() * vm.getNumberOfPes();
                    if (mipsAtual > maiorMIPS) {
                        vmMaiorMIPS = vm;
                        maiorMIPS = vmMaiorMIPS.getMips() * vmMaiorMIPS.getNumberOfPes();
                    }
                }
            }
            //*****
            if (vmMaiorMIPS != null) {
                stillHasVm = true;
                vmMaiorMIPS.setState(WorkflowSimTags.VM_STATUS_BUSY);
                cloudlet.setVmId(vmMaiorMIPS.getId());
                double calcFila = cloudlet.getCloudletTotalLength()
                        / (vmMaiorMIPS.getMips() * vmMaiorMIPS.getNumberOfPes());
                vmMaiorMIPS.setQueue(vmMaiorMIPS.getQueue() + calcFila);
                getScheduledList().add(cloudlet);
                Log.printLine("#IDLE# Cloudlet - " + cloudlet.getCloudletId() + " VM - " + vmMaiorMIPS.getId()
                        + " Queue Time - " + vmMaiorMIPS.getQueue());
                continue;
            }

            //Alocação para VMs Busy com Melhor MIPS e Menor Fila Possível. 
            double menorFila = Double.MAX_VALUE;
            vmMaiorMIPS = null;
            maiorMIPS = 0.0;
            for (Iterator itc = vmSet.iterator(); itc.hasNext();) {
                CondorVM vm = null;
                vm = (CondorVM) itc.next();
                double fila = vm.getQueue();
                double mipsAtual = vm.getMips() * vm.getNumberOfPes();
                if (vm.getState() == WorkflowSimTags.VM_STATUS_BUSY
                        && fila < menorFila && mipsAtual > maiorMIPS) {
                    menorFila = vm.getQueue();
                    vmMaiorMIPS = vm;
                    maiorMIPS = mipsAtual;
                }
            }

            if (vmMaiorMIPS != null) {
                stillHasVm = true;
                vmMaiorMIPS.setState(WorkflowSimTags.VM_STATUS_BUSY);
                cloudlet.setVmId(vmMaiorMIPS.getId());
                double calcFila = cloudlet.getCloudletTotalLength()
                        / (vmMaiorMIPS.getMips() * vmMaiorMIPS.getNumberOfPes());
                vmMaiorMIPS.setQueue(vmMaiorMIPS.getQueue() + calcFila);
                getScheduledList().add(cloudlet);
                Log.printLine("#IDLE# Cloudlet - " + cloudlet.getCloudletId() + " VM - " + vmMaiorMIPS.getId()
                        + " Queue Time - " + vmMaiorMIPS.getQueue());
                continue;
            }

            //no vm available 
            if (!stillHasVm) {
                break;
            }

        }

//******************************************************************************
//        setVmList(filterVmsBySelectedDatacenter());
//        int size = getCloudletList().size();
//
//        for (int i = 0; i < size; i++) {
//            Cloudlet cloudlet = (Cloudlet) getCloudletList().get(i);
//            int vmSize = getVmList().size();
//            CondorVM firstIdleVm = null;
//
//            for (int j = 0; j < vmSize; j++) {
//                CondorVM vm = (CondorVM) getVmList().get(j);
//                if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
//                    firstIdleVm = vm;
//                    break;
//                }
//            }
//            if (firstIdleVm == null) {
//                break;
//            }
//
//            for (int j = 0; j < vmSize; j++) {
//                CondorVM vm = (CondorVM) getVmList().get(j);
//                if ((vm.getState() == WorkflowSimTags.VM_STATUS_IDLE)
//                        && (vm.getCurrentRequestedTotalMips() > firstIdleVm.getCurrentRequestedTotalMips())) {
//                    firstIdleVm = vm;
//                }
//            }
//            firstIdleVm.setState(WorkflowSimTags.VM_STATUS_BUSY);
//            cloudlet.setVmId(firstIdleVm.getId());
//            getScheduledList().add(cloudlet);
//            Log.printLine("Schedules " + cloudlet.getCloudletId() + " with "
//                    + cloudlet.getCloudletLength() + " to VM " + firstIdleVm.getId()
//                    + " with " + firstIdleVm.getCurrentRequestedTotalMips());
//        }
    }

    public class VmListComparator implements Comparator<CondorVM> {

        @Override
        public int compare(CondorVM v1, CondorVM v2) {
            return Integer.compare(v1.getId(), v2.getId());
        }
    }

    public class CloudletListComparator implements Comparator<Cloudlet> {

        @Override
        public int compare(Cloudlet c1, Cloudlet c2) {
            return Integer.compare(c1.getCloudletId(), c2.getCloudletId());
        }
    }

    public double getTotalVmQueueTime(Vm vmAval) {

        //Obtendo o escalonador de tarefas da Vm (vmAval)
        CloudletSchedulerSpaceShared csspace
                = (CloudletSchedulerSpaceShared) vmAval.getCloudletScheduler();

        //Variável temporária para a soma de MIPs de fila 
        double tmp = 0.0;

        //Cálculo de MIPs na FILA da VM no instante atual
        tmp = csspace.getCurrentRemainingMips(vmAval.getId()) / (vmAval.getNumberOfPes() * vmAval.getMips());

        return tmp;
    }

}

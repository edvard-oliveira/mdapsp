package br.usp.icmc.lasdpc.workflowarch.edvard;

import br.usp.icmc.lasdpc.BeQoS.classes.Utilidades;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowSimTags;
import org.workflowsim.scheduling.BaseSchedulingAlgorithm;
import org.workflowsim.utils.Parameters;

/**
 *
 * @author Edvard Oliveira
 * @author Mario Henrique de Souza Pardo
 * @version 1.0
 *
 * Algoritmo de Escalonamento baseado na análise dos parâmetros Utilidades.amin,
 * Utilidades.ger e Utilidades.pop que estarão gravados na tag <parameters> do
 * arquivo DAX.
 *
 * Exemplo:
 * <parameters Utilidades.amin="49" Utilidades.pop="35" Utilidades.ger="10"/>
 *
 * O escalonamento das tarefas será feito com base nos valores das três
 * variáveis (Utilidades.amin, Utilidades.ger e Utilidades.pop) para apenas uma
 * infraestrutura, escolhida com base numa tabela de valores de referência
 * obtidos a partir de consulta ao grupo de trabalho de pesquisas científicas da
 * USP relacionado com esse domínio de problema.
 *
 * Este algoritmo é referente à opção de escalonamento: EDVSCHED1.
 */
public class MultiDatacenterSchedulingAlgorithm extends BaseSchedulingAlgorithm {

    private String daxPath;

    private String infraestrutura;  //Infraestrutura para execução das tarefas
    private boolean dynamicValues = false;

    public MultiDatacenterSchedulingAlgorithm() {
        super();

        //Inicializando variáveis e objetos de trabalho
        daxPath = Parameters.getDaxPath();

        Log.printLine("\n\n.....Usando Escalonador SCHED1.....");
    }

    @Override
    public void run() throws Exception {
        //Log.printLine("Método run() ->" + getCloudletList().size());

        //1-Efetuar a leitura dos parâmetros do DAX (Utilidades.amin,Utilidades.pop,Utilidades.ger)
        if (!Utilidades.isDynamicParameterValues()) {
            readParameters();  //Leitura dos Parâmetros Estáticos do Arquivo DAX
        }

        Log.printLine("Utilidades.amin = " + Utilidades.amin);
        Log.printLine("Utilidades.pop = " + Utilidades.pop);
        Log.printLine("Utilidades.ger = " + Utilidades.ger);
        //*********************************************************************************
        //2-Designar para qual infraestrutura as cloudlets serão despachadas.
        //OBS - Conforme Tabela de Cenários do Edvard
        choiceInfrastructure();
        //*********************************************************************************

        //3-Escalonar tarefas apenas para as VMs da Infraestrutura escolhida.
        //3.1-Separar VMs por infraestrutura...
        //Componentes do Mapa de Datacenters
//        printDatacentersMap();
        //**********************************************************************
        //Carregando nova lista de VMs apenas com as escolhidas por infraestrutura
        List<CondorVM> vmSet = separateVmsByInfrastructure();

        //**********************************************************************
        //Trecho copiado do algoritmo FCFS-Scheduling-Algorithm
        for (Iterator it = getCloudletList().iterator(); it.hasNext();) {
            Cloudlet cloudlet = (Cloudlet) it.next();
            boolean stillHasVm = false;
            //Mudei apenas a linha abaixo para vmSet onde era getVmList()
            for (Iterator itc = vmSet.iterator(); itc.hasNext();) {

                CondorVM vm = (CondorVM) itc.next();
                if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    stillHasVm = true;
                    vm.setState(WorkflowSimTags.VM_STATUS_BUSY);
                    cloudlet.setVmId(vm.getId());
                    getScheduledList().add(cloudlet);
                    break;
                }
            }
            //no vm available 
            if (!stillHasVm) {
                break;
            }
        }

    } //end-método

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

    /* O presente método (generateParameters) efetua a geração dinâmica dos valores para as variáveis
       Utilidades.amin, Utilidades.pop e Utilidades.ger, substituindo o uso da leitura desses parâmetros no arquivo DAX.
     */
    private void readParameters() {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document dom = builder.build(new File(daxPath));
            Element root = dom.getRootElement();
            List<Element> list = root.getChildren();
            for (Element node : list) {
                switch (node.getName().toLowerCase()) {
                    case "parameters":
                        Utilidades.amin = Integer.parseInt(node.getAttributeValue("amin"));
                        Utilidades.pop = Integer.parseInt(node.getAttributeValue("pop"));
                        Utilidades.ger = Integer.parseInt(node.getAttributeValue("ger"));
                        break;
                }

                if (Utilidades.amin > 0 && Utilidades.pop > 0 && Utilidades.ger > 0) {
                    break;
                }

            }
            Log.printLine("--->Valores lidos dos parâmetros<---");

        } catch (JDOMException | IOException ex) {
            Logger.getLogger(MultiDatacenterSchedulingAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void choiceInfrastructure() {
        Log.printLine("---Tabela de Cenários---");
        infraestrutura = null;
        if (Utilidades.amin < 50 && Utilidades.pop < 50 && Utilidades.ger < 50) { //Cenario 1
            Log.printLine("Cenário 1");
            infraestrutura = "WorkStation";
        } else if ((Utilidades.amin < 50 && Utilidades.pop >= 50) && (Utilidades.pop < 200 || Utilidades.ger < 150)) { //Cenário 2
            infraestrutura = "Cluster";
            Log.printLine("Cenário 2");
        } else if (Utilidades.selectInfra == Utilidades.COM_NUVEM) {
            if ((Utilidades.amin < 50 && Utilidades.pop >= 200) || Utilidades.ger >= 150) { //Cenário 3
                infraestrutura = "Cloud";
                Log.printLine("Cenário 3");
            }
        } else if (Utilidades.amin >= 50 && Utilidades.amin < 150 && Utilidades.pop < 30 && Utilidades.ger < 20) { //Cenário 4
            infraestrutura = "WorkStation";
            Log.printLine("Cenário 4");
        } else if (Utilidades.amin >= 50 && Utilidades.amin < 150 && Utilidades.pop >= 30 && Utilidades.pop < 80 && Utilidades.ger < 50) { //Cenário 5
            infraestrutura = "Cluster";
            Log.printLine("Cenário 5");
        } else if (Utilidades.selectInfra == Utilidades.COM_NUVEM) {
            if ((Utilidades.amin >= 50 && Utilidades.amin < 150 && Utilidades.pop >= 80) || Utilidades.ger >= 50) { //Cenário 6
                infraestrutura = "Cloud";
                Log.printLine("Cenário 6");
            }
        } else if (Utilidades.amin >= 150 && Utilidades.pop < 40 && Utilidades.ger < 40) { //Cenário 7
            infraestrutura = "Cluster";
            Log.printLine("Cenário 7");
        } else if (Utilidades.selectInfra == Utilidades.COM_NUVEM) {
            if (Utilidades.amin >= 150 && Utilidades.pop >= 40 && Utilidades.ger >= 40) { //Cenário 8
                infraestrutura = "Cloud";
                Log.printLine("Cenário 8");
            }
        }

        if (infraestrutura == null) {
            //Exceções...
            infraestrutura = "Cluster";
            Log.printLine("Cenário 9 - Exceções");
        }

        //System.out.println("Infraestrutura escolhida:" + infraestrutura);
        Log.printLine("--- ---");
    }

    public void printDatacentersMap() {
        Log.printLine("***Mapa de Datacenters***\n");
        for (Map.Entry<Integer, String> entry : Utilidades.tabelaDatacenters.entrySet()) {
            System.out.println(entry.getKey() + " / " + entry.getValue());
        }
        Log.printLine("***");
        Log.printLine("--->Tamanho da lista de VMs recebidas pelo escalonador<---");
        Log.printLine("vmList size = " + getVmList().size());
        Log.printLine("***");
    }

    public List<CondorVM> separateVmsByInfrastructure() {

        List<CondorVM> vmSet = new ArrayList();
        //Map<Integer, CondorVM> mId2Vm = new HashMap<>();
        for (int i = 0; i < getVmList().size(); i++) {
            CondorVM vmx = (CondorVM) getVmList().get(i);
            //System.out.println("infraestrutura = " + infraestrutura);
            if (vmx != null) {
                if (infraestrutura.equals("WorkStation")
                        && Utilidades.tabelaDatacenters.get(vmx.getHost().getDatacenter().getId()).equals("DC_WorkStation")) {
                    vmSet.add(vmx);
                    // mId2Vm.put(vmx.getId(), vmx);

                } else if (infraestrutura.equals("Cluster")
                        && (Utilidades.tabelaDatacenters.get(vmx.getHost().getDatacenter().getId()).equals("DC_Andromeda")
                        || Utilidades.tabelaDatacenters.get(vmx.getHost().getDatacenter().getId()).equals("DC_Cosmos")
                        || Utilidades.tabelaDatacenters.get(vmx.getHost().getDatacenter().getId()).equals("DC_Halley"))) {
                    vmSet.add(vmx);
                    //mId2Vm.put(vmx.getId(), vmx);
                } else if (infraestrutura.equals("Cloud")
                        && Utilidades.tabelaDatacenters.get(vmx.getHost().getDatacenter().getId()).equals("DC_SoftLayer")) {
                    //System.out.println("Cloud DC Id = " + vmx.getHost().getDatacenter().getId());
                    vmSet.add(vmx);
                    //mId2Vm.put(vmx.getId(), vmx);
                }

            }
        } //end-for
//        System.out.println("%%% Nova Lista de VM Filtrada = " + vmSet.size());
        return vmSet;
    }

    public boolean isDynamicValues() {
        return dynamicValues;
    }

    public void useDynamicValues(boolean dynamicValues) {
        this.dynamicValues = dynamicValues;
    }

}

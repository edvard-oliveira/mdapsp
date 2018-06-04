package br.usp.icmc.lasdpc.BeQoS.classes;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowDatacenter;
import org.workflowsim.WorkflowScheduler;

/**
 * @file MonitorRecursos.java Nova Classe incorporada à estrutura do CloudSim
 * que representa um monitor de consumo de recursos para um datacenter (DC) em
 * percentual a partir da contabilização dos MIPS consumidos.
 *
 * Incorporada ao projeto por:
 * @author Adriana Molina Centurion
 * @author Mário Henrique de Souza Pardo
 * @author Paulo Sérgio Franco Eustáquio
 *
 * @version 8.1
 * @date 08/08/2013
 */
public class MonitorRecursos extends SimEntity {

    //Constantes de Evento do Monitor de Recursos
    public static final int RESOURCE_MONITOR_START = 19000;
    public static final int RESOURCE_MONITOR_FINISHED = 19001;
    public static final int RESOURCE_MONITOR_OPERATING = 19002;

    //Objeto para formatação padrão dos valores em porcentagem (0 a 100%)
    private DecimalFormat dft1 = new DecimalFormat("###.##");

    //Variáveis de instância do Monitor de Recursos
    private boolean running = false;
    public WorkflowScheduler broker;

    public MonitorRecursos(String name, WorkflowScheduler brk) {
        super(name);
        this.broker = brk;
        gerarCabecalhoSaidaMonRecursos();
    }

    /* Métodos herdados de SimEntity */
    @Override
    public void startEntity() {
        SimEvent ev;
        ev = new SimEvent(CloudSim.clock(), this.getId(), RESOURCE_MONITOR_START);
        processEvent(ev);
    }

    @Override
    public void processEvent(SimEvent ev) {
        switch (ev.getType()) {
            case RESOURCE_MONITOR_START:
                Log.printLine("Monitor de Recursos: " + this.getName() + " / " + this.getId() + ": Iniciado em " + CloudSim.clock() + "...");
                running = true;
                this.pause(1.0);
                break;

            case RESOURCE_MONITOR_OPERATING:
                this.executarMonitoramento();
                this.pause(60.0);  //Monitoramento ocorre a cada um minuto (60 segundos)
                break;

            case RESOURCE_MONITOR_FINISHED:
                running = false;
                Log.printLine(this.getName() + " id " + super.getId() + " is shutting down.");
                break;
        }
    }

    @Override
    public void shutdownEntity() {
        SimEvent ev = new SimEvent((int) CloudSim.clock(), this.getId(), RESOURCE_MONITOR_FINISHED);
        processEvent(ev);
    }

    @Override
    public void run() {

        if (broker.isRunning() && running == true) {
            double tempo = CloudSim.clock();
            SimEvent ev = new SimEvent((int) tempo, this.getId(), RESOURCE_MONITOR_OPERATING);
            processEvent(ev);
        }
    }
    //************************************************************************

    /*  Este método deverá calcular a carga de trabalho atual por Datacenter conforme
        a métrica de porcentagem. O valor mensurado (entre 0 a 100%) deverá ser gravado
        numa variável para posterior geração de arquivo de saída.
     */
    public void executarMonitoramento() {

        Log.printLine(CloudSim.clock() + " - ########### Monitoramento ###########");
        String linha = "" + CloudSim.clock();
        List lstEntidades = CloudSim.getEntityList();
        for (Object se : lstEntidades) {
            if (se instanceof WorkflowDatacenter) {
                WorkflowDatacenter dc = (WorkflowDatacenter) se;
                List<CondorVM> vms = dc.getVmList();
                Log.printLine("DC - " + dc.getName());
                double totalMipsDC = 0.0;  //Total de MIPs de cada Datacenter

                for (Vm v1 : vms) {
                    totalMipsDC += v1.getMips();
                }
                Log.printLine("TOTAL_MIPS_DC=" + totalMipsDC);

                double totalMipsUso = 0.0;  //Total de MIPS alocados nas VMs atualmente em USO.
                for (Vm v2 : vms) {
                    CloudletSchedulerSpaceShared csspace = (CloudletSchedulerSpaceShared) v2.getCloudletScheduler();
                    double usoCPU = csspace.getTotalUtilizationOfCpuPercentage(CloudSim.clock());
                    Log.printLine("usoCPU = " + usoCPU);
                    totalMipsUso += (v2.getMips() * usoCPU);
                    Log.printLine("totalMipsUso = " + totalMipsUso);
                }
                double porcentUsoDC = (totalMipsUso / totalMipsDC) * 100;
                linha += "\t" + dft1.format(porcentUsoDC);
                Log.printLine("workload total do DC " + dc.getName() + " = " + porcentUsoDC);
            }
        }
        Utilidades.saidaMonRecursos += linha + "\n";
        Utilidades.registraConsumoRecursos(linha);
    }

    public void gerarCabecalhoSaidaMonRecursos() {
        String cabecalho = "";
        cabecalho = "Clock";
        List lstEntidades = CloudSim.getEntityList();
        for (Object se : lstEntidades) {
            if (se instanceof WorkflowDatacenter) {
                WorkflowDatacenter dc = (WorkflowDatacenter) se;
                cabecalho += "\t" + dc.getName();
            }
        }

//        if (Utilidades.tabelaDatacenters != null) {
//            for (Map.Entry pair : Utilidades.tabelaDatacenters.entrySet()) {
//                WorkflowDatacenter dc = (WorkflowDatacenter) CloudSim.getEntity(Integer.parseInt(pair.getKey().toString()));
//                
//            }
//            //cabecalho+= "\n";
//        }
        Utilidades.saidaMonRecursos += cabecalho;
        Utilidades.registraConsumoRecursos(cabecalho);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.lasdpc.BeQoS.classes;

/**
 *
 * @author Mário
 */
public class DatacenterConfiguration {

    public DatacenterConfiguration() {
    }
    
        public int hostId;  //índice inicial para identificação dos Hosts
        public int mips; //MIPS por core 
        public int num_cores; //Núcleos (cores físico) por Processador
        public int num_hosts; //Número de Máquinas
        public int ram; //memória RAM dos Hosts
        public long storage; // disco do Host
        public int bw; //Bandwidth - banda de rede do Host
        public String arch; // Arquitetura dos computadores
        public String os; // Sistema Operacional
        public String vmm; //Monitor de Máquina Virtual
        //Modelo de Custo (pay-as-you-go)
        public double time_zone; // tarifa de fuso horário do recurso alocado
        public double cost; // custo de uso de Processamento de CPU
        public double costPerMem; // custo de uso de memória
        public double costPerStorage; // custo de uso de Storage (disco)
        public double costPerBw; // custo de uso de Rede
        public int maxTransferRate; //largura de banda máxima para transferência de arquivos
        
}

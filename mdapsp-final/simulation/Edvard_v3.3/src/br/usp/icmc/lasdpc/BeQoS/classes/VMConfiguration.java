package br.usp.icmc.lasdpc.BeQoS.classes;

/**
 *
 * @author Mário
 */
public class VMConfiguration {

    public String vmType; //type of VM - possible values: "prv" or "pub".
    public int totalNumberServices; //total of service numbers used in the simulation
    public int maxNumberServicesByVM; //maximum number of services by VM
    public int numInitialServices; //initial number of VM services
    public int idShift; //initial value for VM ids
    public long size; //Storage amount for VM
    public int ram; //vm memory (MB)
    public int mips; //MIPs for each PE
    public long bw; //bandwidth for VM
    public int pesNumber; //number of PEs 
    public String vmm; //VMM name
    
    public VMConfiguration() {
    }
    
}

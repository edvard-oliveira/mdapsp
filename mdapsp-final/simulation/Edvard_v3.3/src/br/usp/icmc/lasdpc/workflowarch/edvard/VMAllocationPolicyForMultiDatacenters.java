package br.usp.icmc.lasdpc.workflowarch.edvard;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;

/**
 *
 * @author Edvard Oliveira
 * @author Mário Henrique de Souza Pardo
 * @version 1.0
 * 
 * O objetivo da implementação desta política de alocação de VMs foi... (complementar)
 * 
 * 
 * 
 */

public class VMAllocationPolicyForMultiDatacenters extends VmAllocationPolicySimple {

    public VMAllocationPolicyForMultiDatacenters(List<? extends Host> list) {
        super(list);
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        int requiredPes = vm.getNumberOfPes();
        boolean result = false;
        int tries = 0;
        List<Integer> freePesTmp = new ArrayList<Integer>();
        for (Integer freePes : getFreePes()) {
            freePesTmp.add(freePes);
        }

        //Verifica se a VM em questão ainda não foi criada...
        if (!getVmTable().containsKey(vm.getUid())) {
            do {
                Host host = null;
                int idx = -1;
                int i = 0;
                for (Host h : getHostList()) {
                    if (h.getId() == vm.getId()) {
                        host = h;
                        idx = i;
                        break;
                    }
                    i++;
                }

                //***
                if (host != null) {
                    result = host.vmCreate(vm);
                }

                if (idx >= 0) {
                    if (result) { // if vm were succesfully created in the host
//                        System.out.println("VM #" + vm.getId() 
//                                + " criada no Host #" + host.getId() + " no Datacenter #" + host.getDatacenter().getName() );
                        getVmTable().put(vm.getUid(), host);
                        getUsedPes().put(vm.getUid(), requiredPes);
                        getFreePes().set(idx, getFreePes().get(idx) - requiredPes);
                        result = true;
                        break;
                    } else {
                        freePesTmp.set(idx, Integer.MIN_VALUE);
                    }
                }
                tries++;
            } while (!result && tries < getFreePes().size());

        }

        return result;
    }

}

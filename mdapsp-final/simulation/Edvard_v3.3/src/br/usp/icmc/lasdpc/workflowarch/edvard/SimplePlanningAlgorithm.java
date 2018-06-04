
package br.usp.icmc.lasdpc.workflowarch.edvard;

import org.cloudbus.cloudsim.Log;
import org.workflowsim.planning.BasePlanningAlgorithm;

/**
 *
 * @author Edvard Oliveira
 * @author Mário Henrique de Souza Pardo
 * @version 1.0
 * 
 * O presente algoritmo de planejamento de Workflow simplificado apenas repassada
 * às entidades subsequentes de gestão do fluxo de trabalho a lista de tarefas e
 * de máquinas virtuais a serem utilizadas no momento do escalonamento. Nenhum
 * tipo de técnica de agrupamento (clustering) é realizada.
 */

public class SimplePlanningAlgorithm extends BasePlanningAlgorithm{

    public SimplePlanningAlgorithm() {
        super();
    }
    
    
    /**
     * A princípio este algoritmo de planejamento de workflow
     * não faz nada porque na arquitetura inicial do Edvard não
     * se pensou em nenhum aspecto de arranjo ou agrupamento de
     * tarefas.
     * 
     * @throws Exception 
     */
    @Override
    public void run() throws Exception {
//        Log.printLine(" -----------------------------------------------");
//        Log.printLine("taskList tamanho -> " + getTaskList().size());
//        Log.printLine("vmList tamanho -> " + getVmList().size());
//        Log.printLine(" -----------------------------------------------");
    }
    
}

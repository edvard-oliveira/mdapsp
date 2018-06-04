package experimentos;

import br.usp.icmc.lasdpc.BeQoS.classes.Utilidades;
import org.cloudbus.cloudsim.Log;

/**
 *
 * @Programa: Esta classe será utilizada para executar o planejamento de
 * experimentos envolvendo os cenários de simulação formulados. A classe executa
 * cada um dos experimentos individualmente, conforme as repetições desejadas e
 * cria, ao final da execução de cada instância de experimento, os arquivos de
 * saída em formato MS-Excel para a posterior tabulação, elaboração de fórmulas
 * e geração de gráficos.
 *
 * @author Edvard Oliveira
 * @author Mário Pardo
 *
 * @version 1.0
 */
public class ExecutorExperimentosPadrao {

    public static void main(String[] args) {
        System.out.println("* * * Simulação WorkFlowSim - Arquitetura Edvard Oliveira * * * ");
        System.out.println("--> Experimentos para Avaliação da Distribuição de Workload <--");
        System.out.println("---> Processo iniciado em: " + new java.util.Date().toString());

        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 1;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento1 obj = new Experimento1();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);

            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 2;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento2 obj = new Experimento2();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 3;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento3 obj = new Experimento3();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 4;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento4 obj = new Experimento4();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 5;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento5 obj = new Experimento5();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 6;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento6 obj = new Experimento6();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 7;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento7 obj = new Experimento7();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 8;
            System.out.println("\n" + nomeExp + idExp + "_Rep_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento8 obj = new Experimento8();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("Relatorio" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 9;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento9 obj = new Experimento9();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 10;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento10 obj = new Experimento10();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 11;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento11 obj = new Experimento11();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 12;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento12 obj = new Experimento12();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 13;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento13 obj = new Experimento13();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 14;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento14 obj = new Experimento14();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 15;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento15 obj = new Experimento15();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 16;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento16 obj = new Experimento16();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 17;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento17 obj = new Experimento17();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 18;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento18 obj = new Experimento18();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 19;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento19 obj = new Experimento19();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 20;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento20 obj = new Experimento20();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();            
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 21;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento21 obj = new Experimento21();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 22;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento22 obj = new Experimento22();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 23;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento23 obj = new Experimento23();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 24;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento24 obj = new Experimento24();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 25;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento25 obj = new Experimento25();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();

            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);
            
            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 26;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento26 obj = new Experimento26();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
        
        for (int i = 1; i <= 10; i++) {
            String nomeExp = "Experimento";
            int idExp = 27;
            System.out.println("\n" + nomeExp + idExp + "_Repetição_" + i);

            //Arquivo de Classe do Experimento (Conforme Tabela de Experimentos definida)
            Experimento27 obj = new Experimento27();
            obj.executar();

            //Gravando arquivos de saída da simulação...
            Utilidades.gravaXLSSaidaCloudSimReport("" + nomeExp + idExp + "_Rep_" + i);
            
            //Gravando arquivo do Monitor de Recursos por Datacenter...
            Utilidades.gravaXLSConsumoRecursos("MonRec_"+ nomeExp + idExp + "_Rep_" + i);
            Utilidades.saidaMonRecursos="";
            Utilidades.saidaConsumoRecursos = new StringBuffer();
            
            //Registrando a Configuração do Experimento...
            Utilidades.gravarConfiguracoesExperimento("Configuracao_" + nomeExp + idExp + "_Rep_" + i);

            //Limpando saídas de dados de simulação...
            Utilidades.clearSaidaCloudSimReport();

            //Clean no espaço de memória
            obj = null;
            System.gc();
            System.runFinalization();
        }
        //*********************************************************************************
           
    }

}

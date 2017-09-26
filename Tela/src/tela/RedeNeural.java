/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tela;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author evert
 */
public class RedeNeural {

    DadosConfiguracao dadosConfiguracao = new DadosConfiguracao();

    //A Taxa de aprendizado faz a variação apos não consegui convergir
    private static double taxaAprendizado, erroAceitavel;

    public static int numeroNeuroniosPrimeiraCamada;
    private final int numeroNeuroniosEntrada = 5;

    private int epocas = 0;
    private static int numEpocasMaxima;

    //Faz a ligação entre os neuronios
    private double[][] conexoesPrimeiraCamada;
    private double[] conexoesSegundaCamada;
    
    public static TelaPrincipal telaPrincipal;

    public static void exportarConf(DadosConfiguracao dados) {
        //int numCamadas = dados.getNumCamadas();
        numeroNeuroniosPrimeiraCamada = dados.getNumCamadas();
        numEpocasMaxima = dados.getNumEpocas();
        erroAceitavel = dados.getErroAceito();
        taxaAprendizado = dados.getTaxaAprendizado();

        System.out.println("Numero de Camadas: " + numeroNeuroniosPrimeiraCamada);
        System.out.println("Numero de Epocas: " + numEpocasMaxima);
        System.out.println("Erro Aceitavel: " + erroAceitavel);
        System.out.println("Taxa Aprendizado: " + taxaAprendizado);
    }

    //Inicializando o construtor
    public RedeNeural() {
        this.inicializarConexoesSinapticasDaRede();
    }

    //Ler o arquivo csv mamografia e joga em uma matriz de double
    public double[][] LerConjuntoTreinamento() throws FileNotFoundException {
        File treinamento = new File("mamografiaSemIdade.csv");

        String linha = new String();

        Scanner leitor = new Scanner(treinamento);

        double[][] padroes = new double[5][800];
        int qntd = 0;
        while (leitor.hasNext()) {
            linha = leitor.nextLine();
            String[] valoresEntreVirgulas = linha.split(";");
            double[] valoresDouble = Arrays.stream(valoresEntreVirgulas).mapToDouble(Double::parseDouble).toArray();
            padroes[qntd] = valoresDouble;
            qntd++;
            //System.out.println(valoresEntreVirgulas[0] + valoresEntreVirgulas[1] + valoresEntreVirgulas[2] + valoresEntreVirgulas[3] + valoresEntreVirgulas[4]);
        }

        return padroes;
    }

    //Ler o arquivo csv resultado e joga em um vetor de double
    public double[] LerValoresEsperados() throws FileNotFoundException {
        File treinamento = new File("resultado.csv");

        String linha = new String();

        Scanner leitor = new Scanner(treinamento);

        while (leitor.hasNext()) {
            linha = leitor.nextLine();
            String[] valoresEntreVirgulas = linha.split(";");
            double[] valoresDouble = Arrays.stream(valoresEntreVirgulas).mapToDouble(Double::parseDouble).toArray();
            return valoresDouble;
            //System.out.println(valoresEntreVirgulas[0] + valoresEntreVirgulas[1] + valoresEntreVirgulas[2]);
        }

        return null;
    }

    public void treinar(double[][] conjuntoTreinamento, double[] valoresEsperados) {
        double erro = 1.0;
        while ((Math.abs(erro) > erroAceitavel) && (epocas < numEpocasMaxima)) {
            for (int i = 0; i < conjuntoTreinamento[0].length; i++) {
                double[] entradaSegundaCamada = propagarSinalPelaPrimeiraCamada(conjuntoTreinamento, i);
                double valorSaida = propagarSinalPelaSegundaCamada(entradaSegundaCamada);
                erro = valoresEsperados[i] - valorSaida;
                double gradiente = valorSaida * (1 - valorSaida) * erro;
                backPropagationPelaSegundaCamada(entradaSegundaCamada, gradiente);
                backPropagationPelaPrimeiraCamada(conjuntoTreinamento, entradaSegundaCamada, gradiente, i);
            }
            epocas++;
            telaPrincipal.addLog("Epoca #" + epocas + "\nerro #" + erro+"\n");            
        }
        telaPrincipal.addLog("<<<FIM>>>");
        telaPrincipal.exibirLog();
    }

    
    public void classificar(double[] entrada) {
        if (epocas > numEpocasMaxima - 1) {
            JOptionPane.showMessageDialog(null,"Não convergiu.");
        } else {
            double[] saidasPrimeiraCamada = getSaidaClassificacaoPrimeiraCamada(entrada);
            double[] entradaSegundaCamada = getEntradasSegundaCamada(saidasPrimeiraCamada);
            double y = propagarSinalPelaSegundaCamada(entradaSegundaCamada);
            long value = Math.round(y);
            if (value <= 0) {
                JOptionPane.showMessageDialog(null,"Benigno");
            } else {
                JOptionPane.showMessageDialog(null,"Maligno");
            }
        }
    }

    //Faz a conexção entre a camada atual e a camada seguite - Mantem os pesos fixos 
    private double[] propagarSinalPelaPrimeiraCamada(double[][] conjuntoTreinamento, int i) {
        double[] saidasPrimeiraCamada = getSaidaTreinamentoPrimeiraCamada(conjuntoTreinamento, i);
        return getEntradasSegundaCamada(saidasPrimeiraCamada);
    }

    private double propagarSinalPelaSegundaCamada(double[] entradaSegundaCamada) {
        double u = 0;
        for (int j = 0; j < conexoesSegundaCamada.length; j++) {
            u += entradaSegundaCamada[j] * conexoesSegundaCamada[j];
        }
        return getFuncaoTransferencia(u);
    }

    //Referencia-se ao valor da saida da primeira camada
    private double[] getEntradasSegundaCamada(double[] saidasPrimeiraCamada) {
        double[] entradaSegundaCamada = Arrays.copyOf(saidasPrimeiraCamada, saidasPrimeiraCamada.length + 1);
        entradaSegundaCamada[entradaSegundaCamada.length - 1] = 1.0;
        return entradaSegundaCamada;
    }

    //É o valor que será passado com entrada para a proxima camada
    private double[] getSaidaTreinamentoPrimeiraCamada(double[][] conjuntoTreinamento, int i) {
        double[] saidasPrimeiraCamada = new double[numeroNeuroniosPrimeiraCamada];
        for (int j = 0; j < conexoesPrimeiraCamada.length; j++) {
            double u = 0;
            for (int k = 0; k < conexoesPrimeiraCamada[j].length; k++) {
                u += conjuntoTreinamento[k][i] * conexoesPrimeiraCamada[j][k];
            }
            saidasPrimeiraCamada[j] = getFuncaoTransferencia(u);
        }
        return saidasPrimeiraCamada;
    }

    private double[] getSaidaClassificacaoPrimeiraCamada(double[] entrada) {
        double[] saidasPrimeiraCamada = new double[numeroNeuroniosPrimeiraCamada];
        for (int j = 0; j < conexoesPrimeiraCamada.length; j++) {
            double u = 0;
            for (int k = 0; k < conexoesPrimeiraCamada[j].length; k++) {
                u += entrada[k] * conexoesPrimeiraCamada[j][k];                
            }
            saidasPrimeiraCamada[j] = getFuncaoTransferencia(u);
        }
        return saidasPrimeiraCamada;
    }

    /* 
     * RetropropagarErro
     * 
     * Faz a multiplicação das entradas da segunda camada com o gradiente e a 
     * taxa de aprendizado definida e soma ao valor.
     * 
     * Durante este processo os pesos da rede são ajustados tendo por base uma 
     * medida de erro por base
     */
    private void backPropagationPelaPrimeiraCamada(double[][] conjuntoTreinamento, double[] entradaSegundaCamada, double gradiente, int i) {
        for (int j = 0; j < entradaSegundaCamada.length - 1; j++) {
            double derivadaFuncaoTransferencia = entradaSegundaCamada[j] * (1.0 - entradaSegundaCamada[j]);
            double sigma = derivadaFuncaoTransferencia * (conexoesSegundaCamada[j] * gradiente);
            for (int k = 0; k < conexoesPrimeiraCamada[j].length; k++) {
                conexoesPrimeiraCamada[j][k] += RedeNeural.taxaAprendizado * sigma * conjuntoTreinamento[k][i];
            }
        }
    }

    private void backPropagationPelaSegundaCamada(double[] entradaSegundaCamada, double gradiente) {
        for (int j = 0; j < conexoesSegundaCamada.length; j++) {

            conexoesSegundaCamada[j] = conexoesSegundaCamada[j] + RedeNeural.taxaAprendizado * entradaSegundaCamada[j] * gradiente;
            //conexoesSegundaCamada[j] += RedeNeural.TAXA_APRENDIZADO * entradaSegundaCamada[j] * gradiente;
        }
    }

    // Tambem conhecida como função SIGMIDAL - Determina a flexibilidade da rede, ou seja, tangente hiperbólica
    private double getFuncaoTransferencia(double u) {
        return 1.0 / (1.0 + Math.exp(-u));
    }

    private void inicializarConexoesSinapticasDaRede() {
        inicializarConexoesDaPrimeiraCamada();
        inicializarConexoesDaSegundaCamada();
    }

    //Peso incial - Para todas as entradas pode ser aleatorio ou fixo.
    private void inicializarConexoesDaPrimeiraCamada() {
        conexoesPrimeiraCamada = new double[numeroNeuroniosPrimeiraCamada][numeroNeuroniosEntrada];
        for (int i = 0; i < conexoesPrimeiraCamada.length; i++) {
            for (int j = 0; j < conexoesPrimeiraCamada[i].length; j++) {
                conexoesPrimeiraCamada[i][j] = Math.random();
                //conexoesPrimeiraCamada[i][j] = 1.0;
            }
        }
    }

    //Peso incial - Para todas as entradas pode ser aleatorio ou fixo.
    private void inicializarConexoesDaSegundaCamada() {
        conexoesSegundaCamada = new double[numeroNeuroniosPrimeiraCamada + 1];
        for (int i = 0; i < conexoesSegundaCamada.length; i++) {
            conexoesSegundaCamada[i] = Math.random();
            //conexoesSegundaCamada[i] = 1.0;
        }
    }

    public void imprimirValoresConexoes() {
        System.out.println("\n Conexoes da primeira camada:");
        for (int i = 0; i < conexoesPrimeiraCamada.length; i++) {
            for (int j = 0; j < conexoesPrimeiraCamada[i].length; j++) {
                System.out.println(conexoesPrimeiraCamada[i][j] + " ");
            }
            System.out.println("\n");
        }

        System.out.println("\n Conexoes da segunda camada:");
        for (int i = 0; i < conexoesSegundaCamada.length; i++) {
            System.out.println(conexoesSegundaCamada[i] + " ");
        }

        System.out.println("\n\n");
    }

    public double[][] getConexoesPrimeiraCamada() {
        return conexoesPrimeiraCamada;
    }

    public void setConexoesPrimeiraCamada(double[][] conexoesPrimeiraCamada) {
        this.conexoesPrimeiraCamada = conexoesPrimeiraCamada;
    }

    public double[] getConexoesSegundaCamada() {
        return conexoesSegundaCamada;
    }

    public void setConexoesSegundaCamada(double[] conexoesSegundaCamada) {
        this.conexoesSegundaCamada = conexoesSegundaCamada;
    }
}

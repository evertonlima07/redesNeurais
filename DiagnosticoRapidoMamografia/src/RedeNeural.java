
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class RedeNeural {

    //A Taxa de aprendizado
	
 public static double  TAXA_APRENDIZADO ;
 //Pode ter 100% de erro.
public static double erro ;

    //Representão os Ws, ou seja, as entradas para cada neuronio
    private double[][] conexoesPrimeiraCamada;
    private double[] conexoesSegundaCamada;

    private int numeroNeuroniosPrimeiraCamada;
    private int numeroNeuroniosEntrada;

    private int epocas = 0;
private boolean treinou; 

	//Inicializando o construtor
    public RedeNeural(int numeroNeuroniosPrimeiraCamada, int numeroNeuroniosEntrada) {
	treinou = false;
        this.numeroNeuroniosPrimeiraCamada = numeroNeuroniosPrimeiraCamada;
        this.numeroNeuroniosEntrada = numeroNeuroniosEntrada;
        this.inicializarConexoesSinapticasDaRede();
    }

    //Ler o arquivo csv mamografia e joga em uma matriz de double
    public double[][] LerConjuntoTreinamento() throws FileNotFoundException {
        File treinamento = new File("src//mamografiaSemIdade.csv");

        String linha = new String();

        Scanner leitor = new Scanner(treinamento);

        double[][] padroes = new double[5][829];
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
        File treinamento = new File("src//resultado.csv");

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

    public String treinar(double[][] conjuntoTreinamento, double[] valoresEsperados) {
        //Pode ter 100% de erro.
       // double erro = 1.0;
        String result = ""; 
        //Enquanto o erro for maior que 10% e o numero deepocas menor que 10 mill
        while ((Math.abs(erro) > 0.05) && (epocas < 10000)) {
            for (int i = 0; i < conjuntoTreinamento[0].length; i++) {
                //Entrada da segunda camada é o valor passando na conexção entre a camada atual e a camada seguite
                double[] entradaSegundaCamada = propagarSinalPelaPrimeiraCamada(conjuntoTreinamento, i);

                double valorSaida = propagarSinalPelaSegundaCamada(entradaSegundaCamada);
                //Calcula o valor do Erro
                erro = valoresEsperados[i] - valorSaida;
                //Calcula o valor do deslocamento combase no erro e no valor de saida
                double deslocamento = valorSaida * (1 - valorSaida) * erro;

                /*
                 * Responsavel por fazer as retropropagações Processo de aprendizado 
                 * ele volta da segunda cama para a primeira fazendo as devidas correções de 
                 * acordo com as entradas da segunda camada e o deslocamento
                 */
                retropropagarErroPelaSegundaCamada(entradaSegundaCamada, deslocamento);
                retropropagarErroPelaPrimeiraCamada(conjuntoTreinamento, entradaSegundaCamada, deslocamento, i);
            }
            epocas++;
             String logepocas = "Epoca #" + epocas;
             
             String logerro = "erro #" + erro;
             
             result+=logepocas+"\n"+logerro+"\n";
             
            //Mostra o numero de epocas ate convergir
           // System.out.println(logepocas);
            //Mostra o valor do erro
            //System.out.println(logerro);
        }
        treinou = true;
        return result;
    }

    public String classificar(double[] entrada) {
        if (epocas > 9999) {
            System.out.println("Não convergio.");
            return "Não convergio!";
        } else {
            double[] saidasPrimeiraCamada = getSaidaClassificacaoPrimeiraCamada(entrada);
            double[] entradaSegundaCamada = getEntradasSegundaCamada(saidasPrimeiraCamada);
            double y = propagarSinalPelaSegundaCamada(entradaSegundaCamada);
            long value = Math.round(y);
            
            if (value <= 0){
                //System.out.println(value+" Maligno");
                return "Maligno!";
            }
            else{
                //System.out.println(value+" Benigno");
                return "Benigno!";
            }
        }
    }

    //Faz a conexção entre a camada atual e a camada seguite
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

    private void retropropagarErroPelaPrimeiraCamada(double[][] conjuntoTreinamento, double[] entradaSegundaCamada, double deslocamento, int i) {
        for (int j = 0; j < entradaSegundaCamada.length - 1; j++) {
            double derivadaFuncaoTransferencia = entradaSegundaCamada[j] * (1.0 - entradaSegundaCamada[j]);
            double sigma = derivadaFuncaoTransferencia * (conexoesSegundaCamada[j] * deslocamento);
            for (int k = 0; k < conexoesPrimeiraCamada[j].length; k++) {
                conexoesPrimeiraCamada[j][k] += RedeNeural.TAXA_APRENDIZADO * sigma * conjuntoTreinamento[k][i];
            }
        }
    }

    /* 
     * Faz a multiplicação das entradas da segunda camada com o deslocamento e a 
     * taxa de aprendizado definida e soma ao valor 
     */
    private void retropropagarErroPelaSegundaCamada(double[] entradaSegundaCamada, double deslocamento) {
        for (int j = 0; j < conexoesSegundaCamada.length; j++) {
            conexoesSegundaCamada[j] += RedeNeural.TAXA_APRENDIZADO * entradaSegundaCamada[j] * deslocamento;
        }
    }

    // Determina a flexibilidade da rede
    private double getFuncaoTransferencia(double u) {
        return 1.0 / (1.0 + Math.exp(-u));
    }

    private void inicializarConexoesSinapticasDaRede() {
        inicializarConexoesDaPrimeiraCamada();
        inicializarConexoesDaSegundaCamada();
    }

    private void inicializarConexoesDaPrimeiraCamada() {
        conexoesPrimeiraCamada = new double[numeroNeuroniosPrimeiraCamada][numeroNeuroniosEntrada];
        for (int i = 0; i < conexoesPrimeiraCamada.length; i++) {
            for (int j = 0; j < conexoesPrimeiraCamada[i].length; j++) {
                //conexoesPrimeiraCamada[i][j] = Math.random();
                conexoesPrimeiraCamada[i][j] = 1.0;
            }
        }
    }

    private void inicializarConexoesDaSegundaCamada() {
        conexoesSegundaCamada = new double[numeroNeuroniosPrimeiraCamada + 1];
        for (int i = 0; i < conexoesSegundaCamada.length; i++) {
            //O valor da conexão/peso é gerado de forma eleatoria
            //conexoesSegundaCamada[i] = Math.random();
            conexoesSegundaCamada[i] = 1.0;
            
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
    public boolean isTreinou() {
	return treinou;
}


}


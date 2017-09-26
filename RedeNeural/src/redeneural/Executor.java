package redeneural;

import java.io.FileNotFoundException;

public class Executor {
    public static void main(String[] args) throws FileNotFoundException {
        int numeroNeuroniosCamadaIntermediaria = 12;
        int numeroNeuroniosEntrada = 5;
  
        //Criada a Rede Neural
        RedeNeural rede = new RedeNeural(numeroNeuroniosCamadaIntermediaria, numeroNeuroniosEntrada);

        //Defenido matriz com os dados do arquivo "mamografia.csv"
        double[][] conjuntoTreinamento = rede.LerConjuntoTreinamento();
        //Defenido vetor com os dados do arquivo "resultados.csv"
        double[] valoresEsperados = rede.LerValoresEsperados();
        
        //Fazendo o treinamento da Rede Neural
        System.out.println("Treinando...");
        rede.treinar(conjuntoTreinamento, valoresEsperados);

        //Entradas da Rede
        System.out.println("Testando:");
        
        rede.classificar(new double[]{4, 1, 1, 3, 1}); //0
        rede.classificar(new double[]{4, 4, 3, 3, 1}); //1
        rede.classificar(new double[]{5, 4, 5, 3, 1}); //1
    }

}

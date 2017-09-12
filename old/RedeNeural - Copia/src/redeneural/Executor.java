package redeneural;

import java.io.FileNotFoundException;

public class Executor {
    public static void main(String[] args) throws FileNotFoundException {
        int numeroNeuroniosCamadaIntermediaria = 5;
        int numeroNeuroniosEntrada = 5;
  
        //Criada a Rede Neural
        RedeNeural rede = new RedeNeural(numeroNeuroniosCamadaIntermediaria, numeroNeuroniosEntrada);

        //Defenido matriz com os dados do arquivo "mamografia.csv"
        double[][] treino = rede.LerTreino();
        //Defenido vetor com os dados do arquivo "resultados.csv"
        double[] resultado = rede.LerResultado();
        
        //Fazendo o treinamento da Rede Neural
        System.out.println("Treinando...");
        rede.treinar(treino, resultado);

        //Entradas da Rede
        System.out.println("Testando:");
        rede.classificar(new double[]{4, 3, 1, 2, 1});
    }

}

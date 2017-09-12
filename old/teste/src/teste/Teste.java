/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

/**
 *
 * @author Everton
 */
import java.io.File;
import java.util.List;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationReLU;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

public class Teste {

    //Conjunto de treino
    /*
    public static double XOR_ENTRADA[][] = {
        {0.0, 0.0}, 
        {1.0, 0.0},
        {0.0, 1.0}, 
        {1.0, 1.0}
    };

    //Valor esperado
    public static double XOR_IDEAL[][] = {{0.0}, {1.0}, {1.0}, {0.0}};
    */
    
    /**
     * @param args No arguments are used.
     */
    
    public static void main(final String args[]) {

        File xor_input = new File("xor.csv");
        
        //Cria a rede neural
        BasicNetwork network = new BasicNetwork();
        //Neuronio de entrada
        network.addLayer(new BasicLayer(null, true, 2));
        //Multiplas camadas
        network.addLayer(new BasicLayer(new ActivationReLU(), true, 9));
        //Neuronio de Saida
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
        
        network.getStructure().finalizeStructure();
        network.reset();

        // Cria os dados de treinamento
        //MLDataSet trainingSet = new BasicMLDataSet(XOR_ENTRADA, XOR_IDEAL);
        MLDataSet trainingSet = new BasicMLDataSet((List<MLDataPair>) xor_input);

        // Treinar a rede neural
        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

        //Epocas, número de vezes que a rede foi execultada
        int epoch = 1;

        do {
            train.iteration();
            System.out.println("Epoca #" + epoch + " Erro:" + train.getError());
            epoch++;
        } while (train.getError() > 0.1);
        train.finishTraining();
        
        //Salvando o treinamento em um arquivo
        double e = network.calculateError(trainingSet);
        System.out.println("rede error: "+e);
        System.out.println("Sanvando rede");
        EncogDirectoryPersistence.saveObject(new File("filename.txt"), network);   

        //Testando a rede neural
        System.out.println("Resultados da Rede Neural:");
        for (MLDataPair pair : trainingSet) {
            final MLData output = network.compute(pair.getInput());
            System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
                    + ", Atual = " + output.getData(0) + ", ideal = " + pair.getIdeal().getData(0));
        }

        Encog.getInstance().shutdown();
    }
    
}

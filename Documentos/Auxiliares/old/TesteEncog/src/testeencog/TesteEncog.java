/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeencog;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;


/**
 *
 * @author Everton
 */
public class TesteEncog {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double[][] x_entrada = {
            new double[]{0.0, 0.0},
            new double[]{0.5, 0.5}
        };
        
        double[][] y_saida = {
            new double[]{0.0},
            new double[]{0.5}
        };
        
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(2)); //atributos iniciais ou nerounios na camada de entrada
        network.addLayer(new BasicLayer(2)); //Quantidade de neuronios que deremos na rede
        network.addLayer(new BasicLayer(1)); //neuronio de saida
        
        //network.Structure.FinalizeStructure();
        
        network.reset();
        
        
        BasicMLDataSet dataset = new BasicMLDataSet(y_saida, y_saida);
        Backpropagation propagation = new Backpropagation(network, dataset, 0.4, 0.12);
        
        int epocas = 0;
        
        while(true){
            propagation.iteration();
            System.out.println("Época: "+epocas+" erro: "+propagation.Error);
            epocas++;
            
            if(epocas > 3500 || propagation.Error < 0.1) break;
        }
        
        for (MLDataPair d in dataset){
            MLData o = network.compute(d.getInput());
            
            System.out.println("");
        }
    }
}

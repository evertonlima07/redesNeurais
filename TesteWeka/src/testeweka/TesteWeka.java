/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeweka;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Everton
 */
public class TesteWeka {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        DataSource ds =  new DataSource("src/testeweka/mamografia.arff");
        Instances ins = ds.getDataSet();
        
        //System.out.println(ins.toString());
        
        ins.setClassIndex(5); //O atributo que deseja fazer a previsão do resultado, esta no indice 3 da base de dados vendas.arff
        
        MultilayerPerceptron mp = new MultilayerPerceptron();
        mp.buildClassifier(ins);
        //NaiveBayes nb = new NaiveBayes();
        //nb.buildClassifier(ins); //Cria o classificador
        
        
        Instance novo = new DenseInstance(6); //Nova instancia com o numero de atributos que existem na base de dados vendas.arff
        novo.setDataset(ins);
        
        //Inputs
        /*
        //Vai da maligno
        novo.setValue(0, "5");
        novo.setValue(1, "67");
        novo.setValue(2, "3");
        novo.setValue(3, "5");
        novo.setValue(4, "3");
        */
        
        //Vai da benigno
        novo.setValue(0, "4");
        novo.setValue(1, "28");
        novo.setValue(2, "1");
        novo.setValue(3, "1");
        novo.setValue(4, "3");
        
        
        double probabilidade[] = mp.distributionForInstance(novo);
        //double probabilidade[] = nb.distributionForInstance(novo);
        if (probabilidade[0] > probabilidade[1] )
            System.out.println("maligno: "+probabilidade[0]);
        else
            System.out.println("benigno: "+probabilidade[1]);
        
    }
}

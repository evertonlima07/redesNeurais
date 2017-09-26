/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;
import org.encog.util.simple.TrainingSetUtil;

/**
 *
 * @author Everton
 */
public class XORCSV {

    public static void main(final String args[]) {

        if (args.length == 0) {
            System.out.println("Usage:\n\nXORCSV xor.csv");
        } else {
            final MLDataSet trainingSet = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, args[0], false, 2, 1);
            final BasicNetwork network = EncogUtility.simpleFeedForward(2, 4, 0, 1, true);

            System.out.println();
            System.out.println("Training Network");
            EncogUtility.trainToError(network, trainingSet, 0.01);

            System.out.println();
            System.out.println("Evaluating Network");
            EncogUtility.evaluate(network, trainingSet);
        }
        Encog.getInstance().shutdown();
    }
}

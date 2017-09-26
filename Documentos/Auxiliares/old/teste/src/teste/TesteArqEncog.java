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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import static jdk.nashorn.tools.ShellFunctions.input;
import org.encog.Encog;
import org.encog.app.analyst.AnalystFileFormat;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.csv.normalize.AnalystNormalizeCSV;
import org.encog.app.analyst.wizard.AnalystWizard;
import org.encog.bot.BotUtil;
import org.encog.engine.network.activation.ActivationReLU;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.simple.TrainingSetUtil;

public class TesteArqEncog {

    //Conjunto de treino
    public static double XOR_ENTRADA[][] = {
        {0.0, 0.0},
        {1.0, 0.0},
        {0.0, 1.0},
        {1.0, 1.0}
    };

    //Valor esperado
    public static double XOR_IDEAL[][] = {{0.0}, {1.0}, {1.0}, {0.0}};

    /**
     * @param args No arguments are used.
     */
    public static String DATA_URL = "https://archive.ics.uci.edu/ml/machine-learning-databases/auto-mpg/auto-mpg.data";

    private String tempPath;

    public File downloadData(String[] args) throws MalformedURLException {
        if (args.length != 0) {
            tempPath = args[0];
        } else {
            tempPath = System.getProperty("java.io.tmpdir");
        }

        File mpgFile = new File(tempPath, "auto-mpg.data");
        BotUtil.downloadPage(new URL(AutoMPGRegression.DATA_URL), mpgFile);
        System.out.println("Downloading auto-mpg dataset to: " + mpgFile);
        return mpgFile;
    }

    public static void main(final String args[]) throws MalformedURLException {

        /*
        File origemFile = new File("xor.csv");
        File destinoFile = new File("xor2.csv");

        EncogAnalyst analyst = new EncogAnalyst();
        AnalystWizard wizard = new AnalystWizard(analyst);

        wizard.wizard(origemFile, true, AnalystFileFormat.DECPNT_COMMA);

        final AnalystNormalizeCSV norm = new AnalystNormalizeCSV();
        //norm.analyze(origemFile, true, CSVFormat.EG_FORMAT, analyst);
        norm.analyze(origemFile, true, CSVFormat.ENGLISH, analyst);

        //norm.setOutputFormat(CSVFormat.ENGLISH);
        norm.setProduceOutputHeaders(true);

        norm.normalize(destinoFile);
        */
        //Cria a rede neural
        BasicNetwork network = new BasicNetwork();
        //Neuronio de entrada
        network.addLayer(new BasicLayer(null, true, 2));
        //Multiplas camadas
        network.addLayer(new BasicLayer(new ActivationReLU(), true, 5));
        //Neuronio de Saida
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        network.getStructure().finalizeStructure();
        network.reset();

        // Cria os dados de treinamento
        //MLDataSet trainingSet = new BasicMLDataSet(XOR_ENTRADA, XOR_IDEAL);
        final MLDataSet trainingSet = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, args[0], false, 2, 1);

        // Treinar a rede neural
        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

        //Epocas, número de vezes que a rede foi execultada
        int epoch = 1;
        /*
        do {
            train.iteration();
            System.out.println("Epoca #" + epoch + " Erro:" + train.getError());
            epoch++;
        } while (train.getError() > 0.1);
        train.finishTraining();
         */

        //File filename = downloadData(args);
        File filename = new File(DATA_URL);

        CSVFormat format = new CSVFormat('.', ' '); // decimal point and space separated
        VersatileDataSource source = new CSVDataSource(filename, false, format);

        ReadCSV csv = new ReadCSV(filename, false, format);
        String[] line = new String[7];
        //MLData input = helper.allocateInputVector();

        while (csv.next()) {
            StringBuilder result = new StringBuilder();

            line[0] = csv.get(1);
            line[1] = csv.get(2);
            line[2] = csv.get(3);
            line[3] = csv.get(4);
            line[4] = csv.get(5);
            line[5] = csv.get(6);
            line[6] = csv.get(7);

            String correct = csv.get(0);
            /*
            helper.normalizeInputVector(line, input.getData(), false);
            MLData output = bestMethod.compute(input);
            String predictedMPG = helper.denormalizeOutputVectorToString(output)[0];
            */
            
            result.append(Arrays.toString(line));
            result.append(" -> predicted: ");
            //result.append(predictedMPG);
            result.append("(correct: ");
            result.append(correct);
            result.append(")");

            System.out.println(result.toString());
        }

        // Delete data file and shut down.
        filename.delete();

        Encog.getInstance()
                .shutdown();

        /*
        //Salvando o treinamento em um arquivo
        double e = network.calculateError(trainingSet);
        System.out.println("rede error: " + e);
        System.out.println("Sanvando rede");
        EncogDirectoryPersistence.saveObject(new File("filename.txt"), network);
         */
        //Testando a rede neural
        System.out.println(
                "Resultados da Rede Neural:");
        for (MLDataPair pair : trainingSet) {
            final MLData output = network.compute(pair.getInput());
            System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
                    + ", Atual = " + output.getData(0) + ", ideal = " + pair.getIdeal().getData(0));
        }

        //Encog.getInstance().shutdown();
    }
}

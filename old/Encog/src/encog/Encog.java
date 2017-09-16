/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encog;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Scanner;
import org.encog.ConsoleStatusReportable;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.simple.EncogUtility;

/**
 *
 * @author Everton
 */
public class Encog {

    //public static String DATA_URL = "D:/iris.data";

    private String tempPath;

    /*
    public File downloadData(String[] args) throws MalformedURLException {
        if (args.length != 0) {
            tempPath = args[0];
        } else {
            tempPath = System.getProperty("java.io.tmpdir");
        }

        File irisFile = new File(tempPath, "iris.csv");

        
        //BotUtil.downloadPage(new URL(iris.DATA_URL), irisFile);
        //System.out.println("Downloading Iris dataset to: " + irisFile);
         
        return irisFile;
    }
     */
    public void run(String[] args) {
        try {
            //Arquivo Local - Aprendizado
            File irisFile = new File("mamografiaSemIdade.csv");
            //File irisFile = new File("iris.csv");

            // Define o formato da base de dados
            VersatileDataSource source = new CSVDataSource(irisFile, false, CSVFormat.DECIMAL_POINT);
            VersatileMLDataSet data = new VersatileMLDataSet(source);

            // Colunas da base de dados (nome, indice e tipo)
            /*
            data.defineSourceColumn("sepal-length", 0, ColumnType.continuous);
            data.defineSourceColumn("sepal-width", 1, ColumnType.continuous);
            data.defineSourceColumn("petal-length", 2, ColumnType.continuous);
            data.defineSourceColumn("petal-width", 3, ColumnType.continuous);
            */

            data.defineSourceColumn("BI-RADS", 0, ColumnType.continuous);
            //data.defineSourceColumn("Idade", 1, ColumnType.ignore);
            data.defineSourceColumn("Forma", 1, ColumnType.continuous);
            data.defineSourceColumn("Margem", 2, ColumnType.continuous);
            data.defineSourceColumn("Densidade", 3, ColumnType.continuous);
            
            // Coluna Resultado Pretendido (nome, indice e tipo).
            //ColumnDefinition outputColumn = data.defineSourceColumn("species", 4, ColumnType.nominal);
            ColumnDefinition outputColumn = data.defineSourceColumn("Diagnostico", 4, ColumnType.continuous);

            // Analise dos dados
            data.analyze();
            data.defineSingleOutputOthersInput(outputColumn);

            // Tipos de metodos de classificação
            EncogModel model = new EncogModel(data);
            //model.selectMethod(data, MLMethodFactory.TYPE_PNN);
            //model.selectMethod(data, MLMethodFactory.TYPE_SOM);

            //model.selectMethod(data, MLMethodFactory.TYPE_SVM); //Perfeito
            //model.selectMethod(data, MLMethodFactory.TYPE_NEAT); //ok
            //model.selectMethod(data, MLMethodFactory.TYPE_RBFNETWORK); //ok
            model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD); //ok

            // Imprime o aprendizado (Epocas -> Valor -> Esperado)
            model.setReport(new ConsoleStatusReportable());

            // Normaliza os dados de acordo com o tipo escolhido em: data.defineSourceColumn("sepal-length", 0, ColumnType.continuous);
            data.normalize();

            /*
             * TEM QUE DECOBRI PARA QUE SERVE EU NAO ENTENDI
             */
            // Hold back some data for a final validation.
            // Shuffle the data into a random ordering.
            // Use a seed of 1001 so that we always use the same holdback and will get more consistent results.
            model.holdBackValidation(0.3, true, 1001);
            //model.holdBackValidation(TAXA_ACERTO, true, 1000);

            // Faz o treinamento
            model.selectTrainingType(data);

            // numero de vezes que roda para aprender.
            MLRegression bestMethod = (MLRegression) model.crossvalidate(2, true);
            //Backpropagation bp = (Backpropagation) model.crossvalidate(5, true);

            // Display the training and validation errors.
            System.out.println("\n\n\nErro no Trainamento: " + EncogUtility.calculateRegressionError(bestMethod, model.getTrainingDataset()));
            System.out.println("Erro na Validação: " + EncogUtility.calculateRegressionError(bestMethod, model.getValidationDataset()) + "\n");

            // Display our normalization parameters.
            NormalizationHelper helper = data.getNormHelper();
            System.out.println(helper.toString());

            // Display the final model.
            System.out.println("\n\n\nFinal model: " + bestMethod);

            // Loop over the entire, original, dataset and feed it through the model.
            // This also shows how you would process new data, that was not part of your
            // training set.  You do not need to retrain, simply use the NormalizationHelper
            // class.  After you train, you can save the NormalizationHelper to later
            // normalize and denormalize your data.
            ReadCSV csv = new ReadCSV(irisFile, false, CSVFormat.DECIMAL_POINT);
            String[] line = new String[4];
            MLData input = helper.allocateInputVector();

            
            
            while (csv.next()) {
                StringBuilder result = new StringBuilder();
                line[0] = csv.get(0);
                line[1] = csv.get(1);
                line[2] = csv.get(2);
                line[3] = csv.get(3);
                //line[4] = csv.get(4);

                String valorCorreto = csv.get(4);
                helper.normalizeInputVector(line, input.getData(), false);
                MLData output = bestMethod.compute(input);
                String valorEscolhido = helper.denormalizeOutputVectorToString(output)[0];

                result.append(Arrays.toString(line));
                result.append("-> Saida: "+valorEscolhido+" (Desejado: "+valorCorreto+")");
                /*
                result.append(" -> Saida: ");
                result.append(irisChosen);
                result.append("(Desejado: ");
                result.append(correct);
                result.append(")");
                */

                System.out.println(result.toString());
            }

            // Delete data file ande shut down.
            irisFile.delete();
            org.encog.Encog.getInstance().shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Encog prg = new Encog();
        prg.run(args);
    }
}

package com.example.FileWriter;

import com.example.MachineLearning.Inputs;
import com.example.MachineLearning.PrepareInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    @Autowired
    public PrepareInputs prepareInputs;

    public CsvFileWriter(){}
    public void writeCsvFile() {

        //get the winners (class1 matchup instances)
        List<Inputs> cl1Inputs=prepareInputs.getClass1Inputs();
        List<Inputs> cl0Inputs=prepareInputs.getClass0Inputs();

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter("C:/Users/Oisín/Documents/SHIT TO DO/fyp/testMLData/airline.arff");

            //Write the CSV file header
              fileWriter.append("@relation matchups\n" +
                      "\n" +
                      " @attribute matchupId  NUMERIC\n" +
                      "  @attribute TotalFight NUMERIC\n" +
                      "  @attribute WinPct NUMERIC\n" +
                      "  @attribute height NUMERIC\n" +
                      "  @attribute reach NUMERIC\n" +
                      "  @attribute WeightClass {Welterweight,Heavyweight,Middleweight,Light_Heavyweight,Lightweight,Featherweight,Bantamweight,Flyweight,Women_Bantamweight,Women_Featherweight,Women_Strawweight}\n" +
                      "  @attribute strikingaccuracy NUMERIC\n" +
                      "  @attribute sapm NUMERIC\n" +
                      "  @attribute slpm NUMERIC\n" +
                      "  @attribute strikingdefense NUMERIC\n" +
                      "  @attribute takedownAverage NUMERIC\n" +
                      "  @attribute takedownDefense NUMERIC\n" +
                      "  @attribute submissionAverage NUMERIC\n" +
                      "  @attribute numberOfUfcFights NUMERIC\n" +
                      "  @attribute numberOfUfcWins NUMERIC\n" +
                      "  @attribute numberOfUfcLosses NUMERIC\n" +
                      "  @attribute class NUMERIC" +
                      "\n"+
                      "@data");

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            //Write a new student object list to the CSV file
            for (Inputs inputs : cl0Inputs) {
                fileWriter.append(String.valueOf(inputs.getMatchupId()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getTotalFights()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getWinPct()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1height()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1reach()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getWeightClass()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_strikingaccuracy()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_sapm()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_slpm()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_strikingdefense()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_takedownaverage()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_takedowndefense()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_submissionsaverage()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getNumberOfUfcFights()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getNumberOfUfcWins()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getNumberOfUfcLosses()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getClas()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            for (Inputs inputs : cl1Inputs) {
                fileWriter.append(String.valueOf(inputs.getMatchupId()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getTotalFights()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getWinPct()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1height()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1reach()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getWeightClass()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_strikingaccuracy()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_sapm()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_slpm()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_strikingdefense()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_takedownaverage()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_takedowndefense()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getFighter1_submissionsaverage()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getNumberOfUfcFights()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getNumberOfUfcWins()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getNumberOfUfcLosses()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(inputs.getClas()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }



            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }
}
package com.example.FileWriter;

import com.example.MachineLearning.Input;
import com.example.MachineLearning.PrepareInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private FileWriter fileWriter;
    public CsvFileWriter(){}

    public void setFileWriter(String filepath){
        try {
            fileWriter = new FileWriter(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeCsvFile(boolean past) {

        try {
            //Write the CSV file header
              fileWriter.append("@relation matchups\n" +
                      "\n" +
                      " @attribute matchupId  NUMERIC\n" +
                      " @attribute fighterId  NUMERIC\n" +
                      "  @attribute TotalFight NUMERIC\n" +
                      "  @attribute WinPct NUMERIC\n" +
                      "  @attribute height NUMERIC\n" +
                      "  @attribute reach NUMERIC\n" +
                      "  @attribute strikingaccuracy NUMERIC\n" +
                      "  @attribute sapm NUMERIC\n" +
                      "  @attribute slpm NUMERIC\n" +
//                      "  @attribute strikingdefense NUMERIC\n" +
                      "  @attribute takedownAverage NUMERIC\n" +
                      "  @attribute takedownDefense NUMERIC\n" +
//                      "  @attribute submissionAverage NUMERIC\n" +
                      "  @attribute numberOfUfcFights NUMERIC\n" +
                      "  @attribute numberOfUfcWins NUMERIC\n" +
                      "  @attribute class NUMERIC" +
                      "\n"+
                      "@data");

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            //if we are creating an arff data file for future or past matchups

            //change how many months ago to go by x number of fights each time for consistency
            if(past){
                createPastMatchupData();
            }else {
                createFutureMatchupData();
            }

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            closeFileWriter();
        }
    }

    public void closeFileWriter(){
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
        }
    }

    public void createPastMatchupData()  {
        //get the winners (class1 matchup instances)
        List<Input> cl1Inputs=prepareInputs.getClass1Inputs();
        List<Input> cl0Inputs=prepareInputs.getClass0Inputs();
        try {
            writeFile(cl0Inputs);
            writeFile(cl1Inputs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createFutureMatchupData() throws IOException {
        //get info for future matchups
        List<Input> futureMatchups=prepareInputs.createFutureMatchupInputs();
        writeFile(futureMatchups);
    }

    public void writeFile(List<Input> inputs) throws IOException {
        for (Input input : inputs) {
            fileWriter.append(String.valueOf(input.getMatchupId()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getCurrentFighter()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getTotalFights()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getWinPct()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getFighter1height()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getFighter1reach()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getFighter1_strikingaccuracy()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getFighter1_sapm()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getFighter1_slpm()));
            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(String.valueOf(input.getFighter1_strikingdefense()));
//            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getFighter1_takedownaverage()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getFighter1_takedowndefense()));
            fileWriter.append(COMMA_DELIMITER);
//            fileWriter.append(String.valueOf(input.getFighter1_submissionsaverage()));
//            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getufcFinishPct()));
            fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(input.getufcWinPct()));
                fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(input.getClas()));
                fileWriter.append(NEW_LINE_SEPARATOR);

        }
    }
}
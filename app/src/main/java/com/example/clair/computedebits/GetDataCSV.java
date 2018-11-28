/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.clair.computedebits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author t25
 */
public class GetDataCSV {

    /**
     * @param args the command line arguments
     */
    public ArrayList<ArrayList<Integer>> GetDataCSV () {
        // TODO code application logic here

        String csvFile = new File("").getAbsolutePath();
        csvFile += "/CCD_Data.csv";
        String line = "";
        String cvsSplitBy = ";";
        int i=0;
        
        ArrayList<ArrayList<Integer>> dataInput = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            
            while ((line = br.readLine()) != null) {
                // use comma as separator
                
                String[] dataCCD = line.split(cvsSplitBy);
                ArrayList<Integer> byHoursDataInput = new ArrayList<>();

                
                byHoursDataInput.add(Integer.parseInt(dataCCD[0]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[1]));

                byHoursDataInput.add(Integer.parseInt(dataCCD[2]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[3]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[4]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[5]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[6]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[7]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[8]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[9]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[10]));
                byHoursDataInput.add(Integer.parseInt(dataCCD[11]));
                
                dataInput.add(byHoursDataInput);
                //System.out.println(i);
                //System.out.println(byHoursDataInput);
                i ++;      
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //System.out.println(dataInput);
        return dataInput;
    }
}

    


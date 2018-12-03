/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.clair.computedebits.debitDevider;

import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;

/**
 *
 * @author clair
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int qtot;
        int upElevation;
        int q1Data;
        int p1Data;
        int q2Data;
        int p2Data;
        int q3Data;
        int p3Data;
        int q4Data;
        int p4Data;
        int q5Data;
        int p5Data; 
        float errorPower;
        float errorDebit;
        float sumErrorPower=0;
        float sumErrorDebit=0;
        float averrageErrorPower=0;
        float averrageErrorDebit=0;
        int power=0;
        int debit=0;
        int powerData;
        int debitData;
       
        
        
        
        System.out.println("Start");
        System.out.println(currentTimeMillis());
        ArrayList<ArrayList<Integer>> data = new GetDataCSV().GetDataCSV();
        System.out.println("Data import");
        System.out.println(data.size());
        for (int i=0; i < data.size(); i++) {    
            power=0;
            System.out.println("Repartition for hour : "+i);
            ArrayList<Integer> dataByHour = new ArrayList<>();
            dataByHour = data.get(i);
            qtot = dataByHour.get(0);
            upElevation = dataByHour.get(1);
            q1Data = dataByHour.get(2);
            p1Data = dataByHour.get(3);
            q2Data = dataByHour.get(4);
            p2Data = dataByHour.get(5);
            q3Data = dataByHour.get(6);
            p3Data = dataByHour.get(7);
            q4Data = dataByHour.get(8);
            p4Data = dataByHour.get(9);
            q5Data = dataByHour.get(10);
            p5Data = dataByHour.get(11);            
            
            ArrayList<Integer> maxDebits = new ArrayList<>();
            if(q1Data == 0){
                maxDebits.add(0);
            }else {maxDebits.add(160);}
            if(q2Data == 0){
                maxDebits.add(0);
            }else {maxDebits.add(160);}
            if(q3Data == 0){
                maxDebits.add(0);
            }else {maxDebits.add(160);}
            if(q4Data == 0){
                maxDebits.add(0);
            }else {maxDebits.add(160);}
            if(q5Data == 0){
                maxDebits.add(0);
            }else {maxDebits.add(160);}
            
            PowerPlant powerPlant = new PowerPlant(qtot, upElevation, maxDebits);
            ArrayList<Integer> repartitionDebits = powerPlant.getRepartitionOpti().get(0);
            ArrayList<Integer> repartitionPower = powerPlant.getRepartitionOpti().get(1);
       
            powerData = p1Data + p2Data + p3Data + p4Data + p5Data;
            debitData = q1Data + q2Data + q3Data + q4Data + q5Data;
            power = 0;
            debit = 0;
            for (int j=0; j < repartitionPower.size(); j++) {
                debit = debit + repartitionDebits.get(j);
                power = power + repartitionPower.get(j);
            }
            errorPower = (float) (power-powerData)/power*100;
            errorDebit = (float) (debit-debitData)/debit*100;
            sumErrorPower = sumErrorPower + Math.abs(errorPower);
            sumErrorDebit = sumErrorDebit + Math.abs(errorDebit);
            averrageErrorPower = sumErrorPower/200;
            averrageErrorDebit = sumErrorDebit/200;
            
            //System.out.println("Débit : notre repartition puis celle des Datas");
            //System.out.println(repartitionDebits);
            //System.out.println("["+q1Data+", "+q2Data+", "+q3Data+", "+q4Data+", "+q5Data+"]");
            //System.out.println("DebitData: "+debitData);
            //System.out.println("Debit: "+debit);
            //System.out.println("ErrorDebit of "+ Math.abs(errorDebit)+"%");
            //System.out.println("");
            //System.out.println("Power : notre repartition puis celle des Datas");
            //System.out.println(repartitionPower);
            //System.out.println("["+p1Data+", "+p2Data+", "+p3Data+", "+p4Data+", "+p5Data+"]");
            System.out.println("Power: "+power);
            System.out.println("PowerData: "+powerData);
            //System.out.println("ErrorPower of "+ Math.abs(errorPower)+"%");
            //System.out.println("");

          
        }

        System.out.println("Averrage error Debit: "+ averrageErrorDebit+"%");
        System.out.println("Averrage error Power: "+ averrageErrorPower+"%");
        System.out.println(currentTimeMillis());
        
    }

}

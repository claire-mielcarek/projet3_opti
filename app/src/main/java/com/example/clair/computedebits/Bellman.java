/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.clair.computedebits;

import java.util.ArrayList;

/**
 *
 * @author clair
 */
public class Bellman {

    private final int qtot;
    private final ArrayList<Turbine> turbines;
    private ArrayList<Integer> solution; //Debit of each turbine
    private ArrayList<Integer> objectif; //Power of each turbine
    private ArrayList<ArrayList<Integer>> repartition;
    private ArrayList<ArrayList<ArrayList<Integer>>> steps;

    public Bellman(int qtot, ArrayList<Turbine> turbines) {
        this.qtot = qtot;
        this.turbines = turbines;
        
        steps = new ArrayList<>();
        solution = new ArrayList<>();
        objectif = new ArrayList<>();
        repartition = new ArrayList<>();
    }

    public ArrayList<ArrayList<Integer>> getRepartition() {
        backward();
        forward();
        repartition.add(solution);
        repartition.add(objectif);
        return repartition;
    }
    
    

    //Build the table of each step
    private void backward() {
        ArrayList<ArrayList<Integer>> matrix;
        for (int turbineNumber = 5; turbineNumber >= 1; turbineNumber--) {
            switch (turbineNumber) {
                case 1:
                    matrix = computeMaxPowerOfFirstTurbine(turbineNumber);
                    break;

                case 5:
                    matrix = computeMaxPowerOfLastTurbine(turbineNumber);
                    break;

                default:
                    matrix = computeMaxPowerOfMiddleTurbine(turbineNumber);
                    break;
            }
            steps.add(0, matrix);
        }
    }

    private ArrayList<ArrayList<Integer>> computeMaxPowerOfLastTurbine(int turbineNumber) {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        Turbine t = turbines.get(turbineNumber - 1);

        ArrayList<Integer> powers = (ArrayList<Integer>) t.getData().clone();
        //We have to complete the powers until it contains the power 
        //corresponding to Qtot
        while (powers.size() <= (qtot / 5) + 1) {
            powers.add(powers.get(powers.size() - 1));
        }
        matrix.add(powers);
        ArrayList<Integer> correspondingDebits = new ArrayList<>();
        for (int i = 0; i < powers.size(); i++) {
            correspondingDebits.add(i * 5);
        }
        matrix.add((ArrayList<Integer>) correspondingDebits.clone());
        return matrix;

    }

    private ArrayList<ArrayList<Integer>> computeMaxPowerOfFirstTurbine(int turbineNumber) {

        Turbine t = turbines.get(turbineNumber - 1);
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        ArrayList<Integer> turbinePowers = t.getData();
        ArrayList<Integer> maxPowers = new ArrayList<>();
        ArrayList<Integer> correspondingDebits = new ArrayList<>();
        int maxPower = 0;
        int debitOfMaxPower = 0;
        int maxTestPower =0;
        int curPower;
        ArrayList<Integer> powerLastTurbine = steps.get(0).get(0);
        int availableDebit = qtot;
        for (int assignedDebit = 0; assignedDebit <= t.getMaxDebit(); assignedDebit += 5) {
            curPower = turbinePowers.get(assignedDebit / 5) + powerLastTurbine.get((availableDebit - assignedDebit) / 5);
            if (curPower > maxPower) {
                maxPower = curPower;
                debitOfMaxPower = assignedDebit;
            }
        }
        maxPower = turbinePowers.get(debitOfMaxPower / 5);
        maxPowers.add(maxPower);
        correspondingDebits.add(debitOfMaxPower);
        matrix.add(maxPowers);
        matrix.add(correspondingDebits);
        return matrix;
    }

    private ArrayList<ArrayList<Integer>> computeMaxPowerOfMiddleTurbine(int turbineNumber) {
        Turbine t = turbines.get(turbineNumber - 1);
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

        ArrayList<Integer> turbinePowers = t.getData();
        ArrayList<Integer> maxPowers = new ArrayList<>();
        ArrayList<Integer> correspondingDebits = new ArrayList<>();
        int maxPower = 0;
        int debitOfMaxPower = 0;
        int curPower;
        ArrayList<Integer> powerLastTurbine = steps.get(0).get(0);
        for (int availableDebit = 0; availableDebit <= qtot; availableDebit += 5) {
            for (int assignedDebit = 0; assignedDebit <= t.getMaxDebit(); assignedDebit += 5) {
                if(assignedDebit > availableDebit){
                    curPower = 0;
                }
                else{
                    curPower = turbinePowers.get(assignedDebit / 5) + powerLastTurbine.get((availableDebit - assignedDebit) / 5);                    
                }
                if (curPower > maxPower) {
                    maxPower = curPower;
                    debitOfMaxPower = assignedDebit;
                }
            }
            maxPowers.add(maxPower);
            correspondingDebits.add(debitOfMaxPower);
            maxPower = 0;
            debitOfMaxPower = 0;
        }
        matrix.add(maxPowers);
        matrix.add(correspondingDebits);
        return matrix;
    }

    //Trace the solution back trough the steps' table
    private void forward() {
        int curDebit = steps.get(0).get(1).get(0);
        int curPower = steps.get(0).get(0).get(0);
        solution.add(curDebit);
        objectif.add(curPower);
        //System.out.println("turbine: 0");
        //System.out.println(steps.get(0).get(0));
        int availableDebit = qtot - curDebit;
        for (int numTurbine = 1; numTurbine <= 4; numTurbine++){
            //System.out.println("turbine: "+numTurbine);
            //System.out.println(availableDebit);
            curDebit = steps.get(numTurbine).get(1).get(availableDebit/5);
            curPower = steps.get(numTurbine).get(0).get(curDebit/5);
            solution.add(curDebit);
            objectif.add(curPower);
            availableDebit = availableDebit - curDebit;
        }        
    }

}

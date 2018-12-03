/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.clair.computedebits.debitDevider;

import static java.lang.Math.pow;
import java.util.ArrayList;

/**
 *
 * @author clair
 */
public class Turbine {

    private final int turbineNumber;
    private final int maxDebit; //Is null when the turbin isn't activated
    private final double p00;
    private final double p01;
    private final double p10;
    private final double p20;
    private final double p11;
    private final int height;
    private ArrayList<Integer> data;

    public Turbine(int turbineNumber, int maxDebit, double p00, double p10, double p01, double p20, double p11, int height) {
        this.turbineNumber = turbineNumber;
        this.maxDebit = maxDebit;
        this.p00 = p00;
        this.p01 = p01;
        this.p10 = p10;
        this.p20 = p20;
        this.p11 = p11;
        this.height = height;

        //Intitialization of the turbine's data
        //that's to say the table of the power in function of debit
        data = new ArrayList<>();
        for (int currDebit = 0; currDebit <= maxDebit; currDebit += 5) {
            data.add(g(currDebit));
        }
    }

    private int g(int debit) {
        int netHeight = (int) (height-0.5*pow(10,-5)*debit*debit);
        return (int) (p00 + p10 * debit + p01 * netHeight + p20 * debit * debit + p11 * debit * netHeight);
    }

    public int getTurbineNumber() {
        return turbineNumber;
    }

    public boolean isActivated() {
        return (maxDebit == 0);
    }

    public int getMaxDebit() {
        return maxDebit;
    }

    public ArrayList<Integer> getData() {
        return data;
    }
    
    

}

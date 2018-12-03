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
public class PowerPlant {
    private final int height;
    private final int upElevation;
    private final int qtot;
    private final ArrayList<Turbine> turbines;

    public PowerPlant(int qtot, int upElevation, ArrayList<Integer> maxDebits) {
        this.upElevation = upElevation;
        this.qtot = qtot;
        
        //Calcul of the down elevation with the equation obtained in the first part of the project
        int downElevation = (int) ( -7.014*pow(10,-7)*pow(qtot,2) + 0.004107*qtot+137.2);
        this.height = (int) (this.upElevation - downElevation);
        
        //Initialization of the 5 turbines
        turbines = new ArrayList<>();
        turbines.add(new Turbine(1, maxDebits.get(0), -0.02523, 0.0348, 0.0007935, -0.0004142, 0.009093, height));
        turbines.add(new Turbine(2, maxDebits.get(1), 0.4617, 0.01806, -0.01374, -0.0003304, 0.009382, height));
        turbines.add(new Turbine(3, maxDebits.get(2), -34.24, 0.5811, -0.4255, -0.0026, 0.0125, height));
        turbines.add(new Turbine(4, maxDebits.get(3), 0.04647, 0.01604, -0.002264, -0.0003378, 0.009945, height));
        turbines.add(new Turbine(5, maxDebits.get(4), 0.6575, -0.00511, -0.02129, -0.0004141, 0.01073, height));
    }
    
    public ArrayList<ArrayList<Integer>> getRepartitionOpti(){
        Bellman optimization =new Bellman(qtot, turbines);
        return optimization.getRepartition();
    }
    
}

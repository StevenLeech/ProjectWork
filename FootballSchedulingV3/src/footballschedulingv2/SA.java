/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footballschedulingv2;

import java.util.Random;

public class SA {

    private RandGen prand;
    private Problem pproblem;
    private Problem best;
    private Problem backup;
    private double temperature;
    private int stepMax;
    private int nIter;
    private double rho;
    private double limitAcceptanceRatio;
    private double infty;
    private double tol;
    private double explim;
    private int rejected;
    private int positivelyAccepted;
    private int negativelyAccepted;
    private int neutrallyAccepted;

    /**
     * main constructor
     */
    public SA(Problem problem1, RandGen rand1, double temperature1,
            int stepMax1, int nIter1, double rho1, double limitAcceptanceRatio1,
            double tolerance1, double infinity1, double exponentialLimit1) {

        pproblem = problem1;
        prand = rand1;
        temperature = temperature1;
        stepMax = stepMax1;
        nIter = nIter1;
        rho = rho1;
        limitAcceptanceRatio = limitAcceptanceRatio1;
        infty = infinity1;
        tol = tolerance1;
        explim = exponentialLimit1;
    }

   
    /**
     * method to print initial statistics
     */
    private void initStats() {
        rejected = positivelyAccepted = negativelyAccepted
                = neutrallyAccepted = 0;
        System.out.println("Cost " + pproblem.returnCost());
        System.out.println("w/penalty " + pproblem.getAdjustedCost());
    }
    
    /**
     * method do display current statistics at the end of a step
     */
    private void displayStats(int step) {
        System.out.println("Temperature step number : " + step);
        System.out.println("Temperature             : " + temperature);
        double pcent = (double) 100 * (neutrallyAccepted) / (double) (rejected
                + neutrallyAccepted + positivelyAccepted + negativelyAccepted);
        System.out.println("Proportion of moves that did not change the cost : "
                + pcent);
        pcent = (double) 100 * (rejected) / (double) (rejected
                + positivelyAccepted + negativelyAccepted);
        System.out.println("Proportion of non-zero cost moves that were rejected : "
                + +pcent);
        pcent = (double) 100 * (negativelyAccepted + positivelyAccepted) / (double) (rejected + positivelyAccepted + negativelyAccepted);
        System.out.println("Proportion of non-zero cost moves that were accepted : "
                + pcent);
    }
    
    /**
     * method to decide whether or not to accept a move
     */
    public boolean metropolis(double deltacost) {
        double epsilon;
        if (deltacost > infty) {
            rejected++;
            return false;
        }
        if (deltacost > tol) {
            epsilon = -(double) (deltacost) / temperature;
            if (epsilon > explim) {
                epsilon = Math.exp(epsilon);//exponential in java
                if (prand.myRand() > epsilon) {
                    rejected++;
                    return false;
                } else {
                    positivelyAccepted++;
                    return true;
                }
            } else {
                rejected++;
                return false;
            }
        } else if (deltacost > -tol) {
            neutrallyAccepted++;
            return true;
        } else {
            negativelyAccepted++;
            return true;
        }
    }
    
    /**
     * method to perform a move
     */
    public void move() {
        Random rand = new Random();
        int selectDivision = rand.nextInt(4);
        Division division = pproblem.getDivisions()[selectDivision];
        backup = new Problem(division,pproblem);
        double adjustedCost = pproblem.getAdjustedCost();
        double newCost = pproblem.modify(division);
        if (metropolis(newCost - adjustedCost)) /* accepted */ {
            if (newCost < pproblem.getBestCost()) {
                pproblem.setBestCost(newCost);
                best = new Problem(pproblem);
            }
            /*double cCost = pproblem.calculateCost();
            if(newCost - cCost>0.0001 || newCost - cCost<-0.0001  ){
                System.out.println("Error, costs different -----------------------" + newCost + "--"+cCost);
            }*/
        } else /*rejected */ {
            pproblem = backup;
        }
    }
    
    /**
     * method to start a simulation and run the program
     */
    public double simulation() {
        double costChange1=40;
        double costChange2=50;
        double costChange3=60;
        double previousCost = 0;
        int iter;
        double acceptanceRatio = 1;
        for (int step = 1; ((step <= stepMax) && (acceptanceRatio > limitAcceptanceRatio)); step++) {
            initStats();
            for (iter = 1; iter <= nIter; iter++) {
                move();
            }
            costChange3 = Math.abs(costChange2);
            costChange2 = Math.abs(costChange1);
            costChange1 = Math.abs(pproblem.getAdjustedCost()- previousCost);
            if(costChange1+costChange2+costChange3 <2 && costChange1+costChange2+costChange3>-2){
                step = stepMax;
            }
            previousCost = pproblem.getAdjustedCost();
            displayStats(step);
            acceptanceRatio = (double) (positivelyAccepted + negativelyAccepted)
                    / (double) (positivelyAccepted + negativelyAccepted + rejected);
            temperature = temperature * rho;
        }
        pproblem = new Problem(best);
        pproblem.checkAndDisplay();
        return pproblem.getBestCost();
    }

    /**
     * @param backup the backup to set
     */
    public void setBackup(Problem backup) {
        this.backup = backup;
    }
    
    /**
     * return the best problem found.
     */
    public Problem getProblem(){
        return this.best;
    }
}

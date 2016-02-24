/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footballschedulingv2;

public class SA {

    private RandGen prand;
    private Problem pproblem;
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

    public SA() {

    }

    private void initStats() {
        rejected = positivelyAccepted = negativelyAccepted
                = neutrallyAccepted = 0;
        System.out.println(pproblem.getCost());
    }

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

    public void move() {
        Problem backup = new Problem(pproblem);
        double cost = pproblem.getAdjustedCost();
        double newCost = pproblem.modify();
        if (metropolis(newCost - cost)) /* accepted */ {
            if (newCost < pproblem.getBestCost()) {
                pproblem.setBestCost(newCost);
            }
        } else /*rejected */ {
            pproblem = backup;
        }
    }

    public void simulation() {

        int iter;
        double acceptanceRatio = 1;
        for (int step = 1; ((step <= stepMax) && (acceptanceRatio > limitAcceptanceRatio)); step++) {
            initStats();
            for (iter = 1; iter <= nIter; iter++) {
                move();
            }

            displayStats(step);
            acceptanceRatio = (double) (positivelyAccepted + negativelyAccepted)
                    / (double) (positivelyAccepted + negativelyAccepted + rejected);
            temperature = temperature * rho;
        }
        pproblem.checkAndDisplay();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedannealing;

/**
 *
 * @author Steven
 */
public class SA {
    private Problem pproblem;
    private RandGen prand;
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
            double tolerance1, double infinity1, double exponentialLimit1){
        
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
    private void initStats(){
        rejected = positivelyAccepted = negativelyAccepted =
                neutrallyAccepted =0;
    }
    private void displayStats(int step){
        System.out.println("Temperature step number : "+ step);
        System.out.println("Temperature             : "+ temperature);
        double pcent = (double) 100*(neutrallyAccepted)/(double)(rejected+
                neutrallyAccepted+positivelyAccepted+negativelyAccepted);
        System.out.println("Proportion of moves that did not change the cost : "+
                pcent);
        pcent = (double) 100*(rejected)/(double)(rejected+
                positivelyAccepted + negativelyAccepted);
        System.out.println("Proportion of non-zero cost moves that were rejected : "+
                +pcent);
        pcent = (double) 100*(negativelyAccepted+positivelyAccepted)/(double)
                (rejected+positivelyAccepted+negativelyAccepted);
        System.out.println("Proportion of non-zero cost moves that were accepted : "+
                pcent);
    }
    private int metropolis(double deltacost){
        double epsilon;
        if(deltacost>infty){
            rejected++;
            return(0);
        }
        if(deltacost > tol){
            epsilon = - (double)(deltacost)/temperature;
            if(epsilon > explim){
                epsilon = Math.exp(epsilon);//exponential in java
                if(prand.myRand() > epsilon){
                    rejected++;
                    return(0);
                }
                else{
                    positivelyAccepted++;
                    return(1);
                }
            }
            else{
                rejected++;
                return(0);
            }
        }
        else if(deltacost > -tol){
            neutrallyAccepted ++;
            return(1);
        }
        else{
            negativelyAccepted++;
            return(1);
        }
    }
    private void move(){
        double cost = pproblem.getCost();
        double newCost = pproblem.modify();
        if(metropolis(newCost - cost)==1){
            pproblem.update();
            if(newCost < pproblem.getBestCost()){
                pproblem.updateBest();
            }
        }
        else{
            pproblem.restore();
        }
    }
    public void simulation(){
        int iter;
        double acceptanceRatio = 1;
        for(int step =1;((step<=stepMax)&&(acceptanceRatio>limitAcceptanceRatio));step++){
            initStats();
            for(iter = 1; iter<=nIter; iter++){
                move();
            }
            displayStats(step);
            acceptanceRatio = (double) (positivelyAccepted + negativelyAccepted)/
                    (double) (positivelyAccepted + negativelyAccepted + rejected);
            temperature = temperature * rho;
        }
        pproblem.checkAndDisplay();
    }
}

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
public class quadratic2 extends Problem{

    private RandGen qrand;
    private int n;
    private int[][] mcost;
    private double cost;
    private double bestCost;
    private double modifiedCost;
    private int[] x;
    private int[] xBest;
    private double[] delta;
    private int flipped;

    public quadratic2(int n, int[][] inputm, double seed) {
        mcost = new int[n][];
        qrand = new RandGen();
        qrand.setSeed(seed);
        x = new int[n];
        xBest = new int[n];
        for (int i = 0; i < n; i++) {
            mcost[i] = new int[n];
            for (int j = 0; j < n; j++) {
                mcost[i][j] = inputm[i][j];
            }
        }
        this.n = n;
        for (int i = 0; i < n; i++) {
            xBest[i] = x[i] = qrand.randInt(0, 1);
        }
        cost = 0.0f;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost += mcost[i][j] * x[i] * x[j];
            }
        }
        bestCost = cost;
        delta = new double[n];
        for (int i = 0; i < n; i++) {
            delta[i] = mcost[i][i];
            for (int j = 0; j < i; j++) {
                delta[i] += (mcost[i][j] + mcost[j][i]) * x[j];
            }
            for (int j = i + 1; j < n; j++) {
                delta[i] += (mcost[i][j] + mcost[j][i]) * x[j];
            }
        }
    }

    public quadratic2(quadratic2 qpb) {
        this.copy(qpb);
    }

    public void release() {

    }

    public void copy(quadratic2 qpb) {
        qrand = qpb.qrand;
        n = qpb.n;
        cost = qpb.cost;
        bestCost = qpb.bestCost;
        modifiedCost = qpb.modifiedCost;
        flipped = qpb.flipped;

        mcost = new int[n][];
        x = new int[n];
        xBest = new int[n];
        delta = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = qpb.x[i];
            xBest[i] = qpb.xBest[i];
            delta[i] = qpb.delta[i];
            mcost[i] = new int[n];
            for (int j = 0; j < n; j++) {
                mcost[i][j] = qpb.mcost[i][j];
            }
        }
    }

    @Override
    public double getCost() {
        return cost;
    }
   

    @Override
    public void update() {
        cost = modifiedCost;
        x[flipped] = 1 - x[flipped];
        if (x[flipped] == 1) {
            for (int i = 0; i < flipped; i++) {
                delta[i] += mcost[i][flipped] + mcost[flipped][i];
            }
            for (int i = flipped + 1; i < n; i++) {
                delta[i] += mcost[i][flipped] + mcost[flipped][i];
            }
        } else {
            for (int i = 0; i < flipped; i++) {
                delta[i] -= mcost[i][flipped] + mcost[flipped][i];
            }
            for (int i = flipped + 1; i < n; i++) {
                delta[i] -= mcost[i][flipped] + mcost[flipped][i];
            }
        }
    }
    @Override
    public double getBestCost(){
        return bestCost;
    }
    @Override
    public void updateBest(){
        for(int i=0;i<n;i++){
            xBest[i]=x[i];
        }
        bestCost = cost;
        checkAndDisplay();
    }
    @Override
    public void restore(){
        
    }
    @Override
    public void checkAndDisplay(){
        double calc=0.0;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                calc += mcost[i][j]*xBest[i]*xBest[j];
            }
        }
        if(Math.abs(calc-bestCost)>1e-6){
            System.out.println("problem with recalculated cost: "+ calc+
                    " and best cost stored: "+ bestCost);
        }
        else{
            System.out.println("Best cost is: "+ bestCost);
        }   
    }
    public void initRand(double seed){
        qrand.setSeed(seed);
    }

    @Override
    public double modify() {
        flipped = qrand.randInt(0, n - 1);
        if (x[flipped] == 0) {
            modifiedCost = cost + delta[flipped];
        } else {
            modifiedCost = cost - delta[flipped];
        }
        return modifiedCost;
    }
}

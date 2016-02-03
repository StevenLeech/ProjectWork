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
public abstract class Problem {
    
    public abstract double getCost();
    public abstract double modify();
    public abstract void update();
    public abstract double getBestCost();
    public abstract void updateBest();
    public abstract void restore();
    public abstract void checkAndDisplay();  
    
    
}

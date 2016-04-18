/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footballschedulingv2;

public class RandGen {
    private double seed;
    private double randomNumber;
    private static double a;
    private static double b;
    private static double m;
    
    public RandGen(){
        
    }
    public RandGen(double aseed){
        this.a = 62605;
        this.b = 113218009;
        this.m = 536870912;
        setSeed(aseed);
    }
    public double getSeed(){
        return seed;
    }
    public void setSeed(double aseed){
        this.seed = aseed;
        this.randomNumber = seed;
    }
    public float myRand(){
        randomNumber = ((a*randomNumber + b) % m);
        return((float)(randomNumber/m));
    }
    public int randInt(int x, int y){
        int z;
        float f;
        if(x>y){
            z=x;
            x=y;
            y=z;
        }
        f=myRand();
        z= (int) Math.ceil((double) (f*(x-1) + (1-f) * y));
        
        if(z>y){
            z=y;
        }
        if(z<x){
            z=x;
        }
        return(z);
    }
           
}

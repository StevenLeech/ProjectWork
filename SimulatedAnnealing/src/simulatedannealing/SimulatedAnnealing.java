/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedannealing;

import java.util.Scanner;

/**
 *
 * @author Steven
 */
public class SimulatedAnnealing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n;
        Scanner scan = new Scanner(System.in);
        System.out.println("Problem size? = ");
        n = Integer.parseInt(scan.next());
        
        RandGen rgen = new RandGen(123.0);
        int[][] a = new int[n][];
        for(int i=0;i<n;i++){
            a[i] = new int[n];
            for(int j=0;j<n;j++){
                a[i][j] = rgen.randInt(-10, 100);
            }
        }
        quadratic2 quad= new quadratic2(n,a,123.0);
        rgen.setSeed(123.0);
        SA mysa = new SA(quad,rgen,n,30,300*n*n,0.9,0.01,1e-6,1e10,-50);
        mysa.simulation();
        System.out.println("-------------------");
        
    }
    
}
